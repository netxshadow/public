package com.example.ray.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Gameplay {

    String gameID;
    String playerID;
    String playerName;
    String response;
    String result;
    String s0, p1, p2;
    Boolean gamePlay = false;
    ServerConnection sc;
    Player player;

    public static final int GAME_CHECK_TIMEOUT = 1;

    public void setGameConnect()
    {
        sc = new ServerConnection();

        response = sc.request("actionID=gameConnect");

        try {

            JSONObject dataJsonObj = new JSONObject(response);
            this.gameID = dataJsonObj.getString("gameID");
            this.playerName = dataJsonObj.getString("playerName");
            this.playerID = dataJsonObj.getString("playerID");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        player = new Player(playerID, playerName);
        player.setCanMove(true);
        gamePlay = true;

    }

    public void setGamePlay()
    {
        while(gamePlay)
        {
            response = sc.request("actionID=checkGamePlay&gameID=" + gameID);
            try {
                try {
                    JSONObject dataJsonObj = new JSONObject(response);
                    this.s0 = dataJsonObj.getString("s0");
                    this.p1 = dataJsonObj.getString("p1");
                    this.p2 = dataJsonObj.getString("p2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public String setPlayerMove(Integer value){
        if(player.getCanMove()){
            response = sc.request("actionID=playerMove&player=" + playerID + "&value=" + value);
            try {
                JSONObject dataJsonObj = new JSONObject(response);
                if(dataJsonObj.getString("result").equals("ok")){
                    result = "ok";
                    player.setCanMove(false);
                }else{
                    result = "error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            result = "wait";
        }

        result = "ok";

        return result;
    }

}
