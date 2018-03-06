package com.uw.fydp.flexeat.flexeat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String selectedFragment= intent.getStringExtra("fragment");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentSelected = null;

        try{
            if(selectedFragment != null && !selectedFragment.equals("")){
                // passed in which fragment to use
                // checkIn = CheckInFragment, main = MainFragment
                if(selectedFragment.equals("checkIn")){
                    fragmentSelected = (Fragment) CheckInFragment.class.newInstance();
                } else if (selectedFragment.equals("main")){
                    fragmentSelected = (Fragment) MainFragment.class.newInstance();
                }
            } else {
                // default to MainFragment
                fragmentSelected = (Fragment) MainFragment.class.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.flContent, fragmentSelected).commit();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String userID = (String) object.get("id");
                            String fullName = (String) object.get("name");
                            String userEmail = (String) object.get("email");
                            URL profilePicUrl = new URL("https://graph.facebook.com/" + userID+ "/picture?type=large");
                            Picasso.with(getApplicationContext()).load(profilePicUrl.toString()).fit().into((ImageView) navigationView.findViewById(R.id.user_image));
                            TextView userFullName = (TextView) navigationView.findViewById(R.id.user_name);
                            TextView email = (TextView)navigationView.findViewById(R.id.user_email);
                            userFullName.setText(fullName);
                            email.setText(userEmail);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
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
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(BaseActivity.this, FbLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        switch (item.getItemId()) {
            case R.id.home_menu:
                Intent intent = new Intent(BaseActivity.this, BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("fragment", "main");
                startActivity(intent);
                break;
            case R.id.edit_profile_menu:
                Intent goToProfileActivity = new Intent(BaseActivity.this, ProfileSetupActivity.class);
                startActivity(goToProfileActivity);
                break;
            case R.id.partnered_restaurants:
                Intent gotoRestaurantsActivity = new Intent(BaseActivity.this, RestaurantsActivity.class);
                startActivity(gotoRestaurantsActivity);
                break;
            case R.id.logout_menu:
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;

        }

        // Close the navigation drawer
        mDrawer.closeDrawers();

        return true;
    }

}
