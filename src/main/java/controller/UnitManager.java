package main.java.controller;

import java.util.ArrayList;
import java.util.List;
import main.java.model.GroupUnit;
import main.java.model.Player;
import main.java.model.Unit;
import main.java.model.board.Board;
import main.java.model.board.Path;
import main.java.model.board.Position;

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
        boolean landedOnCenter = false; // center에 멈췄는지 여부

        if (distance > 0) {
            if (!groupPosition.isVertex() && (groupPosition.getIndex() == 0 || groupPosition.getIndex() < board.getLastOuterPosNum())) {
                groupPosition = moveNormal(group, distance);
                if (groupPosition.isCenter()) landedOnCenter = true;

            } else if (groupPosition.isCenter()) {
                moveCenterToStart(group);
                return; // 바로 0으로 이동하므로 종료

            } else if (groupPosition.isVertex()) {
                if (group.hasPath()) {
                    group.releasePath();
                    groupPosition = moveNormal(group, distance);
                    if (groupPosition.isCenter()) landedOnCenter = true;
                } else {
                    for (Path p : board.getPaths()) {
                        if (p.getStartPos().getIndex() == groupPosition.getIndex()) {
                            group.setPath(p);
                            break;
                        }
                    }
                    Position start = group.getCurrentPath().getStartPos();
                    Position moved = moveAndRecordHistory(group, start, distance);
                    groupPosition = board.getPosition(moved.getIndex());
                    if (groupPosition.isCenter()) landedOnCenter = true;
                }

            } else {
                if (group.hasPath()) {
                    Path path = group.getCurrentPath();
                    Position p = path.getPosition(groupPosition.getIndex());
                    int remain = distance - path.getRemainLength(groupPosition.getIndex());

                    if (remain < 0) {
                        Position moved = moveAndRecordHistory(group, p, distance);
                        groupPosition = board.getPosition(moved.getIndex());
                        if (groupPosition.isCenter()) landedOnCenter = true;
                    } else {
                        Position end = path.getEndPos();
                        while (p != end) {
                            p = p.getNext();
                            group.pushHistory(p);
                        }
                        groupPosition = board.getPosition(p.getIndex());
                        group.setPosition(groupPosition);
                        group.releasePath();

                        groupPosition = moveNormal(group, remain);
                        if (groupPosition.isCenter()) landedOnCenter = true;
                    }
                }
            }

            group.setPosition(groupPosition);

            // [3] center에 멈춘 경우 → altNext 경로로 0까지 이동
            if (landedOnCenter) {
                Position p = groupPosition.getAltNext();
                while (p.getIndex() != 0) {
                    group.pushHistory(p);
                    p = p.getAltNext();
                }
                group.pushHistory(p);
                group.setPosition(p);
                groupPosition = p;
            }

            // [1] 0에 도착하면 flag 설정
            if (groupPosition.getIndex() == 0 && !group.hasPassedZero()) {
                group.markPassedZero();
            }

            // [2] 0 지나면 완주 처리
            if (group.hasPassedZero() && groupPosition.getIndex() != 0) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.END);
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
