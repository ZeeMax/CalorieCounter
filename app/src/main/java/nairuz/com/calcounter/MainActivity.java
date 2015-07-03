package nairuz.com.calcounter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;


public class MainActivity extends ActionBarActivity {

    private EditText foodName;
    private EditText caloriesNumbers;
    private Button submitButton;
    private DatabaseHandler dba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditTexts:
        foodName =(EditText) findViewById(R.id.food_nameId);
        caloriesNumbers = (EditText) findViewById(R.id.calorie_Id);


        //database:
        dba = new DatabaseHandler(MainActivity.this);

        //Button
        submitButton = (Button) findViewById(R.id.submitButton_Id);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataToDB();

            }
        });


    }



    public void saveDataToDB (){

        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String calString = caloriesNumbers.getText().toString().trim();

        //convert the claories numbers to text
        int cal = Integer.parseInt(calString);

        if (name.equals(" ") || calString.equals(" ")){

            Toast.makeText(getApplicationContext(),"Please enter information", Toast.LENGTH_LONG).show();
        }else{

            food.setFoodName(name);
            food.setCalories(cal);

            //call addFood method from the DatabaseHandler
            dba.addFood(food);
            dba.close();


            //clear the editTexts
            foodName.setText("");
            caloriesNumbers.setText("");

            //take the user to the next screen
            //
             startActivity(new Intent(MainActivity.this, DisplayFoodsActivity.class));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
