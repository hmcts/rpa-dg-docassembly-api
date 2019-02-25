package uk.gov.hmcts.reform.dg.docassembly.service;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TemplateContentExtractorTest {

    TemplateContentExtractor templateContentExtractor = new TemplateContentExtractor();

    @Test
    public void testExtraction() throws Exception {
        Optional<String> extractTextBetweenTags = templateContentExtractor.extractTextBetweenTags(
                getClass().getClassLoader().getResourceAsStream("template1.docx"),
                "<<cs_{displaycode=‘1’}>>", "<<es_>>");

        assertNotNull(extractTextBetweenTags);
        assertEquals("[]", extractTextBetweenTags.get().trim());
    }

}
