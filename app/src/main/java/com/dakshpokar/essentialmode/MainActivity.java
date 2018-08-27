package com.dakshpokar.essentialmode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Intent.CATEGORY_LAUNCHER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try
                {
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textViewTime = (TextView)findViewById(R.id.time);
                                TextView textViewDate = (TextView)findViewById(R.id.date);
                                SimpleDateFormat timeData = new SimpleDateFormat("h:mm a");
                                SimpleDateFormat dateData = new SimpleDateFormat("MMM dd, yyyy");
                                long date = System.currentTimeMillis();
                                String timeString = timeData.format(date);
                                String dateString = dateData.format(date);
                                textViewDate.setText(dateString);
                                textViewTime.setText(timeString);
                            }
                        });
                    }
                }
                catch (InterruptedException e){

                }
            }
        };
        thread.start();
        setContentView(R.layout.activity_main);

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
}
