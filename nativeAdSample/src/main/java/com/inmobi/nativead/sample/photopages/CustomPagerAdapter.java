package com.inmobi.nativead.sample.photopages;

import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.R;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PageItem> mItems;
    private NativeProvider mNativeProvider;

    public CustomPagerAdapter(Context context, List<PageItem> items, NativeProvider nativeProvider) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mNativeProvider = nativeProvider;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        CustomViewPagerItemWrapper coverFlowItem = new CustomViewPagerItemWrapper(mContext);
        View wrappedView = getCoverFlowItem(container, position);
        if (null == wrappedView) {
            throw new NullPointerException("getCoverFlowItem() was expected to return a view, but null was returned.");
        }

        coverFlowItem.addView(wrappedView);
        coverFlowItem.setLayoutParams(wrappedView.getLayoutParams());

        container.addView(coverFlowItem);
        return coverFlowItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //InMobiNative.unbind(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private View getCoverFlowItem(ViewGroup container, int position) {
        View pageView = mInflater.inflate(R.layout.page_item_view, container, false);
        ImageView imageView = (ImageView) pageView.findViewById(R.id.photo);
        TextView tag = (TextView) pageView.findViewById(R.id.sponsored);
        LinearLayout lineaeview=(LinearLayout) pageView.findViewById(R.id.photoview);
        PageItem item = mItems.get(position);
        imageView.setVisibility(View.VISIBLE);
        lineaeview.setVisibility(View.GONE);
        imageView.setImageURI(Uri.parse(item.imageUrl));

        WeakReference<InMobiNative> nativeAdRef = item.inMobiNative;
        if (null == nativeAdRef) {
            tag.setVisibility(View.GONE);
            lineaeview.setVisibility(View.GONE);
            //InMobiNative.unbind(container);
        } else {
            // we have an ad at this position
            InMobiNative nativeAd = nativeAdRef.get();
            if (nativeAd != null) {
                lineaeview.setVisibility(View.VISIBLE);
                tag.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                tag.setText("Sponsored");
                lineaeview.removeAllViews();
                lineaeview.addView(nativeAd.getPrimaryViewOfWidth(mContext,lineaeview,container,0));
            }
        }

        return pageView;
    }
}