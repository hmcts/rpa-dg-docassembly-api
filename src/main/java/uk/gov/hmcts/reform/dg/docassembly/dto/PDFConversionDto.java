package uk.gov.hmcts.reform.dg.docassembly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@ToString
public class PDFConversionDto {

    @NotNull
    private UUID documentId;

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }
}
