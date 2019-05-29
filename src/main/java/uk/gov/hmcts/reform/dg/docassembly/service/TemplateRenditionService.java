package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class TemplateRenditionService {

    private final DmStoreUploader dmStoreUploader;
    private final DocmosisApiClient docmosisApiClient;

    public TemplateRenditionService(DmStoreUploader dmStoreUploader, DocmosisApiClient docmosisApiClient) {
        this.dmStoreUploader = dmStoreUploader;
        this.docmosisApiClient = docmosisApiClient;
    }

    public CreateTemplateRenditionDto renderTemplate(CreateTemplateRenditionDto createTemplateRenditionDto)
            throws IOException {

        Response response = this.docmosisApiClient.render(createTemplateRenditionDto);

        if (!response.isSuccessful()) {
            throw new TemplateRenditionException(
                    String.format("Could not render a template %s. HTTP response and message %d, %s",
                            createTemplateRenditionDto.getTemplateId(), response.code(), response.body().string()));
        }

        File file = File.createTempFile(
                "docmosis-rendition",
                createTemplateRenditionDto.getOutputType().getFileExtension());

        IOUtils.copy(response.body().byteStream(), new FileOutputStream(file));

        dmStoreUploader.uploadFile(file, createTemplateRenditionDto);

        return createTemplateRenditionDto;
    }
}
