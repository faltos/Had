package com.example.kru13.sokoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kru13 on 12.10.16.
 */
public class SnakeView extends View {

    private Bitmap[] bmp;

    private int lx = 16;
    private int ly = 12;

    private int width;
    private int height;

    private final int initialSnakePosition = 14;
    private int currentSnakePosition = 14;
    private int newSnakePosition = currentSnakePosition;

    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int APPLE = 2;
    private final int STAR = 3;
    private final int SNAKE = 4;

    public boolean updateScore = false;
    public char typeOfMove = 'R'; // Up, Down, Left, Right
    public int speed = 2000;
    public int score = 0;

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
        bmp = new Bitmap[5];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.snake);
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

    public void moveSnake(){
        switch (typeOfMove) {
            case 'R':
                moveRight();
                break;
            case 'L':
                moveLeft();
                break;
            case 'D':
                moveDown();
                break;
            case 'U':
                moveUp();
                break;
        }
    }

    public void moveRight(){
        if(currentSnakePosition % + 1 < 192){
            newSnakePosition = currentSnakePosition + 1;
        }
    }

    public void moveLeft(){
        if(currentSnakePosition - 1 >= 0){
            newSnakePosition = currentSnakePosition - 1;
        }
    }

    public void moveUp(){
        if(currentSnakePosition -12 >= 0){
            newSnakePosition = currentSnakePosition - 12;
        }
    }

    public void moveDown(){
        if(currentSnakePosition +12 < 192){
            newSnakePosition = currentSnakePosition + 12;
        }
    }

    public void update(){
        processSnakeMovement();
        redraw();
    }

    public void setLevel2(){
        //levelGame = Arrays.copyOf(currentLevel, currentLevel.length);
        currentSnakePosition = initialSnakePosition;
        redraw();
    }

    private void processSnakeMovement(){
        // new is wall
        if(isWallOnPosition(newSnakePosition)){
            return;
        }

        if(isAppleOnPosition(newSnakePosition)){
            updateScore = true;
            score++;
            Log.d("Apple", "Method");
        }

        // hero movement
        levelGame[currentSnakePosition] = EMPTY;
        levelGame[newSnakePosition] = SNAKE;
        currentSnakePosition = newSnakePosition;
    }

    private void redraw(){
        this.invalidate();
    }

    private boolean isWallOnPosition(int pos){
        if(levelGame[pos] == WALL){
            return true;
        }
        return false;
    }

    private boolean isAppleOnPosition(int pos){
        if(levelGame[pos] == APPLE){
            return true;
        }
        return false;
    }
}
