package uk.gov.hmcts.reform.dg.docassembly.service.exception;

public class DocumentProcessingException extends RuntimeException {
    public DocumentProcessingException(String message) {
        super(message);
    }
}
