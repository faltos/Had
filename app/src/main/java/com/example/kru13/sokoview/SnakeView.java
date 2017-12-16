package com.example.kru13.sokoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

/**
 * Created by kru13 on 12.10.16.
 */
public class SnakeView extends View {

    private Bitmap[] bmp;

    private int lx = 16;
    private int ly = 12;

    private int width;
    private int height;

    private final int initialHeroPos2 = 14;
    private int currentHeroPos = 14;
    private int newSnakePosition = currentHeroPos;

    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int APPLE = 2;
    private final int GOAL = 3;
    private final int SNAKE = 4;
    private final int BOXOK = 5;

    Integer level1[] = new Integer[192];

    Integer level2[] = new Integer[100];

    Integer[] levelGame;

    public SnakeView(Context context) {
        super(context);
        init(context);
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.snake);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                canvas.drawBitmap(bmp[levelGame[i * ly + j]], null,
                        new Rect(j * width, i * height, (j + 1) * width, (i + 1) * height), null);
            }
        }

    }

    public void moveRight(){
        if(currentHeroPos% + 1 < 192){
            newSnakePosition = currentHeroPos + 1;
        }
    }

    public void moveLeft(){
        if(currentHeroPos - 1 >= 0){
            newSnakePosition = currentHeroPos - 1;
        }
    }

    public void moveUp(){
        if(currentHeroPos-12 >= 0){
            newSnakePosition = currentHeroPos - 12;
        }
    }

    public void moveDown(){
        if(currentHeroPos+12 < 192){
            newSnakePosition = currentHeroPos + 12;
        }
    }

    public void update(){
        processHeroMovement();
        redraw();
    }

    public boolean isEndOfGame(){
        for(int i = 0; i < levelGame.length; i++){
            if(levelGame[i] == GOAL || levelGame[i] == APPLE){
                return false;
            }
        }
        return true;
    }

    public void setLevel2(){
        //levelGame = Arrays.copyOf(currentLevel, currentLevel.length);
        currentHeroPos = initialHeroPos2;
        redraw();
    }

    private void processHeroMovement(){
        // new is wall
        if(isWallOnPos(newSnakePosition)){
            return;
        }

        // hero movement
        levelGame[currentHeroPos] = EMPTY;
        levelGame[newSnakePosition] = SNAKE;
        currentHeroPos = newSnakePosition;
    }

    private void redraw(){
        this.invalidate();
    }

    private boolean isWallOnPos(int pos){
        if(levelGame[pos] == WALL){
            return true;
        }
        return false;
    }

    private boolean isAnyBoxOnPos(int pos){
        if(levelGame[pos] == APPLE || levelGame[pos] == BOXOK){
            return true;
        }
        return false;
    }
}
