package org.trvsdv.textnode.wrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExtractorTest {

    private final Logger logger = LoggerFactory.getLogger(ExtractorTest.class);
    private Extractor extractor;
    private String simpleHtml;
    private String nestedHtml;
    private final ArrayList<Position> indices = new ArrayList<>();

    @BeforeAll
    public void getHtmlStrings() throws IOException {
        simpleHtml = Testutils.getResourceAsString("simple.html");
        nestedHtml = Testutils.getResourceAsString("nested.html");
    }

    @BeforeEach
    public void clearIndices() {
        indices.clear();
    }

    private void init(Document doc, String[] target) {
        TextExtractor extractor = new TextExtractor();
        doc.traverse(extractor);
        for (String s : target) {
            int start = extractor.text().indexOf(s);
            int end = start + (s.length() -1);
            indices.add(new Position(start, end));
        }
        this.extractor = new Extractor(indices);
    }

    private ArrayList<String> createStrings(ArrayList<Composition> compositions) {
        ArrayList<String> strings = new ArrayList<>();
        compositions.forEach(composition -> strings.add(composition.getText()));
        return strings;
    }

    @Test
    public void simple() {
        Document doc = Jsoup.parse(simpleHtml, Parser.xmlParser());
        String[] target = {"Hello", "World"};
        init(doc, target);
        doc.traverse(extractor);
        logger.info("simple: \n{}", doc);
        Assertions.assertTrue(createStrings(extractor.getCompositons()).containsAll(Arrays.asList(target)));
    }

    @Test
    public void nested() {
        Document doc = Jsoup.parse(nestedHtml, Parser.xmlParser());
        String[] target = {"Hello", "World"};
        init(doc, target);
        doc.traverse(extractor);
        logger.info("nested: \n{}", doc);
        Assertions.assertTrue(createStrings(extractor.getCompositons()).containsAll(Arrays.asList(target)));
    }
}
