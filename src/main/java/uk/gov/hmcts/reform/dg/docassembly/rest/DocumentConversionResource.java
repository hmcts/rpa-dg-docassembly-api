package uk.gov.hmcts.reform.dg.docassembly.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dg.docassembly.service.FileToPDFConverterService;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;


/**
 * REST controller for converting File types to PDF using Docmosis.
 */
@RestController
@RequestMapping("/api")
public class DocumentConversionResource {

    private final Logger log = LoggerFactory.getLogger(DocumentConversionResource.class);
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
    @PostMapping("/convert/{documentId}")
    public ResponseEntity<Object> convert(@PathVariable UUID documentId) {
        try {
            log.debug("REST request to get Document Conversion To PDF : {}", documentId);
            File convertedFile = fileToPDFConverterService.convertFile(documentId);

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
