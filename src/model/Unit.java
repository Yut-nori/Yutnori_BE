package model;

public class Unit {
    private int currentPositionIdx;
    private enum status {
        READY, ON, END
    }
    private boolean isGrouped;

    public int getStatus() {
        return 1;
    }

    public int getPositionIdx() {
        return currentPositionIdx;
    }

    public boolean isGrouped() {
        return true;
    }

}
