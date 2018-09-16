package com.dakshpokar.essentialmode;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rd.PageIndicatorView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements AppChooserListener, HomeFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static HomeFragment homeFragment;
    static NotificationsFragment notSetFragment;
    static PageIndicatorView pageIndicatorView;
    private Integer invoker;
    DevicePolicyManager deviceManger;

    TextView txtView;
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.pureBlack));
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
        //pageIndicatorView = (PageIndicatorView)findViewById(R.id.pageIndicatorView);
        //pageIndicatorView.setVisibility(View.INVISIBLE);
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        homeFragment = new HomeFragment();
        //txtView = (TextView)findViewById(R.id.txtView);
        //notSetFragment = new NotificationsFragment();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    //pageIndicatorView.setVisibility(View.INVISIBLE);
                }
                /*else if(position == 1)
                {
                    pageIndicatorView.setVisibility(View.INVISIBLE);
                }*/
                else{
                    //pageIndicatorView.setVisibility(View.INVISIBLE);
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
        hideSystemUI();
    }

    private void hideSystemUI() {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.pureBlack));

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
    public void clicko(View view){
        homeFragment.mDatabaseHelper.removeAll();
    }
    public void selector(View view){
        invoker = view.getId();
        AppChooserDialog.show(view.getContext(), this);
    }
    @Override
    public void onAppChooserSelected(AppItem value) {
        homeFragment.setApp(invoker, value);
    }

    @Override
    public void onAppChooserCancel() {

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
            /*
            else if(position == 1){
                return notSetFragment;
            }
            */
            else{
                return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

    }
    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
            txtView.setText(temp);
        }
    }

    @Override
    protected void onPause() {
        hideSystemUI();
        super.onPause();
    }

    @Override
    protected void onResume() {
        hideSystemUI();
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.pureBlack));
        super.onResume();
    }

    @Override
    protected void onRestart() {
        hideSystemUI();
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.pureBlack));
        super.onRestart();
    }
    public void ToggleWifi(View view){
        WifiManager wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ImageButton imageButton = (ImageButton) findViewById(R.id.wifi_setting);
        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_off_24dp));
        }
        else{
            wifiManager.setWifiEnabled(true);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_on_24dp));
        }
    }
    public void ToggleBluetooth(View view){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ImageButton imageButton = (ImageButton) findViewById(R.id.bluetooth_setting);
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_off_24dp));
        }
        else{
            bluetoothAdapter.enable();
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_on_24dp));
        }
    }
    public void ToggleAutoRotate(View view){
        ImageButton imageButton = (ImageButton) findViewById(R.id.bluetooth_setting);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }
}
