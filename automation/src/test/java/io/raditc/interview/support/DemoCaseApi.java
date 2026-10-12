package io.raditc.interview.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Local synthetic fixture, not an implementation task or production API. */
public final class DemoCaseApi implements AutoCloseable {
 public static final String DEMO_AUTH = "Bearer fictional-interview-user";
 public static final String RESTRICTED_ID = "restricted-case";
 private record StoredCase(String id, String reference, long visibleAt) {}
 private final Map<String, StoredCase> cases = new ConcurrentHashMap<>();
 private final ObjectMapper json = new ObjectMapper();
 private final ExecutorService executor = Executors.newCachedThreadPool();
 private final HttpServer server;
 private final int delayMillis;
 public DemoCaseApi() throws IOException { this(Integer.getInteger("demo.visibility.delay.ms", 500)); }
 public DemoCaseApi(int delayMillis) throws IOException {
  if (delayMillis < 0 || delayMillis > 3000) throw new IllegalArgumentException("Visibility delay must be 0..3000 ms");
  this.delayMillis = delayMillis;
  server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
  server.createContext("/", this::handle);
  server.setExecutor(executor);
  server.start();
 }
 public String baseUrl() { return "http://127.0.0.1:" + server.getAddress().getPort(); }
 private void handle(HttpExchange exchange) throws IOException {
  try {
   String path = exchange.getRequestURI().getPath();
   String method = exchange.getRequestMethod();
   if (path.equals("/health") && method.equals("GET")) { send(exchange, 200, Map.of("status", "ok")); return; }
   if (!DEMO_AUTH.equals(exchange.getRequestHeaders().getFirst("Authorization"))) { send(exchange, 401, Map.of("error", "unauthorised")); return; }
   if (path.equals("/cases") && method.equals("POST")) {
    JsonNode body;
    try { body = json.readTree(exchange.getRequestBody()); }
    catch (com.fasterxml.jackson.core.JsonProcessingException ex) { send(exchange, 400, Map.of("error", "invalid JSON")); return; }
    JsonNode referenceNode = body == null ? null : body.get("reference");
    if (referenceNode == null || !referenceNode.isTextual() || !referenceNode.asText().matches("TEST-[A-Z0-9-]{1,40}")) {
     send(exchange, 400, Map.of("error", "invalid reference")); return;
    }
    String id = UUID.randomUUID().toString();
    var item = new StoredCase(id, referenceNode.asText(), System.nanoTime() + delayMillis * 1_000_000L);
    cases.put(id, item);
    send(exchange, 201, Map.of("id", id, "reference", item.reference())); return;
   }
   if (path.startsWith("/cases/") && method.equals("GET")) {
    String id = path.substring("/cases/".length());
    if (id.equals(RESTRICTED_ID)) { send(exchange, 403, Map.of("error", "forbidden")); return; }
    var item = cases.get(id);
    if (item == null || System.nanoTime() - item.visibleAt() < 0) { send(exchange, 404, Map.of("error", "not found")); return; }
    send(exchange, 200, Map.of("id", item.id(), "reference", item.reference())); return;
   }
   send(exchange, 404, Map.of("error", "not found"));
  } finally { exchange.close(); }
 }
 private void send(HttpExchange exchange, int status, Object body) throws IOException {
  byte[] bytes = json.writeValueAsBytes(body);
  exchange.getResponseHeaders().set("Content-Type", "application/json");
  exchange.getResponseHeaders().set("X-Correlation-Id", UUID.randomUUID().toString());
  exchange.sendResponseHeaders(status, bytes.length);
  exchange.getResponseBody().write(bytes);
 }
 @Override public void close() { server.stop(0); executor.shutdownNow(); cases.clear(); }
}
