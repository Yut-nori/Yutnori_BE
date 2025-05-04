import controller.Starter;
import model.board.Board;
import model.board.CenterPosition;
import model.board.Path;
import model.board.Position;

public class Main {
    public static void main(String[] args) {
        // 기존 게임 시작 코드 (유지)
        //testBoardStructure(6); // 사각형
        System.out.println("안녕");
        Starter starter = new Starter(2,  new String[] {"test1", "test2"}, 2, 4);
        starter.start(true, new int[]{4, 4, 2, 4, 2,1});


        // 추가 테스트: 사각형, 오각형, 육각형 보드 생성 및 출력
        //testBoardStructure(4); // 사각형
        //testBoardStructure(5); // 오각형
        //testBoardStructure(6); // 육각형


    }

    public static void testBoardStructure(int edgeNum) {
        System.out.println("\n==== " + edgeNum + "각형 보드 테스트 ====");
        Board board = new Board(edgeNum, 4, 2); // 외부 4칸, 내부 2칸

        Position[] positions = board.getPositionArr();
        for (Position pos : positions) {
            if (pos == null) continue;
            int idx = pos.getIndex();
            int nextIdx = pos.getNext() != null ? pos.getNext().getIndex() : -1;
            int backIdx = pos.getBack() != null ? pos.getBack().getIndex() : -1;
            int altNextIdx = pos.getAltNext() != null ? pos.getAltNext().getIndex() : -1;
            int altBackIdx = pos.getAltBack() != null ? pos.getAltBack().getIndex() : -1;
            boolean isVertex = pos.isVertex();
            boolean isCenter = pos.isCenter();

            System.out.printf(
                    "[Idx: %2d] → Next: %2d | Back: %2d | AltNext: %2d | AltBack: %2d | Vertex: %s | Center: %s\n",
                    idx, nextIdx, backIdx, altNextIdx, altBackIdx, isVertex, isCenter
            );
        }

        CenterPosition centerPos = board.getCenterPosition();
        Position[] nextPositions = centerPos.getNextPositions();
        Position[] backPositions = centerPos.getBackPositions();

        int nextPaths = nextPositions.length;

        for(int i=0;i<nextPaths;i++) {
            System.out.println(backPositions[i].getIndex() + " -> " + centerPos.getIndex() + " -> "+ nextPositions[i].getIndex());
        }
    }
}