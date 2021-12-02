package com.example.bluetooth_connection;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DeviceListFragment extends DialogFragment {

    BluetoothAdapter bluetoothAdapter;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
     Set<BluetoothDevice> pairedDevices;
    Button connect ;
    RecyclerAdapter recyclerAdapter;
    MainActivity mainActivity;
    public DeviceListFragment( ) {
      //  this.bluetoothAdapter=bluetoothAdapter;

    }

  RecyclerView recyclerView;
    /*public static DeviceListFragment newInstance(BluetoothAdapter bluetoothAdapter) {
        DeviceListFragment fragment = new DeviceListFragment(bluetoothAdapter);

        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity= (MainActivity) getActivity();
        checkCoarseLocationPermission();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);
        View view= inflater.inflate(R.layout.fragment_device_list, container, false);
        recyclerView=view.findViewById(R.id.listview);
         connect=view.findViewById(R.id.connect);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerAdapter=new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        list();

        bluetoothAdapter.startDiscovery();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("hello",recyclerAdapter.current+recyclerAdapter.deviceList.get(recyclerAdapter.current));
               mainActivity.setConnection(recyclerAdapter.deviceList.get(recyclerAdapter.current),recyclerAdapter.deviceMacList.get(recyclerAdapter.current));
            }
        });
        return view;
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mainActivity.pairedDevices.add(device);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.v("device",deviceName);
               // list_devices.add(deviceName);
               recyclerAdapter.addDevice(deviceName,deviceHardwareAddress);
            }
        }
    };
    public void list() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list_devices=new ArrayList<>();
        ArrayList<String> list_devices_mac=new ArrayList<>();
        for (BluetoothDevice bt : pairedDevices) {
            mainActivity.pairedDevices.add(bt);
            list_devices.add(bt.getName() + " paired");
            list_devices_mac.add(bt.getAddress());
        }
        recyclerAdapter.addDeviceList(list_devices,list_devices_mac);

    }



    private boolean checkCoarseLocationPermission() {
        //checks all needed permissions
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        }else{

            return true;
        }

    }








    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}