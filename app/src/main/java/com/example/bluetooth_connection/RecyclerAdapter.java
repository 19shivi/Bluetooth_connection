package com.example.bluetooth_connection;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<String> deviceList=new ArrayList<>();
    ArrayList<String> deviceMacList=new ArrayList<>();
    public  int current=0;

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);

        return new RecyclerAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
           holder.setIsRecyclable(false);
        holder.name.setText(deviceList.get(position));
        if(position==current)
            holder.button.setChecked(true);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current=position;
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    public void addDevice(String name,String mac)
    {  if(!deviceMacList.contains(mac)) {
        deviceList.add(name);
        deviceMacList.add(mac);
        notifyDataSetChanged();
    }
    }
    public void addDeviceList(ArrayList<String> name,ArrayList<String> mac)
    {
        deviceList.addAll(name);
        deviceMacList.addAll(mac);
        notifyDataSetChanged();
    }

     class ViewHolder  extends RecyclerView.ViewHolder {
        TextView name;
        RadioButton button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.device);
            button=itemView.findViewById(R.id.radio);
        }
    }
}
