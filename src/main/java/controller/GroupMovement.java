package controller;
import java.util.List;
import model.GroupUnit;
import model.Unit;
import model.board.Board;
import model.board.Path;
import model.board.Position;

public class GroupMovement {
    private GroupManager groupManager;

    public GroupMovement(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    // 인스턴스 메서드: 반드시 this.groupMovement.moveGroup(...) 로 호출해야 함
    public void moveGroup(GroupUnit group, int distance) {
        System.out.println("Group Positon: "+group.getCurrentPosition().getIndex() + ", this turn move Distance : "+distance);
        if (group == null) return;

        //List<GroupUnit> groupList = groupManager.getGroup();
        Board board = BoardManager.getBoard();
        Position groupPosition = group.getCurrentPosition();
        boolean landedOnCenter = false;

        //빽도를 제외한 움직임
        if (distance > 0) {
            if (!groupPosition.isVertex() && (groupPosition.getIndex() == 0 || groupPosition.getIndex() <= board.getLastOuterPosNum())) {
                groupPosition = moveNormal(group, distance);
                if (groupPosition.isCenter()) landedOnCenter = true;

            } 
            //위치가 센터인 경우
            else if (groupPosition.isCenter()) {
                moveCenterToStart(group);
                return;

            } 
            //위치가 vertex인 경우
            else if (groupPosition.isVertex()) {
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
            }
            else {
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

            // 최종 위치 적용
            group.setPosition(groupPosition);
        }
        else {
            // 뒤로 한 칸 (빽도)
            if (group.isHistoryEmpty()) {
                System.out.println("출발한 유닛이 없어 [빽도]를 진행할 수 없습니다. 넘어갑니다.");
                return;
            }

            //유닛 그룹이 0에 위치한 경우
            if (group.getCurrentPosition().getIndex() == 0 && group.getUnitGroup().get(0).getStatus() == Unit.Status.ON) {
                //0 지난 것 초기화
                group.markPassedZero();
                if (distance == -1) {
                    System.out.println("[빽도: 0 → 이전 경로로 복귀]");
                    group.markNotPassedZero();
                    group.popHistory(); // 0 제거

                    if (group.isHistoryEmpty()) {
                        // 1 → 0 → 빽도 → 또 빽도인 경우: 스택이 비었으면 0으로 강제 복귀
                        // [1 → 0 → 빽도 → 또 빽도] 상황
                        // 원래 1에서 왔다고 간주하고 다시 1로 되돌림
                        System.out.println("스택이 비었기 때문에 1에서 왔던 것으로 간주하여 1번으로 이동합니다.");
                        group.markNotPassedZero();
                        Position backToOne = board.getPosition(1);
                        group.pushHistory(backToOne);
                        group.setPosition(backToOne);
                    } else {
                        // 정상적으로 이전 경로로 이동
                        Position backPos = board.getPosition(group.peekHistory());
                        group.setPosition(backPos);
                    }
                } else {
                    // 빽도가 아니면 → 완주로 처리
                    for (Unit unit : group.getUnitGroup()) {
                        unit.setStatus(Unit.Status.END);
                    }
                    groupManager.getGroup().remove(group);
                    System.out.println("[완주] 유닛이 0을 통과한 후 다시 이동하여 종료됩니다.");
                }
                return;
            }

            // 일반 빽도 처리
            System.out.println("[빽도]를 진행합니다.");
            group.popHistory();
            if (group.isHistoryEmpty()) {
                Position startPos = board.getPosition(0);
                group.pushHistory(startPos);         // 0 위치 기록
                group.setPosition(startPos);         // 0 위치 이동
            } else {
                Position backPos = board.getPosition(group.peekHistory());
                group.setPosition(backPos);
            }
            if(group.getCurrentPosition().getIndex() == 0){
                group.markPassedZero();
            }
        }
    }

    private Position moveNormal(GroupUnit group, int distance) {
        return moveAndRecordHistory(group, group.getCurrentPosition(), distance);
    }


    //moveAndRecordHistory 에서, stack에 쌓는 도중 0이 나오면, markPassedZeor 호출
    private Position moveAndRecordHistory(GroupUnit group, Position start, int distance) {
        Position current = start;
        for (int i = 0; i < distance; i++) {
            current = current.getNext();
            group.pushHistory(current);
            //System.out.println("Unitgroup is moving to "+ current.getIndex());
            if(current.getIndex() == 0){
                //System.out.println("UnitGroup pass zero!!");
                group.markPassedZero();
            }
        }

        for (Unit unit : group.getUnitGroup()) {
            if (unit.getStatus() == Unit.Status.READY) {
                unit.setStatus(Unit.Status.ON);
            }
        }
        /*System.out.println("[디버깅] 현재 그룹의 유닛 상태:");
        for (Unit unit : group.getUnitGroup()) {
            System.out.println(" - 유닛 상태: " + unit.getStatus());
        }*/

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