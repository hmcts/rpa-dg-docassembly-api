package uk.gov.hmcts.reform.dg.docassembly.dto;

import java.io.InputStream;

public class TemplateRenditionOutputDto extends TemplateIdDto {

    private InputStream rendition;

    private String outputFileName;

    private long renditionSize;

    public InputStream getRendition() {
        return rendition;
    }

    public void setRendition(InputStream rendition) {
        this.rendition = rendition;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public long getRenditionSize() {
        return renditionSize;
    }

    public void setRenditionSize(long renditionSize) {
        this.renditionSize = renditionSize;
    }
}
