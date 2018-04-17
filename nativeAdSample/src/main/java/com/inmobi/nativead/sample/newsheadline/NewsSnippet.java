package com.inmobi.nativead.sample.newsheadline;

import android.view.View;

import com.inmobi.ads.InMobiNative;

import java.lang.ref.WeakReference;

public class NewsSnippet {
    public String title;
    public String imageUrl;
    public String content;
    public String landingUrl;
    public String description;
    public int progress = 0;
    public WeakReference<InMobiNative> inMobiNative;
    public boolean isBig;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLandingUrl() {
        return landingUrl;
    }

    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public WeakReference<InMobiNative> getInMobiNative() {
        return inMobiNative;
    }

    public void setInMobiNative(WeakReference<InMobiNative> inMobiNative) {
        this.inMobiNative = inMobiNative;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setBig(boolean big) {
        isBig = big;
    }
}