package model.board;

public class Position {
    private Position next;
    private Position back;
    private boolean isVertex;
    private Position altNext;
    private int index;

    public void setVertex(boolean isVertex) {

    }

    public int getIndex() {
        return index;
    }

    public int getNextIndex() {
        return index + 1;
    }
}

