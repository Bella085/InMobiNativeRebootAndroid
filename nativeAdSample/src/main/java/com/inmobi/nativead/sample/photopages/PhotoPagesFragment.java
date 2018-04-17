package com.inmobi.nativead.sample.photopages;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.Constants;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;
import com.inmobi.nativead.sample.newsheadline.NewsHeadlinesFragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoPagesFragment extends Fragment implements NativeProvider {

    private static final String TAG = "PhotoPages====";
    private static final int MAX_ADS = 50;

    private static final int[] SAMPLE_IMAGE_RESOURCE_IDS = {R.drawable.cover1,
            R.drawable.cover2, R.drawable.cover3, R.drawable.cover4,
            R.drawable.cover5, R.drawable.cover6, R.drawable.cover7,
            R.drawable.cover8, R.drawable.cover9, R.drawable.cover10,
            R.drawable.cover11, R.drawable.cover12, R.drawable.cover13,
            R.drawable.cover14};

    private Map<PageItem, WeakReference<InMobiNative>> mNativeAdMap = new HashMap<>();
    private List<PageItem> mItemList = new ArrayList<>();
    private InMobiNative[] mNativeAds = new InMobiNative[MAX_ADS];
    private PagerAdapter mAdapter;

    private static final int[] AD_PLACEMENT_POSITIONS = new int[]{2, 4, 8, 13};


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pages, container, false);
        for (int imageResourceId : SAMPLE_IMAGE_RESOURCE_IDS) {
            PageItem item = new PageItem();
            item.imageUrl = "res://" + getActivity().getPackageName() + "/" + imageResourceId;
            mItemList.add(item);
        }

        CustomViewPager pager = (CustomViewPager) rootView.findViewById(R.id.custom_page_view);
        mAdapter = new CustomPagerAdapter(getActivity(), mItemList, this);
        pager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        placeNativeAds();
        return rootView;
    }


    @Override
    public void onDetach() {
        mNativeAdMap.clear();
        mItemList.clear();
        super.onDetach();
    }

    private void placeNativeAds() {
        for (int i = 0; i < AD_PLACEMENT_POSITIONS.length; i++) {
            final int position = AD_PLACEMENT_POSITIONS[i];
            InMobiNative nativeAd = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_PHOTOPAGES, new InMobiNative.NativeAdListener() {
                @Override
                public void onAdLoadSucceeded(final InMobiNative inMobiNative) {

                        JSONObject content = inMobiNative.getCustomAdContent();
                        Log.e(TAG, "onAdLoadSucceeded" + content.toString());
                        PageItem item = new PageItem();
                        item.imageUrl =inMobiNative.getAdIconUrl();// content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                                //getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                        item.inMobiNative=new WeakReference<>(inMobiNative);
                        mItemList.add(position, item);
                        mNativeAdMap.put(item, new WeakReference<>(inMobiNative));
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Placed ad unit (" + inMobiNative.hashCode() +
                                ") at position " + position);

                }

                @Override
                public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
                    Log.e(TAG, "Failed to load ad. " + inMobiAdRequestStatus.getMessage());
                }

                @Override
                public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                    Log.e(TAG, "onAdFullScreenDismissed. " );
                }

                @Override
                public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {

                }

                @Override
                public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                    Log.e(TAG, "onAdFullScreenDisplayed. " );
                }

                @Override
                public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                    Log.e(TAG, "onUserWillLeaveApplication. " );
                }

                @Override
                public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
                    Log.e(TAG, "onAdImpressed. " );
                    Toast.makeText(PhotoPagesFragment.this.getContext(),"AdImpressed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClicked(@NonNull InMobiNative inMobiNative) {
                    Log.e(TAG, "onAdClicked. " );
                    Toast.makeText(PhotoPagesFragment.this.getContext(),"AdClicked",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {
                    Log.e(TAG, "onMediaPlaybackComplete. " );
                }

                @Override
                public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {

                }

                @Override
                public void onUserSkippedMedia(@NonNull InMobiNative inMobiNative) {

                }


            });
            Map<String,String>map=new HashMap<>();
            nativeAd.setExtras(map);
            nativeAd.load();
            mNativeAds[i] = nativeAd;
        }
    }

    @Override
    public WeakReference<InMobiNative> provideInmobiNative(PageItem pageItem) {
        return mNativeAdMap == null ? null : mNativeAdMap.get(pageItem);
    }
}
