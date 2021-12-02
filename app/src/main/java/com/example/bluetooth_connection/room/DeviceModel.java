package com.example.bluetooth_connection.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class DeviceModel {

    // below line is to auto increment
    // id for each course.
    @PrimaryKey(autoGenerate = true)
    private int id;

    // below line is a variable
    // for course name.
    private String DeviveName;

    // below line is use for
    // course description.
    private String MacAddress;
    private long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviveName() {
        return DeviveName;
    }

    public void setDeviveName(String deviveName) {
        DeviveName = deviveName;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}