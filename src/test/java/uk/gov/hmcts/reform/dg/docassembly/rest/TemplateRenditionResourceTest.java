package uk.gov.hmcts.reform.dg.docassembly.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.auth.checker.core.service.Service;
import uk.gov.hmcts.reform.auth.checker.core.service.ServiceRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.core.user.UserRequestAuthorizer;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.service.TemplateRenditionService;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TemplateRenditionResourceTest {

    @MockBean
    private TemplateRenditionService templateRenditionService;

    @MockBean
    private ServiceRequestAuthorizer serviceRequestAuthorizer;

    @MockBean
    private UserRequestAuthorizer userRequestAuthorizer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCallTemplateRenditionService() throws Exception {

        Mockito
                .when(serviceRequestAuthorizer.authorise(Mockito.any(HttpServletRequest.class)))
                .thenReturn(new Service("ccd"));

        Mockito
                .when(userRequestAuthorizer.authorise(Mockito.any(HttpServletRequest.class)))
                .thenReturn(new User("john", Stream.of("caseworker").collect(Collectors.toSet())));

        CreateTemplateRenditionDto templateRenditionOutputDto = new CreateTemplateRenditionDto();
        templateRenditionOutputDto.setRenditionOutputLocation("x");

        Mockito
                .when(templateRenditionService.renderTemplate(Mockito.any(CreateTemplateRenditionDto.class)))
                .thenReturn(templateRenditionOutputDto);

        this.mockMvc
                .perform(post("/api/template-renditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"outputType\":\"PDF\", \"templateId\":\"1\"}")
                        .header("Authorization", "xxx")
                        .header("ServiceAuthorization", "xxx"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito
                .verify(templateRenditionService, Mockito.times(1))
                .renderTemplate(Mockito.any(CreateTemplateRenditionDto.class));
    }
}
