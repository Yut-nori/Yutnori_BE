package model.board;

public class CenterPosition extends Position {
    private int edgeNum;
    private int nextPosNum;
    private Position[] nextPositions;
    private Position[] backPositions;

    public CenterPosition(int edgeNum, int index) {
        super(index);
        this.edgeNum = edgeNum;
        this.nextPosNum = (int) Math.ceil(edgeNum / 2.0); // 나눗셈 정확히 반올림
        this.nextPositions = new Position[nextPosNum];
        this.backPositions = new Position[nextPosNum];
        this.setCenter(true); // isCenter 설정
    }

    public void setSingleNext(int index, Position nextPos) {
        if (index >= 0 && index < nextPosNum) {
            nextPositions[index] = nextPos;
        }
    }

    public Position getSingleNext(int index) {
        if (index >= 0 && index < nextPosNum) {
            return nextPositions[index];
        }
        return null;
    }

    public void setSingleBack(int index, Position backPos) {
        if (index >= 0 && index < nextPosNum) {
            backPositions[index] = backPos;
        }
    }

    public Position getSingleBack(int index) {
        if (index >= 0 && index < nextPosNum) {
            return backPositions[index];
        }
        return null;
    }
}