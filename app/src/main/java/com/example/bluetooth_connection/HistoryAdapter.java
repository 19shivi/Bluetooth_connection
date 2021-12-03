package com.example.bluetooth_connection;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth_connection.room.DeviceModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<DeviceModel> deviceList=new ArrayList<>();


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new HistoryAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        holder.name.setText(deviceList.get(position).getDeviceName());
        holder.address.setText(deviceList.get(position).getMacAddress());
        holder.time.setText(new Date(deviceList.get(position).getTimeStamp()).toString());


    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void addDeviceList(List<DeviceModel> list)
    {
        deviceList.addAll(list);

        notifyDataSetChanged();
    }

    class ViewHolder  extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_device);
            address=itemView.findViewById(R.id.address_device);
            time=itemView.findViewById(R.id.time);
        }
    }
}
