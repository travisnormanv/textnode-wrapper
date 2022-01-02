package org.trvsdv.textnode.wrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WrapperTest {

    private final Logger logger = LoggerFactory.getLogger(WrapperTest.class);
    private String simpleHtml;
    private String nestedHtml;
    private final ArrayList<Position> positions = new ArrayList<>();

    @BeforeAll
    public void getHtmlStrings() throws IOException {
        simpleHtml = Testutils.getResourceAsString("simple.html");
        nestedHtml = Testutils.getResourceAsString("nested.html");
    }

    private void init(Document doc, ArrayList<String> target) {
        TextExtractor extractor = new TextExtractor();
        doc.traverse(extractor);
        positions.clear();
        for (String s : target) {
            int start = extractor.text().indexOf(s);
            int end = start + (s.length() -1);
            positions.add(new Position(start, end));
        }
    }


    @Test
    public void simple() {
        ArrayList<String> target = new ArrayList<>();
        target.add("Hello");
        target.add("World");
        Document doc = Jsoup.parse(simpleHtml, Parser.xmlParser());
        init(doc, target);
        Wrapper wrapper = new Wrapper();
        String result = wrapper.wrapTextStrings(simpleHtml, positions);
        logger.info("\n{}", result);
    }

    @Test
    public void nested() {
        ArrayList<String> target = new ArrayList<>();
        target.add("Hello");
        target.add("World");
        Document doc = Jsoup.parse(nestedHtml, Parser.xmlParser());
        init(doc, target);
        Wrapper wrapper = new Wrapper();
        String result = wrapper.wrapTextStrings(nestedHtml, positions);
        logger.info("\n{}", result);
    }
}
