package model;

import model.board.Position;

import java.util.List;

public class GroupUnit {
    private Player player;
    private List<Unit> unitGroup;
    private Position currentPosition;

    public GroupUnit(Player player, List<Unit> unitGroup) {
        this.player = player;
        this.unitGroup = unitGroup;
        this.currentPosition = unitGroup.get(0).getCurrentPosition();
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

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setPosition(Position position) {
        this.currentPosition = position;
        for(Unit unit : this.getUnitGroup()) {
            unit.setPosition(currentPosition);
        }
    }
}

