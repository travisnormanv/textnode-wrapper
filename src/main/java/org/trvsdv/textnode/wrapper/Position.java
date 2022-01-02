package org.trvsdv.textnode.wrapper;

import java.security.InvalidParameterException;

/**
 * Position of a text string.
 * @author travisdev
 */
class Position {

    private int start;
    private int end;

    public enum Bound {
        INBOUND, START_INBOUND, END_INBOUND, OUTBOUND
    }

    public Position() {
       start = 0;
       end = 0;
    }

    public Position(int start, int end) {
        if(start > end) throw new InvalidParameterException("'start' must be lower than 'end'");
        if(start < 0) throw new InvalidParameterException("'start' and 'end' should be positive");
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Compare indices
     * Bound check order is as follows
     * INBOUND, START_INBOUND, END_INBOUND, OUTBOUND
     * @param val Position to be range checked with
     * @return range check result
     */
    public Bound compare(Position val) {
        if(start >= val.getStart() && end <= val.getEnd()) return Bound.INBOUND;
        else if(start >= val.getStart() && start <= val.getEnd()) return  Bound.START_INBOUND;
        else if(end >= val.getStart() && end <= val.getEnd()) return  Bound.END_INBOUND;
        else return Bound.OUTBOUND;
    }

    private void assertIndexBounds(int pos) {
        if(pos < start || pos > end) throw new IndexOutOfBoundsException("'pos' is out of bounds");
    }

    public Position split(int pos) {
        assertIndexBounds(pos);
        Position result = new Position(pos, end);
        this.end = pos - 1;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Position comp)) return false;
        return start == comp.getStart() && end == comp.getEnd();
    }
}
