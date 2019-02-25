package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;

public class TemplateRenditionResourceTests {

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testTemplateRendition() {

        Response response = testUtil
                .authRequest()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"formPayload\":{\"a\":1}, \"templateId\":\""
                        + testUtil.encodeBase64("FL-FRM-APP-ENG-00002.docx")
                        + "\"}")
                .request("POST",Env.getTestUrl() + "/api/template-renditions");

        System.out.println(response.getBody().print());
        Assert.assertEquals(200, response.getStatusCode());

    }
}
