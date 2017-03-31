package com.fg.nearbyrestaurant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fg.nearbyrestaurant.adapter.RestaurantAdapter;
import com.fg.nearbyrestaurant.model.Place;
import com.fg.nearbyrestaurant.utility.DownloadUrl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_REQUEST_RESOLUTION = 9000;
    private final static int UPDATE_INTERVAL = 10000;
    private final static int FASTEST_UPDATE = 5000;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    DownloadUrl downloadUrl;
    RestaurantAdapter adapter;

    double latitude;
    double longitude;
    private int RADIUS = 1000;
    private String RESTAURANT ="restaurant";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inset the toolbar on this acctivity
        initToolbar();
        // receive data from LoginActivity
        String user = getIntent().getStringExtra("user");

        // initialize textView widget
        textView = (TextView) findViewById(R.id.logged_user);
        textView.setText("welcome " + user);

        // initialize GoogleApiClient
        buildGoogleApiClient();
        // initialize location request updates
        createLocationRequest();

    }

    /**
     * method that initialize the toolbar
     */
    public void initToolbar() {
        // initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // set the toolbar to act as the app bar for this activity window
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nearby Restaurants");
    }

    /**
     * a method that instantiate GoogleApiClient
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * a method that instantiate location updates
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_UPDATE);
    }


    private void handleNewLocation(Location location){
        Log.d(TAG, location.toString());
        mCurrentLocation = location;
        String msg = "Your Current location is "
                +String.valueOf(mCurrentLocation.getLatitude()) +"\n"
                +String.valueOf(mCurrentLocation.getLongitude());
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    protected String getUrl(double latitude, double longitude) {
        StringBuilder sb = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                .append("location=" + latitude + "," + longitude)
                .append("&radius=" +RADIUS)
                .append("&type=" + RESTAURANT)
                .append("&key=" + "AIzaSyA-I9gzltM0QkJB3OcoyaAcFPgHL7zOpN4")
                .append("&sensor=true");

        Log.d(TAG, sb.toString());

        return sb.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services is connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null){
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

            latitude = location.getLatitude();
            latitude = location.getLongitude();
            Double[] params = {latitude, longitude};
            // pass params to RestaurantAsyTask to start searching for
            // nearplaces
            new RestaurantAsyncTask().execute(params);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "location services connection failed with code"
        + connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
            handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()){

            case R.id.login:
                // perform user login credentials
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            case R.id.register:
                // perfom user registration
                Intent registerUserIntent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(registerUserIntent);
                return true;
            case R.id.logout:
                // current user logout
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates
                    (mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public class RestaurantAsyncTask extends AsyncTask<Double, String, String>{

        String data;
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setTitle("Restaurants nearby");
            mDialog.setMessage("Loading please wait...");
            mDialog.show();
        }

        @Override
        protected String doInBackground(Double... params) {
            double lat = params[0]; // location latitude
            double lng = params[1]; // location longitude

            String url = getUrl(lat, lng); // attach the lat and lng to url string

            downloadUrl = new DownloadUrl(); // initialize the DownloadUrl
            try {
                data = downloadUrl.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(final String result) {
            // dismiss dialog after getting all the results
            mDialog.dismiss();
            // start parsing the Google places in JSON format invoked from the
            // doInBackground method
            // updating UI from the Background thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parseGooglePlaces(result);
                }
            });
            //textView.setText(result.toString());
        }
        /**
         * method that parses the JSON returned from the Googlep laces Api
         */
        private ArrayList parseGooglePlaces(final String result){
            final ArrayList<Place> restaurant = new ArrayList();

        }
    }
}
