package uk.gov.hmcts.reform.dg.docassembly.rest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateRenditionOutputDto;
import uk.gov.hmcts.reform.dg.docassembly.service.impl.TemplateRenditionService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TemplateRenditionResource {

    private final TemplateRenditionService templateRenditionService;

    public TemplateRenditionResource(TemplateRenditionService templateRenditionService) {
        this.templateRenditionService = templateRenditionService;
    }

    @PostMapping("/template-rendition")
    public ResponseEntity<Resource> createTemplateRendition(
            @RequestBody @Valid CreateTemplateRenditionDto createTemplateRenditionDto,
            @RequestHeader("Authorization") String jwt) throws IOException {

        createTemplateRenditionDto.setJwt(jwt);

        TemplateRenditionOutputDto templateRenditionOutputDto =
                templateRenditionService.renderTemplate(createTemplateRenditionDto);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s", templateRenditionOutputDto.getOutputFileName()));

        return ResponseEntity.ok()
                .contentLength(templateRenditionOutputDto.getRenditionSize())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(templateRenditionOutputDto.getRendition()));

    }

}
