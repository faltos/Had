package com.example.kru13.sokoview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

    private SimpleGestureFilter detector;
    private SnakeView gameView;
    private TextView infoText;

    private final String level1Text = "0, 0, 0, 0, 0, 0, 0, 0, 0, 0,\n" +
            "0, 0, 0, 1, 1, 1, 0, 0, 0, 0,\n" +
            "0, 0, 0, 1, 3, 1, 0, 0, 0, 0,\n" +
            "0, 0, 0, 1, 0, 1, 1, 1, 1, 0,\n" +
            "0, 1, 1, 1, 2, 0, 2, 3, 1, 0,\n" +
            "0, 1, 3, 0, 2, 4, 1, 1, 1, 0,\n" +
            "0, 1, 1, 1, 1, 2, 1, 0, 0, 0,\n" +
            "0, 0, 0, 0, 1, 3, 1, 0, 0, 0,\n" +
            "0, 0, 0, 0, 1, 1, 1, 0, 0, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0";

    private final String level2Text = "1, 1, 1, 1, 1, 1, 1, 1, 1, 0,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 1, 0,\n" +
            "1, 0, 2, 3, 3, 2, 1, 0, 1, 0,\n" +
            "1, 0, 1, 3, 2, 3, 2, 0, 1, 0,\n" +
            "1, 0, 2, 3, 3, 2, 4, 0, 1, 0,\n" +
            "1, 0, 1, 3, 2, 3, 2, 0, 1, 0,\n" +
            "1, 0, 2, 3, 3, 2, 1, 0, 1, 0,\n" +
            "1, 0, 0, 0, 0, 0, 0, 0, 1, 0,\n" +
            "1, 1, 1, 1, 1, 1, 1, 1, 1, 0,\n" +
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (SnakeView)findViewById(R.id.game_view);

        // save levels
        try {
            saveLevel("level1.txt", level1Text);
            saveLevel("level2.txt", level2Text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read from file
        try {
            gameView.level1 = readLevel("level1.txt").toArray(gameView.level1);
            gameView.level2 = readLevel("level2.txt").toArray(gameView.level2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameView.currentLevel = Arrays.copyOf(gameView.level1, gameView.level1.length);
        gameView.levelGame = Arrays.copyOf(gameView.currentLevel, gameView.currentLevel.length);

        // info
        infoText = (TextView)findViewById(R.id.info_text);
        infoText.setText("Level 1");

        // Detect touched area
        detector = new SimpleGestureFilter(MainActivity.this, this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        //Detect the swipe gestures and move
        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                gameView.moveRight();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                gameView.moveLeft();
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                gameView.moveDown();
                break;
            case SimpleGestureFilter.SWIPE_UP:
                gameView.moveUp();
                break;

        }
        gameView.update();
        if(gameView.isEndOfGame()){
            infoText.setText("Winner!!!");
        }
    }

    //level 2 when double tapped on screen
    @Override
    public void onDoubleTap() {
        infoText.setText("Level 2");
        gameView.setLevel2();
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

}

