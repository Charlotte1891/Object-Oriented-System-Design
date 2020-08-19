package edu.stanford.cs108.cityinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText name;
    EditText continent;
    EditText population;
    String nameAdded;
    String continentAdded;
    int populationAdded;
    Button toastBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.name_Text);
        continent = findViewById(R.id.continent_Text);
        population = findViewById(R.id.population_Text);
        toastBtn = (Button) findViewById(R.id.toast_btn);

        db = openOrCreateDatabase("CitiesDB", MODE_PRIVATE, null);
    }


    public void onAdd(View view){
        // insert user input into database
        nameAdded = name.getText().toString();
        continentAdded= continent.getText().toString();
        populationAdded = Integer.parseInt(population.getText().toString());
        insertData(nameAdded, continentAdded, populationAdded);

        // provide user feedback with a toast
        Toast toast = Toast.makeText(
                AddActivity.this,
                name.getText().toString() + " Added",
                Toast.LENGTH_SHORT);
        toast.show();

        // clear EditText fields
        name.setText("");
        continent.setText("");
        population.setText("");
    }


    private boolean insertData(String Name, String Continent, int Population){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", Name);
        contentValues.put("continent",Continent);
        contentValues.put("population", Population);

        long result = db.insert("Cities", null, contentValues);

        if(result == -1) return false;
        else return true;
    }

}
