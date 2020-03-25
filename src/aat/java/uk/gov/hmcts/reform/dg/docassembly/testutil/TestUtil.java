package uk.gov.hmcts.reform.dg.docassembly.testutil;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.em.test.dm.DmHelper;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TestUtil {

    private String idamAuth;
    private String s2sAuth;
    private final String invalidAuthToken = "238beab2-b563-4fee-80fa-63f224bc56f6";
    private final String invalidServiceAuthToken = "438bexy2-b545-4fef-80ab-63f234ae57f58f6";

    @Autowired
    private IdamHelper idamHelper;
    @Autowired
    private S2sHelper s2sHelper;
    @Autowired
    private DmHelper dmHelper;

    @Value("${test.url}")
    private String testUrl;

    @PostConstruct
    public void init() {
        idamHelper.createUser("a@b.com", Stream.of("caseworker").collect(Collectors.toList()));
        RestAssured.useRelaxedHTTPSValidation();
        idamAuth = idamHelper.authenticateUser("a@b.com");
        s2sAuth = s2sHelper.getS2sToken();
    }

    public RequestSpecification authRequest() {
        return RestAssured
            .given()
            .header("Authorization", idamAuth)
            .header("ServiceAuthorization", s2sAuth);
    }

    public String getTestUrl() {
        return testUrl;
    }

    private RequestSpecification s2sAuthRequest() {
        return RestAssured.given().header("ServiceAuthorization", s2sAuth);
    }

    public RequestSpecification emptyIdamAuthRequest() {
        return s2sAuthRequest().header("Authorization", null);
    }

    public RequestSpecification emptyIdamAuthAndEmptyS2SAuth() {
        return RestAssured.given().header("ServiceAuthorization", null).header("Authorization", null);
    }

    public RequestSpecification randomHeadersInRequest() {
        return RestAssured.given().header("randomHeader1", "random1").header("randomHeader2", "random2");
    }

    public RequestSpecification validAuthRequestWithEmptyS2SAuth() {
        return emptyS2sAuthRequest().header("Authorization", idamAuth);
    }

    public RequestSpecification validS2SAuthWithEmptyIdamAuth() {
        return s2sAuthRequest().header("Authorization", null);
    }

    private RequestSpecification emptyS2sAuthRequest() {
        return RestAssured.given().header("ServiceAuthorization", null);
    }

    public RequestSpecification invalidIdamAuthrequest() {
        return s2sAuthRequest().header("Authorization", invalidAuthToken);
    }

    public RequestSpecification noHeadersInRequest() {
        return RestAssured.given();
    }

    public RequestSpecification invalidS2SAuth() {
        return invalidS2sAuthRequest().header("Authorization", idamAuth);
    }

    private RequestSpecification invalidS2sAuthRequest() {
        return RestAssured.given().header("ServiceAuthorization", invalidServiceAuthToken);
    }

}
