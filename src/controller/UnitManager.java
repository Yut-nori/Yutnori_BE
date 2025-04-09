package controller;

import model.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitManager {
    private List<Pair<String, List<Unit>>> group;
    private int unitPositionIdx;

    public void registerUnit(String player, Unit unit) {
        List<Unit> units = new ArrayList<>();
        units.add(unit);
        group.add(new Pair<>(player, units));
    }

    public boolean setGroup() {
        return true;
    }

    public void setStatus() {

    }

    public void getStatus() {}

    public int move() {
        return unitPositionIdx;
    }

    public List<Pair<String, List<Unit>>> getGroup() {
        return group;
    }

    public List<Pair<String, List<Unit>>> getCurrentGroup(String current) {
        List<Pair<String, List<Unit>>> result = new ArrayList<>();
        for (Pair<String, List<Unit>> pair : group) {
            if (pair.getFirst().equals(current)) {
                result.add(pair);
            }
        }
        return result;
    }
}
