package uk.gov.hmcts.reform.dg.docassembly.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.DocumentTaskProcessingException;
import uk.gov.hmcts.reform.dg.docassembly.service.impl.DmStoreDownloaderImpl;

import java.io.IOException;
import java.util.UUID;

public class DmStoreDownloaderImplTest {

    @Autowired
    DmStoreDownloader dmStoreDownloader;

    AuthTokenGenerator authTokenGenerator;

    @Before
    public void setup() {

        OkHttpClient client = new OkHttpClient.Builder()
                    .build();

        authTokenGenerator = Mockito.mock(AuthTokenGenerator.class);

        dmStoreDownloader = new DmStoreDownloaderImpl(client,
            authTokenGenerator,
            "http://localhost:4603",
            new ObjectMapper());
    }

    @Test(expected = DocumentTaskProcessingException.class)
    public void invalidDocumentId() throws DocumentTaskProcessingException {
        dmStoreDownloader.downloadFile("abc");
    }

    @Test (expected = DocumentTaskProcessingException.class)
    public void testRuntimeExceptionThrown() throws DocumentTaskProcessingException {

        UUID dmStoreDocId = UUID.randomUUID();
        Mockito.when(dmStoreDownloader.downloadFile(dmStoreDocId.toString())).thenThrow(RuntimeException.class);
        dmStoreDownloader.downloadFile(dmStoreDocId.toString());

    }

    @Test (expected = DocumentTaskProcessingException.class)
    public void testIOExceptionThrown() throws DocumentTaskProcessingException {

        UUID dmStoreDocId = UUID.randomUUID();
        Mockito.when(dmStoreDownloader.downloadFile(dmStoreDocId.toString())).thenThrow(IOException.class);
        dmStoreDownloader.downloadFile(dmStoreDocId.toString());

    }
}
