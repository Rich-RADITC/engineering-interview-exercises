package io.raditc.interview.support;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.awaitility.core.ThrowingRunnable;

import java.time.Duration;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;

public class Helper {

    long delayMs = Long.getLong("demo.visibility.delay.ms", 500L);

    Duration visibilityDelay = Duration.ofMillis(delayMs);
    Duration deadline = visibilityDelay.plusMillis(500);
    public String validatePostResponse(Response response,String referenceValue)
    {
        response.then().statusCode(201).header("Content-Type", containsString("application/json"))
                .header("X-Correlation-ID", notNullValue())
                .body("id", notNullValue())
                .body("reference",equalTo(referenceValue));
        String id= response.jsonPath().getString("id");
        return id;
    }

    public void waitUntilAsserted(ThrowingRunnable assertion) {
        await()
                .atMost(deadline)
                .pollInterval(Duration.ofMillis(250))
                .untilAsserted(assertion);
    }

    public Response createCase(String reference, RequestSpecification requesType) {
        return given()
                .spec(requesType)
                .contentType("application/json")
                .body(Map.of("reference", reference))
                .post("/cases");
    }
}
