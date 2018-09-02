package com.dakshpokar.essentialmode;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.text.SimpleDateFormat;

import static com.dakshpokar.essentialmode.MainActivity.pageIndicatorView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHelper mDatabaseHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public View view;
    private ImageButton app1, app2, app3, app4;
    View.OnLongClickListener longClickListener;
    public static final int RESULT_ENABLE = 11;

    public HomeFragment() {
        pageIndicatorView.setVisibility(View.INVISIBLE);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    private void lock() {
        PowerManager pm = (PowerManager)getContext().getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            DevicePolicyManager policy = (DevicePolicyManager)
                    getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
            try {
                policy.lockNow();
            } catch (SecurityException ex) {
                Toast.makeText(getActivity(), "Please enable Device Administrator", Toast.LENGTH_LONG).show();
                ComponentName admin = new ComponentName(getContext(), AdminReceiver.class);
                Intent intent = new Intent(
                        DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).putExtra(
                        DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
                getContext().startActivity(intent);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
        Thread thread = new Thread(){
            @Override
            public void run(){
                try
                {
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView textViewTime = (TextView)view.findViewById(R.id.time);
                                TextView textViewDate = (TextView)view.findViewById(R.id.date);
                                SimpleDateFormat timeData = new SimpleDateFormat("h:mm a");
                                SimpleDateFormat dateData = new SimpleDateFormat("MMM dd, yyyy");
                                Resources res = getResources();
                                String text = String.format(res.getString(R.string.time), timeData);
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
        super.onCreate(savedInstanceState);
    }
    public void populateAppList(){
        Cursor data = mDatabaseHelper.getData();
        ApplicationInfo applicationInfo;
        PackageManager packageManager = getContext().getPackageManager();
        while(data.moveToNext()) {
            try {
                applicationInfo = packageManager.getApplicationInfo(data.getString(1), 0);
                AppItem appItem = new AppItem(getContext(), applicationInfo);
                ImageButton app = null;
                Log.i("DB", String.valueOf(data.getInt(0)));
                switch (data.getInt(0)) {
                    case 1:
                        app = app1;
                        Log.i("DB", "app1");
                        break;
                    case 2:
                        app = app2;
                        Log.i("DB", "app2");
                        break;
                    case 3:
                        app = app3;
                        Log.i("DB", "app3");
                        break;
                    case 4:
                        app = app4;
                        Log.i("DB", "app4");
                        break;
                }
                final String pkg_name = appItem.getPackageName();
                    if(data.getInt(0) < 5) {
                        app.setImageDrawable(appItem.getIcon(getContext()));
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        app.setColorFilter(filter);
                        app.setOnClickListener(new View.OnClickListener() {
                            Integer count = 0;

                            @Override
                            public void onClick(View v) {
                                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(pkg_name);
                                Bundle optsBundle = null;
                                ActivityOptions opts = null;
                                int left = 0, top = 0;
                                int width = v.getMeasuredWidth(), height = v.getMeasuredHeight();
                                opts = ActivityOptions.makeClipRevealAnimation(v, left, top, width, height);
                                optsBundle = opts != null ? opts.toBundle() : null;
                                getContext().startActivity(launchIntent, optsBundle);
                            }
                        });
                        app.setOnLongClickListener(longClickListener);
                    }
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
    }
    Integer invoker = 0;
    public void selector(View view){
        invoker = view.getId();
        AppChooserDialog.show(view.getContext(), (MainActivity)getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    lock();
                    return super.onDoubleTap(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mDatabaseHelper = new DatabaseHelper(getActivity());
        //Setting icons on home screen
        app1 = (ImageButton)view.findViewById(R.id.app1);
        app2 = (ImageButton)view.findViewById(R.id.app2);
        app3 = (ImageButton)view.findViewById(R.id.app3);
        app4 = (ImageButton)view.findViewById(R.id.app4);
        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Integer id = 0;
                ImageButton app = null;
                switch (v.getId()) {
                    case R.id.app1:
                        id = 1;
                        app = app1;
                        break;
                    case R.id.app2:
                        id = 2;
                        app = app2;
                        break;
                    case R.id.app3:
                        id = 3;
                        app = app3;
                        break;
                    case R.id.app4:
                        id = 4;
                        app = app4;
                        break;
                }
                app.setColorFilter(null);
                app.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selector(v);
                    }
                });
                mDatabaseHelper.remove(id);
                vibrator.vibrate(100);
                Toast.makeText(getActivity(), "Removed App "+id, Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        app1.setOnLongClickListener(longClickListener);
        app2.setOnLongClickListener(longClickListener);
        app3.setOnLongClickListener(longClickListener);
        app4.setOnLongClickListener(longClickListener);
        populateAppList();

        TextView textViewTime = (TextView)view.findViewById(R.id.time);
        TextView textViewDate = (TextView)view.findViewById(R.id.date);
        SimpleDateFormat timeData = new SimpleDateFormat("h:mm a");
        SimpleDateFormat dateData = new SimpleDateFormat("MMM dd, yyyy");
        Resources res = getResources();
        String text = String.format(res.getString(R.string.time), timeData);
        long date = System.currentTimeMillis();
        String timeString = timeData.format(date);
        String dateString = dateData.format(date);
        textViewDate.setText(dateString);
        textViewTime.setText(timeString);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setApp(Integer id, AppItem item){
        ImageButton app = null;
        final String pkg_name = item.getPackageName();
        Integer mainID = 0;
        Integer ID = 0;
        if(invoker != 0){
            ID = invoker;
        }
        else {
            ID = id;
        }
        switch(ID){
            case R.id.app1:
                app = app1;
                mainID = 1;
                break;
            case R.id.app2:
                app = app2;
                mainID = 2;
                break;
            case R.id.app3:
                app = app3;
                mainID = 3;
                break;
            case R.id.app4:
                app = app4;
                mainID = 4;
                break;
        }
        Log.i("ID: ", String.valueOf(ID));
        if(mainID<5) {
            mDatabaseHelper.addData(mainID, pkg_name);
        }
        app.setImageDrawable(item.getIcon(getContext()));
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        app.setColorFilter(filter);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(pkg_name);
                Bundle optsBundle = null;
                ActivityOptions opts = null;
                int left = 0, top = 0;
                int width = v.getMeasuredWidth(), height = v.getMeasuredHeight();
                opts = ActivityOptions.makeClipRevealAnimation(v, left, top, width, height);
                optsBundle = opts != null ? opts.toBundle() : null;
                getContext().startActivity(launchIntent, optsBundle);

            }
        });
        invoker = 0;
    }

}
