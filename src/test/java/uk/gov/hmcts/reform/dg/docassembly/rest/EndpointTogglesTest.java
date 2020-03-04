package uk.gov.hmcts.reform.dg.docassembly.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.dg.docassembly.Application;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;
import uk.gov.hmcts.reform.dg.docassembly.service.FormDefinitionService;
import uk.gov.hmcts.reform.dg.docassembly.service.TemplateRenditionService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestSecurityConfiguration.class})
public class EndpointTogglesTest extends BaseTest {

    @MockBean
    TemplateRenditionService templateRenditionService;

    @MockBean
    FormDefinitionService formDefinitionService;

    @BeforeClass
    public static void setup() {
        System.setProperty("endpoint-toggles.form-definitions", "false");
        System.setProperty("endpoint-toggles.template-renditions", "false");
    }

    @AfterClass
    public static void cleanup() {
        System.setProperty("endpoint-toggles.form-definitions", "true");
        System.setProperty("endpoint-toggles.template-renditions", "true");
    }


    @Test
    public void testFormDefinitionToggle() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        when(formDefinitionService.getFormDefinition(Mockito.any(TemplateIdDto.class)))
                .thenReturn(Optional.of(objectMapper.readTree("{}")));

        restLogoutMockMvc
                .perform(get("/api/form-definitions/1234")
                        .header("Authorization", "xxx")
                        .header("ServiceAuthorization", "xxx"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCallTemplateRenditionService() throws Exception {

        CreateTemplateRenditionDto templateRenditionOutputDto = new CreateTemplateRenditionDto();
        templateRenditionOutputDto.setRenditionOutputLocation("x");

        when(templateRenditionService.renderTemplate(Mockito.any(CreateTemplateRenditionDto.class)))
                .thenReturn(templateRenditionOutputDto);

        restLogoutMockMvc
                .perform(post("/api/template-renditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"outputType\":\"PDF\", \"templateId\":\"1\"}")
                        .header("Authorization", "xxx")
                        .header("ServiceAuthorization", "xxx"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
