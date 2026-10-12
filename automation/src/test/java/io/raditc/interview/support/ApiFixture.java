package io.raditc.interview.support;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
public abstract class ApiFixture {
 protected DemoCaseApi api;
 @BeforeEach void startApi() throws Exception { api = new DemoCaseApi(); }
 @AfterEach void stopApi() { if (api != null) api.close(); }
 protected RequestSpecification authenticatedRequest() {
  return anonymousRequest().header("Authorization", DemoCaseApi.DEMO_AUTH);
 }
 protected RequestSpecification anonymousRequest() {
  return new RequestSpecBuilder().setBaseUri(api.baseUrl()).setConfig(RestAssuredConfig.config()
   .httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", 1000)
    .setParam("http.socket.timeout", 1000))).build();
 }
 protected String restrictedCaseId() { return DemoCaseApi.RESTRICTED_ID; }
}
