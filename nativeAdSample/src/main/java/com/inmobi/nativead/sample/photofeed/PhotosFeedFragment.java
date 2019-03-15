package com.inmobi.nativead.sample.photofeed;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.Constants;
import com.inmobi.nativead.sample.DataFetcher;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;
import com.inmobi.nativead.sample.newsheadline.NewsHeadlinesFragment;
import com.inmobi.nativead.sample.newsheadline.NewsSnippet;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotosFeedFragment extends ListFragment implements NativeProvider {

    private static final String TAG = "PhotoFeed=====";
    private static final int MAX_ADS = 50;

    @NonNull
    private final Handler mHandler = new Handler();
    private Map<PhotosFeedItem, WeakReference<InMobiNative>> mNativeAdMap = new HashMap<>();
    private List<PhotosFeedItem> mItemList = new ArrayList<>();
    private InMobiNative[] mNativeAds = new InMobiNative[MAX_ADS];
    private PhotosAdapter mAdapter;
    private DataFetcher mDataFetcher = new DataFetcher();
    private static final int[] AD_PLACEMENT_POSITIONS = new int[]{2, 4, 8, 13, 18};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new PhotosAdapter(getActivity(), mItemList, this);
        setListAdapter(mAdapter);
        getPhotos();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getPhotos() {
        mDataFetcher.getFeed(Constants.FEED_URL, new DataFetcher.OnFetchCompletedListener() {
            @Override
            public void onFetchCompleted(final String data, final String message) {
                if (null != data) {
                    Log.e(TAG, "loading photos Not null====");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadPhotos(data);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // if the item at this position is an ad handle this
        // if the item at this position is an ad handle this
        PhotosFeedItem photoItem = mItemList.get(position);
        final WeakReference<InMobiNative> nativeAdRef = mNativeAdMap.get(photoItem);
        if (nativeAdRef != null && nativeAdRef.get() != null) {
            //nativeAdRef.get().reportAdClickAndOpenLandingPage(null);
        }
        getListView().setItemChecked(position, true);
    }

    private void loadPhotos(String data) {
        try {
            Log.d(TAG, "loading photos");
            JSONArray feed = new JSONObject(data).
                    getJSONArray(Constants.FeedJsonKeys.FEED_LIST);
            for (int i = 0; i < feed.length(); i++) {
                JSONObject item = feed.getJSONObject(i);
                Log.d(TAG, item.toString());
                PhotosFeedItem feedEntry = new PhotosFeedItem();
                try {
                    feedEntry.title = item.getString(Constants.FeedJsonKeys.CONTENT_TITLE);
                    JSONObject enclosureObject = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE);
                    if (!enclosureObject.isNull(Constants.FeedJsonKeys.CONTENT_LINK)) {
                        feedEntry.imageUrl = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE).
                                getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    } else {
                        feedEntry.imageUrl = Constants.FALLBACK_IMAGE_URL;
                    }
                    feedEntry.landingUrl = item.getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    mItemList.add(feedEntry);
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                }
            }
            mAdapter.notifyDataSetChanged();
            placeNativeAds();
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
    }

    private void placeNativeAds() {
        Log.e(TAG, "loading photos Not null====placeNativeAds");

        for (int i = 0; i < AD_PLACEMENT_POSITIONS.length; i++) {
            final int position = AD_PLACEMENT_POSITIONS[i];

            InMobiNative nativeAd = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_PHOTOFEED, new NativeAdEventListener() {
                @Override
                public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                    super.onAdLoadSucceeded(inMobiNative);
                    JSONObject content = inMobiNative.getCustomAdContent();
                        Log.e(TAG, "PhotosFeedFragment onAdLoadSucceeded" + content.toString());
                        PhotosFeedItem item = new PhotosFeedItem();
                        item.title = inMobiNative.getAdTitle();//content.getString(Constants.AdJsonKeys.AD_TITLE);
                        item.description=inMobiNative.getAdDescription();//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                        //item.landingUrl = content.getString(Constants.AdJsonKeys.AD_CLICK_URL);
                        item.imageUrl = inMobiNative.getAdIconUrl();//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                                //getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                        item.inMobiNative=new WeakReference<>(inMobiNative);
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
            nativeAd.load();
            mNativeAds[i] = nativeAd;
        }
    }


    @Override
    public void onDetach() {
        mNativeAdMap.clear();
        mItemList.clear();
        super.onDetach();
    }

    @Override
    public WeakReference<InMobiNative> provideInmobiNative(PhotosFeedItem photosFeedItem) {
        return mNativeAdMap == null ? null : mNativeAdMap.get(photosFeedItem);
    }
}
