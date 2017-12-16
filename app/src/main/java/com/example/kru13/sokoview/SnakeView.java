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

    private int lx = 10;
    private int ly = 10;

    private int width;
    private int height;

    private final int initialHeroPos2 = 46;
    private int currentHeroPos = 55;
    private int newHeroPos = currentHeroPos;
    private int possibleNext = currentHeroPos;

    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int BOX = 2;
    private final int GOAL = 3;
    private final int HERO = 4;
    private final int BOXOK = 5;

    Integer level1[] = new Integer[100];

    Integer level2[] = new Integer[100];

    Integer[] currentLevel;
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
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
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
                canvas.drawBitmap(bmp[levelGame[i * 10 + j]], null,
                        new Rect(j * width, i * height, (j + 1) * width, (i + 1) * height), null);
            }
        }

    }

    public void moveRight(){
        if(currentHeroPos%10 != 9){
            newHeroPos = currentHeroPos + 1;
            if(newHeroPos%10 != 9){
                possibleNext = newHeroPos + 1;
            } else {
                possibleNext = -1;
            }
        }
    }

    public void moveLeft(){
        if(currentHeroPos%10 != 0){
            newHeroPos = currentHeroPos - 1;
            if(newHeroPos%10 != 0){
                possibleNext = newHeroPos - 1;
            } else {
                possibleNext = -1;
            }
        }
    }

    public void moveUp(){
        if(currentHeroPos/10 != 0){
            newHeroPos = currentHeroPos - 10;
            if(newHeroPos/10 != 0){
                possibleNext = newHeroPos - 10;
            } else {
                possibleNext = -1;
            }
        }
    }

    public void moveDown(){
        if(currentHeroPos/10 != 9){
            newHeroPos = currentHeroPos + 10;
            if(newHeroPos/10 != 9){
                possibleNext = newHeroPos + 10;
            } else {
                possibleNext = -1;
            }
        }
    }

    public void update(){
        processHeroMovement();
        redraw();
    }

    public boolean isEndOfGame(){
        for(int i = 0; i < levelGame.length; i++){
            if(levelGame[i] == GOAL || levelGame[i] == BOX){
                return false;
            }
        }
        return true;
    }

    public void setLevel2(){
        currentLevel = Arrays.copyOf(level2, level2.length);
        levelGame = Arrays.copyOf(currentLevel, currentLevel.length);
        currentHeroPos = initialHeroPos2;
        redraw();
    }

    private void processHeroMovement(){
        // new is wall
        if(isWallOnPos(newHeroPos)){
            return;
        }
        // new is box or boxok
        if(isAnyBoxOnPos(newHeroPos)){
            // can't move the box
            if(possibleNext == -1 || isWallOnPos(possibleNext) || isAnyBoxOnPos(possibleNext)){
                return;
            }
            // can move the box
            if(levelGame[possibleNext] == GOAL){
                levelGame[possibleNext] = BOXOK;
            } else {
                levelGame[possibleNext] = BOX;
            }
        }

        // hero movement
        levelGame[currentHeroPos] = currentLevel[currentHeroPos] == GOAL? GOAL : EMPTY;
        levelGame[newHeroPos] = HERO;
        currentHeroPos = newHeroPos;
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
        if(levelGame[pos] == BOX || levelGame[pos] == BOXOK){
            return true;
        }
        return false;
    }
}
