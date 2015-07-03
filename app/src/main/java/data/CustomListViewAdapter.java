package data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import model.Food;
import nairuz.com.calcounter.FoodItemDetailsActivity;
import nairuz.com.calcounter.R;

/**
 * Created by nairuz on 0001, July, 1, 2015.
 */

public class CustomListViewAdapter extends ArrayAdapter <Food> {

    private int layoutResources;
    private Activity activity  ;
    private ArrayList<Food> foodList = new ArrayList<>();

    public CustomListViewAdapter(Activity act, int resource, ArrayList<Food> data) {
        super(act, resource, data);
        activity =act;
        layoutResources= resource;
        foodList =data;

        //notify the changes:
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Food getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public int getPosition(Food item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //This method is responible for showing the information in a listView format (inflating view)
        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag() == null)){

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResources, null );

            holder = new ViewHolder();
            holder.foodName = (TextView) row.findViewById(R.id.name);
            holder.foodCalories = (TextView) row.findViewById(R.id.calories);
            holder.foodDate = (TextView) row.findViewById(R.id.dateText_id);

            row.setTag(holder);
        }
        else{
            holder= (ViewHolder) row.getTag();
        }

        holder.food = getItem(position);

        holder.foodName.setText(holder.food.getFoodName());
        holder.foodCalories.setText(String.valueOf(holder.food.getCalories()));
        holder.foodDate.setText(String.valueOf(holder.food.getRecordDate()));


        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (activity, FoodItemDetailsActivity.class);

                //pass information from one actvity to another
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user Obj", finalHolder.food );
                intent.putExtras(mBundle);
                activity.startActivity(intent); //to pass the activity
            }
        });

        return row;
    }


    public class ViewHolder{
        Food  food;
        TextView foodName;
        TextView foodCalories;
        TextView foodDate;
    }
}
