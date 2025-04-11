package model;

import java.util.List;

public class GroupUnit {
    private Player player;
    private List<Unit> unitGroup;
    private int currentPositionIdx;
    public GroupUnit(Player player, List<Unit> unitGroup) {
        this.player = player;
        this.unitGroup = unitGroup;
        this.currentPositionIdx = unitGroup.get(0).getPositionIdx(); // 첫 번째 유닛의 위치로 초기화
    }

    public Player getPlayer() {
        return player;
    }

    public List<Unit> getUnitGroup() {
        return unitGroup;
    }

    public void setUnitGroup(List<Unit> unitGroup) {
        this.unitGroup = unitGroup;
    }

    public int getPositionIdx() {
        return currentPositionIdx;
    }

    public void setPositionIdx(int currentPositionIdx) {
        this.currentPositionIdx = currentPositionIdx;
        for (Unit unit : this.getUnitGroup()) {
            unit.setPositionIdx(currentPositionIdx);
        }
    }
}

