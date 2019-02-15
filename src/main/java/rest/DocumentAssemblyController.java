package rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dg.docassembly.service.impl.DocumentAssemblyService;

@RestController
@RequestMapping("/api")
public class DocumentAssemblyController {
    private final Logger log = LoggerFactory.getLogger(DocumentAssemblyController.class);
    private DocumentAssemblyService documentAssemblyService;

    public DocumentAssemblyController(DocumentAssemblyService documentAssemblyService) {
        this.documentAssemblyService = documentAssemblyService;
    }
}
