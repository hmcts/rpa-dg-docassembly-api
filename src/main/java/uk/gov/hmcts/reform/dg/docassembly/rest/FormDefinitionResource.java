package uk.gov.hmcts.reform.dg.docassembly.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dg.docassembly.dto.TemplateIdDto;
import uk.gov.hmcts.reform.dg.docassembly.service.FormDefinitionService;

@ConditionalOnProperty("endpoint-toggles.form-definitions")
@RestController
@RequestMapping("/api")
public class FormDefinitionResource {

    private final FormDefinitionService formDefinitionService;

    public FormDefinitionResource(FormDefinitionService formDefinitionService) {
        this.formDefinitionService = formDefinitionService;
    }

    @ApiOperation(
        value = "Retrieves Document Assembly Form Definition (Formly JSON see https://github.com/ngx-formly/ngx-formly)."
    )
    @ApiImplicitParam(name = "ServiceAuthorization", paramType = "header", required = true, dataTypeClass = String.class)
    @GetMapping("/form-definitions/{templateId}")
    public ResponseEntity<JsonNode> getFormDefinition(
            @ApiParam(required = true) @PathVariable String templateId,
            @RequestHeader("Authorization") String jwt) {
        TemplateIdDto templateIdDto = new TemplateIdDto();
        templateIdDto.setTemplateId(templateId);
        templateIdDto.setJwt(jwt);

        return formDefinitionService.getFormDefinition(templateIdDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
