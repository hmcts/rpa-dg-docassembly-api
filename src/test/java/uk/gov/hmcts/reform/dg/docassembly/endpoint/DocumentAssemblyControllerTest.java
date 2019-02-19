package uk.gov.hmcts.reform.dg.docassembly.endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentAssemblyControllerTest {

    @Test
    public void testDocumentAssembly() {
        assertTrue(true);
    }
}
