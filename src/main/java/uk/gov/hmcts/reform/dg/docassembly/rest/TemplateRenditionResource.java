package uk.gov.hmcts.reform.dg.docassembly.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dg.docassembly.dto.CreateTemplateRenditionDto;
import uk.gov.hmcts.reform.dg.docassembly.service.TemplateRenditionService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TemplateRenditionResource {

    private final TemplateRenditionService templateRenditionService;

    public TemplateRenditionResource(TemplateRenditionService templateRenditionService) {
        this.templateRenditionService = templateRenditionService;
    }

    @PostMapping("/template-renditions")
    public ResponseEntity<CreateTemplateRenditionDto> createTemplateRendition(
            @RequestBody @Valid CreateTemplateRenditionDto createTemplateRenditionDto,
            @RequestHeader("Authorization") String jwt) throws IOException {

        createTemplateRenditionDto.setJwt(jwt);

        CreateTemplateRenditionDto templateRenditionOutputDto =
                templateRenditionService.renderTemplate(createTemplateRenditionDto);

        return ResponseEntity.ok(templateRenditionOutputDto);

    }

}
