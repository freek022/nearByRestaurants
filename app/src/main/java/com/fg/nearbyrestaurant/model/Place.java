package com.fg.nearbyrestaurant.model;

import android.media.Image;

import com.fg.nearbyrestaurant.R;

/**
 * Created by fred on 3/30/2017.
 */

public class Place {

    private String place_id;
    //private String icon;
    private String namePlace;
    private String category;
    private String rating;
    private String openNow;
    private String vicinity;
    private double latitude;
    private double longitude;

    public Place(){
        this.place_id = "";
        this.namePlace = "";
        this.category = "";
        this.rating = "";
        this.openNow = "";
        this.vicinity = "";
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setLatLng(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }
}
