package uk.gov.hmcts.reform.dg.docassembly.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import uk.gov.hmcts.reform.dg.docassembly.service.impl.DocumentAssemblyService;


@Controller
public class DocumentAssemblyController {

    private final Logger log = LoggerFactory.getLogger(DocumentAssemblyController.class);

    private DocumentAssemblyService documentAssemblyService;


    public DocumentAssemblyController(DocumentAssemblyService documentAssemblyService) {
        this.documentAssemblyService = documentAssemblyService;
    }
}
