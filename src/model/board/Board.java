package model.board;

public class Board {
    private int edgeNum;
    private int positionNum;
    private Position start, end;

    public Board(int edgeNum) {
        this.edgeNum = edgeNum;
        this.positionNum = calcPositionNum();
        start = end = null;
        createPositions();
    }

    private int calcPositionNum() {
        return 7 * edgeNum + 1;
    }

    private void createPositions() {
        for(int i=0;i<positionNum;i++) {
            Position p = new Position(i);
            if(i == 0) {
                start = end = p;
            } else {
                p.setBack(end);
                end.setNext(p);
                end = p;
            }
            if((i % 5 == 0) && (i < 5 * (edgeNum - 1)) || i == positionNum - 1) {
                //바깥쪽 Vertex(시작점 이전의 꼭짓점만 제외) + 중심점
                p.setVertex(true);

                //TODO : 각 Vertex의 altNext 지정
            }
        }
    }

    public void print() {
        Position p = start;
        while(p != end.getNext()) {
            System.out.print(p.getIndex() + "번째 ");
            if(p.isVertex()) System.out.print("{Vertex} ");
            System.out.print(" -> ");
            p = p.getNext();
        }
        System.out.println();
    }

    public void backPrint() {
        Position p = end;
        while(p != start)  {
            System.out.print(p.getIndex() + "번째 ");
            if(p.isVertex()) System.out.print("{Vertex} ");
            System.out.print(" <- ");
            p = p.getBack();
        }
        System.out.println();
    }
}
