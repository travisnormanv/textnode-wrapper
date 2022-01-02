package org.trvsdv.textnode.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Extracts text strings from document based on positions.
 * @author travisdev
 */
class Extractor implements NodeVisitor {

    private final Logger logger = LoggerFactory.getLogger(Extractor.class);

    private final ArrayList<Position> positions;
    private int pos;
    private Piece preProcessPiece;
    private Piece postProcessPiece;
    private final ArrayList<Composition> compositions;

    public Extractor(ArrayList<Position> positions) {
        this.positions = positions;
        pos = 0;
        compositions = new ArrayList<>();
    }

    public ArrayList<Composition> getCompositons() {
        return compositions;
    }

    private @NotNull Piece createReferencePiece(@NotNull TextNode textNode, @Nullable Piece piece, int depth) {
        Piece referencePiece;
        int length = textNode.getWholeText().length();

        if(piece == null) referencePiece = new Piece(textNode, new Position(0, length - 1), depth);
        else referencePiece = piece.createReltativeTo(textNode, length, depth);

        logger.trace("Create reference piece: text: '{}', start: '{}', end: '{}'", referencePiece.getText(), referencePiece.getStart(), referencePiece.getEnd());
        return referencePiece;
    }

    private @Nullable Piece startInBound(@NotNull Piece piece, @NotNull Position position) {
        if(piece.getStart() != position.getStart()) {
            logger.trace("Split textNode at start: {}", position.getStart());
            return piece.split(position.getStart());
        }

        return null;
    }

    private void endInBound(@NotNull Piece piece, @NotNull Position position) {
        if(piece.getEnd() != position.getEnd()) {
            logger.trace("Split piece at end: {}", position.getEnd());
            piece.split(position.getEnd() + 1);
        }
    }

    private void splitPiece(@NotNull Piece piece, @NotNull Position position) {
        if(piece.equals(position)) {
            logger.trace("Skip piece splitting if indices are equal");
            return;
        }

        Position.Bound bound = position.compare(piece);
        logger.trace("Bound: {}", bound);
        switch(bound) {
            case INBOUND -> endInBound(Objects.requireNonNull(startInBound(piece, position)), position);
            case START_INBOUND -> startInBound(piece, position);
            case END_INBOUND -> endInBound(piece, position);
            case OUTBOUND -> logger.trace("skipping...");
        }
    }


    @Override
    public void head(@NotNull Node node, int depth) {
        if(node instanceof TextNode textNode) {
            if(pos < positions.size()) {
                preProcessPiece = createReferencePiece(textNode, preProcessPiece, depth);
                splitPiece(preProcessPiece, positions.get(pos));
            }
        }
    }

    private void createCompositions(@NotNull Piece piece, @NotNull Position position) {
        if(piece.compare(position) != Position.Bound.OUTBOUND) {
            if(compositions.size() < pos + 1) {
                Composition composition = new Composition(String.valueOf(pos), piece);
                this.compositions.add(composition);
            }
            else compositions.get(pos).addPiece(piece);

            logger.trace("Add piece to composition, text: {}, depth: {}", pos, piece.getTextNode(), piece.getDepth());
        }

        if(piece.equals(position) || piece.getEnd() == position.getEnd()) {
            logger.debug("Extracted text ({}), text: {}", pos, compositions.get(pos).getText());
            logger.trace("proceed to next position: {}", pos + 1);
            pos++;
        }
    }

    @Override
    public void tail(@NotNull Node node, int depth) {
        if(node instanceof TextNode textNode) {
            if(pos < positions.size()) {
                postProcessPiece = createReferencePiece(textNode, postProcessPiece, depth);
                createCompositions(postProcessPiece, positions.get(pos));
            }
        }
    }
}
