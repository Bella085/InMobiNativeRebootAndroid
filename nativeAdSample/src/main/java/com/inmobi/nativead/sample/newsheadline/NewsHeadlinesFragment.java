package com.inmobi.nativead.sample.newsheadline;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.Constants;
import com.inmobi.nativead.sample.DataFetcher;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;
import com.inmobi.nativead.sample.newsfeed.NewsFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class NewsHeadlinesFragment extends ListFragment implements NativeProvider {

    public static final String ARGS_PLACE_NATIVE_ADS = "should_place_native_ads";

    private static final String TAG = "NewsHeaderLines==";
    private static final int MAX_ADS = 50;

    @NonNull
    private final Handler mHandler = new Handler();
    private Map<NewsSnippet, WeakReference<InMobiNative>> mNativeAdMap = new HashMap<>();
    private List<NewsSnippet> mItemList = new ArrayList<>();
    private InMobiNative[] mNativeAds = new InMobiNative[MAX_ADS];
    private FeedAdapter mAdapter;
    private static final int[] AD_PLACEMENT_POSITIONS = new int[]{2, 4, 8, 13, 18};

    private OnHeadlineSelectedListener mCallback;
    private ViewGroup viewGroup;


    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewGroup=container;
        //placeNativeAds();
        mAdapter = new FeedAdapter(getActivity(), mItemList, this);
        setListAdapter(mAdapter);
        mCallback = (OnHeadlineSelectedListener) getActivity();
        getHeadlines();

        return super.onCreateView(inflater, container, savedInstanceState);
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
                            //placeNativeAds();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, final long id) {
                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getActivity());
                confirmationDialog.setTitle("Delete Item?");

                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewsSnippet newsSnippet = mItemList.get(position);
                        mItemList.remove(newsSnippet);
                        WeakReference<InMobiNative> nativeAdRef = mNativeAdMap.remove(newsSnippet);
                        if (nativeAdRef != null) {
                            InMobiNative nativeAd = nativeAdRef.get();
                            if (nativeAd != null) {

                            }
                        }
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
                        feedEntry.imageUrl = Constants.FALLBACK_IMAGE_URL;
                    }
                    feedEntry.landingUrl = item.getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    feedEntry.content = item.getString(Constants.FeedJsonKeys.FEED_CONTENT);
                    mItemList.add(feedEntry);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                }
            }

            mAdapter.notifyDataSetChanged();
            Bundle args = getArguments();
            boolean shouldPlaceNativeAds = args.getBoolean(ARGS_PLACE_NATIVE_ADS, true);
            Log.e(TAG, "Begin to load1==");
            if (shouldPlaceNativeAds) {
                Log.e(TAG, "Begin to load2==");
                placeNativeAds();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
    }

    private void placeNativeAds() {
        for (int i = 0; i < AD_PLACEMENT_POSITIONS.length; i++) {
            final int position = AD_PLACEMENT_POSITIONS[i];

            InMobiNative nativeAd = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_HEADERLINE, new NativeAdEventListener() {
                @Override
                public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                    super.onAdLoadSucceeded(inMobiNative);
                    JSONObject content = inMobiNative.getCustomAdContent();

                        Log.e(TAG, "onAdLoadSucceeded===" + content.toString());
                        NewsSnippet item = new NewsSnippet();
                        item.setTitle(inMobiNative.getAdTitle());// content.getString(Constants.AdJsonKeys.AD_TITLE);
                        //item.landingUrl = content.getString(Constants.AdJsonKeys.AD_CLICK_URL);
                        item.setImageUrl(inMobiNative.getAdIconUrl());//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                                //getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                        item.setDescription(inMobiNative.getAdDescription());//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                        //inMobiNative.
                        item.setInMobiNative(new WeakReference<>(inMobiNative));

                    if(position==2||position==4)
                    {
                        item.setBig(true);
                    }else
                    {
                        item.setBig(false);
                    }
                        //item.view =inMobiNative.getPrimaryViewOfWidth(mAdapter.,viewGroup,0);
                        mItemList.add(position,item);
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
                    Toast.makeText(NewsHeadlinesFragment.this.getContext(),"AdImpressed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClicked(InMobiNative inMobiNative) {
                    super.onAdClicked(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                    Toast.makeText(NewsHeadlinesFragment.this.getContext(),"AdClicked",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdStatusChanged(InMobiNative inMobiNative) {
                    super.onAdStatusChanged(inMobiNative);
                    Log.e(TAG, "onAdFullScreenDisplayed ");
                    getListView().getChildAt(position).findViewById(R.id.pb);
                    if (inMobiNative.getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADING) {
                        Log.e(TAG, "onAdStatusChanged " + inMobiNative.getDownloader().getDownloadProgress());
                    }
                    if (inMobiNative.getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADED) {
                        Log.e(TAG, "onAdStatusChanged OPEN");


                    }
                }
            });
            nativeAd.setDownloaderEnabled(true);
            nativeAd.load();
            Map<String,String>map=new HashMap<>();
            //map.put("x-forwarded-for","106.39.48.50");
            nativeAd.setExtras(map);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        // if the item at this position is an ad handle this
        NewsSnippet newsSnippet = mItemList.get(position);
        final WeakReference<InMobiNative> nativeAdRef = mNativeAdMap.get(newsSnippet);
        if (nativeAdRef != null && nativeAdRef.get() != null) {
            //nativeAdRef.get().reportAdClickAndOpenLandingPage(null);

        }
        mCallback.onArticleSelected(position);
        getListView().setItemChecked(position, true);
    }

    @Override
    public WeakReference<InMobiNative> provideInmobiNative(NewsSnippet newsSnippet) {
        return mNativeAdMap == null ? null : mNativeAdMap.get(newsSnippet);
    }
}
