package model.Yut;

import java.util.Random;

public class Yut {
    private boolean isHead;
    private boolean isTail;

    public void setHead() { // 확률 처리
        Random rand = new Random();
        // 0이면 head, 1이면 tail
        if (rand.nextBoolean()) {
            isHead =true;
            isTail = false;
        }
        else {
            isHead = false;
            isTail = true;
        }
    }

    public boolean getHead() {
        return isHead;
    }

    public boolean getTail() {
        return isTail;
    }
}
