package com.example.mycookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {



    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 6;
    private static final int NUM_COOKIES = 5;
    private int numClicks = 0;


    Button buttons [][] = new Button[NUM_ROWS][NUM_COLS];
    boolean isCookie[][] = getCookiePositions();
    boolean isPressed[][] = setAllValuesToFalse(new boolean[NUM_ROWS][NUM_COLS]);
    int nearCookies[][] = new int[NUM_ROWS][NUM_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // outer loop


        initializeNearCookies();

        for (int i = 0; i < NUM_ROWS; i++) {
            // codes

            // inner loop
            Log.i("outer", " x \n");
            for(int j = 0; j < NUM_COLS; j++) {
                // codes
                Log.i("inner ", Integer.toString(nearCookies[i][j]) );
            }

        }

        populateButtons();


    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForBottons);

        for (int row = 0; row < NUM_ROWS; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);
            for (int col = 0; col < NUM_COLS; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

//                button.setText(" " +  row + "," + col);

                //make text good for small buttons
                button.setPadding(0, 0, 0 , 0  );

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);
                        //gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[row][col] = button;

            }
        }
    }

    private void gridButtonClicked(int col, int row) {
        numClicks++;
        TextView textView = findViewById(R.id.countClicks);
        textView.setText("Number of clicks  : " + String.valueOf(numClicks));

//        Log.i("row and col", Integer.toString(row) +" "+ Integer.toString(col));
//        Log.i("buttons: ", Boolean.toString(buttons != null));
//        Log.i("buttons height: ", Integer.toString(buttons.length));
//        Log.i("buttons width: ", Integer.toString((buttons[0]).length));
        Button button = buttons[row][col];
        //Button button = buttons[col][row];


        //lock button sizes
        lockButtonSizes();



//        //does not scale image
//        button.setBackgroundResource( R.drawable.image_use);

        int numWid = button.getWidth();
        int numHeight = button.getHeight();
        Bitmap originalBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_use);
        Bitmap scaledBitMap = Bitmap.createScaledBitmap(originalBitMap, numWid, numHeight, true);

        if(isCookie[row][col] == true){
            button.setBackground(new BitmapDrawable(getResources(), scaledBitMap));
            if (!isPressed[row][col]){
                updateCookie(row, col);
            }
        } else {
            button.setText(String.valueOf(nearCookies[row][col]));
        }
        isPressed[row][col] = true;

//        button.setBackground(new BitmapDrawable(getResources(), scaledBitMap));

    }

    private void lockButtonSizes() {
        for (int row = 0; row < NUM_ROWS; row++){
            for (int col = 0; col < NUM_COLS; col++){
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
        }
    }

    private boolean[][] getCookiePositions(){
        int multiplication = NUM_COLS*NUM_ROWS;
        List allPositions = new ArrayList();

        boolean[][] cords = new boolean[NUM_ROWS][NUM_COLS];
        cords = setAllValuesToFalse(cords);

        for (int i = 0; i < multiplication; i++){
            allPositions.add(i);
        }
        Collections.shuffle(allPositions);

        int x;
        int y;

        for (int i = 0; i < NUM_COOKIES; i++){
            x = (int) allPositions.get(i) % NUM_COLS;
            y = (int) allPositions.get(i) / NUM_COLS;

            cords[y][x] = true;
        }
        return cords;
    }

    private boolean[][] setAllValuesToFalse(boolean[][] array){
        for (int y = 0; y < array.length-1; y++){
            for (int x = 0; x < array[0].length-1; x++){
                array[y][x] = false;
            }
        }
        return array;
    }

    private void initializeNearCookies(){
        int count;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (isCookie[i][j]){
                    //TODO
                    nearCookies[i][j] = NUM_COLS*2;
                    continue;
                }
                count = 0;
                for (int k = 0; k < NUM_ROWS; k++) {
                    if (isCookie[k][j]){
                        count++;
                    }
                }
                for (int w = 0; w < NUM_COLS; w++) {
                    if (isCookie[i][w]){
                        count++;
                    }
                }
                nearCookies[i][j] = count;
            }
        }
    }

    private void updateCookie(int row,int col){
        Button button;
        for (int i = 0; i < NUM_COLS; i++) {
            button = buttons[row][i];
            //if this button was already pressed AND is not a cookie, then update its visual and -
            // set it to be -1 of what it was before
            if (!isCookie[row][i]){
                if (nearCookies[row][i] > 0){
                    nearCookies[row][i]--;
                }
                if (isPressed[row][i]){
                    button.setText(String.valueOf(nearCookies[row][i]));
                }
            }
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            button = buttons[i][col];
            //if this button was already pressed AND is not a cookie, then update its visual and -
            // set it to be -1 of what it was before
            if (!isCookie[i][col]){
                if (nearCookies[i][col] > 0){
                    nearCookies[i][col]--;
                }
                if (isPressed[i][col]){
                    button.setText(String.valueOf(nearCookies[i][col]));
                }
            }
        }
        
    }
}