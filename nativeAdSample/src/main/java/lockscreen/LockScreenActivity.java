package lockscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;


public class LockScreenActivity extends AppCompatActivity {

    private LinearLayout container;
    private InMobiNative nativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lockscreen_activity_main);
        UnlockBar unlock = (UnlockBar) findViewById(R.id.unlock);
        container=(LinearLayout) findViewById(R.id.lineracontainer);

        // Attach listener
        unlock.setOnUnlockListenerRight(new UnlockBar.OnUnlockListener() {
            @Override
            public void onUnlock()
            {
                Toast.makeText(LockScreenActivity.this, "Right Action", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        unlock.setOnUnlockListenerLeft(new UnlockBar.OnUnlockListener() {
            @Override
            public void onUnlock()
            {
                Toast.makeText(LockScreenActivity.this, "Left Action", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

//        nativeAd=new InMobiNative(this, PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW, new InMobiNative.NativeAdListener() {
//            @Override
//            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdLoadSucceeded");
//                container.removeAllViews();
//                container.addView(inMobiNative.getPrimaryViewOfWidth(LockScreenActivity.this,container,container,container.getWidth()));
//            }
//
//            @Override
//            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
//                Log.e("LockScreenActivity","onAdLoadFailed");
//            }
//
//            @Override
//            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdFullScreenDismissed");
//            }
//
//            @Override
//            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdFullScreenWillDisplay");
//            }
//
//            @Override
//            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdFullScreenDisplayed");
//            }
//
//            @Override
//            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onUserWillLeaveApplication");
//            }
//
//            @Override
//            public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdImpressed");
//            }
//
//            @Override
//            public void onAdClicked(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdClicked");
//            }
//
//            @Override
//            public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onMediaPlaybackComplete");
//            }
//
//            @Override
//            public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onAdStatusChanged");
//            }
//
//            @Override
//            public void onUserSkippedMedia(@NonNull InMobiNative inMobiNative) {
//                Log.e("LockScreenActivity","onUserSkippedMedia");
//            }
//        });

        nativeAd=new InMobiNative(this, PlacementId.INMOBI_SPLASH_PLACEMENT_STATIC, new NativeAdEventListener() {
            @Override
            public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                super.onAdLoadSucceeded(inMobiNative);
                Log.e("LockScreenActivity","onAdLoadSucceeded");
                container.removeAllViews();
                container.addView(inMobiNative.getPrimaryViewOfWidth(LockScreenActivity.this,container,container,container.getWidth()));
            }

            @Override
            public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus);
                Log.e("LockScreenActivity","onAdLoadFailed");
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                super.onAdFullScreenDismissed(inMobiNative);
                Log.e("LockScreenActivity","onAdFullScreenDismissed");
            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                super.onAdFullScreenWillDisplay(inMobiNative);
                Log.e("LockScreenActivity","onAdFullScreenWillDisplay");
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                super.onAdFullScreenDisplayed(inMobiNative);
                Log.e("LockScreenActivity","onAdFullScreenDisplayed");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                super.onUserWillLeaveApplication(inMobiNative);
                Log.e("LockScreenActivity","onUserWillLeaveApplication");
            }

            @Override
            public void onAdImpressed(InMobiNative inMobiNative) {
                super.onAdImpressed(inMobiNative);
                Log.e("LockScreenActivity","onAdImpressed");
            }

            @Override
            public void onAdClicked(InMobiNative inMobiNative) {
                super.onAdClicked(inMobiNative);
                Log.e("LockScreenActivity","onAdClicked");
            }

            @Override
            public void onAdStatusChanged(InMobiNative inMobiNative) {
                super.onAdStatusChanged(inMobiNative);
                Log.e("LockScreenActivity","onAdStatusChanged");
            }

            @Override
            public void onRequestPayloadCreated(byte[] bytes) {
                super.onRequestPayloadCreated(bytes);
                Log.e("LockScreenActivity","onRequestPayloadCreated");
            }

            @Override
            public void onRequestPayloadCreationFailed(InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onRequestPayloadCreationFailed(inMobiAdRequestStatus);
                Log.e("LockScreenActivity","onRequestPayloadCreationFailed");
            }
        });
        nativeAd.showOnLockScreen(new InMobiNative.LockScreenListener() {
            @Override
            public void onActionRequired(InMobiNative inMobiNative) {
                Log.e("LockScreenActivity","onActionRequired");
                finish();
                //必须的，为了打开落地页
                /**
                 * clicking to any ad will require to
                 * open browser or play store both can not be opened on lockscreen
                 * so we first need to dismiss the lock screen and open home screen
                 * where if we call  take action it will open landing page.
                 */
                nativeAd.takeAction();
            }
        });

        nativeAd.load();

    }

    @Override
    public void onAttachedToWindow() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onAttachedToWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LockApplication) getApplication()).lockScreenShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LockApplication) getApplication()).lockScreenShow = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
