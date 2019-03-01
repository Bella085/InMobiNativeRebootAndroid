package com.inmobi.nativead.sample.newsfeed;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.Constants;
import com.inmobi.nativead.sample.DataFetcher;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;
import com.inmobi.nativead.sample.photofeed.PhotosFeedItem;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inmobi.nativead.sample.Constants.FALLBACK_IMAGE_URL;

public class NewsFragment extends Fragment implements NativeProvider {

    private static final String TAG = "NewsFeed=====";
    private static final int MAX_ADS = 50;

    private Handler mHandler = new Handler();
    private Map<NewsTileItem, WeakReference<InMobiNative>> mNativeAdMap = new HashMap<>();
    private List<NewsTileItem> mItemList = new ArrayList<>();
    private InMobiNative[] mNativeAds = new InMobiNative[MAX_ADS];
    private NewsAdapter mAdapter;
    private DataFetcher mDataFetcher = new DataFetcher();

    private static final int[] AD_PLACEMENT_POSITIONS = new int[]{2, 4, 8, 13, 18};

    private OnTileSelectedListener mCallback;


    public interface OnTileSelectedListener {
        void onTileSelected(int position);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_board, container, false);
        final GridView gridView = (GridView) rootView.findViewById(R.id.news_grid);
        mAdapter = new NewsAdapter(getActivity(), mItemList, this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // if the item at this position is an ad handle this
                NewsTileItem newsTile = mItemList.get(position);
                final WeakReference<InMobiNative> nativeAdRef = mNativeAdMap.get(newsTile);
                if (nativeAdRef != null && nativeAdRef.get() != null) {
                    //nativeAdRef.get().reportAdClickAndOpenLandingPage(null);
                }

                mCallback.onTileSelected(position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    gridView.setItemChecked(position, true);
                }
            }
        });
        mCallback = (OnTileSelectedListener) getActivity();
        getTiles();
        return rootView;
    }

    private void getTiles() {
        mDataFetcher.getFeed(Constants.FEED_URL, new DataFetcher.OnFetchCompletedListener() {
            @Override
            public void onFetchCompleted(final String data, final String message) {
                if (null != data) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadNewsTiles(data);
                        }
                    });
                }
            }
        });
    }

    private void loadNewsTiles(String data) {
        try {
            JSONArray feed = new JSONObject(data)
                    .getJSONArray(Constants.FeedJsonKeys.FEED_LIST);
            int length = feed.length();
            for (int i = 0; i < length; i++) {
                JSONObject item = feed.getJSONObject(i);
                Log.v(TAG, item.toString());
                NewsTileItem feedEntry = new NewsTileItem();
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
                    mItemList.add(feedEntry);
                } catch (JSONException e) {
                    Log.d(TAG, "Error while parsing JSON", e);
                }
            }
            mAdapter.notifyDataSetChanged();
            placeNativeAds();
        } catch (JSONException e) {
            Log.d(TAG, "Error while parsing JSON", e);
        }
    }


    private void placeNativeAds() {
        for (int i = 0; i < AD_PLACEMENT_POSITIONS.length; i++) {
            final int position = AD_PLACEMENT_POSITIONS[i];

            InMobiNative nativeAd = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_PHOTOFEED, new NativeAdEventListener() {
                @Override
                public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                    super.onAdLoadSucceeded(inMobiNative);
                    Log.e(TAG, "=========== owner=="+PlacementId.YOUR_PLACEMENT_ID_NEWSFEED);

                        JSONObject content = inMobiNative.getCustomAdContent();
                        Log.e(TAG, "onAdLoadSucceeded" + content.toString());
                        NewsTileItem item = new NewsTileItem();
                        item.title =inMobiNative.getAdTitle();// content.getString(Constants.AdJsonKeys.AD_TITLE);
                        item.content=inMobiNative.getAdDescription();//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                        //item.landingUrl = content.getString(Constants.AdJsonKeys.AD_CLICK_URL);
                        item.imageUrl = inMobiNative.getAdIconUrl();//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                                //getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                        item.inMobiNative=new WeakReference<>(inMobiNative);
                        Log.e(TAG, item.toString());
                        mItemList.add(position, item);
                        mNativeAdMap.put(item, new WeakReference<>(inMobiNative));
                        mAdapter.notifyDataSetChanged();
                        Log.e(TAG, "Placed ad unit (" + inMobiNative.hashCode() +
                                ") at position " + position);


                }

                @Override
                public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
                    super.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus);
                    Log.e(TAG, "Failed to load ad. " + inMobiAdRequestStatus.getMessage());
                }

                @Override
                public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                    super.onAdFullScreenDisplayed(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                }

                @Override
                public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                    super.onAdFullScreenWillDisplay(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                }

                @Override
                public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                    super.onAdFullScreenDismissed(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                }

                @Override
                public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                    super.onUserWillLeaveApplication(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                }

                @Override
                public void onAdImpressed(InMobiNative inMobiNative) {
                    super.onAdImpressed(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                    Toast.makeText(getActivity(),"AdImpressed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClicked(InMobiNative inMobiNative) {
                    super.onAdClicked(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                    Toast.makeText(getActivity(),"AdClicked",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdStatusChanged(InMobiNative inMobiNative) {
                    super.onAdStatusChanged(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                }
            });
            Map<String,String>map=new HashMap<>();
            nativeAd.setExtras(map);
            nativeAd.load();
            mNativeAds[i] = nativeAd;
        }
    }


    @Override
    public void onDetach() {
        mCallback = null;
        mNativeAdMap.clear();
        mItemList.clear();
        super.onDetach();
    }


    @Override
    public WeakReference<InMobiNative> provideInmobiNative(NewsTileItem newsTileItem) {
        return mNativeAdMap == null ? null : mNativeAdMap.get(newsTileItem);
    }
}
