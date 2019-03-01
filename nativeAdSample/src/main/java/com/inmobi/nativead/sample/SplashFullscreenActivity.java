package com.inmobi.nativead.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.newsheadline.NewsSnippet;
import com.inmobi.sdk.InMobiSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import lockscreen.utils.LockScreen;

import static com.inmobi.sdk.InMobiSdk.IM_GDPR_CONSENT_AVAILABLE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashFullscreenActivity extends AppCompatActivity implements View.OnClickListener  {


    private static final String TAG="SplashActivity==";
    private long lastTime;

    private CountDownView count_down_view,preroll_btn;


    private LinearLayout FrameLayoutView;
    private boolean isIn = false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count_down_view.start();
            count_down_view.bringToFront();
        }
    };



    InMobiNative nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar myactionbar  = getSupportActionBar();
        myactionbar.hide();

        Log.e(TAG, "==onCreate");

        InMobiSdk.init(this, "35cd4640484c490d8d7b59484fa52952");

        /** 欧盟国家流量必须使用以下初始化方式
        /*JSONObject consentObject = new JSONObject();
        try {
            consentObject.put(IM_GDPR_CONSENT_AVAILABLE,true);
            consentObject.put("gdpr","1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(this, "35cd4640484c490d8d7b59484fa52952",consentObject); */
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

        setContentView(R.layout.activity_splash_fullscreen);


        FrameLayoutView=(LinearLayout) findViewById(R.id.fullscreen_view);
        count_down_view = (CountDownView) findViewById(R.id.countDownView);
        preroll_btn=(CountDownView)findViewById(R.id.PreRollBtn);
        count_down_view.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {
                Toast.makeText(getApplicationContext(),"开始计时",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinishCount() {
                Toast.makeText(getApplicationContext(),"计时结束",Toast.LENGTH_SHORT).show();
                if(!isIn) {
                    isIn = true;
                    Intent intent = new Intent(SplashFullscreenActivity.this, NativeAdsActivity.class);
                    SplashFullscreenActivity.this.startActivity(intent);
                    SplashFullscreenActivity.this.finish();
                }
            }
        });
        count_down_view.setOnClickListener(this);
        preroll_btn.setOnClickListener(this);
        preroll_btn.bringToFront();

        nativeAd=new InMobiNative(SplashFullscreenActivity.this,PlacementId.INMOBI_SPLASH_PLACEMENT_STATIC, new NativeAdEventListener(){
            @Override
            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {


                    JSONObject content = inMobiNative.getCustomAdContent();
                    //Log.e("da",nativeAd.getCustomAdContent() + "") ;
                    Log.e(TAG, "onAdLoadSucceeded===" + content.toString());
                    NewsSnippet item = new NewsSnippet();
                    item.title = inMobiNative.getAdTitle();//content.getString(Constants.AdJsonKeys.AD_TITLE);
                    item.imageUrl = inMobiNative.getAdIconUrl();//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                    item.description=inMobiNative.getAdDescription();//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                try {
                    item.isVideo = content.getBoolean("isVideo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.inMobiNative=new WeakReference<>(inMobiNative);
                    FrameLayoutView.removeAllViews();

                /**
                 * 为了适应9：18的全面屏设备 开屏有3种方式：
                 *   1. 开屏的container设置垂直居中
                 *   2. getPrimaryViewOfWidth中的最后一个参数width值可以传：（素材的宽度*设备的高度/素材的高度）
                 *   3. 调整自己的logo高度 【如果开屏展示自家的logo】
                 */
                  FrameLayoutView.addView(inMobiNative.getPrimaryViewOfWidth(SplashFullscreenActivity.this,FrameLayoutView,FrameLayoutView,FrameLayoutView.getWidth()));


            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.e(TAG, "Failed to load ad. " + inMobiAdRequestStatus.getMessage());
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                Log.e(TAG, "onAdFullScreenDismissed ");
            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                Log.e(TAG, "onAdFullScreenDisplayed ");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                Log.e(TAG, "onUserWillLeaveApplication ");
            }

            @Override
            public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
                Log.e(TAG, "onAdImpressed ");
                Toast.makeText(SplashFullscreenActivity.this,"AdImpressed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked(@NonNull InMobiNative inMobiNative) {
                Log.e(TAG, "onAdClicked ");
                Toast.makeText(SplashFullscreenActivity.this,"AdClicked",Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
                if (inMobiNative.getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADING) {
                    Log.e(TAG, "onAdStatusChanged "+inMobiNative.getDownloader().getDownloadProgress());
                }
                if (inMobiNative.getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADED) {
                    Log.e(TAG, "onAdStatusChanged OPEN");
                }
            }
        });

        nativeAd.setDownloaderEnabled(true);
        nativeAd.load();

        Log.e(TAG, "==onCreate load==");
        handler.sendEmptyMessageDelayed(0,1000);
//锁屏
//        LockScreen.getInstance().init(this,true);
        //激活LockScreen
//        if(!LockScreen.getInstance().isActive())
//        {
//            LockScreen.getInstance().active();
//        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nativeAd.destroy();
        nativeAd=null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownView:
                if(!isIn) {
                    isIn = true;
                    Intent intent = new Intent(SplashFullscreenActivity.this, NativeAdsActivity.class);
                    SplashFullscreenActivity.this.startActivity(intent);
                    SplashFullscreenActivity.this.finish();
                }
                break;
            case R.id.PreRollBtn:
                Intent intent1 = new Intent(SplashFullscreenActivity.this,PreRollActivity.class);
                SplashFullscreenActivity.this.startActivity(intent1);
                SplashFullscreenActivity.this.finish();
                break;
        }

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

    //连按两次退出应用程序
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "请再按一次", Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }
}
