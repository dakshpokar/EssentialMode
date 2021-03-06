package com.dakshpokar.essentialmode;

import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txtView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    View view;
    ArrayList<StatusBarNotification> arrayList;
    ArrayAdapter<StatusBarNotification> adapter;

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        listView = (ListView)view.findViewById(R.id.notification_list);
        arrayList = new ArrayList<>();
        ImageView notification_icon = (ImageView)view.findViewById(R.id.notification_icon);
        TextView notification_app_name = (TextView) view.findViewById(R.id.notification_app_name);
        TextView notification_description = (TextView) view.findViewById(R.id.upper_text);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
                ImageButton imageButton = (ImageButton) view.findViewById(R.id.wifi_setting);
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                ImageButton bluetoothImage = (ImageButton) view.findViewById(R.id.bluetooth_setting);
                while(true) {
                    if (wifiManager.isWifiEnabled()) {
                        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_on_24dp));
                    } else {
                        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_off_24dp));
                    }

                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_on_24dp));
                    } else {
                        bluetoothImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_off_24dp));
                    }
                }
            }
        });
        thread.start();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void addItem(StatusBarNotification sbn){

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
    class ListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.notification_list_item, null);

            return null;
        }
    }
}
