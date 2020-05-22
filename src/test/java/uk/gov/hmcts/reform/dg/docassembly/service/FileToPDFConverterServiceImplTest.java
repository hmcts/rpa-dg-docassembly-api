package uk.gov.hmcts.reform.dg.docassembly.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.dg.docassembly.conversion.DocmosisConverter;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.DocumentProcessingException;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.DocumentTaskProcessingException;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.FileTypeException;
import uk.gov.hmcts.reform.dg.docassembly.service.impl.FileToPDFConverterServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class FileToPDFConverterServiceImplTest {

    @InjectMocks
    private FileToPDFConverterServiceImpl fileToPDFConverterServiceImpl;

    @Mock
    private DmStoreDownloader dmStoreDownloader;

    @Mock
    DocmosisConverter docmosisConverter;

    private ObjectMapper mapper = new ObjectMapper();

    private String documentStoreResponse = "{"
        + "\"_embedded\": {"
        + "\"documents\": [{"
        + "\"modifiedOn\": \"2020-04-23T14:37:02+0000\","
        + "\"size\": 19496,"
        + "\"createdBy\": \"7f0fd7bf-48c0-4462-9056-38c1190e391f\","
        + "\"_links\": {"
        + "\"thumbnail\": {"
        + "\"href\": \"http://localhost:4603/documents/0e38e3ad-171f-4d27-bf54-e41f2ed744eb/thumbnail\""
        + "},"
        + "\"binary\": {"
        + "\"href\": \"http://localhost:4603/documents/0e38e3ad-171f-4d27-bf54-e41f2ed744eb/binary\""
        + "},"
        + "\"self\": {"
        + "\"href\": \"http://localhost:4603/documents/0e38e3ad-171f-4d27-bf54-e41f2ed744eb\""
        + "}"
        + "},"
        + "\"lastModifiedBy\": \"7f0fd7bf-48c0-4462-9056-38c1190e391f\","
        + "\"originalDocumentName\": \"stitched9163237694642183694.pdf\","
        + "\"mimeType\": \"application/pdf\","
        + "\"classification\": \"PUBLIC\","
        + "\"createdOn\": \"2020-04-23T14:37:02+0000\""
        + "}]"
        + "}"
        + "}";

    private static final UUID docStoreUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileToPDFConverterServiceImpl.fileExtensionsList = Arrays.asList("doc", "docx","pptx", "ppt", "rtf", "txt", "xlsx", "xls","jpeg");
    }

    @Test
    public void convertPptTest() throws DocumentTaskProcessingException, IOException {
        File mockFile = new File("potential_and_kinetic.ppt");
        Mockito.when(dmStoreDownloader.downloadFile(docStoreUUID.toString())).thenReturn(mockFile);
        Mockito.when(docmosisConverter.convertFileToPDF(mockFile)).thenReturn(mockFile);

        File convertedFile = fileToPDFConverterServiceImpl.convertFile(docStoreUUID);
        Assert.assertEquals(convertedFile.getName(), mockFile.getName());
    }

    @Test(expected = DocumentProcessingException.class)
    public void convertDocumentToPDFFailureTest() throws DocumentTaskProcessingException {

        UUID docStoreUUID = UUID.randomUUID();
        Mockito.when(dmStoreDownloader.downloadFile(docStoreUUID.toString())).thenThrow(DocumentTaskProcessingException.class);

        fileToPDFConverterServiceImpl.convertFile(docStoreUUID);
    }

    @Test(expected = FileTypeException.class)
    public void convertNotAllowedFileTypeTest() throws DocumentTaskProcessingException, IOException {
        File mockFile = new File("sample.ppsx");
        Mockito.when(dmStoreDownloader.downloadFile(docStoreUUID.toString())).thenReturn(mockFile);
        Mockito.when(docmosisConverter.convertFileToPDF(mockFile)).thenReturn(mockFile);

        File convertedFile = fileToPDFConverterServiceImpl.convertFile(docStoreUUID);
        Assert.assertEquals(convertedFile.getName(), mockFile.getName());
    }

}
