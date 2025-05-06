package controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StarterTest {

    /* Test 1. 정상적인 초기화 및 실행 흐름이 깨지지 않는가?
    Starter의 객체를 생성해서 초기화 인자가 제대로 전달되는지 확인
    객체가 null이 아닌지 검증, 정상적으로 생성됐는지 확인
    테스트 모드로 게임을 시작함. true는 테스트 모드이고 따로 미리 정해둔 윷 던지기 결과를 실행
     */
    @Test
    void testStarterInitializationAndStart() {

        int numPlayer = 2;
        String[] playerNameList = {"박석진", "김하람"};
        int playerUnitNum = 2;
        int boardEdgeNum = 4;

        Starter starter = new Starter(numPlayer, playerNameList, playerUnitNum, boardEdgeNum);

        assertNotNull(starter);

        // 예시 실행해보기
        starter.start(true, new int[]{1, 2, 3, 4});
    }
}