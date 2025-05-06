package main.java.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Player;
import main.java.model.Unit;
import main.java.model.board.Board;
import main.java.controller.interfaces.*;
import main.java.view.interfaces.IView;

public class PlayManager {
    private List<Player> playerList;
    private int numPlayer;
    private int currentPlayer;
    private int playerUnitNum;
    private Board gameBoard;


    private BoardManager boardManager;
    private GroupManager groupManager;
    /* TurnManger와 MoveManager 인터페이스화*/
    private  ITurnManager turnManager;
    private  IMoveManager moveManager;
    private IView iView;

    public PlayManager(int numPlayer, int boardEdgeNum, String[] playerNameList, int playerUnitNum, IView view) {
        this.numPlayer = numPlayer;
        this.currentPlayer = 0;
        this.playerList = new ArrayList<>();
        this.playerUnitNum = playerUnitNum;
        this.groupManager = new GroupManager();
        this.iView = view;
        BoardManager.createBoard(boardEdgeNum);
        gameBoard = BoardManager.getBoard();
        createPlayer(playerNameList, playerUnitNum);
        createGroupManager(this.playerList);
        this.moveManager = new MoveManager(this.groupManager, this.iView);
        this.turnManager = new TurnManager(numPlayer, this.groupManager, this.moveManager, this.iView);


    }

    //유저 생성
    public final void createPlayer(String[] playerNameList, int playerUnitNum) {
        for (String name : playerNameList) {
            Player player = new Player(name, playerUnitNum, false);
            for (int i = 0; i < playerUnitNum; i++) {
                player.addUnit(new Unit());
            }
            this.playerList.add(player);
        }
    }

    //그룹 매니저 생성 -> (구)UnitManager
    public final void createGroupManager(List<Player> playerList) {
        for (Player player : playerList) {
            List<Unit> units = player.getUnits();
            for (Unit unit : units) {
                this.groupManager.createGroup(player, unit);
            }
        }
    }

    //게임 종료 확인, GamePlay가 끝날 때마다 모든 player을 조회하여 끝난 플레이어가 있으면 return 하여 종료
    public boolean checkEnd() {
        // 게임의 끝 확인
        for (Player player : playerList) {
            if (player.isWinner()) return true;
        }
        return false;
    }

    public void GamePlay(boolean isTest, int[] testResult) {
        Player current = this.playerList.get(currentPlayer);
        turnManager.doPlayerTurn(current);
        if(checkEnd()){
            return;
        }
        this.currentPlayer = turnManager.getNextPlayer();
    }
}
