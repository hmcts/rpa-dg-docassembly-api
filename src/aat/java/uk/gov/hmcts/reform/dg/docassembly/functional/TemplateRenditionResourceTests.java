package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.dg.docassembly.Application;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.IdamHelper;
import uk.gov.hmcts.reform.dg.docassembly.testutil.S2sHelper;

import static uk.gov.hmcts.reform.dg.docassembly.testutil.Base64.base64;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, properties = "SpringBootTest")
@TestPropertySource(locations = "classpath:application-aat.yaml")
@ActiveProfiles("aat")
public class TemplateRenditionResourceTests {

    @Autowired
    private IdamHelper idamHelper;

    @Autowired
    private S2sHelper s2sHelper;

    private String idamAuth;
    private String s2sAuth;

    @Before
    public void setup() {
        idamAuth = idamHelper.getIdamToken();
        s2sAuth = s2sHelper.getS2sToken();
        System.out.println(idamAuth);
        System.out.println(s2sAuth);

        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testTemplateRendition() {
        Response response = RestAssured.given()
            .header("Authorization", idamAuth)
            .header("ServiceAuthorization", s2sAuth)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"templateId\":\""
                        + base64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",Env.getTestUrl() + "/api/template-renditions");

        System.out.println(response.getBody().print());
        Assert.assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testTemplateRenditionToDoc() {

        Response response = RestAssured.given()
            .header("Authorization", idamAuth)
            .header("ServiceAuthorization", s2sAuth)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOC\", \"templateId\":\""
                        + base64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",Env.getTestUrl() + "/api/template-renditions");

        System.out.println(response.getBody().print());
        Assert.assertEquals(200, response.getStatusCode());

    }


    @Test
    public void testTemplateRenditionToDocX() {
        Response response = RestAssured.given()
            .header("Authorization", idamAuth)
            .header("ServiceAuthorization", s2sAuth)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOCX\", \"templateId\":\""
                        + base64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",Env.getTestUrl() + "/api/template-renditions");

        System.out.println(response.getBody().print());
        Assert.assertEquals(200, response.getStatusCode());

    }

}
