package uk.gov.hmcts.reform.dg.docassembly.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FormDefinitionService {

    private final TemplateManagementApiClient templateManagementApiClient;
    private final TemplateContentExtractor templateContentExtractor;
    private final ObjectMapper objectMapper;

    private final String startTag = "<<cs_{displaycode=‘1’}>>";
    private final String endTag = "<<es_>>";

    public FormDefinitionService(TemplateManagementApiClient templateManagementApiClient,
                                 TemplateContentExtractor templateContentExtractor,
                                 ObjectMapper objectMapper) {
        this.templateManagementApiClient = templateManagementApiClient;
        this.templateContentExtractor = templateContentExtractor;
        this.objectMapper = objectMapper;
    }

    public JsonNode getFormDefinition(TemplateIdDto templateIdDto) {

        try (InputStream inputStream = templateManagementApiClient.getTemplate(templateIdDto)) {

            String uiText = templateContentExtractor.extractTextBetweenTags(inputStream, startTag, endTag);

            return objectMapper.readTree(uiText);

        } catch (IOException e) {
            throw new FormDefinitionRetrievalException(
                    String.format("Could not retrieve form definition for template %s: '%s'",
                            templateIdDto.getTemplateId(),
                            e.getMessage()), e);
        }

    }

}
