package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CheckInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    EditText restaurantCode;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        mPrefs = this.getSharedPreferences("com.uw.fydp.flexeat.flexeat", MODE_PRIVATE);
        restaurantCode = (EditText) findViewById(R.id.check_in_code_field);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name, email and image in menu
        TextView navUserName = navigationView.getHeaderView(0).findViewById(R.id.userNameText);
        navUserName.setText(mPrefs.getString("userName", ""));
        TextView navUserEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmailText);
        navUserEmail.setText(mPrefs.getString("userEmail", ""));
        String profilePicUrl = mPrefs.getString("userImagePath", "");
        Picasso.with(getApplicationContext()).load(profilePicUrl).fit().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.profilePic));

    }

    public void onSubmitClick(View view) {
        if(restaurantCode.getText().toString().equals("1234")){
            Intent goToMenuScreen = new Intent(CheckInActivity.this, MenuActivity.class);
            startActivity(goToMenuScreen);
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong code", Toast.LENGTH_LONG).show();
        }
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
            Intent goToProfileEdit = new Intent(CheckInActivity.this, ProfileSetupActivity.class);
            startActivity(goToProfileEdit);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
