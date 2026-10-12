package io.raditc.interview;
import io.raditc.interview.support.ApiFixture;
import io.restassured.response.Response;
import java.util.Map;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.equalTo;

/** Intentionally weak test. Improve it using automation/BRIEF.md. */
@ExtendWith(SerenityJUnit5Extension.class)
class CreateCaseIT extends ApiFixture {
 @Test void createsCase() throws Exception {
  Response created = given().spec(authenticatedRequest())
   .contentType("application/json")
   .body(Map.of("reference", "TEST-123")).post("/cases");
  String id = created.jsonPath().getString("id");
  Thread.sleep(3000);
  given().spec(authenticatedRequest()).get("/cases/" + id)
   .then().body("reference", equalTo("TEST-123"));
 }
 // TODO: add one negative test and describe your other priority scenarios.
}
