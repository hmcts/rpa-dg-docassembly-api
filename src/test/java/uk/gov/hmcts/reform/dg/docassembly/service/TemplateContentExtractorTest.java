package uk.gov.hmcts.reform.dg.docassembly.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class TemplateContentExtractorTest {

    TemplateContentExtractor templateContentExtractor = new TemplateContentExtractor();

    @Test
    public void testExtraction() throws Exception {
        String extractTextBetweenTags = templateContentExtractor.extractTextBetweenTags(
                getClass().getClassLoader().getResourceAsStream("template1.docx"),
                "<<cs_{displaycode=‘1’}>>", "<<es_>>");

        assertNotNull(extractTextBetweenTags);
        assertEquals("[]", extractTextBetweenTags.trim());
    }

}
