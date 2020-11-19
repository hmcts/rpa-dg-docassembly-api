package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class DocumentConversionScenarios extends BaseTest {

    @Test
    public void testPDFConversionWithWordDocument() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadDOCDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPDFConversionWithDocx() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadDocxDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }


    @Test
    public void testPDFConversionWithPptx() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadPptxDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }


    @Test
    public void testPDFConversionWithPPT() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadPptDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPDFConversionWithXlsx() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadXlsxDocumentAndReturnUrl();

        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPDFConversionWithXLS() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadXLSDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPDFConversionWithRTF() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadRTFDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPDFConversionWithTXT() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadTXTDocumentAndReturnUrl();
        Response response = createAndProcessRequest(newDocId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testFailedConversion() {
        Assume.assumeTrue(toggleProperties.isEnableDocumentConversionEndpoint());
        String newDocId = testUtil.uploadTXTDocumentAndReturnUrl();
        Response response = createAndProcessRequestFailure(newDocId + "567");

        Assert.assertEquals(400, response.getStatusCode());
    }

    private Response createAndProcessRequest(String newDocId) {

        UUID docId = UUID.fromString(newDocId.substring(newDocId.lastIndexOf('/') + 1));

        Response convertTaskResponse =
                testUtil
                        .authRequest()
                        .baseUri(testUtil.getTestUrl())
                        .contentType(APPLICATION_JSON_VALUE)
                        .post("/api/convert/" + docId);
        return convertTaskResponse;
    }

    private Response createAndProcessRequestFailure(String newDocId) {

        String docId = newDocId.substring(newDocId.lastIndexOf('/') + 1);

        Response response =
                testUtil
                        .authRequest()
                        .baseUri(testUtil.getTestUrl())
                        .contentType(APPLICATION_JSON_VALUE)
                        .post("/api/convert/" + docId);

        return response;
    }

}
