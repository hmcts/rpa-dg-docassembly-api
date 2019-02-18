package uk.gov.hmcts.reform.dg.docassembly.service.impl;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TemplateManagementApiClient {

    @Value("${template-management-api.base-url}${template-management-api.resource}")
    private String templateManagementApiUrl;

    private final AuthTokenGenerator authTokenGenerator;

    private final OkHttpClient httpClient;

    public TemplateManagementApiClient(AuthTokenGenerator authTokenGenerator, OkHttpClient httpClient) {
        this.authTokenGenerator = authTokenGenerator;
        this.httpClient = httpClient;
    }

    public InputStream getTemplate(TemplateIdDto templateIdDto) throws IOException {
        final Request request = new Request.Builder()
                .addHeader("Authorization", templateIdDto.getJwt())
                .addHeader("ServiceAuthorization", authTokenGenerator.generate())
                .url(String.format("%s/%s", templateManagementApiUrl, templateIdDto.getTemplateId()))
                .get()
                .build();

        return httpClient.newCall(request).execute().body().byteStream();

    }

}
