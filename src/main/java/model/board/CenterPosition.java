package main.java.model.board;

public class CenterPosition extends Position {
    private int edgeNum;
    private int nextPosNum;
    private Position[] nextPositions;
    private Position[] backPositions;

    public CenterPosition(int edgeNum, int index) {
        super(index);
        this.edgeNum = edgeNum;
        this.nextPosNum = (int) Math.ceil(edgeNum / 2.0);
        this.nextPositions = new Position[nextPosNum];
        this.backPositions = new Position[nextPosNum];
        this.setCenter(true); // isCenter 설정
        this.setVertex(true);

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

    /**
     * 짝수/홀수 edgeNum에 따라 center에서 갈 수 있는 next Position을 자동 계산해주는 유틸리티
     * 이 메서드는 Board.createPositions()에서 호출해야 함
     */
    public void assignAutoForwardPaths(Position[] positionArr, Integer[] vertexIndices, int outerPositionNum, int innerPositionNum) {
        for (int i = 1; i < vertexIndices.length; i++) { // i == 0은 제외 (출발점)
            int fromVertexIdx = vertexIndices[i];
            int forwardOffset;

            if (edgeNum % 2 == 0) {
                forwardOffset = innerPositionNum * (edgeNum / 2);
            } else {
                forwardOffset = innerPositionNum * ((edgeNum - 1) / 2);
            }

            int forwardTargetIdx = (fromVertexIdx + forwardOffset) % (edgeNum * (outerPositionNum + 1));
            Position forwardVertex = positionArr[forwardTargetIdx];

            setSingleNext(i - 1, forwardVertex);
        }
    }

    public Position[] getNextPositions() {
        return nextPositions;
    }

    public Position[] getBackPositions() {
        return backPositions;
    }
}