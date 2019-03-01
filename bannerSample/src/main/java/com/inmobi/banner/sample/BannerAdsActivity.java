package com.inmobi.banner.sample;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.BannerAdEventListener;
import com.inmobi.sdk.InMobiSdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.inmobi.banner.sample.Constants.FALLBACK_IMAGE_URL;

public class BannerAdsActivity extends AppCompatActivity {

    private static final String TAG = BannerAdsActivity.class.getSimpleName();

    private InMobiBanner mBannerAd;
    private Button btnXmlIntegration;
    private ListView mNewsListView;


    @NonNull
    private final Handler mHandler = new Handler();
    private List<NewsSnippet> mItemList = new ArrayList<>();
    private NewsFeedAdapter mAdapter;

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
    }

    private OnHeadlineSelectedListener mCallback = new OnHeadlineSelectedListener() {
        @Override
        public void onArticleSelected(int position) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

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
        setContentView(R.layout.activity_banner_ads);
        btnXmlIntegration = (Button) findViewById(R.id.btnXmlIntegration);
        btnXmlIntegration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BannerAdsActivity.this, BannerXmlActivity.class));
            }
        });
        setupListView();
        //getHeadlines();
        setupBannerAd();
    }

    private void setupBannerAd() {
        mBannerAd = new InMobiBanner(BannerAdsActivity.this, PlacementId.YOUR_PLACEMENT_ID);
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.ad_container);
        mBannerAd.setAnimationType(InMobiBanner.AnimationType.ROTATE_HORIZONTAL_AXIS);
        mBannerAd.setListener(new BannerAdEventListener() {
            @Override
            public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
                super.onAdLoadSucceeded(inMobiBanner);
                Log.e("InmobiBanner","banner_______onAdLoadSucceeded");
            }

            @Override
            public void onAdLoadFailed(InMobiBanner inMobiBanner, InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiBanner, inMobiAdRequestStatus);
                Log.e("InmobiBanner","banner_______onAdLoadFailed");
            }

            @Override
            public void onAdClicked(InMobiBanner inMobiBanner, Map<Object, Object> map) {
                super.onAdClicked(inMobiBanner, map);
                Log.e("InmobiBanner","banner_______onAdClicked");
            }

            @Override
            public void onAdDisplayed(InMobiBanner inMobiBanner) {
                super.onAdDisplayed(inMobiBanner);
                Log.e("InmobiBanner","banner_______onAdDisplayed");
            }

            @Override
            public void onAdDismissed(InMobiBanner inMobiBanner) {
                super.onAdDismissed(inMobiBanner);
                Log.e("InmobiBanner","banner_______onAdDismissed");
            }

            @Override
            public void onUserLeftApplication(InMobiBanner inMobiBanner) {
                super.onUserLeftApplication(inMobiBanner);
                Log.e("InmobiBanner","banner_______onUserLeftApplication");
            }

            @Override
            public void onRewardsUnlocked(InMobiBanner inMobiBanner, Map<Object, Object> map) {
                super.onRewardsUnlocked(inMobiBanner, map);
                Log.e("InmobiBanner","banner_______onRewardsUnlocked");
            }

            @Override
            public void onRequestPayloadCreated(byte[] bytes) {
                super.onRequestPayloadCreated(bytes);
                Log.e("InmobiBanner","banner_______onRequestPayloadCreated");
            }

            @Override
            public void onRequestPayloadCreationFailed(InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onRequestPayloadCreationFailed(inMobiAdRequestStatus);
                Log.e("InmobiBanner","banner_______onRequestPayloadCreationFailed");
            }

        });
        setBannerLayoutParams();
        adContainer.addView(mBannerAd);
        mBannerAd.load();

        adContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG==","TAG+++++");
                Toast.makeText(BannerAdsActivity.this,"Click",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBannerLayoutParams() {
        int width = toPixelUnits(320);
        int height = toPixelUnits(50);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(width, height);
        bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mBannerAd.setLayoutParams(bannerLayoutParams);
    }


    private int toPixelUnits(int dipUnit) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dipUnit * density);
    }

    private void setupListView() {
        mNewsListView = (ListView) findViewById(R.id.lvNewsContainer);
        mAdapter = new NewsFeedAdapter(this, mItemList);
        mNewsListView.setAdapter(mAdapter);
        mNewsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, final long id) {
                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(BannerAdsActivity.this);
                confirmationDialog.setTitle("Delete Item?");
                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewsSnippet newsSnippet = mItemList.get(position);
                        mItemList.remove(newsSnippet);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                confirmationDialog.show();
                return true;
            }
        });

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCallback.onArticleSelected(position);
                mNewsListView.setItemChecked(position, true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getHeadlines() {
        new DataFetcher().getFeed(Constants.FEED_URL, new DataFetcher.OnFetchCompletedListener() {
            @Override
            public void onFetchCompleted(@Nullable final String data, @Nullable final String message) {
                if (null != data) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadHeadlines(data);
                        }
                    });
                }
            }
        });
    }


    private void loadHeadlines(String data) {
        try {
            JSONArray feed = new JSONObject(data).
                    getJSONArray(Constants.FeedJsonKeys.FEED_LIST);
            for (int i = 0; i < feed.length(); i++) {
                JSONObject item = feed.getJSONObject(i);
                Log.v(TAG, item.toString());
                NewsSnippet feedEntry = new NewsSnippet();
                try {
                    feedEntry.title = item.getString(Constants.FeedJsonKeys.CONTENT_TITLE);
                    JSONObject enclosureObject = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE);
                    if (!enclosureObject.isNull(Constants.FeedJsonKeys.CONTENT_LINK)) {
                        feedEntry.imageUrl = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE).
                                getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    } else {
                        feedEntry.imageUrl = FALLBACK_IMAGE_URL;
                    }
                    feedEntry.landingUrl = item.getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    feedEntry.content = item.getString(Constants.FeedJsonKeys.FEED_CONTENT);
                    feedEntry.isSponsored = false;
                    mItemList.add(feedEntry);
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "JSONException for loadHeadlines", e);
        }
    }
}
