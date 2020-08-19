package edu.stanford.cs108.colorpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeColor(View view){
        SeekBar redBar = findViewById(R.id.redBar);
        SeekBar greenBar = findViewById(R.id.greenBar);
        SeekBar blueBar = findViewById(R.id.blueBar);
        int red = redBar.getProgress();
        int green = greenBar.getProgress();
        int blue = blueBar.getProgress();
        View backGround = findViewById(R.id.background);
        backGround.setBackgroundColor(Color.rgb(red,green,blue));
        String updateColor = "Red: " + red + ", Green: " + green + ", Blue: " + blue;
        TextView textView = findViewById(R.id.rgb);
        textView.setText(updateColor);
    }
}
