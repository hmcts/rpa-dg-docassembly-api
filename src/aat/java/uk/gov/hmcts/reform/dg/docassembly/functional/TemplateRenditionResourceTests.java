package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.http.MediaType;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;

import static uk.gov.hmcts.reform.dg.docassembly.testutil.Base64.base64;

public class TemplateRenditionResourceTests {

    TestUtil testUtil = new TestUtil();

    @Test
    public void testTemplateRendition() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isTemplateRenditionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"formPayload\":{\"a\":1}, \"templateId\":\""
                    + base64("FL-FRM-APP-ENG-00002.docx")
                    + "\"}")
            .request("POST",Env.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testTemplateRenditionToDoc() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isTemplateRenditionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOC\", \"templateId\":\""
                    + base64("FL-FRM-APP-ENG-00002.docx")
                    + "\"}")
            .request("POST",Env.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testTemplateRenditionToDocX() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isTemplateRenditionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"formPayload\":{\"a\":1}, \"outputType\":\"DOCX\", \"templateId\":\""
                    + base64("FL-FRM-APP-ENG-00002.docx")
                    + "\"}")
            .request("POST",Env.getTestUrl() + "/api/template-renditions");

        Assert.assertEquals(200, response.getStatusCode());
    }
}
