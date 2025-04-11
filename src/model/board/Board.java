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
        System.out.println("edgeNum : " + edgeNum + " | outerNum : " + outerPositionNum + " | innerNum : " + innerPositionNum + " | position Num : " + positionNum);

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
            //positionList.add(i, pos);
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
        Position centerPos = new Position(positionNum - 1);
        centerPos.setVertex(true);
        positionArr[positionNum - 1] = centerPos;

        /* 각 바깥쪽 꼭짓점과 이어진 내부 Position 생성 및 연결 */
        for(int i=0;i<lastOuterPosNum;i+=(outerPositionNum + 1)) {
            int innerStartPosNum = edgeNum * (outerPositionNum + 1) + (i / (outerPositionNum + 1)) * innerPositionNum;

            for(int j=0;j<innerPositionNum;j++) {
                Position innerPos = new Position(innerStartPosNum + j);
                positionArr[innerStartPosNum + j] = innerPos;
            }

            Position innerLastPos = positionArr[innerStartPosNum + (innerPositionNum - 1)];

            int tmp = innerStartPosNum;
            while(!positionArr[tmp].equals(innerLastPos)) {
                positionArr[tmp].setNext(positionArr[tmp + 1]);
                positionArr[tmp + 1].setBack(positionArr[tmp]);
                tmp++;
            }
            /* 첫 꼭짓점과 마지막 꼭짓점은 들어오는 길만 존재
            *  따라서 중심점에서 꼭짓점 방향으로 index를 증가시켜 연결
            * */
            if(i == 0 || i == (lastOuterPosNum - outerPositionNum)) {
                innerLastPos.setNext(positionArr[i]);
                positionArr[i].setAltBack(innerLastPos);

                if(i == 0) {
                    centerPos.setAltNext(positionArr[innerStartPosNum]);
                    positionArr[innerStartPosNum].setAltBack(centerPos);
                } else {
                    centerPos.setNext(positionArr[innerStartPosNum]);
                    positionArr[innerStartPosNum].setBack(centerPos);
                }

            /* 나머지 꼭짓점은 중심점으로 들어가는 방향으로 index 증가시켜 연결 */
            } else {
                positionArr[i].setAltNext(positionArr[innerStartPosNum]);
                positionArr[innerStartPosNum].setBack(positionArr[i]);

                innerLastPos.setNext(centerPos);

                /*
                   말이 이동했던 경로에 따라 History 를 가져와서 해당 경로 1개로만 Back 하도록 설정?
                 */

                /* History 방식 */


                // TODO : centerPos의 back Pos를 정하는 방법?
            }
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
