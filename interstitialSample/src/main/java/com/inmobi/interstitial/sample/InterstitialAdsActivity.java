package com.inmobi.interstitial.sample;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;
import com.inmobi.sdk.InMobiSdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class InterstitialAdsActivity extends AppCompatActivity {

    private InMobiInterstitial mInterstitialAd;
    private Button mLoadAdButton;
    private Button mShowAdButton;
    private Button mShowAdWithAnimation;
    private final String TAG = "InterstitialAdsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InMobiSdk.init(this, "1234567890qwerty0987654321qwerty12345");
        /** 欧盟国家流量必须使用以下初始化方式
         /*JSONObject consentObject = new JSONObject();
         try {
         consentObject.put(IM_GDPR_CONSENT_AVAILABLE,true);
         consentObject.put("gdpr","1");

         } catch (JSONException e) {
         e.printStackTrace();
         }
         InMobiSdk.init(this, "1234567890qwerty0987654321qwerty12345",consentObject); */
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
        setContentView(R.layout.activity_interstitial_ads);
        mLoadAdButton = (Button) findViewById(R.id.button_load_ad);
        mShowAdButton = (Button) findViewById(R.id.button_show_ad);
        mShowAdWithAnimation = (Button) findViewById(R.id.button_show_ad_with_animation);

        setupInterstitial();

        mLoadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd.load();
            }
        });

        mShowAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd.show();
            }
        });
        mShowAdWithAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mShowAdButton != null) {
//            mShowAdButton.setVisibility(View.GONE);
//            mShowAdWithAnimation.setVisibility(View.GONE);
//        }
    }

       private void setupInterstitial() {

           mInterstitialAd = new InMobiInterstitial(this, PlacementId.YOUR_PLACEMENT_ID, new InterstitialAdEventListener() {
               @Override
               public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
                   super.onAdLoadSucceeded(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdLoadSucceeded");
               }

               @Override
               public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
                   super.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus);
                   Log.e(TAG, "interstitial_____onAdLoadFailed"+"..."+inMobiAdRequestStatus);
               }

               @Override
               public void onAdReceived(InMobiInterstitial inMobiInterstitial) {
                   super.onAdReceived(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdReceived");
               }

               @Override
               public void onAdClicked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                   super.onAdClicked(inMobiInterstitial, map);
                   Log.e(TAG, "interstitial_____onAdClicked");
               }

               @Override
               public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {
                   super.onAdWillDisplay(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdWillDisplay");
               }

               @Override
               public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {
                   super.onAdDisplayed(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdDisplayed");
               }

               @Override
               public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {
                   super.onAdDisplayFailed(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdDisplayFailed");
               }

               @Override
               public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {
                   super.onAdDismissed(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onAdDismissed");
               }

               @Override
               public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {
                   super.onUserLeftApplication(inMobiInterstitial);
                   Log.e(TAG, "interstitial_____onUserLeftApplication");
               }

               @Override
               public void onRewardsUnlocked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                   super.onRewardsUnlocked(inMobiInterstitial, map);
                   Log.e(TAG, "interstitial_____onRewardsUnlocked");
               }

               @Override
               public void onRequestPayloadCreated(byte[] bytes) {
                   super.onRequestPayloadCreated(bytes);
                   Log.e(TAG, "interstitial_____onRequestPayloadCreated");
               }

               @Override
               public void onRequestPayloadCreationFailed(InMobiAdRequestStatus inMobiAdRequestStatus) {
                   super.onRequestPayloadCreationFailed(inMobiAdRequestStatus);
                   Log.e(TAG, "interstitial_____onRequestPayloadCreationFailed");
               }

           });

    }
}
