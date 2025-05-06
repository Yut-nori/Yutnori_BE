package main.java.controller;

import main.java.model.GroupUnit;
import main.java.model.Player;
import main.java.model.Unit;
import main.java.model.board.Position;

import java.util.List;

public class GroupPositionChecker {
    private GroupManager groupManager; //의존성 주입(결합도 낮추기)

    public GroupPositionChecker(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public boolean isFriendlyInPosition(Player current, Position position ) {
        List<GroupUnit> groups = groupManager.getGroupsByPlayer(current);
        GroupUnit currentGroup = null;
        int findEqual = 0;
        for (GroupUnit group : groups) {
            if (group.getCurrentPosition().equals(position)) {
                //조회 중, 자기 자신을 만난 경우, ++
                findEqual++;
            }
            if (findEqual >= 2) {
                //자기 자신 이외에, 같은 사용자 그룹을 발견하면,
                currentGroup = group;
                groupManager.mergeGroups(currentGroup, position);
                return true;
            }
        }
        return false;
    }

    public boolean isEnemytInPosition(Player current, Position position) {
        List<GroupUnit> groupList = groupManager.getGroup();
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position)
                    && !group.getPlayer().getPlayerName().equals(current.getPlayerName())) {

                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.READY);
                    unit.setPosition(BoardManager.getBoard().getPositionArr()[0]);
                    groupManager.createGroup(group.getPlayer(), unit);
                }
                groupList.remove(group);
                return true;
            }
        }
        return false;
    }
}


