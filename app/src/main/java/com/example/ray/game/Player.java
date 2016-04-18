package com.example.ray.game;

public class Player {

    String playerName;
    String playerID;
    Boolean isCanMove;

    public Player(String playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.isCanMove = false;
    }

    public void setCanMove(Boolean canMove) {
        isCanMove = canMove;
    }

    public Boolean getCanMove() {
        return isCanMove;
    }
}
