package com.dakshpokar.essentialmode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.rd.PageIndicatorView;

import junit.framework.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Intent.CATEGORY_LAUNCHER;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static HomeFragment homeFragment;
    static NotificationsFragment notificationsFragment;
    static SettingsFragment settingsFragment;
    static PageIndicatorView pageIndicatorView;
    DevicePolicyManager deviceManger;

    TextView txtView;
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SimpleDateFormat timeData = new SimpleDateFormat("h:mm a");
        SimpleDateFormat dateData = new SimpleDateFormat("MMM dd, yyyy");
        Resources res = getResources();
        String text = String.format(res.getString(R.string.time), timeData);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        pageIndicatorView = (PageIndicatorView)findViewById(R.id.pageIndicatorView);
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();
        //txtView = (TextView)findViewById(R.id.txtView);
        notificationsFragment = new NotificationsFragment();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    pageIndicatorView.setVisibility(View.INVISIBLE);
                }
                else if(position == 1)
                {
                    pageIndicatorView.setVisibility(View.VISIBLE);
                }
                else if(position == 2){
                    pageIndicatorView.setVisibility(View.VISIBLE);
                }
                else{
                    pageIndicatorView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void OnFragmentInteractionListener(){

    }
    public void buttonClicked(View view){

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    public void selector(View view){


        Intent intent = new Intent();
        Intent myIntent = new Intent("android.intent.action.MAIN",null);
        myIntent.setAction(Intent.ACTION_VIEW);
        myIntent.addCategory("android.intent.category.LAUNCHER");
        intent.putExtra("android.intent.extra.INTENT",myIntent);
        startActivityForResult(intent, 1);

        PackageManager manager = getPackageManager();
        List<ResolveInfo> info = manager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
    }
    public void onBackPressed(){

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return homeFragment;
            }
            else if(position == 1){
                return notificationsFragment;
            }
            else if(position == 2){
                pageIndicatorView.setVisibility(View.INVISIBLE);
                return settingsFragment;
            }
            else{
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }
    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
            txtView.setText(temp);
        }
    }
}
