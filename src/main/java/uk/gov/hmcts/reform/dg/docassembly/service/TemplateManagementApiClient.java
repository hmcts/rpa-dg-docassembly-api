package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TemplateManagementApiClient {

    private final String templateManagementApiUrl;

    private final AuthTokenGenerator authTokenGenerator;

    private final OkHttpClient httpClient;

    public TemplateManagementApiClient(
            AuthTokenGenerator authTokenGenerator,
            OkHttpClient httpClient,
            @Value("${template-management-api.base-url}${template-management-api.resource}") String templateManagementApiUrl) {
        this.authTokenGenerator = authTokenGenerator;
        this.httpClient = httpClient;
        this.templateManagementApiUrl = templateManagementApiUrl;
    }

    public InputStream getTemplate(TemplateIdDto templateIdDto) throws IOException {
        final Request request = new Request.Builder()
                .addHeader("Authorization", templateIdDto.getJwt())
                .addHeader("ServiceAuthorization", authTokenGenerator.generate())
                .url(templateManagementApiUrl + "/" + templateIdDto.getTemplateId())
                .get()
                .build();


        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new FormDefinitionRetrievalException(String.format(
                    "Could not retrieve a template. Http code and message %d, %s", response.code(), response.body().string()
            ));
        }

        return response.body().byteStream();

    }

}
