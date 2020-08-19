package edu.stanford.cs108.mobiledraw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import static edu.stanford.cs108.mobiledraw.CustomView.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void update (View view){
        EditText editText_X = (EditText)findViewById(R.id.x);
        String x = editText_X.getText().toString();

        EditText editText_Y = (EditText)findViewById(R.id.y);
        String y = editText_Y.getText().toString();

        EditText editText_W = (EditText)findViewById(R.id.width);
        String w = editText_W.getText().toString();

        EditText editText_H = (EditText)findViewById(R.id.height);
        String h = editText_H.getText().toString();

        float fx = Float.parseFloat(x);
        float fy = Float.parseFloat(y);
        float fw = Float.parseFloat(w);
        float fh = Float.parseFloat(h);

        // select/erase mode: update current selected shape and display
        if(mode == 0 || mode == 3){
            drawings.get(updateId).left = fx;
            drawings.get(updateId).right = fx + fw;
            drawings.get(updateId).top = fy;
            drawings.get(updateId).bottom = fy + fh;
        }else{
            left = fx;
            top = fy;
            right = fx + fw;
            bottom = fy + fh;
            drawings.remove(updateId);
            shapes.remove(updateId);
        }
        CustomView customView = (CustomView) findViewById(R.id.customview);
        customView.invalidate();
    }

}