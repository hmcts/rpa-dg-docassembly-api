package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.junit.Test;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;

//import org.junit.Assert;

public class FormDefinitionResourceTests {

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testFormDefinitionGetTemplateWithoutUIDefinition() {

        Response response = testUtil.authRequest()
                .request("GET",
                        Env.getTestUrl()
                                + "/api/form-definitions/"
                                + testUtil.encodeBase64("FL-FRM-APP-ENG-00002.docx"));

        /*
        System.out.println(response.getBody().print());
        Assert.assertEquals(404, response.getStatusCode());
         */

    }

}
