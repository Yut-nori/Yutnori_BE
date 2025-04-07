package model.Yut;

import java.util.ArrayList;
import java.util.List;

public class YutResult {
    private static List<Yut> yutList = new ArrayList<Yut>();
    private static int yutResult;

    // 윷 4개 생성. 나중에 윷 개수를 여기서 지정하면 됨.
    // 한 개의 윷은 빽도로 지정
    int backDoIndex = new java.util.Random().nextInt(4);


    public static int getYutResult() {
        return yutResult;
    }
}
