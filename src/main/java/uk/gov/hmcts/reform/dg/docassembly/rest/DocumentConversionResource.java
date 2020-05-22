package uk.gov.hmcts.reform.dg.docassembly.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dg.docassembly.dto.PDFConversionDto;
import uk.gov.hmcts.reform.dg.docassembly.service.FileToPDFConverterService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;


/**
 * REST controller for converting File types to PDF using Docmosis.
 */
@RestController
@RequestMapping("/api")
public class DocumentConversionResource {

    private FileToPDFConverterService fileToPDFConverterService;

    public DocumentConversionResource(FileToPDFConverterService fileToPDFConverterService) {
        this.fileToPDFConverterService = fileToPDFConverterService;
    }

    @ApiOperation(value = "Convert Document to PDF", notes = "A POST request to convert document type to PDF and return the converted document")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully redacted"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Unauthorised"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Server Error"),
    })
    @PostMapping("/convert")
    public ResponseEntity convert(HttpServletRequest request,
                                  @RequestBody PDFConversionDto pdfConversionDto) {
        try {
            File convertedFile = fileToPDFConverterService.convertFile(pdfConversionDto.getDocumentId());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(convertedFile));
            Tika tika = new Tika();

            return ResponseEntity.ok()
                    .contentLength(convertedFile.length())
                    .contentType(MediaType.parseMediaType(tika.detect(convertedFile)))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
