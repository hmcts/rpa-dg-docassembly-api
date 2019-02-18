package uk.gov.hmcts.reform.dg.docassembly.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FormDefinitionService {

    private final TemplateManagementApiClient templateManagementApiClient;
    private final ObjectMapper objectMapper;

    private final String startTag = "<<cs_{displaycode=‘1’}>>";
    private final String endTag = "<<es_>>";

    public FormDefinitionService(TemplateManagementApiClient templateManagementApiClient, ObjectMapper objectMapper) {
        this.templateManagementApiClient = templateManagementApiClient;
        this.objectMapper = objectMapper;
    }

    public JsonNode getFormDefinition(TemplateIdDto templateIdDto) {

        try (InputStream inputStream = templateManagementApiClient.getTemplate(templateIdDto)) {

            XWPFDocument doc = new XWPFDocument(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String docText = extractor.getText();

            int startIndex = docText.indexOf(startTag) + startTag.length();
            int endIndex = docText.indexOf(endTag, startIndex);

            return objectMapper.readTree(docText.substring(startIndex, endIndex));

        } catch(IOException e) {
            throw new FormDefinitionRetrievalException(
                    String.format("Could not retrieve form definition:'%s'", e.getMessage()), e);
        }

    }

}
