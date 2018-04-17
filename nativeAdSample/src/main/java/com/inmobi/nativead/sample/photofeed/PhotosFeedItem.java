package com.inmobi.nativead.sample.photofeed;

import com.inmobi.ads.InMobiNative;

import java.lang.ref.WeakReference;

public final class PhotosFeedItem {
    public String title;
    public String imageUrl;
    public String landingUrl;
    public String description;
    WeakReference<InMobiNative> inMobiNative;
}