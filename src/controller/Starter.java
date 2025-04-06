package controller;

public class Starter {
    private  int numPlayer;
    private  String[] playerNameList;
    private  int playerUnitNum;
    private  int boardEdgeNum;
    private  PlayManager playerManager;
    private  BoardManager boardManager;

    private static Starter instance;
    public Starter(int numPlayer, String[] playerNameList, int playerUnitNum, int boardEdgeNum) {
        this.numPlayer = numPlayer;
        this.playerNameList = playerNameList;
        this.playerUnitNum = playerUnitNum;
        this.boardEdgeNum = boardEdgeNum;
        this.playerManager = new PlayManager(numPlayer);
        this.boardManager = new BoardManager(boardEdgeNum);
    }

    public static void init(int numPlayer, String[] playerNameList, int playerUnitNum, int boardEdgeNum) {
        instance = new Starter(numPlayer, playerNameList, playerUnitNum, boardEdgeNum);
    }

    public static void start() {
        instance.boardManager.createBoard();
        instance.playerManager.createPlayer(instance.playerNameList, instance.playerUnitNum);
        instance.playerManager.controlTurn();
    }
}