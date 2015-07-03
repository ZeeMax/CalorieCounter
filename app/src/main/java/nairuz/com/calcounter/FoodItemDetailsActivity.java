package nairuz.com.calcounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;


public class FoodItemDetailsActivity extends ActionBarActivity {

    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);


        foodName = (TextView) findViewById(R.id.details_FoodName);
        calories = (TextView) findViewById(R.id.details_Caloriesvalue);
        dateTaken = (TextView) findViewById(R.id.detials_dateText);
        shareButton = (Button) findViewById(R.id.details_shareButton);


        Food food = (Food) getIntent().getSerializableExtra("user Obj");
        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());

        foodId = food.getFoodId();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });

    }


    public void shareCals (){
        StringBuilder dataBuilder = new StringBuilder();

        String name =  foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataBuilder.append(" Food: " + name +"\n");
        dataBuilder.append("Calories: " + cals + "\n");
        dataBuilder.append("Consumed on: " + date + "\n");

        //create an intentSend to send something
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(intent.EXTRA_SUBJECT, "CalCounter");
        intent.putExtra(intent.EXTRA_EMAIL, new String[]{"recepient@example.com"});
        intent.putExtra(intent.EXTRA_TEXT, dataBuilder.toString());


        //for checking
        try {
            //sending the email
            startActivity(Intent.createChooser(intent,"send email..."));

        }catch (ActivityNotFoundException e ){

            Toast.makeText(getApplicationContext(),"Please install email clients", Toast.LENGTH_LONG).show();
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_Id) {

            //create Alert box
            AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
            alert.setTitle("Delete ?");
            alert.setMessage("Are you sure you want to delete this Item");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //create database instance :
                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteFood(foodId);

                    //Toast to set a message that the item is deleted
                    Toast.makeText(getApplicationContext(),"Food item deleted! ", Toast.LENGTH_LONG).show();


                    //Go back to the list view
                    startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));

                    //remove the activity from the stack
                    FoodItemDetailsActivity.this.finish();
                }
            });

            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
