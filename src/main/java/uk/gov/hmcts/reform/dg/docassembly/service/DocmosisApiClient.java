package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dg.docassembly.appinsights.DependencyProfiler;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;

import java.io.IOException;
import java.util.Base64;

@Service
public class DocmosisApiClient {


    private final String docmosisUrl;

    private final String docmosisAccessKey;

    private final OkHttpClient httpClient;

    public DocmosisApiClient(OkHttpClient httpClient,
                             @Value("${docmosis.render.endpoint}") String docmosisUrl,
                             @Value("${docmosis.accessKey}") String docmosisAccessKey) {
        this.httpClient = httpClient;
        this.docmosisUrl = docmosisUrl;
        this.docmosisAccessKey = docmosisAccessKey;
    }

    @DependencyProfiler(name = "docmosis", action = "render")
    public Response render(CreateTemplateRenditionDto createTemplateRenditionDto) throws IOException {

        MultipartBody requestBody = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "templateName",
                        new String(Base64.getDecoder().decode(createTemplateRenditionDto.getTemplateId())))
                .addFormDataPart(
                        "accessKey",
                        docmosisAccessKey)
                .addFormDataPart(
                        "outputName",
                        createTemplateRenditionDto.getFullOutputFilename())
                .addFormDataPart(
                        "data",
                        String.valueOf(createTemplateRenditionDto.getFormPayload()))
                .build();

        Request request = new Request.Builder()
                .url(docmosisUrl)
                .method("POST", requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }
}
