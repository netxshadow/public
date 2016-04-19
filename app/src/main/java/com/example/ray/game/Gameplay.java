package com.example.ray.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Gameplay {

    String gameID;
    String playerID;
    String playerName;
    String gameStatus;
    Boolean gamePlay = false;
    String result;
    ServerConnection sc;
    Player player;

    public static final String GAME_STARTED      =   "GAME_STARTED"; //const for game status
    public static final String GAME_FINISHED     =   "GAME_FINISHED";
    public static final int GAME_CHECK_TIMEOUT = 1;

    public void setGameConnect()
    {
        sc = new ServerConnection();
        parseResponse("gameConnect", sc.request("actionID=gameConnect"));
        player = new Player(playerID, playerName);
        player.setCanMove(true);
        gamePlay = true;

    }

    public void setGamePlay()
    {

        while(gamePlay == true)
        {

            gameStatus = parseResponse("checkGameStatus", sc.request("actionID=checkGameStatus&gameID=" + gameID));

            switch(gameStatus)
            {
                case GAME_STARTED:
                    break;
                case GAME_FINISHED:
                    break;
            }


            try {
                TimeUnit.SECONDS.sleep(GAME_CHECK_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setGameStop()
    {
        this.gamePlay = false;
    }

    public String parseResponse(String action, String response){

        JSONObject dataJsonObj = null;

        switch(action){
            case "gameConnect":
                try {

                    dataJsonObj = new JSONObject(response);
                    this.gameID = dataJsonObj.getString("gameID");
                    this.playerName = dataJsonObj.getString("playerName");
                    this.playerID = dataJsonObj.getString("playerID");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "checkGameStatus":

                break;
        }

        return result;
    }

}
