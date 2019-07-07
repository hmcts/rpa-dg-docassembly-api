package uk.gov.hmcts.reform.dg.docassembly.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.mock.ClasspathResources;
import okhttp3.mock.MockInterceptor;
import okhttp3.mock.Rule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.dto.RenditionOutputType;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class TemplateRenditionServiceTest {

    private MockInterceptor interceptor = new MockInterceptor();

    private TemplateRenditionService templateRenditionService;

    private DmStoreUploader dmStoreUploader;

    private CreateTemplateRenditionDto createTemplateRenditionDto;

    @Before
    public void setup() throws IOException {

        interceptor.reset();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        dmStoreUploader = Mockito.mock(DmStoreUploader.class);

        templateRenditionService = new TemplateRenditionService(
            dmStoreUploader,
            new DocmosisApiClient(client, "http://tornado.com", "x")
        );

        createTemplateRenditionDto = createTemplateRenditionDto();
    }

    @Test
    public void testRendition() throws Exception {

        interceptor.addRule(new Rule.Builder()
                .post()
                .respond(ClasspathResources.resource("template1.docx")));

        Mockito.when(
                dmStoreUploader.uploadFile(Mockito.any(File.class),
                Mockito.any(CreateTemplateRenditionDto.class))).thenReturn(createTemplateRenditionDto);


        CreateTemplateRenditionDto templateRenditionOutputDto =
                templateRenditionService.renderTemplate(createTemplateRenditionDto);

        Assert.assertEquals("x", templateRenditionOutputDto.getRenditionOutputLocation());
    }

    @Test
    public void testRenditionWithNoData() throws Exception {
        createTemplateRenditionDto.setFormPayload(null);

        interceptor.addRule(new Rule.Builder()
                .post()
                .respond(ClasspathResources.resource("template1.docx")));

        Mockito.when(
                dmStoreUploader.uploadFile(Mockito.any(File.class),
                Mockito.any(CreateTemplateRenditionDto.class))).thenReturn(createTemplateRenditionDto);


        CreateTemplateRenditionDto templateRenditionOutputDto =
                templateRenditionService.renderTemplate(createTemplateRenditionDto);

        Assert.assertEquals("x", templateRenditionOutputDto.getRenditionOutputLocation());
    }

    @Test(expected = TemplateRenditionException.class)
    public void testRenditionException() throws Exception {

        interceptor.addRule(new Rule.Builder()
                .post()
                .respond("").code(500));

        Mockito.when(
                dmStoreUploader.uploadFile(Mockito.any(File.class),
                        Mockito.any(CreateTemplateRenditionDto.class))).thenReturn(createTemplateRenditionDto);

        templateRenditionService.renderTemplate(createTemplateRenditionDto);
    }

    private CreateTemplateRenditionDto createTemplateRenditionDto() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        CreateTemplateRenditionDto createTemplateRenditionDto = new CreateTemplateRenditionDto();

        createTemplateRenditionDto.setJwt("x");
        createTemplateRenditionDto.setTemplateId(new String(Base64.getEncoder().encode("1".getBytes())));
        createTemplateRenditionDto.setOutputType(RenditionOutputType.PDF);
        createTemplateRenditionDto.setFormPayload(objectMapper.readTree("{}"));
        createTemplateRenditionDto.setRenditionOutputLocation("x");

        return createTemplateRenditionDto;
    }
}
