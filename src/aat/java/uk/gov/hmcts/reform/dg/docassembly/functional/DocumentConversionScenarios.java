package uk.gov.hmcts.reform.dg.docassembly.functional;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import uk.gov.hmcts.reform.dg.docassembly.dto.PDFConversionDto;

import java.util.UUID;

public class DocumentConversionScenarios extends BaseTest {

    @Value("${test.url}")
    String testUrl;

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
        PDFConversionDto pdfConversionDto = new PDFConversionDto();
        pdfConversionDto.setDocumentId(UUID.fromString(newDocId.substring(newDocId.lastIndexOf('/') + 1)));

        JSONObject jsonObject = new JSONObject(pdfConversionDto);

        Response convertTaskResponse = testUtil.authRequest()
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .body(jsonObject)
            .request("POST", testUrl + "/api/convert");
        return convertTaskResponse;
    }

    private Response createAndProcessRequestFailure(String newDocId) {
        PDFConversionDto pdfConversionDto = new PDFConversionDto();
        pdfConversionDto.setDocumentId(UUID.fromString(newDocId.substring(newDocId.lastIndexOf('/') + 1)));

        JSONObject jsonObject = new JSONObject(pdfConversionDto);

        Response response = testUtil.authRequest()
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .body(jsonObject)
            .request("POST", testUrl + "/api/convert");

        return response;
    }

}
