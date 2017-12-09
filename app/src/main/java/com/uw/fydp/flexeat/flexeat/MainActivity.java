package com.uw.fydp.flexeat.flexeat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = this.getSharedPreferences("com.uw.fydp.flexeat.flexeat", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String userID = (String) object.get("id");
                            String fullName = (String) object.get("name");
                            String emailAddress = (String) object.get("email");
                            String profilePicUrlString = "https://graph.facebook.com/" + userID+ "/picture?type=large";
                            URL profilePicUrl = new URL(profilePicUrlString);
                            Picasso.with(getApplicationContext()).load(profilePicUrl.toString()).fit().into((ImageView) findViewById(R.id.profilePic));
                            TextView userFullName = (TextView) findViewById(R.id.userFullName);
                            userFullName.setText(fullName);
                            // store user name and image path into shared preferences so can load in navigation menu
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("userName", fullName);
                            editor.putString("userEmail", emailAddress);
                            editor.putString("userImagePath", profilePicUrlString);
                            editor.apply();

                            // set name and image for this activity's navigation menu
                            TextView navBarName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userNameText);
                            navBarName.setText(fullName);
                            TextView navBarEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmailText);
                            navBarEmail.setText(emailAddress);
                            Picasso.with(getApplicationContext()).load(profilePicUrl.toString()).fit().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.profilePic));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

        Button checkInButton = (Button) findViewById(R.id.check_in_button);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            Intent goToProfileEdit = new Intent(MainActivity.this, ProfileSetupActivity.class);
            startActivity(goToProfileEdit);
        } else if (id == R.id.nav_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            LoginManager.getInstance().logOut();
                            Intent intent = new Intent(MainActivity.this, FbLoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
