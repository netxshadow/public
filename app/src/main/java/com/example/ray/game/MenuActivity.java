package com.example.ray.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

    Button  btnNewGame, btnScores, btnSettings, btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        btnNewGame  =   (Button)    findViewById(R.id.btnNewGame);
        btnScores   =   (Button)    findViewById(R.id.btnScores);
        btnSettings =   (Button)    findViewById(R.id.btnSettings);
        btnHelp     =   (Button)    findViewById(R.id.btnHelp);

        btnNewGame.setOnClickListener(this);
        btnScores.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent   =   new Intent();

        switch(v.getId()){
            case R.id.btnNewGame:
                intent.setClass(this, GameActivity.class);
                intent.putExtra("user", "caban");
                break;
            case R.id.btnScores:
                intent.setClass(this, ScoresActivity.class);
                intent.putExtra("user", "caban");
                break;
            case R.id.btnSettings:
                intent.setClass(this, SettingsActivity.class);
                intent.putExtra("user", "caban");
                break;
            case R.id.btnHelp:
                intent.setClass(this, HelpActivity.class);
                intent.putExtra("user", "caban");
                break;
        }

        startActivity(intent);

    }
}
