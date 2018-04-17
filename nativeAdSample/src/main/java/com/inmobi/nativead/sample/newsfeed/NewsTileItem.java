package com.inmobi.nativead.sample.newsfeed;

import com.inmobi.ads.InMobiNative;

import java.lang.ref.WeakReference;

public final class  NewsTileItem {
    String imageUrl;
    String title;
    String content;
    String landingUrl;
    WeakReference<InMobiNative> inMobiNative;

    @Override
    public String toString() {
        return "NewsTileItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", landingUrl='" + landingUrl + '\'' +
                ", inMobiNative=" + inMobiNative +
                '}';
    }
}