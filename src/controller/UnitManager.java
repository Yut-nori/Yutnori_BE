package controller;

import model.Player;
import model.Unit;

import java.util.List;
import java.util.Map;

public class UnitManager {
    private Map<Player, List<Unit>> group;
    private int unitPositionIdx;

    public boolean setGroup() {
        return true;
    }

    public void setStatus() {

    }

    public int move() {
        return unitPositionIdx;
    }

}
