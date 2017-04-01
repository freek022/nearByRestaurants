package com.fg.nearbyrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fred on 3/30/2017.
 */

public class PlaceDetailsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static final String PLACE_ID = "place_id";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private TextView mPlaceId;
    private TextView mNamePlace;
    private TextView mAddress;
    private TextView mPhone;
    private TextView mWeb;
    private TextView mAttr;

    private Button telephone, map, review;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        //mPlaceId = (TextView) findViewById(R.id.restaurant_id);
        mNamePlace = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.place_address);
        mPhone = (TextView) findViewById(R.id.phone_number);
        mWeb = (TextView) findViewById(R.id.web);
        mAttr = (TextView) findViewById(R.id.attr);

        telephone = (Button) findViewById(R.id.telephone);
        //map = (Button) findViewById(R.id.direction);
        review = (Button) findViewById(R.id.post_review);

        buildGoogleClientApi();

        RestaurantDetails();

    }

    private void RestaurantDetails() {
        Intent intent = getIntent(); // get intent from the MainActivity
        String placeId = intent.getStringExtra(PLACE_ID); // the ID string of the place

        PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        pendingResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()){
                    Log.d("PlaceDetailsResult", "Error: Place query did not complete."
                    + places.getStatus().toString());
                    return;
                }
                // select the first object
                final Place restaurant = places.get(0);
                CharSequence attributions = places.getAttributions();
                LatLng latLng = restaurant.getLatLng();
                // Toast.makeText(PlaceDetailsActivity.this, String.valueOf(
                        // latLng.latitude), Toast.LENGTH_SHORT).show();
                //mPlaceId.setText(restaurant.getId());
                mNamePlace.setText(restaurant.getName());
                mAddress.setText(restaurant.getAddress());
                telephone.setText(restaurant.getPhoneNumber());
                mPhone.setText(restaurant.getPhoneNumber());
                if (restaurant.getWebsiteUri() != null){
                    mWeb.setText(restaurant.getWebsiteUri() +"");
                } else {
                    mWeb.setText("");
                }

                if (attributions != null){
                    mAttr.setText(attributions.toString());
                } else {
                    mAttr.setText("");
                }
            }
        });
    }

    protected synchronized void buildGoogleClientApi(){
        mGoogleApiClient = new GoogleApiClient.Builder(PlaceDetailsActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connectionResult.getErrorCode();
    }
}
