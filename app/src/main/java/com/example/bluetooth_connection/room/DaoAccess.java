package com.example.bluetooth_connection.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao

public interface DaoAccess {
    

        // below method is use to 
        // add data to database.
        @Insert
        void insert(DeviceModel model);

        // below method is use to update 
        // the data in our database.
        @Update
        void update(DeviceModel model);

    // below line is use to delete a
    // specific course in our database.
    @Delete
    default void delete(DeviceModel model) {

    }

    // on below line we are making query to
        // delete all courses from our database.
        @Query("DELETE FROM course_table")
        void deleteAllDevices();

        // below line is to read all the courses from our database.
        // in this we are ordering our courses in ascending order 
        // with our course name.
        @Query("SELECT * FROM course_table ")
        LiveData<List<DeviceModel>> getAllDevices();
    
}
