package controller;

import java.util.ArrayList;
import java.util.List;
import model.GroupUnit;
import model.Player;
import model.Unit;
import model.board.Board;
import model.board.Path;
import model.board.Position;

public class UnitManager {
    private List<GroupUnit> groupList;

    public UnitManager() {
        this.groupList = new ArrayList<>();
    }

    public GroupUnit createGroup(Player player, Unit unit) {
        List<Unit> units = new ArrayList<>();
        units.add(unit);
        GroupUnit group = new GroupUnit(player, units);
        groupList.add(group);
        return group;
    }

    public void moveGroup(GroupUnit group, int distance) {
        if (group == null) return;

        Board board = BoardManager.getBoard();
        Position groupPosition = group.getCurrentPosition();

        //빽도 아님
        if (distance > 0) {
            if (!groupPosition.isVertex() && (groupPosition.getIndex() == 0 || groupPosition.getIndex() < board.getLastOuterPosNum())) {
                // 일반 바깥쪽 경로 -> 그냥 next
                groupPosition = moveNormal(group, distance);

            } else if (groupPosition.isCenter()) {
                // center에서 altNext를 따라 시작점으로
                moveCenterToStart(group);

            } else if (groupPosition.isVertex()) {  // 꼭짓점인 경우 (시작점 제외)
                if (group.hasPath()) {              // 만약 기존 Path가 있다면 Path의 종점에 도착한것임
                    group.releasePath();            // 해당 Path는 필요 X
                    groupPosition = moveNormal(group, distance);        //그대로 next 이동
                } else {
                    // 기존 Path 없음 -> 새로 Path를 찾아서 해당 Path대로 이동
                    for (Path p : board.getPaths()) {               //Path 찾고
                        if (p.getStartPos().getIndex() == groupPosition.getIndex()) {
                            group.setPath(p);
                        }
                    }

                    //Path대로 이동
                    Position start = group.getCurrentPath().getStartPos();
                    Position moved = moveAndRecordHistory(group, start, distance);
                    groupPosition = board.getPosition(moved.getIndex());
                }

            } else {                            // inner position일때 (center도, 바깥도, vertex도 아님)
                if (group.hasPath()) {
                    Path path = group.getCurrentPath();
                    Position p = path.getPosition(groupPosition.getIndex());
                    int remain = distance - path.getRemainLength(groupPosition.getIndex());     //Path 종점까지 남은 거리

                    if (remain < 0) {       //Path 종점(끝 vertex)을 지나가지 않음 (바깥으로 나가지 않는 경우)
                        Position moved = moveAndRecordHistory(group, p, distance);
                        groupPosition = board.getPosition(moved.getIndex());
                    } else {                // Path 종점을 지나가는 경우 -> 종점까지 Path를 타고 남은 거리만큼 그냥 next로 바깥쪽을 돈다
                        Position end = path.getEndPos();
                        while (p != end) {
                            p = p.getNext();
                            group.pushHistory(p);
                        }
                        groupPosition = board.getPosition(p.getIndex());
                        group.setPosition(groupPosition);
                        group.releasePath();

                        groupPosition = moveNormal(group, remain);
                    }
                }
            }

            group.setPosition(groupPosition);       //마지막으로 group 위치 설정

            // 게임의 END 조건 추가
            group.setPosition(groupPosition);  // 마지막 위치 설정

// [1] 0에 도착했으면 플래그 true로 변경
            if (groupPosition.getIndex() == 0 && !group.hasPassedZero()) {
                group.markPassedZero();
            }

// [2] 0 이후 더 전진하면 완주
            if (group.hasPassedZero() && groupPosition.getIndex() != 0) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.END);
                    break;
                }
                groupList.remove(group);
                System.out.println("[완주] 유닛이 한 바퀴를 돌아 도착하였습니다.");
            }
        } else {
            // 뒤로 한 칸 (빽도)
            group.popHistory();
            Position backPos = board.getPosition(group.peekHistory());
            group.setPosition(backPos);
        }
    }

    // 단순한 이동 (next 타고)
    private Position moveNormal(GroupUnit group, int distance) {
        return moveAndRecordHistory(group, group.getCurrentPosition(), distance);
    }

    // 이동하면서 history에 push (나중에 빽도용)
    private Position moveAndRecordHistory(GroupUnit group, Position start, int distance) {
        Position current = start;
        for (int i = 0; i < distance; i++) {
            current = current.getNext();
            group.pushHistory(current);
        }
        return current;
    }

    // Center position에서 시작점으로 들어감
    private void moveCenterToStart(GroupUnit group) {
        Position p = group.getCurrentPosition();
        while (p.getIndex() != 0) {
            p = p.getAltNext();
            group.pushHistory(p);
        }
        group.setPosition(p);
    }

    public List<GroupUnit> getGroupsByPlayer(Player player) {
        List<GroupUnit> result = new ArrayList<>();
        for (GroupUnit group : groupList) {
            if (group.getPlayer().getPlayerName().equals(player.getPlayerName())) {
                result.add(group);
            }
        }
        return result;
    }

    public boolean isFriendlytInPosition(Player current, Position position) {
        GroupUnit currentGroup = null;
        int findEqual = 0;
        for (GroupUnit group : getGroupsByPlayer(current)) {
            if (group.getCurrentPosition().equals(position)) {
                findEqual++;
            }
            if (findEqual >= 2) {
                currentGroup = group;
                mergeGroups(currentGroup, position);
                return true;
            }
        }
        return false;
    }

    public boolean isEnemytInPosition(Player current, Position position) {
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position)
                    && !group.getPlayer().getPlayerName().equals(current.getPlayerName())) {

                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.READY);
                    unit.setPosition(BoardManager.getBoard().getPositionArr()[0]);
                    createGroup(group.getPlayer(), unit);
                }
                groupList.remove(group);
                return true;
            }
        }
        return false;
    }

    private void mergeGroups(GroupUnit targetGroup, Position position) {
        for (GroupUnit group : groupList) {
            if (group.getCurrentPosition().equals(position) && group != targetGroup) {
                targetGroup.getUnitGroup().addAll(group.getUnitGroup());
                groupList.remove(group);
                break;
            }
        }
    }

    public void unitPassed(GroupUnit group) {
        for (Unit unit : group.getUnitGroup()) {
            unit.setStatus(Unit.Status.END);
        }
        groupList.remove(group);
    }

}
