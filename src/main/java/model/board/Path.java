package main.java.model.board;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private int pathID;
    private Position startPos;
    private Position endPos;
    private List<Position> pathPositions;

    public Path(int pathID, Position startPos) {
        this.pathID = pathID;

        this.startPos = new Position(startPos.getIndex());
        this.startPos.setVertex(true);
        endPos = this.startPos;
        pathPositions = new ArrayList<>();
        pathPositions.add(startPos);
    }

    public void addPosition(Position position) {
        Position nextPos = new Position(position.getIndex());
        if(position.isCenter()) nextPos.setCenter(true);
        endPos.setNext(nextPos);
        nextPos.setBack(endPos);
        endPos = nextPos;

        pathPositions.add(nextPos);
    }

    public int getPathID() {
        return pathID;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Position getEndPos() {
        return endPos;
    }

    public boolean isInPath(Position position) {
        boolean isIn = false;
        Position cur = startPos;
        while(cur != endPos) {
            if(position.getIndex() == cur.getIndex()) isIn = true;
            cur = cur.getNext();
        }
        return isIn;
    }

    public void printPath() {
        Position p = startPos;
        while(p != endPos) {
            System.out.print(p.getIndex() + " -> ");
            p = p.getNext();
        }
        System.out.print(endPos.getIndex());
        System.out.println();
    }

    public int getRemainLength(int currentPosIdx) {
        int remainLength = 0;
        Position tmp = getPosition(currentPosIdx);
        while(tmp != endPos) {
            tmp = tmp.getNext();
            remainLength++;
        }
        return remainLength;
    }

    public Position getPosition(int index) {
        Position res = null;
        for(Position p : pathPositions) {
            if(p.getIndex() == index) {
                res = p;
                break;
            }
        }
        return res;
    }
}
