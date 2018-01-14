package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uw.fydp.flexeat.flexeat.adapters.FoodRestrictionsGridAdapter;
import com.uw.fydp.flexeat.flexeat.model.FoodRestrictionItem;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfileSetupActivity extends AppCompatActivity {

    ArrayList<FoodRestrictionItem> listOfFoodRestrictions = new ArrayList<>();
    ArrayList<FoodRestrictionItem> listOfSelectedRestrictions  = new ArrayList<>();
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
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mPrefs = this.getSharedPreferences("com.uw.fydp.flexeat.flexeat", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userFoodRestrictions", "");
        Type foodRestrictionItemType = new TypeToken<ArrayList<FoodRestrictionItem>>(){}.getType();

        if (gson.fromJson(json, foodRestrictionItemType) != null)
            listOfSelectedRestrictions = gson.fromJson(json, foodRestrictionItemType);

        for(int i = 0 ; i < foodRestrictions.length ; i++){
            boolean hasCurrentFoodRestriction = false;
            for(int j = 0; j < listOfSelectedRestrictions.size(); j++){
                if(listOfSelectedRestrictions.get(j).name.equals(foodRestrictions[i])) {
                    hasCurrentFoodRestriction = true;
                    break;
                }
            }
            listOfFoodRestrictions.add(new FoodRestrictionItem(foodRestrictions[i], hasCurrentFoodRestriction));
        }
        listOfSelectedRestrictions.clear(); // don't want any old selected values staying when saving again
        final FoodRestrictionsGridAdapter adapter = new FoodRestrictionsGridAdapter(ProfileSetupActivity.this, listOfFoodRestrictions);
        grid = (GridView)findViewById(R.id.grid_of_food_restrictions);
        grid.setAdapter(adapter);
    }

    public void onSaveFoodRestrictions(View view){
        listOfSelectedRestrictions.clear();

        for(int i = 0; i < listOfFoodRestrictions.size(); i++){
            if(listOfFoodRestrictions.get(i).isCheck){
                listOfSelectedRestrictions.add(listOfFoodRestrictions.get(i));
            }
        }

        // save to shared prefs
        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        String jsonOfSelectedFoodRestrictions = gson.toJson(listOfSelectedRestrictions);
        editor.putString("userFoodRestrictions", jsonOfSelectedFoodRestrictions);
        editor.apply();

        Intent goToMainScreen = new Intent(ProfileSetupActivity.this, BaseActivity.class);
        goToMainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToMainScreen);
        finish();
    }
}
