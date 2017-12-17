package com.example.kru13.sokoview;

import android.os.Vibrator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by kru13 on 12.10.16.
 */
public class SnakeView extends View {

    MediaPlayer mp;
    Vibrator vibration;
    private Bitmap[] bmp;

    private int lx = 16;
    private int ly = 12;

    private int width;
    private int height;

    private final int initialSnakePosition = 14;
    private int currentSnakePosition = 14;
    private int newSnakePosition = currentSnakePosition;
    private int timeToNextStar = -1;
    private int timeToClearStar;
    private int starPosition;
    private boolean starSpawned = false;
    private boolean starTaken = false;
    private boolean starTimerAppleEat = false;

    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int APPLE = 2;
    private final int STAR = 3;
    private final int SNAKE = 4;

    public boolean updateScore = false;
    public char typeOfMove = 'R'; // Up, Down, Left, Right
    public int speed = 200;
    public int score = 0;


    public ArrayList<Integer> snakeBody = new ArrayList<>();

    Integer level1[] = new Integer[192];

    Integer level2[] = new Integer[100];

    Integer[] levelGame;

    public SnakeView(Context context) {
        super(context);
        init(context);

        vibration = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(context, R.raw.front_desk_bells_daniel_simon);
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        vibration = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(context, R.raw.front_desk_bells_daniel_simon);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        vibration = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(context, R.raw.front_desk_bells_daniel_simon);
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
        newSnakePosition = currentSnakePosition + 1;
        if(newSnakePosition % 12 == 0)newSnakePosition = newSnakePosition - 12;
    }

    public void moveLeft(){
        newSnakePosition = currentSnakePosition - 1;
        if(newSnakePosition % 12 == 11)newSnakePosition = newSnakePosition + 12;
    }

    public void moveUp(){
        newSnakePosition = currentSnakePosition - 12;
        if(newSnakePosition < 0) newSnakePosition = 180 + currentSnakePosition % 12;
    }

    public void moveDown(){
        newSnakePosition = currentSnakePosition + 12;
        if(newSnakePosition >= 192) newSnakePosition = 0 + currentSnakePosition % 12;
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

    public void starSpawning (){
        if(starSpawned == false && ((snakeBody.size() % 5) == 0) && starTimerAppleEat == true) {
            timeToNextStar = ThreadLocalRandom.current().nextInt(20, 30 + 1);
            starTimerAppleEat = false;
            Log.d("starSpawning", String.valueOf(snakeBody.size()));
        }
        if(starSpawned == false) {
            if (timeToNextStar == 0) {
                Log.d("starSpawningCreate", String.valueOf(timeToNextStar));
                int randomNum = 0;
                while (true) {
                    randomNum = ThreadLocalRandom.current().nextInt(0, 192 + 1);
                    if (levelGame[randomNum] == EMPTY) break;
                }
                levelGame[randomNum] = STAR;
                starPosition = randomNum;
                timeToClearStar = 20;
                starSpawned = true;
                timeToNextStar--;
            } else {
                timeToNextStar--;
                Log.d("starSpawningMinus", String.valueOf(timeToNextStar));
            }
        }else {
            if (timeToClearStar == 0){
                if(starTaken == false) {
                    levelGame[starPosition] = EMPTY;
                }
                starTaken = false;
                starSpawned = false;
            }
            else {
                timeToClearStar--;
            }
        }
    }

    private void processSnakeMovement(){
        // new is wall
        if(isWallOnPosition(newSnakePosition)){
            vibration.vibrate(500);
            return;
        }

        if(isSnakeOnPosition(newSnakePosition)){
            vibration.vibrate(500);
            return;
        }

        if(isStarOnPosition(newSnakePosition)){
            vibration.vibrate(500);
            updateScore = true;
            score = score + 5;
            starTaken = true;
        }

        if(isAppleOnPosition(newSnakePosition)){
            vibration.vibrate(500);
            mp.start();
            starTimerAppleEat = true;
            updateScore = true;
            score++;
            int randomNum = 0;

            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 192 + 1);
                if(levelGame[randomNum] == EMPTY) break;
            }
            snakeBody.add(snakeBody.get(snakeBody.size() - 1));
            levelGame[randomNum] = APPLE;
        }

        levelGame[snakeBody.get(snakeBody.size() - 1)] = EMPTY;
        levelGame[newSnakePosition] = SNAKE;

        rewriteSnakeBody(newSnakePosition);

        currentSnakePosition = newSnakePosition;
    }

    private void rewriteSnakeBody(int headPosition){
        if(snakeBody.size() > 1){
            for(int i = snakeBody.size() - 1; i>0; i--){
                snakeBody.set(i, snakeBody.get(i-1));
            }
        }
        snakeBody.set(0, headPosition);
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

    private boolean isSnakeOnPosition(int pos){
        if(levelGame[pos] == SNAKE){
            return true;
        }
        return false;
    }

    private boolean isStarOnPosition(int pos){
        if(levelGame[pos] == STAR){
            return true;
        }
        return false;
    }
}
