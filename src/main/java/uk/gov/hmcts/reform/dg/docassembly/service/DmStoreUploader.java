package uk.gov.hmcts.reform.dg.docassembly.service;

import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.auth.checker.core.SubjectResolver;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.dg.docassembly.appinsights.DependencyProfiler;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DmStoreUploader {

    private final OkHttpClient okHttpClient;

    private final AuthTokenGenerator authTokenGenerator;

    private final SubjectResolver<User> userResolver;

    private final String dmStoreAppBaseUrl;

    private static final String ENDPOINT = "/documents";

    public DmStoreUploader(OkHttpClient okHttpClient,
                           AuthTokenGenerator authTokenGenerator,
                           @Value("${dm-store-app.base-url}") String dmStoreAppBaseUrl,
                           SubjectResolver<User> userResolver) {
        this.okHttpClient = okHttpClient;
        this.authTokenGenerator = authTokenGenerator;
        this.dmStoreAppBaseUrl = dmStoreAppBaseUrl;
        this.userResolver = userResolver;
    }

    @DependencyProfiler(name = "dm-store", action = "upload")
    public CreateTemplateRenditionDto uploadFile(InputStream input, CreateTemplateRenditionDto createTemplateRenditionDto) {
        if (createTemplateRenditionDto.getRenditionOutputLocation() != null) {
            uploadNewDocumentVersion(input, createTemplateRenditionDto);
        } else {
            uploadNewDocument(input, createTemplateRenditionDto);
        }
        return createTemplateRenditionDto;
    }

    private void uploadNewDocument(InputStream input, CreateTemplateRenditionDto createTemplateRenditionDto) {

        try {

            MultipartBody requestBody = new MultipartBody
                    .Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("classification", "PUBLIC")
                    .addFormDataPart(
                        "file",
                        "docmosis-rendition",
                        createBody(MediaType.get(createTemplateRenditionDto.getOutputType().getMediaType()), input)
                    ).build();

            Request request = new Request.Builder()
                    .addHeader("user-id", getUserId(createTemplateRenditionDto))
                    .addHeader("user-roles", "caseworker")
                    .addHeader("ServiceAuthorization", authTokenGenerator.generate())
                    .url(dmStoreAppBaseUrl + ENDPOINT)
                    .method("POST", requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(response.body().string());
                String documentUri = jsonObject
                        .getJSONObject("_embedded")
                        .getJSONArray("documents")
                        .getJSONObject(0)
                        .getJSONObject("_links")
                        .getJSONObject("self")
                        .getString("href");

                createTemplateRenditionDto.setRenditionOutputLocation(documentUri);
            } else {
                throw new DocumentUploaderException("Couldn't upload the file. Response code: " + response.code(), null);
            }

        } catch (RuntimeException | IOException e) {
            throw new DocumentUploaderException(String.format("Couldn't upload the file:  %s", e.getMessage()), e);
        }
    }

    private void uploadNewDocumentVersion(InputStream input, CreateTemplateRenditionDto createTemplateRenditionDto) {
        try {

            MultipartBody requestBody = new MultipartBody
                    .Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        "docmosis-rendition",
                        createBody(MediaType.get(createTemplateRenditionDto.getOutputType().getMediaType()), input)
                    )
                    .build();

            Request request = new Request.Builder()
                    .addHeader("user-id", getUserId(createTemplateRenditionDto))
                    .addHeader("user-roles", "caseworker")
                    .addHeader("ServiceAuthorization", authTokenGenerator.generate())
                    .url(createTemplateRenditionDto.getRenditionOutputLocation())
                    .method("POST", requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new DocumentUploaderException("Couldn't upload the file. Response code: " + response.code(), null);
            }

        } catch (RuntimeException | IOException e) {
            throw new DocumentUploaderException("Couldn't upload the file", e);
        }
    }

    private String getUserId(CreateTemplateRenditionDto createTemplateRenditionDto) {
        User user = userResolver.getTokenDetails(createTemplateRenditionDto.getJwt());
        return user.getPrincipal();
    }

    public static RequestBody createBody(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
