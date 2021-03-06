package com.example.kru13.sokoview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.kru13.sokoview.SimpleGestureFilter.SimpleGestureListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity implements SimpleGestureListener {

    Handler handlerForMove = new Handler();

    private SimpleGestureFilter detector;
    private SnakeView gameView;
    private TextView infoText;
    private int levelSelected = 1;

    private final String level1Text =
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0";

    private final String level2Text =
            "1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,\n" +
            "0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1";

    private final String level3Text =
            "1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,\n" +
            "1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1";

    private final String level4Text =
            "1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1,\n" +
            "1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,\n" +
            "1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1";

    private final String level5Text =
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (SnakeView)findViewById(R.id.game_view);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            gameView.speed = extras.getInt("speed");
            levelSelected = extras.getInt("level");
            gameView.levelOfTheGame = levelSelected;
            gameView.soundOn = extras.getBoolean("sound");
            gameView.vibrationOn = extras.getBoolean("vibration");
        }

        // save levels
        try {
            saveLevel("level1.txt", level1Text);
            saveLevel("level2.txt", level2Text);
            saveLevel("level3.txt", level3Text);
            saveLevel("level4.txt", level4Text);
            saveLevel("level5.txt", level5Text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read from file
        try {
            gameView.level1 = readLevel("level1.txt").toArray(gameView.level1);
            gameView.level2 = readLevel("level2.txt").toArray(gameView.level2);
            gameView.level3 = readLevel("level3.txt").toArray(gameView.level3);
            gameView.level4 = readLevel("level4.txt").toArray(gameView.level4);
            gameView.level5 = readLevel("level5.txt").toArray(gameView.level5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (levelSelected) {
            case 1:
                gameView.levelGame = Arrays.copyOf(gameView.level1, gameView.level1.length);
                break;
            case 2:
                gameView.levelGame = Arrays.copyOf(gameView.level2, gameView.level2.length);
                break;
            case 3:
                gameView.levelGame = Arrays.copyOf(gameView.level3, gameView.level3.length);
                break;
            case 4:
                gameView.levelGame = Arrays.copyOf(gameView.level4, gameView.level4.length);
                break;
            case 5:
                gameView.levelGame = Arrays.copyOf(gameView.level5, gameView.level5.length);
                break;
        }

        //gameView.levelGame = Arrays.copyOf(gameView.currentLevel, gameView.currentLevel.length);

        // info
        infoText = (TextView)findViewById(R.id.info_text);
        infoText.setText("Level: " + levelSelected + "  Score: 0");

        gameView.snakeBody.add(14);

        // Detect touched area
        detector = new SimpleGestureFilter(MainActivity.this, this);
        handlerForMove.post(runnableCode);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT:
                if(gameView.typeOfMove != 'L')gameView.typeOfMove = 'R';
                else return;
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                if(gameView.typeOfMove != 'R')gameView.typeOfMove = 'L';
                else return;
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                if(gameView.typeOfMove != 'U')gameView.typeOfMove = 'D';
                else return;
                break;
            case SimpleGestureFilter.SWIPE_UP:
                if(gameView.typeOfMove != 'D')gameView.typeOfMove = 'U';
                else return;
                break;
        }
    }

    @Override
    public void onDoubleTap() {
        handlerForMove.removeCallbacks(runnableCode);
        new AlertDialog.Builder(this)
                .setTitle("Pause")
                .setMessage("Press 'continue' to continue")
                .setCancelable(false)
                .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handlerForMove.post(runnableCode);
                    }
                }).show();
    }

    private void saveLevel(String filePath, String fileText) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            OutputStream outStream = null;
            try{
                outStream = openFileOutput(filePath, Context.MODE_PRIVATE);
                outStream.write(fileText.getBytes());
            }
            finally {
                if(outStream != null){
                    outStream.close();
                }
            }
        }

    }

    private ArrayList<Integer> readLevel(String fileName) throws IOException {
        File file = new File(getFilesDir() + "/" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        ArrayList<Integer> arrayLevel = new ArrayList();
        String arrayLine[];
        while((line = reader.readLine()) != null){
            arrayLine = line.split(",");
            for(int i = 0; i < arrayLine.length; i++){
                arrayLevel.add(Integer.parseInt(arrayLine[i].trim()));
            }
        }

        reader.close();
        return arrayLevel;
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            if(gameView.updateScore == true) {
                infoText.setText("Level: " + levelSelected + "  Score: " + gameView.score);
                gameView.updateScore = false;
            }

            if(gameView.endGame == true){
                endGame();
            }
            else {
                gameView.starSpawning();
                gameView.moveSnake();
                gameView.update();
                handlerForMove.postDelayed(this, gameView.speed);
            }
        }
    };

    private void endGame() {
        handlerForMove.removeCallbacks(runnableCode);
        new AlertDialog.Builder(this)
                .setTitle("End game")
                .setMessage("Your score " + gameView.score)
                .setCancelable(false)
                .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(settingsActivity);
                    }
                }).show();
    }

}

