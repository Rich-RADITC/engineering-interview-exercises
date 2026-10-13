package io.raditc.interview;

import io.raditc.interview.support.ApiFixture;
import io.raditc.interview.support.Helper;
import io.restassured.response.Response;

import java.util.Map;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Intentionally weak test. Improve it using automation/BRIEF.md.
 */
@ExtendWith(SerenityJUnit5Extension.class)
class CreateCaseIT extends ApiFixture {
    Helper helper = new Helper();

    @Test
    void createsCase() throws Exception {
        Response created = given().spec(authenticatedRequest())
                .contentType("application/json")
                .body(Map.of("reference", "TEST-123")).post("/cases");
        String id = created.jsonPath().getString("id");
        Thread.sleep(3000);
        given().spec(authenticatedRequest()).get("/cases/" + id).then().body("reference", equalTo("TEST-123")).statusCode(200);

        // with helper function
//        Response created = given().spec(authenticatedRequest())
//                .contentType("application/json")
//                .body(Map.of("reference", "TEST-123")).post("/cases");
//        // String id = created.jsonPath().getString("id");
//        String id = helper.validatePostResponse(created, "TEST-123");
//        helper.waitUntilAsserted(() -> {
//            given().spec(authenticatedRequest()).get("/cases/" + id).then().body("reference", equalTo("TEST-123")).statusCode(200);
//        });
    }

    // TODO: add one negative test and describe your other priority scenarios.
    // 401 Response- Authentication error
    @Test
    void Missingauthentication() throws Exception {

        Response created = helper.createCase("TEST-123", anonymousRequest());

        try {
            created.then().statusCode(401);
        } catch (AssertionError e) {
            throw new Exception(
                    "Expected status code 401, but received "
                            + created.statusCode(), e);
        }
    }

    //404 Response- Request does not exist

    @Test
    void unknownId() throws Exception {
        Response created = given().spec(authenticatedRequest())
                .contentType("application/json")
                .body(Map.of("reference", "TEST-12")).log().all().post("/cases");
        created.then().log().all().statusCode(201);
        String id = created.jsonPath().getString("id");
        given().spec(authenticatedRequest()).get("/cases/" + id)
                .then().statusCode(404);
    }

    //400 statuscode- Invalid request body
    @Test
    void restricted() throws Exception {
        Response created = given().spec(authenticatedRequest())
                .contentType("application/json")
                .body(Map.of("reference", restrictedCaseId())).post("/cases");
        created.then().statusCode(400);
    }

    @Test
    void forbidden() throws Exception {
        Response created = given().spec(authenticatedRequest())
                .contentType("application/json")
                .body(Map.of("reference", "TEST-123")).post("/cases");
        created.then().statusCode(201);
        String id = created.jsonPath().getString("id");
        //Thread.sleep(3000);
        given().spec(authenticatedRequest()).get("/cases/" + restrictedCaseId())
                .then().statusCode(403);
    }
}


