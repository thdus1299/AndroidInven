package com.Coupon.Tan.CustomStoreListView;

import android.graphics.drawable.Drawable;

/**
 * Created by stories2 on 2016. 11. 28..
 */

public class EachStoreListViewItem {
    Drawable eachStoreIcon;
    String eachStoreTitle, eachStoreSubTitle, eachStoreId;

    public void SetEachStoreIcon(Drawable eachStoreIcon) {
        this.eachStoreIcon = eachStoreIcon;
    }

    public void SetEachStoreTitle(String eachStoreTitle) {
        this.eachStoreTitle = eachStoreTitle;
    }

    public void SetEachStoreSubTitle(String eachStoreSubTitle) {
        this.eachStoreSubTitle = eachStoreSubTitle;
    }

    public void SetEachStoreId(String eachStoreId) {
        this.eachStoreId = eachStoreId;
    }

    public Drawable GetEachStoreIcon() {
        return eachStoreIcon;
    }

    public String GetEachStoreTitle() {
        return eachStoreTitle;
    }

    public String GetEachStoreSubTitle() {
        return eachStoreSubTitle;
    }

    public String GetEachStoreId() {
        return eachStoreId;
    }
}
