package io.raditc.interview;
import io.raditc.interview.support.ApiFixture;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.equalTo;
@ExtendWith(SerenityJUnit5Extension.class)
class SetupIT extends ApiFixture {
 @Test void localApiIsReady() {
  given().spec(anonymousRequest()).get("/health").then().statusCode(200).body("status", equalTo("ok"));
 }
}
