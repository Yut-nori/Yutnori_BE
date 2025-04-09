package controller;

import java.util.ArrayList;
import java.util.List;
import model.GroupUnit;
import model.Player;
import model.Unit;

public class UnitManager {
    private List<GroupUnit> groupList; // 그룹 목록

    public UnitManager() {
        this.groupList = new ArrayList<>();
    }

    // 새로운 그룹 생성 및 추가
    public GroupUnit createGroup(Player player, Unit unit, int currentPositionIdx) {
        unit.setPositionIdx(currentPositionIdx);
        unit.setStatus(Unit.Status.ON);

        List<Unit> units = new ArrayList<>();
        units.add(unit);

        GroupUnit group = new GroupUnit(player, units);
        groupList.add(group);
        return group;
    }

    // 그룹 이동
    public void moveGroup(GroupUnit group, int moveDistance) {
        int newPos = group.getCurrentPositionIdx() + moveDistance;
        group.setCurrentPositionIdx(newPos);
        for (Unit unit : group.getUnitGroup()) {
            unit.setPositionIdx(newPos);
        }
    }

    // 특정 플레이어의 모든 그룹 반환
    public List<GroupUnit> getGroupsByPlayer(Player player) {
        List<GroupUnit> result = new ArrayList<>();
        for (GroupUnit group : groupList) {
            if (group.getPlayer().getPlayerName().equals(player.getPlayerName())) {
                result.add(group);
            }
        }
        return result;
    }

    // 특정 플레이어의 모든 유닛이 종료 상태인지 확인
    public boolean allUnitsEnded(Player player, int totalUnits) {
        int endedUnits = 0;
        for (GroupUnit group : getGroupsByPlayer(player)) {
            for (Unit unit : group.getUnitGroup()) {
                if (unit.getStatus() == Unit.Status.END) {
                    endedUnits++;
                }
            }
        }
        return endedUnits == totalUnits;
    }

    // 이동한 자리에 우리 유닛이 있는지 확인, 병합
    public boolean isFriendlytInPosition(int position, Player current) {
        GroupUnit currentGroup = null;
        for (GroupUnit group : getGroupsByPlayer(current)) {
            if (group.getCurrentPositionIdx() == position) {
                currentGroup = group;
                mergeGroups(currentGroup, position);
                return true;
            }
        }
        return false;
    }

    // 이동한 자리에 상대 유닛이 있는지 확인, 병합
    public boolean isEnemytInPosition(Player current, int position) {
        GroupUnit currentGroup = null;
        for (GroupUnit group : groupList) {
            if (group.getCurrentPositionIdx() == position && !group.getPlayer().getPlayerName().equals(current.getPlayerName())) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.READY);
                }
                groupList.remove(group);
                return true;
            }
        }
        return false;
    }

    // 그룹 병합
    private void mergeGroups(GroupUnit targetGroup, int position) {
        for (GroupUnit group : groupList) {
            if (group.getCurrentPositionIdx() == position && group != targetGroup) {
                targetGroup.getUnitGroup().addAll(group.getUnitGroup());
                groupList.remove(group);
                break;
            }
        }
    }

    // 유닛 도착
    public void unitPassed(GroupUnit group) {
        for (Unit unit : group.getUnitGroup()) {
            unit.setStatus(Unit.Status.END);
        }
        groupList.remove(group);
    }
}