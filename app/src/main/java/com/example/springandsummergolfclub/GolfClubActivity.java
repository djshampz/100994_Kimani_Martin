package com.example.springandsummergolfclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.springandsummergolfclub.databinding.ActivityMainBinding;

public class GolfClubActivity extends AppCompatActivity {


    TextView mHeader, mDetailheader, mDetails;
    ImageView mImageDetails;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.golfclub_details);

        mHeader = findViewById(R.id.details_header);
        mImageDetails = findViewById(R.id.image_details);
        mDetailheader = findViewById(R.id.actualsDetail_header);
        mDetails = findViewById(R.id.actualDetails);

        Intent intent = this.getIntent();
        if (intent != null) {


            String name = intent.getStringExtra("name");
            int imageId = intent.getIntExtra("imageid", R.drawable.download);
            String descreptions = intent.getStringExtra("descriptions");
            String inDetail= intent.getStringExtra("inDetail");


            mHeader.setText(name);
            mImageDetails.setImageResource(imageId);
            mDetailheader.setText(descreptions);
            mDetails.setText(inDetail);
        }

    }
}
