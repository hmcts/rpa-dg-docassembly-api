package uk.gov.hmcts.reform.dg.docassembly.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class DocumentConversionException extends RuntimeException {

    public DocumentConversionException(String message) {
        super(message);
    }

}
