package main.java.controller.interfaces;

import main.java.model.Player;

public interface ITurnManager {
    void doPlayerTurn(Player player);
    int getNextPlayer();
}
