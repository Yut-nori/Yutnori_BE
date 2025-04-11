package controller;

public class Initializer {
    private final PlayManager playManager;

    public Initializer(int numPlayer, String[] playerNameList, int playerUnitNum, int boardEdgeNum) {
        this.playManager = new PlayManager(numPlayer, boardEdgeNum, playerNameList, playerUnitNum);
    }

    public void runGame() {
        while (!playManager.checkEnd()) {
            playManager.controlTurn();
        }
    }
}