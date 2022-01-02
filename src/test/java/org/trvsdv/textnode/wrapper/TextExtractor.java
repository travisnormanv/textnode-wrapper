package org.trvsdv.textnode.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

class TextExtractor implements NodeVisitor {
    private final StringBuilder builder;

    public TextExtractor() {
        builder = new StringBuilder();
    }

    @Override
    public void head(@NotNull Node node, int depth) {
        if(node instanceof TextNode) builder.append(((TextNode) node).getWholeText());
    }

    @Override
    public void tail(@NotNull Node node, int depth) {}

    public String text() {
        return builder.toString();
    }
}
