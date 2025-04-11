package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.GroupUnit;
import model.Player;
import model.Unit;
import model.board.Board;
import model.board.Position;

public class PlayManager {
    private List<Player> playerList;
    private int numPlayer;
    private int currentPlayer;
    private int playerUnitNum;
    private BoardManager boardManager;
    private UnitManager unitManager;

    private Board gameBoard;

    public PlayManager(int numPlayer, int boardEdgeNum, String[] playerNameList, int playerUnitNum) {
        this.numPlayer = numPlayer;
        this.currentPlayer = 0;
        this.playerList = new ArrayList<>();
        this.boardManager = new BoardManager(boardEdgeNum);
        this.unitManager = new UnitManager();
        this.playerUnitNum = playerUnitNum;

        BoardManager.createBoard();

        gameBoard = BoardManager.getBoard();
        createPlayer(playerNameList, playerUnitNum);
        createUnitManager(this.playerList);
    }

    public final void createPlayer(String[] playerNameList, int playerUnitNum) {
        for (String name : playerNameList) {
            Player player = new Player(name, playerUnitNum, false);
            for (int i = 0; i < playerUnitNum; i++) {
                player.addUnit(new Unit());
            }
            this.playerList.add(player);
        }
    }

    public final void createUnitManager(List<Player> playerList) {
        for(Player player : playerList) {
            List<Unit> units = player.getUnits();
            for(Unit unit : units) {
                this.unitManager.createGroup(player, unit);
            }
        }
    }

    public boolean checkEnd() {
        // 게임의 끝 확인
        for(Player player : playerList) {
            if(player.isWinner()) return true;
        }
        return false;
    }


    /*
    controlTurn 메소드 설명 
        1. 현재 플레이어 선택
        2. 윷 던짐 결과 받아오기
        3. 이동 가능 선택지 계산(throwReuslt.empty()까지 반복) -> Board 사용
            3-1. 결과 선택지가 하나밖에 없는 경우 -> 자동 이동
            3-2. 선택지가 여러가지인 경우, 사용자가 선택 -> 선택한 결과에 해당하는 throwReulst 값 제거
            3-3. 이동으로 상대 유닛을 잡은 경우, throwResult.addAll(current.throwYut()) 재실행.
        4. 승리자가 있는지 확인
        5. 턴 넘기기 -> currentPlayer 값 변경

        Board 와 연동 필요함.
    */

    public void controlTurn(boolean isTest, int[] testResult) {
        Player current = this.playerList.get(currentPlayer);
        List<Integer> throwResult = new ArrayList<>();
        System.out.print("[턴] " + current.getPlayerName() + "의 차례입니다. 윷 결과 : ");
        throwResult.addAll(current.throwYut());
        List<GroupUnit> playerGroups = unitManager.getGroupsByPlayer(current);
        int selectedGroup = 0;

        if(isTest) {
            throwResult.clear();
            for(int i : testResult) {
                throwResult.add(i);
            }
        }

        while (!throwResult.isEmpty()) {
            printThrowResult(throwResult);
            selectedGroup = handleUserMove(playerGroups, throwResult);
            handlePostMoveActions(current, playerGroups, throwResult, selectedGroup);
            if (isAllUnitsEnded(current)) {
                current.setWinner(true);
                System.out.println(current.getPlayerName() + "이(가) 승리했습니다!");
                return;
            }
        }

        System.out.println("\n\n");
        setNextPlayer();
    }


    //윷 결과 출력 --> CLI 테스트용
    private void printThrowResult(List<Integer> throwResult) {
        System.out.print("윷 결과: ");
        for (Integer result : throwResult) {
            System.out.print(result + ", ");
        }
        System.out.println();
    }

    //플레이어 선택 이동 --> CLI tesets
    private int handleUserMove(List<GroupUnit> playerGroups, List<Integer> throwResult) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("이동할 수 있는 유닛과 칸을 선택하세요.");
    
        for (int i = 0; i < playerGroups.size(); i++) {
            GroupUnit group = playerGroups.get(i);
            System.out.print("유닛 " + i + "의 현재 위치: " + group.getCurrentPosition().getIndex() + ", 이동 가능한 위치: ");
            for (Integer result : throwResult) {
                System.out.print((group.getCurrentPosition().getIndex() + result) + ", ");
            }
            System.out.println();
        }
    
        System.out.print("유닛 선택: ");
        int selectUnit = scanner.nextInt();
        System.out.print("사용할 결과 선택: ");
        int selectPosition = scanner.nextInt();
    
        GroupUnit selectedGroup = playerGroups.get(selectUnit);
        int moveDistance = throwResult.get(selectPosition);

        Position resultPosition = unitManager.moveGroup(selectedGroup, moveDistance);

        System.out.println("유닛 " + selectUnit + "이 " + selectedGroup.getCurrentPosition().getIndex() + "으로 이동");
        throwResult.remove(selectPosition);

        return selectUnit;

    }

    //이동 후 결과 확인 -> UnitManager로 부터 상대유닛을 잡았는지, 우리 유닛에 업혔는지, 결승선을 통과헀는지 확인하는 메서드 호출
    private void handlePostMoveActions(Player current, List<GroupUnit> playerGroups, List<Integer> throwResult, int selectedGroup) {
        Position newPos = playerGroups.get(selectedGroup).getCurrentPosition();
    
        if (unitManager.isEnemytInPosition(current, newPos)) {
            System.out.println("상대 유닛을 잡았습니다! 추가 윷 던지기를 실행합니다.");
            throwResult.addAll(current.throwYut());
        } else if (unitManager.isFriendlytInPosition(current, newPos)) {
            System.out.println("업어가쟈~");
        } else if (newPos.getIndex() > BoardManager.getBoard().getNumberOfPositions()) {
            GroupUnit completedGroup = playerGroups.get(0);
            unitManager.unitPassed(completedGroup);
            playerGroups.remove(completedGroup);
            System.out.println("유닛 완주!");
        }
    }

    //모든 유닛이 지나갔는지 확인
    private boolean isAllUnitsEnded(Player player) {
        for (Unit unit : player.getUnits()) {
            if(unit.getStatus() != Unit.Status.END){ return false;}
        }
        return true;
    }

    //다음 플레이어에 턴을 넘김
    private void setNextPlayer() {
        this.currentPlayer = (this.currentPlayer + 1) % this.numPlayer;
    }
}
