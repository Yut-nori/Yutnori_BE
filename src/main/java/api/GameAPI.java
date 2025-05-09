package api;

import controller.PlayManager;
import controller.GroupManager;
import controller.TurnManager;
import model.Player;
import model.Unit;
import model.board.Position;
import java.util.List;

public class GameAPI {
    private final PlayManager playManager;
    private final GroupManager groupManager;
    private final TurnManager turnManager;

    public GameAPI(PlayManager playManager, GroupManager groupManager, TurnManager turnManager) {
        this.playManager = playManager;
        this.groupManager = groupManager;
        this.turnManager = turnManager;
    }

    // 윷 결과 전송
    public List<Integer> getThrowYutResult() {
        return turnManager.getthrowResult();
    }

    // 유닛 이동
    public void moveUnit(int groupIndex, int yutResultIndex) {
        // 실제 이동 처리 로직 필요 (예: groupManager.moveUnitByResult)
    }

    // 턴 진행
    public void nextTurn(boolean isTest, int[][] testResult) {
        playManager.GamePlay(isTest, testResult);
    }

    // 게임 종료 여부
    public boolean isGameEnd() {
        return playManager.checkEnd();
    }

    // 승자 이름
    public String getWinnerName() {
        for (Player p : playManager.getPlayerList()) {
            if (p.isWinner()) return p.getPlayerName();
        }
        return null;
    }
}
