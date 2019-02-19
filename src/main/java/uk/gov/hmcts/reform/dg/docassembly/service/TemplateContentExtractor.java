package uk.gov.hmcts.reform.dg.docassembly.service;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TemplateContentExtractor {

    public String extractTextBetweenTags(InputStream wordInputStream, String startTag, String endTag) throws IOException {
        XWPFDocument doc = new XWPFDocument(wordInputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        String docText = extractor.getText();

        int startIndex = docText.indexOf(startTag) + startTag.length();
        int endIndex = docText.indexOf(endTag, startIndex);

        return docText.substring(startIndex, endIndex);
    }

}
