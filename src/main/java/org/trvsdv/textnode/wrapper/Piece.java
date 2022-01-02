package org.trvsdv.textnode.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.TextNode;

/**
 * A piece, used for containing visited TextNode and it's depth and position.
 * Depth is relative on root node of document.
 * Position is relative on first TextNode.
 *
 * @Author travisdev
 */
class Piece extends Position {
    private final TextNode textNode;
    private final int depth;

    public Piece(@NotNull TextNode textNode, @NotNull Position position, int depth) {
        this.textNode = textNode;
        setStart(position.getStart());
        setEnd(position.getEnd());
        this.depth = depth;
    }

    public TextNode getTextNode() {
        return textNode;
    }

    public String getText() {
        return textNode.getWholeText();
    }

    public int getDepth() {
        return depth;
    }

    public Piece createReltativeTo(@NotNull TextNode textNode, int length, int depth) {
        return new Piece(textNode, new Position(getEnd() + 1, getEnd() + length), depth);
    }

    @Override
    public Piece split(int pos) {
        TextNode result = textNode.splitText(pos - getStart());
        Position resultPosition = super.split(pos);
        return new Piece(result, resultPosition, depth);
    }
}
