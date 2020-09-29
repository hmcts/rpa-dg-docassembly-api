package uk.gov.hmcts.reform.dg.docassembly.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.dg.docassembly.Application;
import uk.gov.hmcts.reform.dg.docassembly.service.FileToPDFConverterService;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.DocumentProcessingException;
import uk.gov.hmcts.reform.dg.docassembly.service.exception.FileTypeException;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
public class DocumentConversionResourceTest {

    @InjectMocks
    private DocumentConversionResource documentConversionResource;

    @Mock
    private FileToPDFConverterService fileToPDFConverterService;

    private UUID docId = null;

    private static final File TEST_PDF_FILE = new File(
            ClassLoader.getSystemResource("Test.pdf").getPath());

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        docId = UUID.randomUUID();
    }

    @Test
    public void shouldConvertDocument() {

        when(fileToPDFConverterService.convertFile(docId))
            .thenReturn(TEST_PDF_FILE);

        ResponseEntity response = documentConversionResource.convert(docId);
        assertEquals(200, response.getStatusCodeValue());

        verify(fileToPDFConverterService, Mockito.atMost(1))
            .convertFile(docId);
    }

    @Test
    public void shouldFailConvertDocumentToPDF() {

        when(fileToPDFConverterService.convertFile(docId))
                .thenThrow(DocumentProcessingException.class);

        ResponseEntity response = documentConversionResource.convert(docId);
        assertEquals(400, response.getStatusCodeValue());

        verify(fileToPDFConverterService, Mockito.atMost(1))
                .convertFile(docId);
    }

    @Test
    public void shouldFailConvertOtherThanAcceptedFormatDocumentToPDF() {

        when(fileToPDFConverterService.convertFile(docId))
            .thenThrow(FileTypeException.class);

        ResponseEntity response = documentConversionResource.convert(docId);
        assertEquals(400, response.getStatusCodeValue());

        verify(fileToPDFConverterService, Mockito.atMost(1))
            .convertFile(docId);
    }
}
