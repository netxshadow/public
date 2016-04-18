package com.example.ray.game;

public class Game {

    private Cell[][] field;

    public Game(int rows, int cells){

        field   =   new Cell[rows][cells];

        for (int i = 0; i < field.length; i++)
        {
            for (int j = 0; j < field[i].length; j++)
            {
                field[i][j] =   new Cell();
            }
        }

    }

    public Cell[][] getField(){
        return this.field;
    }
}
