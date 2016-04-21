package com.example.ray.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameActivity extends BaseActivity {

    final int ROWS_COUNT    =   5;
    final int CELLS_COUNT   =   2;
    //private Button[][] buttons  =   new Button[2][5];

    private TableLayout tablelayout;
    TextView tvSystem, tvP1, tvP2;
    Gameplay gameplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        tablelayout = (TableLayout) findViewById(R.id.main_1);
        tvP1    =   (TextView)  findViewById(R.id.tvP1);
        tvSystem = (TextView) findViewById(R.id.tvSystem);

        buildGameField();

        ServerConnection sc = new ServerConnection();

        String response = sc.sendRequestToServer("actionID=gameConnect");

        Log.d(GAME_LOGS, "response from server: " + response);

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

        }
    }


}
