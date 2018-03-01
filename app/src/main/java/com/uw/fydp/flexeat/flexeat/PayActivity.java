package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Button submitPaymentButton = (Button)findViewById(R.id.submit_payment_button);

        submitPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRating = new Intent(PayActivity.this, RatingActivity.class);
                startActivity(gotoRating);
            }
        });
    }
}
