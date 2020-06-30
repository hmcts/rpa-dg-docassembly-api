package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.http.MediaType;

import static uk.gov.hmcts.reform.dg.docassembly.testutil.Base64.base64;

public class TemplateRenditionResourceTests extends BaseTest {

    @Test
    public void testTemplateRendition() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"formPayload\":{\"a\":1}, \"templateId\":\""
                    + base64("FL-FRM-APP-ENG-00002.docx")
                    + "\"}")
            .request("POST",testUtil.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testTemplateRenditionToDoc() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOC\", \"templateId\":\""
                    + base64("FL-FRM-APP-ENG-00002.docx")
                    + "\"}")
            .request("POST",testUtil.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testTemplateRenditionToDocX() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        Response response = testUtil
                .authRequest()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOCX\", \"templateId\":\""
                        + base64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",testUtil.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testTemplateRenditionToOutputName() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        Response response = testUtil
                .authRequest()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOCX\", \"outputName\":\"test-output-name\", \"templateId\":\""
                        + base64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",testUtil.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
        JSONObject jsonBody = new JSONObject(response.body().toString());
        String dmStoreHref = jsonBody.getString("renditionOutputLocation");

//        Request request = new Request.Builder()
//                .addHeader("user-id", getUserId(createTemplateRenditionDto))
//                .addHeader("user-roles", "caseworker")
//                .addHeader("ServiceAuthorization", authTokenGenerator.generate())
//                .url(dmStoreAppBaseUrl + ENDPOINT)
//                .method("POST", requestBody)
//                .build();
//
//        okhttp3.Response response = okHttpClient.newCall(request).execute();
    }
}
