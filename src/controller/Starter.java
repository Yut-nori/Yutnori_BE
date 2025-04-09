package controller;

import model.Unit;

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
    public void start() {
        this.playManager = new PlayManager(numPlayer, boardEdgeNum, playerNameList, playerUnitNum);
        while (!playManager.checkEnd()) {
            playManager.controlTurn();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("게임을 다시 시작하려면 'r', 종료하려면 'q'를 입력하세요.");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("r")) {
            End.retry();
        } else {
            End.quit();
        }
    }
}