package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String userID = (String) object.get("id");
                            String fullName = (String) object.get("name");
                            URL profilePicUrl = new URL("https://graph.facebook.com/" + userID+ "/picture?type=large");
                            Picasso.with(getActivity()).load(profilePicUrl.toString()).fit().into((ImageView) rootView.findViewById(R.id.profilePic));
                            TextView userFullName = (TextView) rootView.findViewById(R.id.userFullName);
                            userFullName.setText(fullName);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

        Button checkInButton = (Button) rootView.findViewById(R.id.check_in_button);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                intent.putExtra("fragment", "checkIn");
                startActivity(intent);
            }
        });
        return rootView;
    }
}
