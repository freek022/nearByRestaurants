package com.fg.nearbyrestaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


/**
 * Created by fred on 2/24/2017.
 */
public class RegisterUserActivity extends AppCompatActivity{
    private static final String TAG = "RegisterUserActivity";

    EditText editUserName, editName, editEmailAddress, editPassword, editConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        // call method to set the toolbar
        initToolbar();

        // initialize EditText widget
        editUserName = (EditText) findViewById(R.id.edit_user_name);
        editName = (EditText) findViewById(R.id.edit_name);
        editEmailAddress = (EditText) findViewById(R.id.edit_email_address);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);

        // call the register user method
        registerUser();
        // redirect to login user method menu
        loginUser();

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
        getSupportActionBar().setTitle("Register User");
    }

    // method that registers the user for the first time
    public void registerUser(){

        Button btnRegisterUser = (Button) findViewById(R.id.btn_register_user);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = editUserName.getText().toString();
                String name = editName.getText().toString();
                String emailAddress = editEmailAddress.getText().toString();
                String password = editPassword.getText().toString();
                String confirmPassword = editConfirmPassword.getText().toString();

                // validate form fields that they are not empty
                if(name.length() == 0 || userName.length() == 0 || emailAddress.length() == 0
                        || password.length() == 0 || confirmPassword.length() == 0){
                    Log.w(TAG, "Missing information on form, can not save credentials");
                    Toast.makeText(RegisterUserActivity.this, "Fields must not be blank.", Toast.LENGTH_SHORT).show();

                    return;

                }
                // validate if password and confirma password match
                if (!password.matches(confirmPassword)){
                    Log.w(TAG, "Password does not match");
                    Toast.makeText(RegisterUserActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }

                // perform transaction
                String[] params = {userName, name, emailAddress, password };
                new EndpointsAsyncTask(RegisterUserActivity.this).execute(params);

            }
        });

    }

    // method that redirects user to login activity
    public void loginUser(){
        TextView loginView = (TextView) findViewById(R.id.text_login_user);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
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
                Intent homeIntent = new Intent(RegisterUserActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                return true;

            case R.id.login:
                // perform user login credentials
                Intent loginIntent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                return true;

            case R.id.logout:
                // current user logout
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class EndpointsAsyncTask extends AsyncTask<String, Void, User>{

        private Context context;
        private ProgressDialog mDialog;
        private UserApi apiService = null;

        public EndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait Registering User....");
            mDialog.show();
        }

        @Override
        protected User doInBackground(String... params) {
            User response = null;
            try {
                UserApi api = new UserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("http://192.168.1.12:8080/_ah/api")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                                request.setDisableGZipContent(true);
                            }
                        }).build();
                User user = new User();
                user.setUserName(params[0]);
                user.setName(params[1]);
                user.setEmail(params[2]);
                user.setPassword(params[3]);

                response = api.insert(user).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            mDialog.dismiss();
            editUserName.setText("");
            editName.setText("");
            editEmailAddress.setText("");
            editPassword.setText("");
            editConfirmPassword.setText("");

            // didplay success message to user
            Toast.makeText(getApplicationContext(), "User registered successfully",Toast.LENGTH_LONG).show();

        }
    }
}
