package api;

import controller.PlayManager;
import controller.GroupManager;
import controller.TurnManager;
import model.Player;
import model.Unit;
import model.board.Position;
import java.util.EnumSet;
import java.util.List;

public class PhaseAPI {
    private final PlayManager playManager;
    private final GroupManager groupManager;
    private final TurnManager turnManager;

    public PhaseAPI(PlayManager playManager, GroupManager groupManager, TurnManager turnManager) {
        this.playManager = playManager;
        this.groupManager = groupManager;
        this.turnManager = turnManager;
    }

    public int getTotalPlayerNumber() {
        return playManager.getNumPlayer();
    }

    public int getUnitNumberPerPlayer() {
        return playManager.getPlayerUnitNum();
    }

    public int getShape() {
        return playManager.getBoard().getEdgeNum();
    }

    public int getCurrentPlayerIndex() {
        return playManager.getCurrentPlayerIndex();
    }

    public String getCurrentPlayerName() {
        return playManager.getPlayerList().get(getCurrentPlayerIndex()).getPlayerName();
    }

    public List<Integer> getYutResults() {
        return turnManager.getLastYutResults();
    }

    public int getLastResult() {
        List<Integer> results = getYutResults();
        if (results.isEmpty()) return -1;
        return results.get(results.size() - 1);
    }

    public int getButtonClickRemaining() {
        return turnManager.getButtonClickRemaining();
    }

    public int getClickedYutResult() {
        return turnManager.getClickedYutResult();
    }

    public int[][] getUnitPosition() {
        int playerNum = getTotalPlayerNumber();
        int unitNum = getUnitNumberPerPlayer();
        int[][] unitPosition = new int[playerNum][unitNum];
        for (int p = 0; p < playerNum; p++) {
            Player player = playManager.getPlayerList().get(p);
            List<Unit> units = player.getUnits();
            for (int u = 0; u < units.size(); u++) {
                Position pos = units.get(u).getPosition();
                unitPosition[p][u] = (pos != null) ? pos.getIndex() : -1;
            }
        }
        return unitPosition;
    }

    public int[][] getUnitNumberPerPosition() {
        int boardSize = playManager.getBoard().getPositionArr().length;
        int playerNum = getTotalPlayerNumber();
        int[][] unitNumberPerPosition = new int[boardSize][playerNum];
        for (int p = 0; p < playerNum; p++) {
            Player player = playManager.getPlayerList().get(p);
            List<Unit> units = player.getUnits();
            for (Unit unit : units) {
                Position pos = unit.getPosition();
                if (pos != null) {
                    unitNumberPerPosition[pos.getIndex()][p]++;
                }
            }
        }
        return unitNumberPerPosition;
    }

    public EnumSet<Phase> getCurrentPhase() {
        return turnManager.getCurrentPhase();
    }

    public boolean isTestMode() {
        return playManager.isTestMode();
    }
}