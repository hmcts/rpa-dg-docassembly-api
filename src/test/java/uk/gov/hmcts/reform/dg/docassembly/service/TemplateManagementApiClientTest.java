package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.OkHttpClient;
import okhttp3.mock.ClasspathResources;
import okhttp3.mock.MockInterceptor;
import okhttp3.mock.Rule;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.InputStream;

public class TemplateManagementApiClientTest {

    AuthTokenGenerator authTokenGenerator;

    MockInterceptor interceptor = new MockInterceptor();

    @InjectMocks
    TemplateManagementApiClient templateManagementApiClient;

    @Before
    public void setup() {

        interceptor.reset();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        authTokenGenerator = Mockito.mock(AuthTokenGenerator.class);

        templateManagementApiClient = new TemplateManagementApiClient(
                authTokenGenerator,
                client, "http://template-management-api/templates");
    }


    @Test
    public void testRetrieval() throws Exception {
        TemplateIdDto templateIdDto = new TemplateIdDto();

        templateIdDto.setJwt("x");
        templateIdDto.setTemplateId("x");

        Mockito.when(authTokenGenerator.generate()).thenReturn("x");

        interceptor.addRule(new Rule.Builder()
                      .get()
                      .respond(ClasspathResources.resource("template1.docx")));

        InputStream inputStream = templateManagementApiClient.getTemplate(templateIdDto);

        IOUtils.contentEquals(inputStream, ClasspathResources.resource("template1.docx"));
    }

}
