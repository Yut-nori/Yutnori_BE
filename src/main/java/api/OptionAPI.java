package api;

import controller.PlayManager;
import controller.GroupManager;
import controller.TurnManager;
import view.interfaces.IView;

public class OptionAPI {
    private PlayManager playManager;
    private GroupManager groupManager;
    private TurnManager turnManager;

    public void setOption(int playerNum, int unitNum, int shape, boolean isTest, String[] playerNames, IView view) {
        this.groupManager = new GroupManager();
        this.turnManager = new TurnManager(playerNum, groupManager, null, view); // moveManager는 이후 할당
        this.playManager = new PlayManager(
                playerNum, shape, playerNames, unitNum, view,
                null, groupManager, null, turnManager
        );
        // isTest 모드 등 추가 설정 필요시 여기서 처리
    }

    public PlayManager getPlayManager() { return playManager; }
    public GroupManager getGroupManager() { return groupManager; }
    public TurnManager getTurnManager() { return turnManager; }
}
