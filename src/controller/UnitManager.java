package controller;

import java.util.ArrayList;
import java.util.List;
import model.GroupUnit;
import model.Player;
import model.Unit;


//-> Board 가 가져야할 역할까지 가지고 있음 -> 통합해서 분리 필요.ㄴ
public class UnitManager {
    private List<GroupUnit> groupList; // 그룹 목록

    public UnitManager() {
        this.groupList = new ArrayList<>();
    }

    // 새로운 그룹 생성 및 추가
    public GroupUnit createGroup(Player player, Unit unit) {
        List<Unit> units = new ArrayList<>();
        units.add(unit);
        GroupUnit group = new GroupUnit(player, units);
        groupList.add(group);
        return group;
    }

    // 그룹 이동
    public void moveGroup(GroupUnit group, int moveDistance) {
        for (Unit unit : group.getUnitGroup()) {
            unit.setStatus(Unit.Status.ON);
        }
        group.setPositionIdx(group.getPositionIdx() + moveDistance);
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

    // 이동한 자리에 우리 유닛이 있는지 확인, 병합
    public boolean isFriendlytInPosition(Player current, int position) {
        GroupUnit currentGroup = null;
        int findEqual = 0;
        for (GroupUnit group : getGroupsByPlayer(current)) {
            if(group.getPositionIdx() == position){
                findEqual++;
            }
            if(findEqual >= 2) {
                currentGroup = group;
                mergeGroups(currentGroup, position);
                return true;
            }
        }
        return false;
    }

    // 이동한 자리에 상대 유닛이 있는지 확인, 병합
    public boolean isEnemytInPosition(Player current, int position) {
        System.out.println("현재 플레이어 : "+current.getPlayerName() + ", 현재 위치 : "+position);
        GroupUnit currentGroup = null;
        for (GroupUnit group : groupList) {
            if (group.getPositionIdx() == position && !group.getPlayer().getPlayerName().equals(current.getPlayerName())) {
                System.out.println("상대 플레이어 : " +group.getPlayer().getPlayerName());
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.READY);
                    createGroup(group.getPlayer(), unit);
                }
                groupList.remove(group);
                return true;
            }
        }
        System.out.println("not enemy player");
        return false;
    }

    // 그룹 병합
    private void mergeGroups(GroupUnit targetGroup, int position) {
        for (GroupUnit group : groupList) {
            if (group.getPositionIdx() == position && group != targetGroup) {
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