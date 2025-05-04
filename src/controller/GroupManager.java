package controller;
import java.util.ArrayList;
import java.util.List;
import model.GroupUnit;
import model.Player;
import model.Unit;
import model.board.Position;

public class GroupManager {
    private List<GroupUnit> groupList;

    public GroupManager() {
        this.groupList = new ArrayList<>();
    }

    //그룹 생성
    public GroupUnit createGroup(Player player, Unit unit) {
        List<Unit> units = new ArrayList<>();
        units.add(unit);
        GroupUnit group = new GroupUnit(player, units);
        groupList.add(group);
        return group;
    }

    //파라미터로 받은 플레이어에 해당하는 모든 유닛 그룹 반환
    public List<GroupUnit> getGroupsByPlayer(Player player) {
        List<GroupUnit> result = new ArrayList<>();
        for (GroupUnit group : groupList) {
            if (group.getPlayer().getPlayerName().equals(player.getPlayerName())) {
                result.add(group);
            }
        }
        return result;
    }

    //그룹을 탐색하여, 같은 위치에 올라온 그룹을 제거, 병합
    public void mergeGroups(GroupUnit targetGroup, Position position) {
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position) && group != targetGroup) {
                targetGroup.getUnitGroup().addAll(group.getUnitGroup());
                groupList.remove(group);
                break;
            }
        }
    }

    //유닛이 결승선을 통과했는지 확인
    public void unitPassed(GroupUnit group) {
        for (Unit unit : group.getUnitGroup()) {
            unit.setStatus(Unit.Status.END);
        }
        groupList.remove(group);
    }

    //그룹 리스트 getter
    public List<GroupUnit> getGroup(){
        return groupList;
    }
}
