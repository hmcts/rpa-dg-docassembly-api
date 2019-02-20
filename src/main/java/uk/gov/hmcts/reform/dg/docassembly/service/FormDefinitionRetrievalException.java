package uk.gov.hmcts.reform.dg.docassembly.service;

public class FormDefinitionRetrievalException extends RuntimeException {

    public FormDefinitionRetrievalException(String message) {
        super(message);
    }

    public FormDefinitionRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

}
