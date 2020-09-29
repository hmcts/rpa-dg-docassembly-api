package uk.gov.hmcts.reform.dg.docassembly.service;

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

    private static final UUID docStoreUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileToPDFConverterServiceImpl.fileExtensionsList = Arrays.asList("doc", "docx","pptx", "ppt", "rtf", "txt", "xlsx", "xls","jpeg");
    }

    @Test
    public void convertDocumentSuccessTest() throws DocumentTaskProcessingException, IOException {
        File mockFile = new File("potential_and_kinetic.ppt");
        Mockito.when(dmStoreDownloader.downloadFile(docStoreUUID.toString())).thenReturn(mockFile);
        Mockito.when(docmosisConverter.convertFileToPDF(mockFile)).thenReturn(mockFile);

        File convertedFile = fileToPDFConverterServiceImpl.convertFile(docStoreUUID);
        Assert.assertEquals(convertedFile.getName(), mockFile.getName());
    }

    @Test(expected = DocumentProcessingException.class)
    public void convertNotProgressAsDmStoreDownloaderException() throws DocumentTaskProcessingException {

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

    @Test(expected = DocumentProcessingException.class)
    public void convertNotAllowedAsIOExceptionIsThrownTest() throws DocumentTaskProcessingException, IOException {
        File mockFile = new File("potential_and_kinetic.ppt");
        Mockito.when(dmStoreDownloader.downloadFile(docStoreUUID.toString())).thenReturn(mockFile);
        Mockito.when(docmosisConverter.convertFileToPDF(mockFile)).thenThrow(IOException.class);
        File convertedFile = fileToPDFConverterServiceImpl.convertFile(docStoreUUID);
        Assert.assertEquals(convertedFile.getName(), mockFile.getName());
    }

}
