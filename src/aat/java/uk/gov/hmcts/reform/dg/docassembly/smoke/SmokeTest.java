package uk.gov.hmcts.reform.dg.docassembly.smoke;

import io.restassured.RestAssured;
import org.junit.Test;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;

public class SmokeTest {

    @Test
    public void testHealthEndpoint() {

        RestAssured.useRelaxedHTTPSValidation();

        RestAssured.given()
            .request("GET", Env.getTestUrl() + "/health")
            .then()
            .statusCode(200);


    }
}
