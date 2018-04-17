package com.inmobi.nativead.sample.recycleview;

import com.inmobi.ads.InMobiNative;

import java.lang.ref.WeakReference;

/**
 * Created by jerry.hu on 12/09/17.
 */

public class Meizi {

    private String url;//图片地址
    private int page;//页数

    public WeakReference<InMobiNative> inMobiNative;

    public WeakReference<InMobiNative> getInMobiNative() {
        return inMobiNative;
    }

    public void setInMobiNative(WeakReference<InMobiNative> inMobiNative) {
        this.inMobiNative = inMobiNative;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
