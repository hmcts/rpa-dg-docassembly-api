package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateRenditionOutputDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class TemplateRenditionService {

    private final String docmosisUrl;

    private final String docmosisAccessKey;

    private final OkHttpClient httpClient;

    public TemplateRenditionService(OkHttpClient httpClient,
                                    @Value("${docmosis.convert.endpoint}") String docmosisUrl,
                                    @Value("${docmosis.accessKey}")String docmosisAccessKey) {
        this.httpClient = httpClient;
        this.docmosisUrl = docmosisUrl;
        this.docmosisAccessKey = docmosisAccessKey;
    }

    public TemplateRenditionOutputDto renderTemplate(CreateTemplateRenditionDto createTemplateRenditionDto)
            throws IOException {

        String tempFileName = String.format("%s%s",
                UUID.randomUUID().toString(),
                createTemplateRenditionDto.getOutputType().getFileExtension());

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
                        tempFileName)
                .addFormDataPart(
                        "data",
                        createTemplateRenditionDto.getFormPayload().asText())
                .build();

        Request request = new Request.Builder()
                .url(docmosisUrl)
                .method("POST", requestBody)
                .build();

        File file = File.createTempFile(
                "docmosis-rendition",
                createTemplateRenditionDto.getOutputType().getFileExtension());

        IOUtils.copy(httpClient.newCall(request).execute().body().byteStream(), new FileOutputStream(file));

        TemplateRenditionOutputDto templateRenditionOutputDto = new TemplateRenditionOutputDto();
        templateRenditionOutputDto.setOutputFileName(tempFileName);
        templateRenditionOutputDto.setRendition(new FileInputStream(file));
        templateRenditionOutputDto.setRenditionSize(file.length());
        templateRenditionOutputDto.setOutputFileName(tempFileName);
        templateRenditionOutputDto.setTemplateId(createTemplateRenditionDto.getTemplateId());

        return templateRenditionOutputDto;
    }

}
