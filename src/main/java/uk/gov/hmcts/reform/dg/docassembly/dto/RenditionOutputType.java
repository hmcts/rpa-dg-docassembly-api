package uk.gov.hmcts.reform.dg.docassembly.dto;

public enum RenditionOutputType {

    PDF(".pdf", "application/pdf");

    private String fileExtension;
    private String mediaType;

    RenditionOutputType(String fileExtension, String mediaType) {
        this.fileExtension = fileExtension;
        this.mediaType = mediaType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getMediaType() {
        return mediaType;
    }
}
