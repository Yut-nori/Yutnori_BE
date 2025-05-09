package api;

import controller.Starter;

public class RestartAPI {
    private Starter starter;

    public RestartAPI(Starter starter) {
        this.starter = starter;
    }

    public void restartGame() {
        // 게임 재시작 로직
        starter.start(false, null);
        System.out.println("Game restarted!");
    }
}
