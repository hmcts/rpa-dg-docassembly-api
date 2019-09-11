package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assume;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;

import static uk.gov.hmcts.reform.dg.docassembly.testutil.Base64.base64;
public class FormDefinitionResourceTests {
    TestUtil testUtil = new TestUtil();

    @Test
    public void testFormDefinitionGetTemplateWithUIDefinition() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isFormDefinitionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .request("GET",
                Env.getTestUrl()
                + "/api/form-definitions/"
                + base64("CV-CMC-GOR-ENG-0004-UI-Test.docx")
            );

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testFormDefinitionGetNotExistingTemplate() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isFormDefinitionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .request("GET",
                Env.getTestUrl()
                + "/api/form-definitions/"
                + base64("dont-exist.docx")
            );

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testFormDefinitionGetTemplateWithoutUIDefinition() {
        // If the Endpoint Toggles are enabled, continue, if not skip and ignore
        Assume.assumeTrue(Env.isFormDefinitionEndpointEnabled());

        Response response = testUtil
            .authRequest()
            .request("GET",
                Env.getTestUrl()
                + "/api/form-definitions/"
                + base64("FL-FRM-APP-ENG-00002.docx")
            );

        Assert.assertEquals(404, response.getStatusCode());
    }
}
