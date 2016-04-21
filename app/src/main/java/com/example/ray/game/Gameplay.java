package com.example.ray.game;

import android.util.Log;

public class Gameplay {

    String gameID;
    String playerID;
    String playerName;
    String response;
    String result;
    String s0, p1, p2;
    Boolean gamePlay = false;
    Player player;

    public static final String GAME_LOGS = "GAME_LOGS";

    public static final int GAME_CHECK_TIMEOUT = 5;

    public void setGamePlay(){

        ServerConnection sc = new ServerConnection();

        response = sc.sendRequestToServer("actionID=gameConnect");

        Log.d(GAME_LOGS, response);

    }


}
