package model.board;

public class Position {
    private Position next;
    private Position back;
    private boolean isVertex;
    private Position altNext;
    private Position altBack;
    private int index;

    public Position(int index) {
        this.index = index;
        next = back = altNext = altBack = null;
        isVertex = false;
    }

    public boolean isVertex() {
        return isVertex;
    }

    public void setVertex(boolean isVertex) {
        this.isVertex = isVertex;
    }

    /* next, back Getter/Setter */
    public Position getNext() {
        return next;
    }

    public void setNext(Position nextPos) {
        this.next = nextPos;
    }

    public Position getBack() {
        return back;
    }

    public void setBack(Position backPos) {
        this.back = backPos;
    }

    /* Alternative Next, Back Getter/Setter */
    public Position getAltNext() {
        return altNext;
    }

    public void setAltNext(Position altNextPos) {
        this.altNext = altNextPos;
    }

    public Position getAltBack() {
        return altBack;
    }

    public void setAltBack(Position altBackPos) {
        this.altBack = altBackPos;
    }

    /* 자기자신, next의 Index Getter */
    public int getIndex() {
        return index;
    }

    public int getNextIndex() {
        if(isVertex) return altNext.getIndex();
        else return index + 1;
    }

    /* 테스트용 move 메서드 */
    public Position testMove(int distance) {
        Position result = this;

        // 현재 Position이 altNext 보유
        if(this.isVertex) {
            result = result.getAltNext();
            for(int i=0;i<distance-1;i++) {
                result = result.getNext();
            }
        } else {
            for(int i=0;i<distance;i++) {
                result = result.getNext();
            }
        }

        return result;
    }
}

