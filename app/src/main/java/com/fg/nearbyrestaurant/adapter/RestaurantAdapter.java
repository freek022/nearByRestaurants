package com.fg.nearbyrestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fg.nearbyrestaurant.R;
import com.fg.nearbyrestaurant.model.Place;

import java.util.ArrayList;

/**
 * Created by fred on 3/31/2017.
 */

public class RestaurantAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    Context con;
    ArrayList<Place> restaurant;

    public RestaurantAdapter(Context context, ArrayList<Place> restaurant) {
        super();
        mInflater = LayoutInflater.from(context);
        con=context;
        this.restaurant = restaurant;
    }
    // get total count of array
    @Override
    public int getCount() {
        return restaurant.size();
    }
    //get item id
    @Override
    public long getItemId(int position) {
        return position;
    }
    // get Place object at given position
    @Override
    public Place getItem(int position) {
        return restaurant.get(position);
    }

    // View method called for each row of the result
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.icon);
            holder.namePlace = (TextView) convertView.findViewById(R.id.name_place);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            // Bind the data efficiently with the holder.
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        // If weren't re-ordering this you could rely on what you set last time
        //holder.logo.setImageResource(restaurant.get(position).getImage());
        holder.namePlace.setText(restaurant.get(position).getNamePlace());
        holder.address.setText(restaurant.get(position).getVicinity());
        return convertView;
    }
    // viewholder class to hold adapter views
    static class ViewHolder{
        ImageView logo;
        TextView namePlace, address;
    }
}
