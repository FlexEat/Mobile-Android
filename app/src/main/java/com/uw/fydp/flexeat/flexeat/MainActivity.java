package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String userID = (String) object.get("id");
                            String fullName = (String) object.get("name");
                            URL profilePicUrl = new URL("https://graph.facebook.com/" + userID+ "/picture?type=large");
                            Picasso.with(getApplicationContext()).load(profilePicUrl.toString()).fit().into((ImageView) findViewById(R.id.profilePic));
                            TextView userFullName = (TextView) findViewById(R.id.userFullName);
                            userFullName.setText(fullName);

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
}
