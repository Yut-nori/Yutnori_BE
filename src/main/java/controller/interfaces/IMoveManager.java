package main.java.controller.interfaces;
import java.util.List;
import main.java.model.GroupUnit;
import main.java.model.Player;

public interface IMoveManager {
    int handleUserMove(List<GroupUnit> groups, List<Integer> result);
    void handlePostMoveActions(Player p, List<GroupUnit> groups, List<Integer> result, int selectedGroup);
}