package controller;

import java.util.Scanner;

public class Starter {
    private  int numPlayer;
    private  String[] playerNameList;
    private  int playerUnitNum;
    private  int boardEdgeNum;

    private PlayManager playManager;

    public Starter(int numPlayer, String[] playerNameList, int playerUnitNum, int boardEdgeNum) {
        this.numPlayer = numPlayer;
        this.playerNameList = playerNameList;
        this.playerUnitNum = playerUnitNum;
        this.boardEdgeNum = boardEdgeNum;
    }

    // 보드 매니저 초기화 및 보드 생성
    // 플레이어 매니저 생성
    // 플레이어 리스트 생성
    // 구조 변경[2025.04.09]: static start -> start
    // 게임 재실행 과정에서 파라미터 유연하게 받을 필요가 있다고 판단.
    // 구조 변경[2025.04.10]: add handleRestartOrQuit 메소드
    // 게임 종료 후 재시작 및 종료 선택 기능 추가 -> End클래스는 종료만 담당
    public void start(boolean isTest, int[] testResult) {
        this.playManager = new PlayManager(numPlayer, boardEdgeNum, playerNameList, playerUnitNum);
        while (!playManager.checkEnd()) {
            playManager.controlTurn(isTest, testResult);
        }
        handleRestartOrQuit();
    }

    private void handleRestartOrQuit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("게임을 다시 시작하려면 'r', 종료하려면 'q'를 입력하세요.");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("r")) {
            System.out.println("게임을 다시 시작합니다!");
            start(false, null); // 게임 재시작
        } else {
            End.quit(); // 게임 종료
        }
    }
}