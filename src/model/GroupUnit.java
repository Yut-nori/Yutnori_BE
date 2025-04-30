package model;

import model.board.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GroupUnit {
    private Player player;
    private List<Unit> unitGroup;
    private Position currentPosition;
    private Stack<Position> groupPathHistory; // 현재 group의 경로 히스토리 스택

    public GroupUnit(Player player, List<Unit> unitGroup) {
        this.player = player;
        this.unitGroup = unitGroup;
        this.currentPosition = unitGroup.get(0).getCurrentPosition();
        this.groupPathHistory = new Stack<>();
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

        if(currentPosition.isCenter()) {
            popHistory();
            currentPosition.setBack(popHistory());
        }
    }

    public void pushHistory(Position position) {
        groupPathHistory.push(position);
    }

    public Position popHistory() {
        return groupPathHistory.pop();
    }
}

