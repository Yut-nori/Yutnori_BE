package model.board;

public class CenterPosition extends Position {
    private int edgeNum;
    private int nextPosNum;
    private Position[] nextPositions;
    private Position[] backPositions;
    public CenterPosition(int edgeNum, int index) {
        super(index);
        this.edgeNum = edgeNum;
        nextPosNum = (int)Math.ceil(edgeNum/2);
        nextPositions = new Position[nextPosNum];
        backPositions = new Position[nextPosNum];
    }

    public void setSingleNext(int index, Position nextPos) {
        nextPositions[index] = nextPos;
    }

    public Position getSingleNext(int index) {
        return nextPositions[index];
    }

    public void setSingleBack(int index, Position backPos) {
        backPositions[index] = backPos;
    }

    public Position getSingleBack(int index) {
        return backPositions[index];
    }


}
