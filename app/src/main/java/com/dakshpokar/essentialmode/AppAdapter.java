package com.dakshpokar.essentialmode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class AppAdapter extends ArrayAdapter<AppItem> {

    private static final int RESOURCE = R.layout.app_row;
    private LayoutInflater inflater;

    static public class ViewHolder {
        public View rootView;
        public TextView textviewTitle;
        public ImageView imageviewIcon;

        public static ViewHolder createOrRecycle(LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.app_row, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                ViewHolder holder = new ViewHolder();
                holder.rootView = convertView;

                holder.textviewTitle = (TextView) convertView
                        .findViewById(R.id.apptitle);
                holder.imageviewIcon = (ImageView) convertView
                        .findViewById(R.id.appicon);

                convertView.setTag(holder);
                return holder;
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                return (ViewHolder)convertView.getTag();
            }
        }
    }

    public AppAdapter(Context context, java.util.List<AppItem> apps) {
        super(context, RESOURCE, apps);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.createOrRecycle(inflater, convertView);
        convertView = holder.rootView;

        AppItem app = (AppItem) getItem(position);

        synchronized  (app) {
            holder.textviewTitle.setText(app.getTitle());
            holder.imageviewIcon.setImageDrawable(app.getIcon(convertView.getContext()));
        }

        return convertView;
    }
}