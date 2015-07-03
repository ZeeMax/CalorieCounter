package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Food;

/**
 * Created by nairuz on 0001, July, 1, 2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    //variables:
    private ArrayList<Food> foodList = new ArrayList<>();

    //Constructor
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table

        //SQL commands to create the table in DB
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.FOOD_NAME +
                " TEXT, " + Constants.FOOD_CALORIES_NAME + " INT, " + Constants.DATE_NAME + " LONG);";

        //After creating the table pass it execution
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to upgrade the database, you have to drop the old version table and create a new one, like the following
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //create a new table
        onCreate(db);
    }


    //This method counts the numbe of items entered in the list
    public int getTotalItems() {

        int totalItems = 0;
        //create a query
        String query = "SELECT * FROM " + Constants.TABLE_NAME;

        //instantiate database:
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null); //to get all the info form the table

        totalItems = cursor.getCount();//count the total items in the list

        //close the cursor
        cursor.close();

        return totalItems;
    }


    //This method counts the number of calories in the list
    public int totalCalories() {

        int cals = 0;

        String query = "SELECT SUM( " + Constants.FOOD_CALORIES_NAME + " ) " +
                "FROM " + Constants.TABLE_NAME;

        //instantiate the SQL db
        SQLiteDatabase dba = this.getReadableDatabase();

        //create a cursor:
        Cursor cursor = dba.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            //count only one column
            cals = cursor.getInt(0);
        }
        //close the cursor after fetching the info
        cursor.close();
        dba.close(); //close the database after fetching the infos
        return cals;
    }


    //Delete food items from the list,
    public void deleteFood (int id){

        //instantiate database:
        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constants.TABLE_NAME, Constants.KEY_ID + "= ?",
                new String[]{String.valueOf(id)});

        dba.close();
    }

    //Add food items to list:
    public void addFood (Food food){

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.FOOD_NAME,food.getFoodName());
        values.put(Constants.FOOD_CALORIES_NAME, food.getCalories());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());

        //insert the infos to the database:
        dba.insert(Constants.TABLE_NAME,null, values);

        //testing :
        Log.v("Food item is added", "yes");

        dba.close();
    }


    //Retrieving all the items that were inserted in the database:
    public ArrayList<Food> getFoods (){

        foodList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.FOOD_NAME, Constants.FOOD_CALORIES_NAME,
                        Constants.DATE_NAME}, null, null, null, null, Constants.DATE_NAME + " DESC ");

        //Loop through
        if (cursor.moveToFirst()) {
            do{
                Food food = new Food();
                food.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.FOOD_NAME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.FOOD_CALORIES_NAME)));
                food.setFoodId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                //format date :
                DateFormat dateFormat = DateFormat.getDateInstance();

                //create a string to hold the date
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                food.setRecordDate(date);

                foodList.add(food);

            }while(cursor.moveToNext());
        }

        //close cursor and db
        cursor.close();
        dba.close();

        return foodList;
    }


}
