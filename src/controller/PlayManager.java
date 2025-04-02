package controller;

import model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayManager {
    private List<Player> playerList;
    private int numPlayer;

    public PlayManager(int numPlayer) {
        this.numPlayer = numPlayer;
        this.playerList = new ArrayList<>();
    }

    public void checkEnd(boolean parameter1) {
        if (parameter1) {
            // 게임의 끝 확인
        }
    }

    public void createPlayer(int numPlayer) {
            // 플레이어 추가
    }

    public void controlTurn() {

    }
}
