package controller;

import java.util.ArrayList;
import java.util.List;
import model.GroupUnit;
import model.Player;
import model.Unit;
import model.board.Position;

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
    public void moveGroup(GroupUnit group, int distance) {
        if (group == null) return;

        // 빽도 처리: 이전 위치로 되돌아가기
        if (distance == -1) {
            group.setPosition(group.popHistory()); // 스택에서 꺼낸 위치로 이동
            return;
        }

        // 일반 이동 처리
        Position currPos = group.getCurrentPosition();
        for (int i = 0; i < distance; i++) {
            if (currPos == null) break;

            // 현재 위치 저장
            group.pushHistory(currPos);

            // 다음 위치로 이동
            currPos = getNextPosition(currPos, 1); // 이동은 한 칸씩
        }

        // 최종 위치 설정
        if (currPos != null) {
            group.setPosition(currPos);
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

    // 이동한 자리에 우리 유닛이 있는지 확인, 병합
    public boolean isFriendlytInPosition(Player current, Position position) {
        GroupUnit currentGroup = null;
        int findEqual = 0;
        for (GroupUnit group : getGroupsByPlayer(current)) {
            if(group.getCurrentPosition().equals(position)){
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
    public boolean isEnemytInPosition(Player current, Position position) {
        System.out.println("현재 플레이어 : "+current.getPlayerName() + ", 현재 위치 : " + position.getIndex());
        GroupUnit currentGroup = null;
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position) && !group.getPlayer().getPlayerName().equals(current.getPlayerName())) {
                System.out.println("상대 플레이어 : " +group.getPlayer().getPlayerName());
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.READY);
                    unit.setPosition(BoardManager.getBoard().getPositionArr()[0]);
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
    private void mergeGroups(GroupUnit targetGroup, Position position) {
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position) && group != targetGroup) {
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

    // 현재 위치에서 distance만큼 이동한 위치 반환
    public Position getNextPosition(Position current, int distance) {
        Position pos = current;
        for (int i = 0; i < distance; i++) {
            if (pos == null) break;

            // 멈춘 경우를 altNext, 통과는 next로 처리 가능
            pos = pos.getNext();
        }
        return pos;
    }

}