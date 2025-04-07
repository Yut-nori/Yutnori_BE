package model.Yut;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YutResult {
    private static final int NUM_OF_YUTS = 4; // 윷 개수 (변경 가능)
    private static List<Yut> yutList = new ArrayList<>();
    private static int yutResult;

    public static void throwYuts() {
        yutList.clear();
        Random rand = new Random();

        int backDoIndex = rand.nextInt(NUM_OF_YUTS); // 빽도 전용 윷 하나 지정
        int flatCount = 0;
        boolean isBackDoCondition = false;

        // 윷 생성 및 던지기
        for (int i = 0; i < NUM_OF_YUTS; i++) {
            boolean isBackDo = (i == backDoIndex);
            Yut yut = new Yut(isBackDo);
            yut.toss();
            yutList.add(yut);
        }

        // 빽도 조건: 빽도 윷은 평평, 나머지는 둥글어야 함
        Yut backDoYut = yutList.get(backDoIndex);
        if (backDoYut.isFlat()) {
            isBackDoCondition = true;
            for (int i = 0; i < NUM_OF_YUTS; i++) {
                if (i != backDoIndex && yutList.get(i).isFlat()) {
                    isBackDoCondition = false;
                    break;
                }
            }
        }

        if (isBackDoCondition) {
            yutResult = -1; // 빽도
        } else {
            for (Yut y : yutList) {
                if (y.isFlat()) flatCount++;
            }

            // flat 개수 → 실제 윷놀이 결과로 변환
            switch (flatCount) {
                case 1: yutResult = 1; break; // 도
                case 2: yutResult = 2; break; // 개
                case 3: yutResult = 3; break; // 걸
                case 4: yutResult = 5; break; // 모
                default: yutResult = 0;       // 예외 처리 (모든 윷이 둥근 경우)
            }
        }
    }

    public static int getYutResult() {
        return yutResult;
    }

    // (선택) 각 윷 상태 출력 (디버깅용)
    public static void printYuts() {
        for (int i = 0; i < yutList.size(); i++) {
            Yut yut = yutList.get(i);
            String shape = yut.isFlat() ? "Flat" : "Round";
            String type = yut.isBackDoYut() ? "(BackDo Yut)" : "";
            System.out.println("Yut " + (i + 1) + ": " + shape + " " + type);
        }
    }

    // (선택) 숫자 결과 → 문자열 결과로 반환
    public static String getResultName() {
        return switch (yutResult) {
            case -1 -> "BackDo";
            case 1 -> "Do";
            case 2 -> "Gae";
            case 3 -> "Geol";
            case 4 -> "Yut";
            case 5 -> "Mo";
            default -> "Invalid";
        };
    }
}
