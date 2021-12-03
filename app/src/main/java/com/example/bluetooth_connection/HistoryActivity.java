package com.example.bluetooth_connection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bluetooth_connection.room.DeviceModel;
import com.example.bluetooth_connection.room.ViewModal;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
  ViewModal viewModal;
  HistoryAdapter adapter;
  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        viewModal=  viewModal = ViewModelProviders.of(this).get(ViewModal.class);
        recyclerView=findViewById(R.id.listview_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new HistoryAdapter();
        recyclerView.setAdapter(adapter);
        viewModal.getAllDevices().observe(this, new Observer<List<DeviceModel>>() {
            @Override
            public void onChanged(List<DeviceModel> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                adapter.addDeviceList(models);
            }
        });
    }
}