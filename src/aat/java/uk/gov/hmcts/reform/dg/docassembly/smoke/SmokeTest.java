package uk.gov.hmcts.reform.dg.docassembly.smoke;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;


//@SpringBootTest(classes = {TestUtil.class, EmTestConfig.class})
//@PropertySource(value = "classpath:application.yml")
//@RunWith(SpringRunner.class)
public class SmokeTest {

    @Autowired
    private TestUtil testUtil;

//    @Test
    public void testHealthEndpoint() {

        RestAssured.useRelaxedHTTPSValidation();

        RestAssured.given()
            .request("GET", testUtil.getTestUrl() + "/health")
            .then()
            .statusCode(200);


    }
}
