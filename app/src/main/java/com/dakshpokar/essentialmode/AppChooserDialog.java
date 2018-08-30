package com.dakshpokar.essentialmode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AppChooserDialog {

    static List<AppItem> appList = new ArrayList<AppItem>();

    public static void show(final Context context,
                            final AppChooserListener appChooserListener) {
        show(context, appChooserListener,
                context.getString(R.string.appchooser_dialogtitle));
    }

    public static void show(final Context context,
                            final AppChooserListener appChooserListener,
                            final String dialogTitle) {

        // if (appList.isEmpty())
        refreshAppList(context);

        Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(Html.fromHtml("<font color='#000'>" + dialogTitle + "</font>"));

        final ListView appListView = new ListView(context);

        AppAdapter appAdapter = new AppAdapter(context, appList);
        appListView.setAdapter(appAdapter);

        builder.setView(appListView);

        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                appChooserListener.onAppChooserCancel();
            }
        });

        final Dialog dialog = builder.create();

        appListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                appChooserListener.onAppChooserSelected((AppItem) appListView
                        .getItemAtPosition(position));

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static void refreshAppList(final Context context) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);

        appList.clear();

        for (ResolveInfo appItem : pkgAppsList) {
            addWithoutDuplicate(appList, new AppItem(context,
                    appItem.activityInfo.applicationInfo));
        }

        java.util.Collections.sort(appList, new AppComparator());
    }

    public static void addWithoutDuplicate(List<AppItem> list, AppItem appItem) {
        for (Iterator<AppItem> iterator = list.iterator(); iterator.hasNext();) {
            AppItem current = iterator.next();

            if (current.getPackageName().equals(appItem.getPackageName())) {
                iterator.remove();
            }
        }

        list.add(appItem);
    }

    static class AppComparator implements Comparator<AppItem> {
        @Override
        public int compare(AppItem o1, AppItem o2) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    }

}