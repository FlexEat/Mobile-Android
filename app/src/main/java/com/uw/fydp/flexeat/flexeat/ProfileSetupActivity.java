package com.uw.fydp.flexeat.flexeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class ProfileSetupActivity extends AppCompatActivity {

    String[] foodRestrictions = {
            "Gluten",
            "Peanuts",
            "Fish",
            "Soy",
            "Halal",
            "Vegan",
            "Vegetarian",
            "Other"
    };

    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        FoodRestrictionsGridAdapter adapter = new FoodRestrictionsGridAdapter(ProfileSetupActivity.this, foodRestrictions);
        grid = (GridView)findViewById(R.id.grid_of_food_restrictions);
        grid.setAdapter(adapter);
    }
}
