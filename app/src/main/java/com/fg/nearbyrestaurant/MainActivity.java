package com.fg.nearbyrestaurant;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

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
        textView.setText("welcome " +user);
    }

    /**
     * method that initialize the toolbar
     */
    public void initToolbar(){
        // initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // set the toolbar to act as the app bar for this activity window
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nearby Restaurants");
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
}
