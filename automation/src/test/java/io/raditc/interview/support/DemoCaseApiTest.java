package io.raditc.interview.support;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/** Infrastructure checks. These do not implement the candidate's Serenity tests. */
class DemoCaseApiTest {
 private final HttpClient client = HttpClient.newHttpClient();
 private HttpResponse<String> request(DemoCaseApi api, String path, String body, boolean auth) throws Exception {
  var b = HttpRequest.newBuilder(URI.create(api.baseUrl() + path));
  if (auth) b.header("Authorization", DemoCaseApi.DEMO_AUTH);
  if (body != null) b.POST(HttpRequest.BodyPublishers.ofString(body)).header("Content-Type", "application/json");
  return client.send(b.build(), HttpResponse.BodyHandlers.ofString());
 }
 @Test void enforcesAuthentication() throws Exception {
  try (var api = new DemoCaseApi(0)) { assertEquals(401, request(api, "/cases/missing", null, false).statusCode()); }
 }
 @Test void rejectsMalformedInput() throws Exception {
  try (var api = new DemoCaseApi(0)) {
   for (String body : new String[]{"{", "{}", "{\"reference\":12}", "{\"reference\":\"BAD!\"}"}) {
    assertEquals(400, request(api, "/cases", body, true).statusCode());
   }
  }
 }
 @Test void restrictedResponseContainsNoCaseDetails() throws Exception {
  try (var api = new DemoCaseApi(0)) {
   var result = request(api, "/cases/" + DemoCaseApi.RESTRICTED_ID, null, true);
   assertEquals(403, result.statusCode());
   assertEquals("{\"error\":\"forbidden\"}", result.body());
  }
 }
 @Test void zeroDelayCaseCanBeRead() throws Exception {
  try (var api = new DemoCaseApi(0)) {
   var created = request(api, "/cases", "{\"reference\":\"TEST-FIXTURE\"}", true);
   assertEquals(201, created.statusCode());
   var id = new ObjectMapper().readTree(created.body()).get("id").asText();
   var found = request(api, "/cases/" + id, null, true);
   assertEquals(200, found.statusCode()); assertEquals(created.body(), found.body());
  }
 }
 @Test void delayAndIsolationAreReal() throws Exception {
  try (var first = new DemoCaseApi(3000); var second = new DemoCaseApi(0)) {
   var created = request(first, "/cases", "{\"reference\":\"TEST-DELAY\"}", true);
   var id = new ObjectMapper().readTree(created.body()).get("id").asText();
   assertEquals(404, request(first, "/cases/" + id, null, true).statusCode());
   assertEquals(404, request(second, "/cases/" + id, null, true).statusCode());
  }
 }
}
