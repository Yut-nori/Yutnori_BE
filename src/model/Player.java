package model;

import controller.YutManager;
import model.Yut.Yut;
import model.Unit;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int unitNum;
    private boolean isWinner;

    private List<Unit> units;
    private YutManager yutManager;

    public Player(String playerName, int unitNum, boolean isWinner) {
        this.playerName = playerName;
        this.unitNum = unitNum;
        this.isWinner = isWinner;
        this.units = new ArrayList<Unit>();
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

    public boolean isWinner() {
        return isWinner;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public List<Unit> getUnits() {
        return units;
    }

    public int getUnitNum() {
        return unitNum;
    }
}
