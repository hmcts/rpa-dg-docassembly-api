package uk.gov.hmcts.reform.dg.docassembly.dto;

public enum RenditionOutputType {

    PDF(".pdf");

    private String fileExtension;

    RenditionOutputType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
