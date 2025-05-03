package model.board;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int edgeNum;            // 변 개수 (사각형 -> 4, 오각형 -> 5, n각형 -> n)
    private int positionNum;        // 필요한 총 Position 수

    private int outerPositionNum;   // 바깥쪽 각 변마다 꼭짓점 사이의 Position 개수 (기본 : 4개)
    private int innerPositionNum;   // 꼭짓점과 중심점 사이의 Position 개수 (기본 : 2개)
    private Position start, end;

    private Position[] positionArr;

    /* n각형 보드 기본 */
    public Board(int edgeNum) {
        this(edgeNum, 4, 2);
    }

    /* n각형 보드에서 바깥쪽 / 안쪽 칸 개수 customize 가능 */
    public Board(int edgeNum, int outerPositionNum, int innerPositionNum) {
        this.edgeNum = edgeNum;
        this.positionNum = edgeNum * (outerPositionNum + innerPositionNum + 1) + 1;

        positionArr = new Position[positionNum];

        this.outerPositionNum = outerPositionNum;
        this.innerPositionNum = innerPositionNum;

        start = end = null;
        createPositions();
    }

    private void createPositions() {
        int lastOuterPosNum = edgeNum * (outerPositionNum + 1) - 1;      // 바깥쪽 Position들 중 가장 마지막 Position

        /* 바깥쪽 테두리 Position 생성 및 연결*/
        for(int i=0;i<=lastOuterPosNum;i++) {
            Position pos = new Position(i);
            positionArr[i] = pos;

            /* 첫 Position 생성 */
            if(i == 0) {
                start = end = pos;
            /* 나머지 점들은 일반적인 방법으로 이중연결리스트 연결 */
            } else {
                pos.setBack(end);
                end.setNext(pos);
                end = pos;
                if(i == lastOuterPosNum) {
                    end.setNext(start);
                    start.setBack(end);
                }
            }

            /* 바깥쪽 n-2 개의 altNextPos를 가질 수 있는 Vertex 지정 */
            if((i % (outerPositionNum + 1) == 0) && (i < (outerPositionNum + 1) * (edgeNum - 1)) && i != 0) pos.setVertex(true);
        }

        /* 중심점 Position 생성 및 연결 */
        /*
        Position centerPos = new Position(positionNum - 1);
        centerPos.setVertex(true);
        centerPos.setCenter();
        positionArr[positionNum - 1] = centerPos;

         */

        CenterPosition centerPos = new CenterPosition(edgeNum, positionNum - 1);
        centerPos.setVertex(true);
        positionArr[positionNum - 1] = centerPos;

        /* 각 바깥쪽 꼭짓점과 이어진 내부 Position 생성 및 연결 */
        for(int i=0;i<lastOuterPosNum;i+=(outerPositionNum + 1)) {
            int innerStartPosNum = edgeNum * (outerPositionNum + 1) + (i / (outerPositionNum + 1)) * innerPositionNum;

            for(int j=0;j<innerPositionNum;j++) {
                Position innerPos = new Position(innerStartPosNum + j);
                positionArr[innerStartPosNum + j] = innerPos;
            }

            Position innerStartPos = positionArr[innerStartPosNum];
            Position innerLastPos = positionArr[innerStartPosNum + (innerPositionNum - 1)];
            Position eachVertexPos = positionArr[i];

            // Center랑 바깥쪽 꼭짓점 제외하고 그 사이 안쪽의 Position들만 next-back으로 연결
            int tmp = innerStartPosNum;
            while(!positionArr[tmp].equals(innerLastPos)) {
                if(i == 0) {
                    positionArr[tmp].setAltNext(positionArr[tmp + 1]);
                    positionArr[tmp + 1].setAltBack(positionArr[tmp]);
                } else {
                    positionArr[tmp].setNext(positionArr[tmp + 1]);
                    positionArr[tmp + 1].setBack(positionArr[tmp]);
                }
                tmp++;
            }
            /*
            outerPositionNum / 2 > i => 들어오는 길과 나가는 길 모두 있음
            그 중에서 0, 마지막 => 들어오는 길만 있음
            * */
            //첫 pos
            if(i == 0) {
                centerPos.setAltNext(innerStartPos);
                innerStartPos.setAltBack(centerPos);
            }
            //마지막 꼭짓점
            if(i == (positionNum - (outerPositionNum + 1))) {

            }
            //나머지 center에서 vertex로 나가는 방향

            // 들어오는 방향 setSingleBack


            /*
            // 여기서 i는 각 꼭짓점
            if(i > ((outerPositionNum + 1) / 2) || i == 0) {
                if(i == 0 || i == (positionNum - (outerPositionNum + 1))) {
                    //0, 마지막 꼭짓점
                    //들어오는 길만
                    if(i == 0) {
                        // 시작 위치로는 중심점에서(centerPos) altNext로 연결 (지름길)
                        centerPos.setAltNext(innerStartPos);
                        innerStartPos.setAltBack(centerPos);
                    }
                    innerLastPos.setNext(eachVertexPos);
                    eachVertexPos.setBack(innerLastPos);
                } else {
                    //들어오고 나가고
                }
            } else {
                //나가는 길만
                // 각 꼭짓점(eachVertexPos) 와 안쪽 첫번째 Position (innerStartPos) 간 alternative next-back 으로 연결
                eachVertexPos.setAltNext(innerStartPos);
                innerStartPos.setAltBack(eachVertexPos);
                // 중심점(centerPos) 와 안쪽 중심점에 가장 가까운 Position(innerLastPos) 와 연결 (단, next만 연결, back은 X => 동적으로)
                innerLastPos.setNext(centerPos);
            }
             */
        }
    }

    public Position[] getPositionArr() {
        return positionArr;
    }

    // 해당 보드의 Center Position return
    public Position getCenterPos() {
        return positionArr[positionNum - 1];
    }

    public int getNumberOfPositions() {
        return positionNum;
    }
}
