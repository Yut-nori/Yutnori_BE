import controller.Starter;

public class Main {
    public static void main(String[] args) {
       /*  Board b = new Board(4);
        Player p = new Player("test", 1, false);

        Position[] positions = b.getPositionArr();

        Position currentPos = positions[0];

        List<Integer> result = new ArrayList<>();

        //test용 윷 결과
        int[] yutRes = {4, 4, 2, 3};
        for(int res : yutRes) {
            result.add(res);
        }

        for(int a : result) {
            System.out.print("윷 결과 : " + a + " ");


            if(a > 0) {
                currentPos = currentPos.testMove(a);
            }

            System.out.println("현재 위치 : " + currentPos.getIndex() + " | isVertex : " + currentPos.isVertex());
        }*/
        Starter starter = new Starter(2,  new String[] {"test1", "test2"}, 2,4);
        starter.start(true, new int[]{5, 3, -1}); // Example usage to avoid "never read" warning


    }
}