package com.fg.nearbyrestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fg.nearbyrestaurant.R;
import com.fg.nearbyrestaurant.model.Place;

import java.util.ArrayList;

/**
 * Created by fred on 3/31/2017.
 */

public class RestaurantAdapter extends BaseAdapter{

    private LayoutInflater mInflator;
    private Context mContext;
    private ArrayList<Place> restaurant;

    public RestaurantAdapter(Context mContext, ArrayList<Place> restaurant){
        super();
        mInflator = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.restaurant = restaurant;
    }
    @Override
    public int getCount() {
        return restaurant.size();
    }

    @Override
    public Place getItem(int position) {
        return restaurant.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // reference to the ViewHolder
        // whenever convertView is not null we can re-use it
        if (convertView == null){
            convertView = mInflator.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(); // initialize the viewHolder

            viewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
            viewHolder.namePlace = (TextView) convertView.findViewById(R.id.name_place);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
        } else {
            // get the viewHolder back to get fast the views
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.logo.setImageResource(R.drawable.ic_action_food);
        viewHolder.namePlace.setText(restaurant.get(position).getNamePlace());
        viewHolder.address.setText(restaurant.get(position).getVicinity());

        return convertView;
    }

    static class ViewHolder{
        private ImageView logo;
        private TextView namePlace, address;
    }
}
