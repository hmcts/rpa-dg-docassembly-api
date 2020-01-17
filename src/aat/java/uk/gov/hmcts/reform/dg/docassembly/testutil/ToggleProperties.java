package uk.gov.hmcts.reform.dg.docassembly.testutil;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "toggle")
public class ToggleProperties {

    private boolean enableFormDefinitionEndpoint;

    private boolean enableTemplateRenditionEndpoint;

    public boolean isEnableFormDefinitionEndpoint() {
        return this.enableFormDefinitionEndpoint;
    }

    public boolean isEnableTemplateRenditionEndpoint() {
        return this.enableTemplateRenditionEndpoint;
    }

    public void setEnableFormDefinitionEndpoint(boolean enableFormDefinitionEndpoint) {
        this.enableFormDefinitionEndpoint = enableFormDefinitionEndpoint;
    }

    public void setEnableTemplateRenditionEndpoint(boolean enableTemplateRenditionEndpoint) {
        this.enableTemplateRenditionEndpoint = enableTemplateRenditionEndpoint;
    }

    public String toString() {
        return "ToggleProperties(enableFormDefinitionEndpoint="
                + this.isEnableFormDefinitionEndpoint() + ", enableTemplateRenditionEndpoint="
                + this.isEnableTemplateRenditionEndpoint() + ")";
    }
}
