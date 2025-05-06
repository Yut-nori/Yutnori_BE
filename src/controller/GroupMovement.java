package controller;
import java.util.List;
import model.GroupUnit;
import model.Unit;
import model.board.Board;
import model.board.Path;
import model.board.Position;

public class GroupMovement {
    private GroupManager groupManager; // 의존성 주입 (결합도 낮추기)

    public GroupMovement(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    // 인스턴스 메서드: 반드시 this.groupMovement.moveGroup(...) 로 호출해야 함
    public void moveGroup(GroupUnit group, int distance) {
        if (group == null) return;

        List<GroupUnit> groupList = groupManager.getGroup();
        Board board = BoardManager.getBoard();
        Position groupPosition = group.getCurrentPosition();
        boolean landedOnCenter = false;

        if (distance > 0) {
            if (!groupPosition.isVertex() && (groupPosition.getIndex() == 0 || groupPosition.getIndex() < board.getLastOuterPosNum())) {
                groupPosition = moveNormal(group, distance);
                if (groupPosition.isCenter()) landedOnCenter = true;

            } else if (groupPosition.isCenter()) {
                moveCenterToStart(group);
                return;

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

    private Position moveNormal(GroupUnit group, int distance) {
        return moveAndRecordHistory(group, group.getCurrentPosition(), distance);
    }

    private Position moveAndRecordHistory(GroupUnit group, Position start, int distance) {
        Position current = start;
        for (int i = 0; i < distance; i++) {
            current = current.getNext();
            group.pushHistory(current);
        }
        return current;
    }

    private void moveCenterToStart(GroupUnit group) {
        Position p = group.getCurrentPosition();
        while (p.getIndex() != 0) {
            p = p.getAltNext();
            group.pushHistory(p);
        }
        group.setPosition(p);
    }
}