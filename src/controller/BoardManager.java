package controller;

import model.board.Board;

public class BoardManager {
    private int edgeNum;

    public BoardManager(int edgeNum) {
        this.edgeNum = edgeNum;
    }

    /*
    public int calcPositionNum() {
        return 7 * edgeNum + 1;
    }

    Board에서 계산해야 될듯. edgeNum도 Board 생성 시 필요함.
     */

    public Board createBoard() {
        return new Board(edgeNum);
    }

}
