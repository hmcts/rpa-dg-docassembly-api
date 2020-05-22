package uk.gov.hmcts.reform.dg.docassembly.functional;

import org.json.JSONObject;
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
        String newDocId = testUtil.uploadDOCDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithDocx() {
        String newDocId = testUtil.uploadDocxDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithPptx() {
        String newDocId = testUtil.uploadPptxDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithPPT() {
        String newDocId = testUtil.uploadPptDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithXlsx() {
        String newDocId = testUtil.uploadXlsxDocumentAndReturnUrl();

        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithXLS() {
        String newDocId = testUtil.uploadXLSDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithRTF() {
        String newDocId = testUtil.uploadRTFDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    @Test
    public void testPDFConversionWithTXT() {
        String newDocId = testUtil.uploadTXTDocumentAndReturnUrl();
        createAndProcessRequest(newDocId);
    }

    private void createAndProcessRequest(String newDocId) {
        PDFConversionDto pdfConversionDto = new PDFConversionDto();
        pdfConversionDto.setDocumentId(UUID.fromString(newDocId.substring(newDocId.lastIndexOf('/') + 1)));

        JSONObject jsonObject = new JSONObject(pdfConversionDto);

        testUtil.authRequest()
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .body(jsonObject)
            .request("POST", testUrl + "/api/convert")
            .then()
            .statusCode(200);
    }

}
