package model;

import model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("이정재", 4, false);
    }

    /* Test 1. 생성 시 초기값이 정확한가?
    플레이어가 생성될 때, 초기값들이 정확하게 설정되는지 검증함.
    기본 상태가 정확해야 유닛 관리나 승리 조건 체크에 오류가 없기 때문에 중요한 테스트
     */
    @Test
    void testPlayerInitialization() {
        assertEquals("이정재", player.getPlayerName(), "플레이어 이름이 올바르지 않음");
        assertEquals(4, player.getUnitNum(), "유닛 수가 올바르지 않음");
        assertFalse(player.isWinner(), "초기 승리 상태는 false여야 함");
        assertTrue(player.getUnits().isEmpty(), "초기 유닛 리스트는 비어 있어야 함");
    }

    /* Test 2. 승리 상태의 변경이 정상적으로 반영되는가?
    setWinner() 메서드가 의도한 대로 승리 상태를 변경하는지 검증
    플레이어가 도착선에 도달했는지를 판단해 게임 종료를 처리할때의 로직
     */
    @Test
    void testSetWinner() {
        player.setWinner(true);
        assertTrue(player.isWinner(), "setWinner(true) 이후 isWinner가 true여야 함");

        player.setWinner(false);
        assertFalse(player.isWinner(), "setWinner(false) 이후 isWinner가 false여야 함");
    }

    /* Test 3. 유닛 추가 후에 목록에 반영되는가?
    유닛을 플레이어에게 추가했을 때, getUnits()로 정확하게 꺼낼 수 있는지 확인
    유닛 추가후에 리스트 크기 : 1, 추가한 유닛과 리스트 내부 객체의 동일 여부를 확인
     */
    @Test
    void testAddAndGetUnits() {
        Board board = new Board(4); // 테스트용 보드 생성
        Unit dummyUnit = new Unit(board, 0);
        player.addUnit(dummyUnit);

        List<Unit> units = player.getUnits();
        assertEquals(1, units.size());
        assertSame(dummyUnit, units.get(0));
    }

    /* Test 4. 윷 던지기 결과가 정상 범위에 있는가?
    Player가 YutManager를 통해 윷을 던질 때, 결과가 유효한 범위 내에 있는지 확인
    턴 로직이 잘 작동하기 위해 결과값이 올바른지 확인, 랜덤 로직의 유효성 보장
     */
    @Test
    void testThrowYut() {
        List<Integer> result = player.throwYut();
        assertNotNull(result);

        for (Integer res : result) {
            assertTrue(res >= -1 && res <= 5, "윷 결과는 1~5 범위여야 함");
        }
    }
}