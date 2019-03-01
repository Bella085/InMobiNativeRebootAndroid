package com.inmobi.nativead.sample.newsfeed;

import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.R;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsTileItem> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewsTileItem> mItems;
    private NativeProvider mNativeProvider;

    public NewsAdapter(Context context, List<NewsTileItem> items, NativeProvider nativeProvider) {
        super(context, R.layout.news_tile_view, items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mNativeProvider = nativeProvider;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView || null == convertView.getTag()) {
            convertView = mInflater.inflate(R.layout.news_tile_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.headline = (TextView) convertView.findViewById(R.id.caption);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.sponsored);
            viewHolder.pixview=(LinearLayout) convertView.findViewById(R.id.pixview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsTileItem newsTileItem = mItems.get(position);
        viewHolder.headline.setText(newsTileItem.title);
        viewHolder.content.setText(newsTileItem.content);
        viewHolder.icon.setImageURI(Uri.parse(newsTileItem.imageUrl));

        WeakReference<InMobiNative> nativeAdRef = newsTileItem.inMobiNative;
        if (null == nativeAdRef) {
            viewHolder.tag.setVisibility(View.GONE);

        } else {
            // we have an ad at this position
            final InMobiNative nativeAd = nativeAdRef.get();
            if (nativeAd != null) {
                viewHolder.tag.setVisibility(View.VISIBLE);
                viewHolder.tag.setText("Sponsored");

                viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeAd.reportAdClickAndOpenLandingPage();
                    }
                });

                viewHolder.pixview.removeAllViews();
                viewHolder.pixview.addView(nativeAd.getPrimaryViewOfWidth(mContext,viewHolder.pixview,parent,1));

            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView headline;
        TextView content;
        TextView tag;
        ImageView icon;
        LinearLayout pixview;
    }
}