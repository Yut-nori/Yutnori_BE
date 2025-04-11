package model;

import controller.YutManager;
import model.Yut.Yut;

import java.util.List;

public class Player {
    private String playerName;
    private int unitNum;
    private boolean isWinner;

    public Player(String playerName, int unitNum, boolean isWinner) {
        this.playerName = playerName;
        this.unitNum = unitNum;
        this.isWinner = isWinner;
    }

    // 윷 던지기 (YutManager를 사용)
    public List<Integer> throwYut() {
        YutManager yutManager = new YutManager();
        yutManager.setExtraTurn();
        return yutManager.getYutResList();
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getUnitNum() {
        return unitNum;
    }

    public boolean isWinner() {
        return isWinner;
    }
}
