//import controller.Starter;
//
//public class Main {
//    public static void main(String[] args) {
//        // 기존 게임 시작 코드 (유지)
//
//        System.out.println("안녕");
//        Starter starter = new Starter(2,  new String[] {"test1", "test2"}, 2, 5);
//        starter.start(true, new int[]{5, 3, 2, 4});
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
        int edgeCount = 5;

        // Starter 인스턴스 생성
        Starter starter = new Starter(playerCount, playerNames, unitCount, edgeCount);

        // 실제 확률 기반 플레이 실행
        starter.start(true, null); // false = 확률 기반, testResult = null
    }
}
