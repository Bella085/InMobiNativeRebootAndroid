package com.inmobi.banner.sample;

import com.inmobi.ads.InMobiBanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BannerXmlActivity extends AppCompatActivity {

    private InMobiBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_xml);
        mBanner = (InMobiBanner) findViewById(R.id.banner);
        mBanner.load();
        mBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG==","TAG+++++");
                Toast.makeText(BannerXmlActivity.this,"Click",Toast.LENGTH_LONG).show();
            }
        });
    }
}
