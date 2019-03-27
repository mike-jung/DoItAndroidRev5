package org.techtown.tutorial.graphic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * PaintBoard를 보여주기 위한 액티비티
 * 
 * @author Mike
 *
 */
public class PaintBoardActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PaintBoard board = new PaintBoard(this);
        setContentView(board);
    }

}
