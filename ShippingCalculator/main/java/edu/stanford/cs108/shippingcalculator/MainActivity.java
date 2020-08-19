package edu.stanford.cs108.shippingcalculator;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.CheckBox;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculateCost(View view) {
        // user input weight
        EditText editText = findViewById(R.id.weight);
        double weight = Double.parseDouble(editText.getText().toString());
        // determine unit cost based on user input
        int unitCost = 0;
        RadioGroup radioGroup = findViewById(R.id.three_Group);
        int currentCheck = radioGroup.getCheckedRadioButtonId();
        switch(currentCheck) {
            case R.id.nextDay:
                unitCost = 10;
                break;
            case R.id.secondDay:
                unitCost = 5;
                break;
            case R.id.standard:
                unitCost = 3;
                break;
        }
        // calculate cost with/without insurance
        double cost = weight * unitCost;
        long totalCost;
        CheckBox checkBox = findViewById(R.id.insurance);
        if(checkBox.isChecked()) {
            totalCost = Math.round(cost * 1.2);
        } else {
            totalCost = Math.round(cost);
        }
        TextView textView = findViewById(R.id.cost);
        textView.setText("$" + String.valueOf(totalCost));
    }
}
