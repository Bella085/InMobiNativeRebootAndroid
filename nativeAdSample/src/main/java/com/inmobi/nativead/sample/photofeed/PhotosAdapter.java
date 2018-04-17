package com.inmobi.nativead.sample.photofeed;

import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.R;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.util.List;

public class PhotosAdapter extends ArrayAdapter<PhotosFeedItem> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PhotosFeedItem> mItems;
    private NativeProvider mNativeProvider;

    public PhotosAdapter(Context context, List<PhotosFeedItem> items, NativeProvider nativeProvider) {
        super(context, R.layout.photos_item_view, items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mNativeProvider = nativeProvider;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView || null == convertView.getTag()) {
            convertView = mInflater.inflate(R.layout.photos_item_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.caption);
            viewHolder.image = (SimpleDraweeView) convertView.findViewById(R.id.photo);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.sponsored);
            viewHolder.icon=(SimpleDraweeView)convertView.findViewById(R.id.photoicon);
            viewHolder.desc=(TextView) convertView.findViewById(R.id.destextView);
            viewHolder.con_view=(LinearLayout)convertView.findViewById(R.id.container_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PhotosFeedItem photosFeedItem = mItems.get(position);
        viewHolder.title.setText(photosFeedItem.title);
        viewHolder.image.setImageURI(Uri.parse(photosFeedItem.imageUrl));
        Log.e("Photo Adapter==","Photo Adapter==1");
        WeakReference<InMobiNative> nativeAdRef = photosFeedItem.inMobiNative;
        if (null == nativeAdRef) {
            viewHolder.tag.setVisibility(View.GONE);
            viewHolder.con_view.setVisibility(View.GONE);
            viewHolder.icon.setVisibility(View.GONE);
            viewHolder.desc.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.VISIBLE);
            Log.e("Photo Adapter==","Photo Adapter==NULL");
            //InMobiNative.unbind(convertView);
        } else {
            // we have an ad at this position
            final InMobiNative nativeAd = nativeAdRef.get();

            if (nativeAd != null) {
                Log.e("Photo Adapter==","Photo Adapter==!=NULL");
                viewHolder.tag.setVisibility(View.VISIBLE);
                viewHolder.tag.setText("Sponsored");
                viewHolder.image.setVisibility(View.GONE);
                //InMobiNative.bind(convertView, nativeAd);
                viewHolder.con_view.setVisibility(View.VISIBLE);
                viewHolder.icon.setVisibility(View.VISIBLE);
                viewHolder.desc.setVisibility(View.VISIBLE);

                viewHolder.icon.setImageURI(Uri.parse(photosFeedItem.imageUrl));
                viewHolder.desc.setText(photosFeedItem.description);
                viewHolder.con_view.removeAllViews();
                viewHolder.con_view.addView(nativeAd.getPrimaryViewOfWidth(mContext,viewHolder.con_view,parent,0));
                viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeAd.reportAdClickAndOpenLandingPage();
                    }
                });

                //nativeAd.resume();
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        SimpleDraweeView image,icon;
        TextView tag,desc;
        LinearLayout con_view;
    }
}