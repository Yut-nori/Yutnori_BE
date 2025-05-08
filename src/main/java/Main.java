//import main.java.controller.Starter;
//import main.java.model.board.Board;
//import main.java.model.board.CenterPosition;
//import main.java.model.board.Path;
//import main.java.model.board.Position;
//
//public class main.java.Main {
//    public static void main(String[] args) {
//        // 기존 게임 시작 코드 (유지)
//        //testBoardStructure(6); // 사각형
//
//        System.out.println("안녕");
//        Starter starter = new Starter(2,  new String[] {"test1", "test2"}, 2, 5);
//        starter.start(true, new int[]{5, 3, 2, 4});
//
//
//        // 추가 테스트: 사각형, 오각형, 육각형 보드 생성 및 출력
//        //testBoardStructure(4); // 사각형
//        //testBoardStructure(5); // 오각형
//        //testBoardStructure(6); // 육각형
//
//
//    }
//
//    public static void testBoardStructure(int edgeNum) {
//        System.out.println("\n==== " + edgeNum + "각형 보드 테스트 ====");
//        Board board = new Board(edgeNum, 4, 2); // 외부 4칸, 내부 2칸
//
//        Position[] positions = board.getPositionArr();
//        for (Position pos : positions) {
//            if (pos == null) continue;
//            int idx = pos.getIndex();
//            int nextIdx = pos.getNext() != null ? pos.getNext().getIndex() : -1;
//            int backIdx = pos.getBack() != null ? pos.getBack().getIndex() : -1;
//            int altNextIdx = pos.getAltNext() != null ? pos.getAltNext().getIndex() : -1;
//            int altBackIdx = pos.getAltBack() != null ? pos.getAltBack().getIndex() : -1;
//            boolean isVertex = pos.isVertex();
//            boolean isCenter = pos.isCenter();
//
//            System.out.printf(
//                    "[Idx: %2d] → Next: %2d | Back: %2d | AltNext: %2d | AltBack: %2d | Vertex: %s | Center: %s\n",
//                    idx, nextIdx, backIdx, altNextIdx, altBackIdx, isVertex, isCenter
//            );
//        }
//
//        CenterPosition centerPos = board.getCenterPosition();
//        Position[] nextPositions = centerPos.getNextPositions();
//        Position[] backPositions = centerPos.getBackPositions();
//
//        int nextPaths = nextPositions.length;
//
//        for(int i=0;i<nextPaths;i++) {
//            System.out.println(backPositions[i].getIndex() + " -> " + centerPos.getIndex() + " -> "+ nextPositions[i].getIndex());
//        }
//    }
//}

import controller.Starter;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 윷놀이 게임을 시작합니다 ===");

        // 플레이어 수, 이름, 유닛 수, 보드 변 수 설정
        int playerCount = 2;
        String[] playerNames = {"player1", "player2"};
        int unitCount = 2;
        int edgeCount = 4;

        // Starter 인스턴스 생성
        Starter starter = new Starter(playerCount, playerNames, unitCount, edgeCount);

        int[][] eachTestResult = new int[playerCount][];

        eachTestResult[0] = new int[]{4};
        eachTestResult[1] = new int[]{5,5,5,4,5,4,3,2,1};
        //{1,-1,-1,-1,4};
        //{4, 4, 2, -1, 5, -1, 4};
        //{3, 3, 3, 3, 3, 3, 3};

        // 실제 확률 기반 플레이 실행
        starter.start(true, eachTestResult); // false = 확률 기반, testResult = null
    }
}
