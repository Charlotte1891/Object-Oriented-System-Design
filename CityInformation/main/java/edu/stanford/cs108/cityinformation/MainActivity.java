package edu.stanford.cs108.cityinformation;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Button toastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toastBtn = (Button) findViewById(R.id.resetBtn);

        db = openOrCreateDatabase("CitiesDB", MODE_PRIVATE, null);

        setUpDatabase();
    }


    private void setUpDatabase() {
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='Cities';",
                null);

        if (tablesCursor.getCount() == 0) {
            // setup the table
            // define a table of Cities
            String setupStr = "CREATE TABLE Cities ("
                    + "name TEXT, continent TEXT, population INTEGER,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(setupStr);

            // insert data into table
            String dataStr = "INSERT INTO Cities VALUES "
                    + "('Cairo','Africa',15200000, NULL),"
                    + "('Lagos','Africa',21000000, NULL),"
                    + "('Kyoto','Asia',1474570, NULL),"
                    + "('Mumbai','Asia',20400000, NULL),"
                    + "('Shanghai','Asia',24152700, NULL),"
                    + "('Melbourne','Australia',3900000, NULL),"
                    + "('London','Europe',8580000, NULL),"
                    + "('Rome','Europe',2715000, NULL),"
                    + "('Rostov-on-Don','Europe',1052000, NULL),"
                    + "('San Francisco','North America',5780000, NULL),"
                    + "('San Jose','North America',7354555, NULL),"
                    + "('New York','North America',21295000, NULL),"
                    + "('Rio de Janeiro','South America',12280702, NULL),"
                    + "('Santiago','South America',5507282, NULL)"
                    + ";";
            db.execSQL(dataStr);
        }
    }


    public void LookupCity(View view){
        Intent intent = new Intent(this, LookUpActivity.class);
        startActivity(intent);
    }


    public void AddCity(View view){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }


    public void Reset(View view){
        // drop the cities table
        String resetStr = "DROP TABLE IF EXISTS Cities";
        db.execSQL(resetStr);

        // set up the table
        setUpDatabase();

        // provide user feedback with a toast
        Toast toast = Toast.makeText(
                MainActivity.this,
                "Database Reset",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
