package com.inmobi.nativead.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.newsheadline.NewsSnippet;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PreRollActivity extends AppCompatActivity {

    private static final String TAG="PreRollActivity==";


    private LinearLayout mContentView;




    InMobiNative nativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_roll);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (LinearLayout)findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        /*mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/
        nativeAd=new InMobiNative(this, PlacementId.INMOBI_SPLASH_PLACEMENT_STATIC, new NativeAdEventListener() {
            @Override
            public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                super.onAdLoadSucceeded(inMobiNative);
                  JSONObject content = inMobiNative.getCustomAdContent();
                Log.e(TAG, "onAdLoadSucceeded===" + content.toString());
                NewsSnippet item = new NewsSnippet();
                item.title = inMobiNative.getAdTitle();//content.getString(Constants.AdJsonKeys.AD_TITLE);
                //item.landingUrl = content.getString(Constants.AdJsonKeys.AD_CLICK_URL);
                item.imageUrl = inMobiNative.getAdIconUrl();//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                // getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                item.description=inMobiNative.getAdDescription();//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                item.inMobiNative=new WeakReference<>(inMobiNative);
                //item.view =inMobiNative.getPrimaryViewOfWidth(mAdapter.,viewGroup,0);
                mContentView.removeAllViews();
                mContentView.addView(inMobiNative.getPrimaryViewOfWidth(PreRollActivity.this,mContentView,mContentView,0));

                mContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeAd.reportAdClickAndOpenLandingPage();
                    }
                });

            }

            @Override
            public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus);
                Log.e(TAG, "onAdLoadFailed ");
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                super.onAdFullScreenDismissed(inMobiNative);
                Log.e(TAG, "onAdFullScreenDismissed ");
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                Log.e(TAG, "onAdFullScreenDisplayed ");

            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                super.onAdFullScreenWillDisplay(inMobiNative);
                Log.e(TAG, "onAdFullScreenWillDisplay ");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                super.onUserWillLeaveApplication(inMobiNative);
                Log.e(TAG, "onUserWillLeaveApplication ");
            }

            @Override
            public void onAdImpressed(InMobiNative inMobiNative) {
                super.onAdImpressed(inMobiNative);
                Log.e(TAG, "onAdImpressed ");
                Toast.makeText(PreRollActivity.this,"onAdImpressed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked(InMobiNative inMobiNative) {
                super.onAdClicked(inMobiNative);
                Log.e(TAG, "onAdClicked ");
                Toast.makeText(PreRollActivity.this,"AdClicked",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdStatusChanged(InMobiNative inMobiNative) {
                super.onAdStatusChanged(inMobiNative);
                Log.e(TAG, "onAdStatusChanged ");
            }

            @Override
            public void onRequestPayloadCreated(byte[] bytes) {
                super.onRequestPayloadCreated(bytes);
                Log.e(TAG, "onRequestPayloadCreated ");
            }

            @Override
            public void onRequestPayloadCreationFailed(InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onRequestPayloadCreationFailed(inMobiAdRequestStatus);
                Log.e(TAG, "onRequestPayloadCreationFailed ");
            }


        });


        Map<String,String> map=new HashMap<>();
        nativeAd.setExtras(map);
        nativeAd.load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nativeAd.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nativeAd.resume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        nativeAd.destroy();
        nativeAd=null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        String message=newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";

        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mWidth = dm.widthPixels;
        int mHeight = dm.heightPixels;

        Toast.makeText(this, message+"=mWidth="+mWidth+"=mHeight="+mHeight, Toast.LENGTH_LONG).show();

        /*ViewGroup.LayoutParams params = mContentView.getLayoutParams();
        params.height = mHeight;
        params.width = mWidth;
        mContentView.setLayoutParams(params);*/
        //mContentView.updateViewLayout();
        //mContentView.refreshDrawableState();

        View view=nativeAd.getPrimaryViewOfWidth(PreRollActivity.this,mContentView,mContentView,0);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = mHeight;
        params.width = mWidth;
        view.setLayoutParams(params);
        mContentView.removeAllViews();
        //mContentView.updateViewLayout(view,params);
        mContentView.addView(view);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PreRollActivity.this,NativeAdsActivity.class);
        PreRollActivity.this.startActivity(intent);
        PreRollActivity.this.finish();
    }
}
