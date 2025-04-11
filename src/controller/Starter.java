package controller;

import java.util.Scanner;

public class Starter {
    private int numPlayer;
    private String[] playerNameList;
    private int playerUnitNum;
    private int boardEdgeNum;

    public Starter(int numPlayer, String[] playerNameList, int playerUnitNum, int boardEdgeNum) {
        this.numPlayer = numPlayer;
        this.playerNameList = playerNameList;
        this.playerUnitNum = playerUnitNum;
        this.boardEdgeNum = boardEdgeNum;
    }

    public void start() {
        System.out.println("게임을 시작합니다!");

        Initializer initializer = new Initializer(numPlayer, playerNameList, playerUnitNum, boardEdgeNum);
        initializer.runGame();

        handleRestartOrQuit(); // while 루프 끝나고 처리
    }

    private void handleRestartOrQuit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("게임을 다시 시작하려면 'r', 종료하려면 'q'를 입력하세요.");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("r")) {
            System.out.println("게임을 다시 시작합니다!");
            start(); // 게임 재시작
        } else {
            End.quit(); // End 클래스에서 종료 처리
        }
    }
}