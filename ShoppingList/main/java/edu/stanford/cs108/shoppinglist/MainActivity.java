package edu.stanford.cs108.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shoppingList = new ArrayList<String>();
    }

    private ArrayList<String> shoppingList;

    public void addItem(View view) {
        // update from EditText
        EditText editText = findViewById(R.id.newItem);
        String text = editText.getText().toString();
        editText.setText("");
        shoppingList.add(text);
        // show items in TextView
        String items = "";
        for(int i = 0; i < shoppingList.size(); i++){
            items = items + shoppingList.get(i) + "\n";
        }
        TextView textView = findViewById(R.id.currentList);
        textView.setText(items);
    }

    public void clearList(View view){
        TextView textView = findViewById(R.id.currentList);
        textView.setText("");
        shoppingList.clear();
    }


}

