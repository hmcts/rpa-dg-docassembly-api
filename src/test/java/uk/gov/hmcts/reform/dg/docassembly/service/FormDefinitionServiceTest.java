package uk.gov.hmcts.reform.dg.docassembly.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.IOException;
import java.io.InputStream;


@RunWith(MockitoJUnitRunner.class)
public class FormDefinitionServiceTest {

    @Mock
    TemplateManagementApiClient templateManagementApiClient;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    TemplateContentExtractor templateContentExtractor;

    @InjectMocks
    FormDefinitionService formDefinitionService;

    @Test
    public void testGetFormDefinition() throws Exception {

        ObjectMapper testObjectMapper = new ObjectMapper();

        TemplateIdDto dto =  Mockito.mock(TemplateIdDto.class);

        Mockito.when(templateManagementApiClient.getTemplate(Mockito.any(TemplateIdDto.class)))
                .thenReturn(Mockito.mock(InputStream.class));

        Mockito.when(templateContentExtractor.extractTextBetweenTags(
                Mockito.any(InputStream.class),
                Mockito.any(String.class),
                Mockito.any(String.class)))
                .thenReturn("{\"a\":1}");

        Mockito.when(objectMapper.readTree(
                Mockito.any(String.class)))
                .thenReturn(testObjectMapper.readTree("{\"a\":1}"));

        JsonNode json = formDefinitionService.getFormDefinition(dto);

        Assert.assertEquals(1, json.get("a").asInt());

    }

    @Test(expected = FormDefinitionRetrievalException.class)
    public void testGetFormDefinitionException() throws Exception {

        TemplateIdDto dto =  Mockito.mock(TemplateIdDto.class);

        Mockito.when(templateManagementApiClient.getTemplate(Mockito.any(TemplateIdDto.class)))
                .thenThrow(new IOException());

        formDefinitionService.getFormDefinition(dto);

    }

}
