package model;


import model.board.Path;
import model.board.Position;

import java.util.List;
import java.util.Stack;

public class GroupUnit {
    private Player player;
    private List<Unit> unitGroup;
    private Position currentPosition;
    private Stack<Integer> groupPathHistory; // 현재 group의 경로 히스토리 스택
    private boolean isInPath;
    private Path currentPath;

    public GroupUnit(Player player, List<Unit> unitGroup) {
        this.player = player;
        this.unitGroup = unitGroup;
        this.currentPosition = unitGroup.get(0).getCurrentPosition();
        this.groupPathHistory = new Stack<>();
        isInPath = false;
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

    public void setPath(Path path) {
        isInPath = true;
        currentPath = path;
    }

    public void releasePath() {
        isInPath = false;
        currentPath = null;
    }

    public boolean hasPath() {
        return isInPath;
    }

    public Path getCurrentPath() {
        return currentPath;
    }



    public void setPosition(Position position) {
        this.currentPosition = position;
        for(Unit unit : this.getUnitGroup()) {
            unit.setPosition(currentPosition);
        }
    }

    public void pushHistory(Position position) {
        groupPathHistory.push(position.getIndex());
    }

    public int popHistory() {
        return groupPathHistory.pop();
    }

    public int peekHistory() {
        return groupPathHistory.peek();
    }


    public void printHistoryStack() {
        for(int i=0;i<groupPathHistory.size();i++) {
            System.out.print(groupPathHistory.get(i) + " => ");
        }
        System.out.println();
    }

    private boolean passedZero = false;  // 0번 지점을 지난 적 있는지 여부

    public boolean hasPassedZero() {
        return passedZero;
    }

    public void markPassedZero() {
        this.passedZero = true;
    }

}

