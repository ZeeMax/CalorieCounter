package nairuz.com.calcounter;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.Food;
import utl.Utils;


public class DisplayFoodsActivity extends Activity {

    private ListView listView;
    private TextView totalCals;
    private TextView totalFoodItems;
    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private Food myFood ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);


        //Texts:
        totalCals = (TextView) findViewById(R.id.totalFoodCalories_Id);
        totalFoodItems = (TextView) findViewById(R.id.total_foodItems_Id);

        //ListView:
        listView = (ListView) findViewById(R.id.listId);


        refreshData();
    }

    public void refreshData (){

        dbFoods.clear();

        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getFoods();

        int totalCalorie = dba.totalCalories();
        int toalItems = dba.getTotalItems();


        String formattedCalories = Utils.formatNumber(totalCalorie);
        String formattedItems = Utils.formatNumber(toalItems);

        //setting the editTexts:
       totalFoodItems.setText("Food Items: " + formattedItems);
        totalCals.setText("Total Calories: " + formattedCalories );


        //Loop
        for (int i = 0; i < foodsFromDB.size(); i ++){

            String name = foodsFromDB.get(i).getFoodName();
            String date = foodsFromDB.get(i).getRecordDate();
            int cal = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("Food Id" , String.valueOf(foodId));

            myFood= new Food();
            myFood.setFoodId(foodId);
            myFood.setFoodName(name);
            myFood.setCalories(cal);
            myFood.setRecordDate(date);

            dbFoods.add(myFood);
        }
        dba.close();

        //setting food Adapter:
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row,dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_foods, menu);
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
