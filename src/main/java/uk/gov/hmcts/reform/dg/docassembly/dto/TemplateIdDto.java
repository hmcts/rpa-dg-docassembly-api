package uk.gov.hmcts.reform.dg.docassembly.dto;

import javax.validation.constraints.NotNull;

public class TemplateIdDto extends JwtDto {

    @NotNull
    private String templateId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
