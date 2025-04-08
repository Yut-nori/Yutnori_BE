import model.board.Board;
import controller.Starter;
import java.util.List;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board b = new Board(4);
        b.print();
        b.backPrint();


        /*Starter_test_CLI version Test Code*/
        Scanner scanner = new Scanner(System.in);
        System.out.print("플레이어 수를 입력하세요(최소 2명, 최대 4명");
        int numPlayer = scanner.nextInt();
        scanner.nextLine();

        String[] playerNameList = new String[numPlayer];
        for(int i = 0; i < numPlayer; i++){
            System.out.print("플레이어" + (i+1) +  "이름 입력" );
            playerNameList[i] = scanner.nextLine();
        }

        System.out.print("유닛의 개수를 입력하세요(최소 2개, 최대 5개");
        int playerUnit = scanner.nextInt();
        scanner.nextLine();

        System.out.print("게임판 유형을 선택하세요(기본 4) : ");
        int boardEdgeNum = scanner.nextInt();

        System.out.print("");

        Starter starter = new Starter(numPlayer,playerNameList,playerUnit,boardEdgeNum);
        starter.start(); // 설정된 값으로 게임 시작

    }
}