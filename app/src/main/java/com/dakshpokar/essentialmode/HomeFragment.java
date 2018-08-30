package com.dakshpokar.essentialmode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public View view;
    private ImageButton app1, app2, app3, app4;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Setting icons on home screen
        app1 = (ImageButton)view.findViewById(R.id.app1);

        app2 = (ImageButton)view.findViewById(R.id.app2);
        app3 = (ImageButton)view.findViewById(R.id.app3);
        app4 = (ImageButton)view.findViewById(R.id.app4);

        try {

            Drawable icon = getActivity().getPackageManager().getApplicationIcon("com.example.testnotification");
            app1.setImageDrawable(icon);

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
        switch(id){
            case R.id.app1:
                app = app1;
                break;
            case R.id.app2:
                app = app2;
                break;
            case R.id.app3:
                app = app3;
                break;
            case R.id.app4:
                app = app4;
                break;
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
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
    }

}
