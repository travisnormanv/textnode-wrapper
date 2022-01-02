package org.trvsdv.textnode.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Wrap text strings based on compositions.
 * @author travisdev
 */
public class Wrapper extends Properties {

    private final Logger logger = LoggerFactory.getLogger(Wrapper.class);

    private void wrapComposition(@NotNull ArrayList<Composition> compositions) {
       compositions.forEach(composition -> {
           Element wrapper = new Element(getProperty("wrapper"));
           wrapper.id("wrapperID" + composition.getId());
           logger.debug("Wrapper ({}), text: {}", composition.getId(), composition.getText());
           composition.wrap(wrapper);
        });
    }

    /**
     * Wrap passed text strings based on positions.
     * @param htmlString html string to be parsed for wrapping
     * @param positions text strings to be wrapped within the html
     * @return html string with wrapped text strings
     */
    public @NotNull String wrapTextStrings(@NotNull String htmlString, @NotNull ArrayList<Position> positions) {
        logger.trace("Create document from htmlString: {}", htmlString);
        Document doc = Jsoup.parse(htmlString, Parser.xmlParser());
        logger.trace("Extract texts from Html");
        Extractor extractor = new Extractor(positions);
        doc.traverse(extractor);
        logger.trace("Wrap extracted texts from Html");
        wrapComposition(extractor.getCompositons());
        return doc.toString();
    }
}
