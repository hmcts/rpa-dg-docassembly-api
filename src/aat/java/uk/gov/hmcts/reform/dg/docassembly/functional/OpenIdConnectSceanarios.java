package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static uk.gov.hmcts.reform.dg.docassembly.testutil.Base64.base64;

public class OpenIdConnectSceanarios extends BaseTest {

    public static final String API_TEMPLATE_RENDITIONS_URL = "/api/template-renditions";

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Test
    public void testValidAuthenticationAndAuthorisation() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        final Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST",testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);

        assertEquals(200, response.getStatusCode());
    }

    @Test // Invalid S2SAuth
    public void testInvalidS2SAuth() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        final Response response = testUtil
            .invalidIdamAuthrequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST",testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);

        assertEquals(401, response.getStatusCode());
    }

    @Test
    public void testWithInvalidIdamAuth() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        final Response response = testUtil
            .invalidIdamAuthrequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST",testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);

        assertEquals(401, response.getStatusCode());

    }

    @Test
    public void testWithEmptyS2SAuth() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        exceptionThrown.expect(IllegalArgumentException.class);
        final Response response = testUtil
            .validAuthRequestWithEmptyS2SAuth()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST",testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);

        assertEquals(401, response.getStatusCode());

    }

    @Test
    public void testWithEmptyIdamAuthAndValidS2SAuth() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        exceptionThrown.expect(IllegalArgumentException.class);

        final Response response = testUtil
            .validS2SAuthWithEmptyIdamAuth()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST", testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);

        exceptionThrown.expect(IllegalArgumentException.class);

    }

    @Test
    public void testIdamAuthAndS2SAuthAreEmpty() throws IOException, InterruptedException {
        assumeTrue(toggleProperties.isEnableTemplateRenditionEndpoint());

        exceptionThrown.expect(IllegalArgumentException.class);

        final Response response = testUtil
            .validS2SAuthWithEmptyIdamAuth()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(getBodyForRequest())
            .request("POST", testUtil.getTestUrl() + API_TEMPLATE_RENDITIONS_URL);
    }

    private String getBodyForRequest() {
        return "{\"formPayload\":{\"a\":1}, \"outputType\":\"DOC\", \"templateId\":\"" + base64("FL-FRM-APP-ENG-00002.docx") + "\"}";
    }
}
