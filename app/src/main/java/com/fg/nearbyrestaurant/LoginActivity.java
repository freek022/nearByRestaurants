package com.fg.nearbyrestaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fg.nearbyrestaurant.api.userApi.UserApi;
import com.fg.nearbyrestaurant.api.userApi.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editEmailAddress;
    EditText editPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize editText widget
        editEmailAddress = (EditText) findViewById(R.id.edit_email_address);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // set onClickListener on the button to pull data from GAE
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call EndpointsAsyncTask
                new EndpointsAyncTask(LoginActivity.this).execute();
            }
        });
        // call method to set the toolbar
        initToolbar();
        // start register activity
        registerUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * method that initialize the toolbar
     */
    public void initToolbar(){
        // initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // set the toolbar to act as the app bar for this activity window
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login Credentials");
    }

    // method that redirects user to Register activity
    public void registerUser(){
        TextView registerView = (TextView) findViewById(R.id.text_register_user);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

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
            case android.R.id.home:
                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                return true;

            case R.id.register:
                // perform user login credentials
                Intent loginIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(loginIntent);
                return true;

            case R.id.logout:
                // current user logout
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**

    @Override
    public void finishProcess(List<User> result) {

        for (User user : result){

            String email = user.getEmail();
            String password = user.getPassword();

            String editEmail = editEmailAddress.getText().toString();
            String editPwd = editPassword.getText().toString();

            if (editEmail.matches(email) && editPwd.matches(password)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "welcome, " + user.getUserName(), Toast.LENGTH_LONG).show();
            }
        }

    }

    */
    public class EndpointsAyncTask extends AsyncTask<Void, Void, List<User>> {
        private Context context;
        private UserApi myApiService = null;
        private AsyncResponse delegate;
        private ProgressDialog mDialog;

        public EndpointsAyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Authenticating User...");
            mDialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... params) {

            if (myApiService == null){
                UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // — 10.0.2.2 is localhost’s IP address in Android emulator
                        // — turn off compression when running against local devappserver
                        .setRootUrl("http://192.168.1.12:8080/_ah/api")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request)
                                    throws IOException {
                                request.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver
                myApiService = builder.build();
            }
            try{
                return myApiService.list().execute().getItems();
            } catch (Exception e){
                return Collections.EMPTY_LIST;

            }
        }

        @Override
        protected void onPostExecute(List<User> results) {

            mDialog.dismiss();
            editEmailAddress.setText("");
            editPassword.setText("");

            for (User user : results){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", String.valueOf(user.getUserName()));
                startActivity(intent);
                Toast.makeText(context, "welcome, "+user.getUserName(), Toast.LENGTH_SHORT).show();

            }


            //delegate.finishProcess(users);

        }


    }
    public String userName(String userName){
        return userName;
    }
}
