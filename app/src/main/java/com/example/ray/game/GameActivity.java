package com.example.ray.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameActivity extends BaseActivity {

    final int ROWS_COUNT    =   5;
    final int CELLS_COUNT   =   2;
    final String GAME_LOGS = "GAME_LOGS";
    ServerConnection sc = new ServerConnection();
    String response;
    String result;
    String gameID;
    boolean gameConnect;
    boolean gamePlay;
    boolean gameCheck;

    String playerID;
    String s0, p1, p2;
    Handler handler;
    boolean isCanMove;
    boolean setMove = false;
    String movePoint;
    //private Button[][] buttons  =   new Button[2][5];
    private TableLayout tablelayout;
    TextView tvSystem, tvP1, tvP2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        tablelayout = (TableLayout) findViewById(R.id.main_1);
        tvP1 = (TextView) findViewById(R.id.tvP1);
        tvSystem = (TextView) findViewById(R.id.tvSystem);

        buildGameField();

        gamePlay = true;
        gameConnect = true;
        gameCheck = true;

        startGameLoop();

    }

    private void startGameLoop(){

        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle bandle = message.getData();
                String actionID = bandle.getString("actionID");
                switch(actionID){
                    case "gameConnect":
                        if(bandle.getString("error").equals("ok")){
                            gameID = bandle.getString("gameID");
                            playerID = bandle.getString("playerID");
                            Toast.makeText(GameActivity.this, "Connected! GameID: " + gameID + " PlayerID: " + playerID, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(GameActivity.this, bandle.getString("error"), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "gameCheck":
                        if(bandle.getString("error").equals("ok")){
                            s0 = bandle.getString("s0");
                            p1 = bandle.getString("p1");
                            p2 = bandle.getString("p2");
                            if(bandle.getString("move").equals("1")){
                                isCanMove = true;
                            }else{
                                isCanMove = false;
                            }
                        }else{
                            //Toast.makeText(GameActivity.this, bandle.getString("error"), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "gameMove":

                        break;
                    default:

                        break;
                }
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bandle = new Bundle();
                while(gamePlay) {

                    if(gameConnect){
                        try {
                            response = sc.sendRequestToServer("actionID=gameConnect");
                            bandle.putString("actionID", "gameConnect");
                            if(getJsonValue(response, "error").equals("ok")){
                                bandle.putString("gameID", getJsonValue(response, "gameID"));
                                bandle.putString("playerID", getJsonValue(response, "playerID"));
                                gameConnect = false;
                            }
                            bandle.putString("error", getJsonValue(response, "error"));
                            Message message1 = handler.obtainMessage();
                            message1.setData(bandle);
                            handler.sendMessage(message1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(GAME_LOGS, "err", e);
                        }
                    }

                    if(setMove){
                        try {
                            response = sc.sendRequestToServer("actionID=gameMove&gameID=" + gameID + "&playerID=" + playerID + "&movePoint=" + movePoint);
                            bandle.putString("actionID", "gameMove");
                            if(getJsonValue(response, "error").equals("ok")){
                                isCanMove = false;
                                setMove = false;
                            }
                            bandle.putString("error", getJsonValue(response, "error"));
                            Message message2 = handler.obtainMessage();
                            message2.setData(bandle);
                            handler.sendMessage(message2);
                        } catch (Exception e) {
                            Log.d(GAME_LOGS, "err", e);
                            e.printStackTrace();
                        }
                    }

                    if(gameCheck){
                        try {
                            response = sc.sendRequestToServer("actionID=gameCheck&gameID=" + gameID + "&playerID=" + playerID);
                            bandle.putString("actionID", "gameCheck");
                            if (getJsonValue(response, "error").equals("ok")) {
                                bandle.putString("s0", getJsonValue(response, "s0"));
                                bandle.putString("p1", getJsonValue(response, "p1"));
                                bandle.putString("p2", getJsonValue(response, "p2"));
                                bandle.putString("move", getJsonValue(response, "move"));
                            }
                            bandle.putString("error", getJsonValue(response, "error"));
                            Message message3 = handler.obtainMessage();
                            message3.setData(bandle);
                            handler.sendMessage(message3);
                        } catch (Exception e) {
                            Log.d(GAME_LOGS, "err", e);
                            e.printStackTrace();
                        }
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void stopGameLoop(){
        this.gamePlay = false;
    }

    private String getJsonValue(String json, String valueName){
        String result = "";
        try {
            JSONObject jsonObj = new JSONObject(json);
            result = jsonObj.getString(valueName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void buildGameField(){

        Game game   =   new Game(ROWS_COUNT, CELLS_COUNT);

        Integer buttonValue = 1;

        Cell[][] field  =   game.getField();

        for (int i  =   0; i < field.length; i++)
        {
            TableRow row    =   new TableRow(this);

            for (int j = 0; j < field[i].length; j++)
            {

                Button button = new Button(this);
                //buttons[i][j] = button;
                button.setOnClickListener(new Listener(i, j, buttonValue));
                row.addView(button, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                button.setWidth(150);
                button.setHeight(150);
                button.setText(buttonValue.toString());
                buttonValue++;
            }
            tablelayout.addView(row, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }



    }

    public class Listener implements View.OnClickListener
    {
        private int x = 0;
        private int y = 0;
        private int value = 1;

        public Listener(int x, int y, int value)
        {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public void onClick(View view)
        {
            Button button = (Button) view;

            if(isCanMove) {
                setMove = true;
                movePoint = value+"";
                Log.d(GAME_LOGS, "Cell: x=" + x + " y=" + y + " Value: " + value);
                tvP1.setText(value + "");
                button.setVisibility(View.INVISIBLE);
            }else{
                Toast.makeText(GameActivity.this, "Wait a second...", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
