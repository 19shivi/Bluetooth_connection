package com.example.bluetooth_connection;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button initialize,history,show_devices,visible,disconnect;
    private BluetoothAdapter BA;
    AcceptThread acceptThread;
    ConnectThread connectThread;
    ArrayList<BluetoothDevice> pairedDevices=new ArrayList<>();
    ListView lv;
    DeviceListFragment dialogFragment;
    TextView status;
    private String name="shivam Gupta";
    private UUID myUUID=UUID.fromString("220da3b2-41f5-11e7-a919-92ebcb67fe33");
    private  final int CONNECTING=1;
    private final int CONNECTING_FAILED=2;
    private  final int CONNECTING_SUCCESSFULLY=3;
    private final int DISCONNECTING=4;
    private final int DISCONNECTING_FAILED=5;
    private final int DISCONNECTING_SUCCESSFULLY=6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, intent);
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBroadcastReceiver3, filter3);
        initialize = (Button) findViewById(R.id.initialize_button);
        history=(Button)findViewById(R.id.history_button);
        status=findViewById(R.id.connection_textview);
        show_devices=(Button)findViewById(R.id.show_devices_button);
        visible=findViewById(R.id.visible_button);
        disconnect=findViewById(R.id.disconnect_button);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Disconnect();
            }
        });
        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int requestCode = 10;
                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent, requestCode);

            }
        });
        initialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Initialize_function();
            }
        });

        BA = BluetoothAdapter.getDefaultAdapter();
        show_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showDialog();


            }
        });
       /* Handler handler= new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                switch (message.what)
                {
                    case  1:status.setText("coneecting");
                    case  2:status.setText("coneectect Successfully");
                    case  3:status.setText("coneecting failed");
                    case  4:status.setText("Disconeecting succesfully ");
                }
                return false;
            }
        });*/

     acceptThread=new AcceptThread();
    acceptThread.start();
    }

    public void Initialize_function(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }


    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


   /* public void list(View v){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!isGpsEnabled) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 4);
                    }
                    else
                    Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       Log.v("request", requestCode+" "+resultCode);
        if(resultCode!=0 && requestCode==10)
        {
            Toast.makeText(getApplicationContext(),"Visible to nearby devices",Toast.LENGTH_SHORT).show();
        }
        else if(resultCode==10)
        {
            Toast.makeText(getApplicationContext(),"Invisible to nearby devices",Toast.LENGTH_SHORT).show();
        }
       else if (resultCode != 0 && resultCode==4) {
            if(dialogFragment!=null) {


            }
        }
       else
        Toast.makeText(this,"Please Enable GPS for Searching",Toast.LENGTH_LONG).show();
    }
    public void showDialog()
    {

         dialogFragment = new DeviceListFragment();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");

    }
    public  void setConnection(String Name,String macAddress)
    {
        for (BluetoothDevice b:pairedDevices) {
            if(b.getAddress().equals(macAddress))
            {
                Log.v("b",b.getName()+name);
               makeConnection(b);
               break;
            }
        }
    }
    public  void makeConnection(BluetoothDevice bluetoothDevice)
    {    dialogFragment.dismiss();
          connectThread=new ConnectThread(bluetoothDevice);
         connectThread.start();
     //   bluetoothDevice.connectGatt()
    }
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(getApplicationContext(),"Paired",Toast.LENGTH_LONG).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(getApplicationContext(),"Paired",Toast.LENGTH_LONG).show();
                }

            }
        }
    };
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {

            BluetoothServerSocket tmp = null;
            try {

                tmp = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(name, myUUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Message message2=Message.obtain();
                    message2.what=(CONNECTING_FAILED);
                    handler.sendMessage(message2);
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }
                  Message message=Message.obtain();
                message.what=(CONNECTING);
                handler.sendMessage(message);
                if (socket != null) {
                    Message message1=Message.obtain();
                    message1.what=(CONNECTING_SUCCESSFULLY);
                    handler.sendMessage(message1);
                   // manageMyConnectedSocket(socket);
                  //  mmServerSocket.close();
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                Message message1=Message.obtain();
                message1.what=(DISCONNECTING);
                handler.sendMessage(message1);
                mmServerSocket.close();
                Message message3=Message.obtain();
                message3.what=(DISCONNECTING_SUCCESSFULLY);
                handler.sendMessage(message3);
            } catch (IOException e) {
                Message message2=Message.obtain();
                message2.what=(DISCONNECTING_FAILED);
                handler.sendMessage(message2);
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                tmp=device.createRfcommSocketToServiceRecord(myUUID);
                Log.v("hello","shiva,");

                Log.v("hello","shiva,error");
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BA.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Message message1=Message.obtain();
                message1.what=(CONNECTING);
                handler.sendMessage(message1);
                mmSocket.connect();
                Log.v("hello","shiva,connect");
            } catch (IOException connectException) {

                // Unable to connect; close the socket and return.
                Log.v("hello","shiva,exceptionconnect"+" "+connectException);
                try {
                    mmSocket.close();
                    Log.v("hello","shicloseva,");
                } catch (IOException closeException) {
                    Log.v("hello","shiva,close exception");
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                Message message5=Message.obtain();
                message5.what=(CONNECTING_FAILED);
                handler.sendMessage(message5);
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            Message message1=Message.obtain();
            message1.what=(CONNECTING_SUCCESSFULLY);
            handler.sendMessage(message1);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                Message message5=Message.obtain();
                message5.what=(DISCONNECTING);
                handler.sendMessage(message5);
                mmSocket.close();
                Message message1=Message.obtain();
                message1.what=(DISCONNECTING_SUCCESSFULLY);
                handler.sendMessage(message1);
            } catch (IOException e) {
                Message message5=Message.obtain();
                message5.what=(DISCONNECTING_FAILED);
                handler.sendMessage(message5);
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                   status.setText("Cconnected Successfully");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    status.setText("Disconnected Successfully");
                    break;
            }
        }
    };
    Handler handler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
             Log.v("Shivam",""+message.what);
            switch (message.what)
            {
                case  CONNECTING :status.setText("connecting");break;
                case  CONNECTING_SUCCESSFULLY:status.setText("connectect Successfully");break;
                case  CONNECTING_FAILED:status.setText("connecting failed");break;
                case  DISCONNECTING :status.setText("Disconnecting");break;
                case  DISCONNECTING_SUCCESSFULLY:status.setText("Disconnectect Successfully");break;
                case  DISCONNECTING_FAILED:status.setText("Disconnecting failed");break;
                default:status.setText("No device Connected");break;
            }
            return false;
        }
    });

    void Disconnect()
    {
        if(connectThread!=null )
        {
            connectThread.cancel();
        }
         if(acceptThread!=null )
            acceptThread.cancel();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPairReceiver);
        unregisterReceiver(mBroadcastReceiver3);
    }
}