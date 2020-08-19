package edu.stanford.cs108.cityinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

public class LookUpActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText name;
    EditText continent;
    EditText population;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up);

        name = findViewById(R.id.nameText);
        continent = findViewById(R.id.continentText);
        population = findViewById(R.id.populationText);
        radioGroup = (RadioGroup) findViewById(R.id.criteria);

        db = openOrCreateDatabase("CitiesDB", MODE_PRIVATE, null);

    }

    public void onSearch(View view){
        // set up search criteria
        // if all three EditText fields are empty
        // display all city data in the database
        String nameStr = name.getText().toString();
        String continentStr = continent.getText().toString();
        String populationStr = population.getText().toString();
        if(populationStr.length() == 0) populationStr = "0";

        int Check = radioGroup.getCheckedRadioButtonId();

        String criteria = "";
        switch (Check) {
            case R.id.GoE:
                criteria = ">=";
                break;
            case R.id.LT:
                criteria = "<";
                break;
        }


        String[] fromArray = {"name", "continent", "population"};
        int[] toArray = {R.id.custom_text_1,R.id.custom_text_2, R.id.custom_text_3};

        Cursor cursor = db.rawQuery("SELECT * FROM Cities " +
                "WHERE name LIKE '%" + nameStr + "%' " +
                "AND continent LIKE '%" + continentStr + "%' " +
                "AND population " + criteria + populationStr + ";", null);
        ListAdapter adapter = new SimpleCursorAdapter( this,R.layout.custom_list_item,cursor, fromArray,toArray,0);
        ListView listView = (ListView) findViewById(R.id.the_list);
        listView.setAdapter(adapter);
    }
}


