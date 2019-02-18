package uk.gov.hmcts.reform.dg.docassembly.dto;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.constraints.NotNull;

public class CreateTemplateRenditionDto extends TemplateIdDto {

    private JsonNode formPayload;

    @NotNull
    RenditionOutputType outputType;

    public JsonNode getFormPayload() {
        return formPayload;
    }

    public void setFormPayload(JsonNode formPayload) {
        this.formPayload = formPayload;
    }

    public RenditionOutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(RenditionOutputType outputType) {
        this.outputType = outputType;
    }
}
