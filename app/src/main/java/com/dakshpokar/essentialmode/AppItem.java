package com.dakshpokar.essentialmode;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppItem {
    private ApplicationInfo applicationInfo;
    private String title;
    private String packageName;
    public Drawable icon;

    public AppItem(Context context, ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        this.title = applicationInfo.loadLabel(context.getPackageManager())
                .toString();
        this.packageName = applicationInfo.packageName;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon(Context context) {
        if (icon == null)
        {
            icon = applicationInfo.loadIcon(context.getPackageManager());
        }
        return icon;
    }
}