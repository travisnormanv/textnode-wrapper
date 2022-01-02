package org.trvsdv.textnode.wrapper;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;

/**
 * Composition of pieces that make up a text string inside a document.
 * @author travisdev
 */
class Composition {
    private final ArrayList<Piece> pieces;
    private final StringBuilder text;
    private TextNode transformNode;
    private final String id;

    public Composition(String id, Piece piece) {
        this.id = id;
        text = new StringBuilder();
        text.append(piece.getText());
        pieces = new ArrayList<>();
        pieces.add(piece);
        transformNode = piece.getTextNode();
    }

    public void addPiece(Piece piece) {
        text.append(piece.getTextNode().getWholeText());
        if(pieces.get(pieces.size() - 1).getDepth() > piece.getDepth()) transformNode = piece.getTextNode();
        pieces.add(piece);
    }

    public void wrap(Element wrapperEl) {
        transform();
        wrapperEl.text(transformNode.getWholeText());
        transformNode.replaceWith(wrapperEl);
    }

    private void transform() {
        pieces.forEach(piece -> {
            if(piece.getTextNode() != transformNode) {
                if(piece.getTextNode().hasParent() && piece.getTextNode().siblingNodes().size() == 0) {
                    assert piece.getTextNode().parentNode() != null;
                    piece.getTextNode().parentNode().remove();
                }
                else piece.getTextNode().remove();
            }
        });

        transformNode.text(text.toString());
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text.toString();
    }
}
