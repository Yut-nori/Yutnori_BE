package controller;
import java.util.List;
import model.GroupUnit;
import model.Unit;
import model.board.Board;
import model.board.Position;

public class GroupMovement {
    private GroupManager groupManager;

    public GroupMovement(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    // 인스턴스 메서드: 반드시 this.groupMovement.moveGroup(...) 로 호출해야 함
    public void moveGroup(GroupUnit group, int distance) {
        if (group == null) return;

        List<GroupUnit> groupList = groupManager.getGroup();
        Board board = BoardManager.getBoard();
        Position groupPosition = group.getCurrentPosition();

        if (distance > 0) {
            //일반 바깥쪽
            if (!groupPosition.isVertex() && (groupPosition.getIndex() == 0 || groupPosition.getIndex() < board.getLastOuterPosNum())) {
                moveNormal(group, distance);

            // 현 위치 Center
            } else if (groupPosition.isCenter() || group.isCenterToStart()) {
                group.setCenterToStart(true);
                moveCenterToStart(group, distance);

            // 처음으로 Path를 타는 경우
            } else if (groupPosition.isVertex() && !group.hasPath()) {
                List<Position> myPath = board.getInnerPath(groupPosition.getIndex());


                group.setPath(groupPosition.getIndex());

                moveInnerPath(group, myPath, distance);
            } else {
                // Path 중
                List<Position> myPath = board.getInnerPath(group.getCurrentPathID());
                moveInnerPath(group, myPath, distance);

            }

            /*
            System.out.println("[디버깅] 최종 위치 인덱스: " + groupPosition.getIndex());
            System.out.println("[디버깅] passedZero 상태: " + group.hasPassedZero());

            if (groupPosition.getIndex() == 0 && !group.hasPassedZero()) {
                group.markPassedZero();
                System.out.println("[디버깅] 0번을 통과함. passedZero 플래그 ON");
            }

            if (group.hasPassedZero() && groupPosition.getIndex() != 0) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.END);
                }
                groupManager.getGroup().remove(group);
                System.out.println("[완주] 유닛이 한 바퀴를 돌아 도착하였습니다.");
            }

            System.out.println("[디버깅] 최종 위치 인덱스: " + groupPosition.getIndex());
            System.out.println("[디버깅] passedZero 상태: " + group.hasPassedZero());

            if (groupPosition.getIndex() == 0 && !group.hasPassedZero()) {
                group.markPassedZero();
                System.out.println("[디버깅] 0번을 통과함. passedZero 플래그 ON");
            }

            if (group.hasPassedZero() && groupPosition.getIndex() != 0) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.END);
                }
                groupManager.getGroup().remove(group);
                System.out.println("[완주] 유닛이 한 바퀴를 돌아 도착하였습니다.");
            }

            if (groupPosition.getIndex() == 0 && !group.hasPassedZero()) {
                group.markPassedZero();
                System.out.println("[디버깅] 0번을 통과함. passedZero 플래그 ON");
            }

            if (group.hasPassedZero() && groupPosition.getIndex() != 0) {
                for (Unit unit : group.getUnitGroup()) {
                    unit.setStatus(Unit.Status.END);
                }
                groupManager.getGroup().remove(group);
                System.out.println("[완주] 유닛이 한 바퀴를 돌아 도착하였습니다.");
            }

             */

        } else {
            // 뒤로 한 칸 (빽도)
            if (group.isHistoryEmpty()) {
                System.out.println("출발한 유닛이 없어 [빽도]를 진행할 수 없습니다. 넘어갑니다.");
                return;
            }
            if (group.getCurrentPosition().getIndex() == 0 && group.getUnitGroup().get(0).getStatus() == Unit.Status.ON) {
                if (distance == -1) {
                    System.out.println("[빽도: 0 → 이전 경로로 복귀]");
                    group.popHistory(); // 0 제거

                    if (group.isHistoryEmpty()) {
                        // 1 → 0 → 빽도 → 또 빽도인 경우: 스택이 비었으면 0으로 강제 복귀
                        // [1 → 0 → 빽도 → 또 빽도] 상황
                        // 원래 1에서 왔다고 간주하고 다시 1로 되돌림
                        System.out.println("스택이 비었기 때문에 1에서 왔던 것으로 간주하여 1번으로 이동합니다.");
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
        }

        group.printHistoryStack();
    }


    private void moveNormal(GroupUnit group, int distance) {
        moveAndRecordHistory(group, group.getCurrentPosition(), distance);
    }

    private void moveAndRecordHistory(GroupUnit group, Position start, int distance) {
        Position current = start;
        for (int i = 0; i < distance; i++) {
            current = current.getNext();
            group.pushHistory(current);
        }

        for (Unit unit : group.getUnitGroup()) {
            if (unit.getStatus() == Unit.Status.READY) {
                unit.setStatus(Unit.Status.ON);
            }
        }
        /*
        System.out.println("[디버깅] 현재 그룹의 유닛 상태:");
        for (Unit unit : group.getUnitGroup()) {
            System.out.println(" - 유닛 상태: " + unit.getStatus() + ", 위치: " + unit.getCurrentPosition().getIndex());
        }

         */

        group.setPosition(current);
    }

    private void moveCenterToStart(GroupUnit group, int distance) {
        Position p = group.getCurrentPosition();
        int remainDistance = distance;

        while(p.getIndex() != 0 && remainDistance > 0) {
            p = p.getAltNext();
            group.pushHistory(p);
            remainDistance--;
        }

        group.setPosition(p);
    }

    private void moveInnerPath(GroupUnit group, List<Position> myPath, int distance) {
        Position p = group.getCurrentPosition();
        int currentPathIdx = myPath.indexOf(p);
        int remainToPathEnd = myPath.size() - currentPathIdx - 1;

        if(remainToPathEnd > distance) {

            for(int i=1;i<=distance;i++) {
                group.pushHistory(myPath.get(currentPathIdx + i));
            }

            group.setPosition(myPath.get(currentPathIdx + distance));
        } else {
            int normalDistance = distance - remainToPathEnd;
            group.releasePath();

            for(int i=1;i<remainToPathEnd;i++) {
                group.pushHistory(myPath.get(currentPathIdx + i));
            }

            group.setPosition(myPath.get(myPath.size() - 1));
            moveNormal(group, normalDistance);
        }

    }
}