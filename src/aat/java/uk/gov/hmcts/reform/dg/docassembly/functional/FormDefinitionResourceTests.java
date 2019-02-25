package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import jodd.util.Base64;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.dg.docassembly.testutil.Env;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;

public class FormDefinitionResourceTests {

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testFormDefinitionGet() {

        Response response = testUtil.authRequest()
                .request("GET",
                        Env.getTestUrl()
                                + "/api/form-definitions/"
                                + Base64.encodeToString("FL-FRM-APP-ENG-00002.docx"));

        System.out.println(response.getBody().print());
        Assert.assertEquals(404, response.getStatusCode());

    }
}
