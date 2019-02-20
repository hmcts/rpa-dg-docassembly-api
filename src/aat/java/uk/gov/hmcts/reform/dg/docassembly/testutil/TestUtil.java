package uk.gov.hmcts.reform.dg.docassembly.testutil;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.springframework.http.MediaType;

public class TestUtil {

    private String s2sToken;
    private String idamToken;

    public RequestSpecification s2sAuthRequest() {
        RestAssured.useRelaxedHTTPSValidation();
        return RestAssured
                .given()
                .header("ServiceAuthorization", "Bearer " + getS2sToken());
    }

    public String getIdamToken() {
        return getIdamToken("test@test.com");
    }

    public String getIdamToken(String username) {
        if (idamToken == null) {
            createUser(username, "password");
            Integer id = findUserIdByUserEmail(username);
            String userId = id.toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", userId);
            jsonObject.put("role", "caseworker");

            Response response = RestAssured
                    .given()
                    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .formParam("id", userId)
                    .formParam("role", "caseworker")
                    .post(Env.getIdamURL() + "/testing-support/lease");

            idamToken = response.getBody().print();
        }
        return idamToken;
    }

    private Integer findUserIdByUserEmail(String email) {
        return RestAssured
                .get(Env.getIdamURL() + "/testing-support/accounts/" + email)
                .getBody()
                .jsonPath()
                .get("id");
    }

    public void createUser(String email, String password) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("forename", "test");
        jsonObject.put("surname", "test");

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(Env.getIdamURL() + "/testing-support/accounts");

    }


    public String getS2sToken() {

        if (s2sToken == null) {
            String otp = String.valueOf(new GoogleAuthenticator().getTotpPassword(Env.getS2SToken()));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("microservice", Env.getS2SServiceName());
            jsonObject.put("oneTimePassword", otp);

            Response response = RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .post(Env.getS2sUrl() + "/lease");
            s2sToken = response.getBody().asString();
            s2sToken = response.getBody().print();
        }

        return s2sToken;
    }
}

