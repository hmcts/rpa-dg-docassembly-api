package uk.gov.hmcts.reform.dg.docassembly.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FormDefinitionRetrievalException extends RuntimeException {

    public FormDefinitionRetrievalException(String message) {
        super(message);
    }

    public FormDefinitionRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

}
