package controller;
import java.util.List;


import controller.interfaces.IMoveManager;
import view.interfaces.*;
import model.GroupUnit;
import model.Player;
import model.board.Position;

public class MoveManager implements IMoveManager {
    private GroupManager GroupManager;
    private GroupMovement GroupMovement;
    private GroupPositionChecker GroupPositionChecker;
    private IView view;

    public static final int ENEMY_CAPTURED = 1;
    public static final int FRIEND_STACKED = 2;
    public static final int GOAL_REACHED = 3;

    public MoveManager(GroupManager groupManager, IView view) {
        this.GroupManager = groupManager;
        this.GroupMovement = new GroupMovement(groupManager);
        this.GroupPositionChecker = new GroupPositionChecker(groupManager);
        this.view = view;
    }

    @Override
    public int handleUserMove(List<GroupUnit> playerGroups, List<Integer> throwResult) {
        /*뷰에 연결 , 유닛 선택 이동을 뷰에 구현*/
        view.displayBoardStatus(playerGroups.get(0).getPlayer(), playerGroups);
        view.ShowDiceResult(throwResult);

        //뷰에서 사용할 유닛, 사용할 윷 결과 받아오기
        int selectUnit = view.getGroupSelection();
        int selectPosition = view.getMoveSelection();

        //선택한 그룹 저장
        GroupUnit selectedGroup = playerGroups.get(selectUnit);
        int moveDistance = throwResult.get(selectPosition);

        //유닛 이동(GroupMovemnet Class)
        GroupMovement.moveGroup(selectedGroup, moveDistance);
        throwResult.remove(selectPosition);
        return selectUnit;
    }

    //이동 후 결과 확인 -> UnitManager로 부터 상대유닛을 잡았는지, 우리 유닛에 업혔는지, 결승선을 통과헀는지 확인하는 메서드 호출
    @Override
    public void handlePostMoveActions(Player current, List<GroupUnit> playerGroups, List<Integer> throwResult, int selectedGroup) {
        Position newPos = playerGroups.get(selectedGroup).getCurrentPosition();
        int result = 0;

        //아군이 있는지, 적군이 있는지, 끝났는지 판단, 판단 결과를 view에서 출력
        //판단은, GroupPositionChecker에서 실행
        if (GroupPositionChecker.isEnemytInPosition(current, newPos)) {
            result = ENEMY_CAPTURED;
            throwResult.addAll(current.throwYut());
        } else if (GroupPositionChecker.isFriendlyInPosition(current, newPos)) {
            result = FRIEND_STACKED;
        } else if (newPos.getIndex() > BoardManager.getBoard().getNumberOfPositions()) {
            result = GOAL_REACHED;
            GroupUnit completedGroup = playerGroups.get(0);
            GroupManager.unitPassed(completedGroup);
            playerGroups.remove(completedGroup);
        }
        /*view 모델에 result 전달 -> 결과값 전달*/
        view.displayMoveResult(result);
    }
}
