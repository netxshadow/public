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
    String gameID;
    Boolean gamePlay;
    String s0;
    Handler h;
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

        gameConnect();

        gamePlay = true;

        startGameLoop();

    }

    private void gameConnect(){

        h  = new Handler(){
            public void handleMessage(android.os.Message msg){
                gameID = msg.obj.toString();
            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                response = sc.sendRequestToServer("actionID=gameConnect");
                gameID = getJsonValue(response, "gameID");
                Message msg = h.obtainMessage(0, gameID);
                h.sendMessage(msg);
            }
        });
        t.start();

    }

    private void startGameLoop(){

        h  = new Handler(){
            public void handleMessage(android.os.Message msg){
                tvSystem.setText(msg.obj.toString());
            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(gamePlay) {
                    response = sc.sendRequestToServer("actionID=checkGamePlay&gameID=" + gameID);
                    if(response.equals("wait")){
                        Log.d(GAME_LOGS, "wait for gamers connect...");
                    }else{
                        s0 = getJsonValue(response, "s0");
                        Message msg = h.obtainMessage(0, s0);
                        h.sendMessage(msg);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

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

            Log.d(GAME_LOGS, "Cell: x=" + x + " y=" + y + " Value: " + value);
            tvP1.setText(value + "");
            button.setVisibility(View.INVISIBLE);
            stopGameLoop();

        }
    }


}
