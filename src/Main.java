import model.board.Board;

public class Main {
    public static void main(String[] args) {
        Board b = new Board(4);
        b.print();
        b.backPrint();
    }
}