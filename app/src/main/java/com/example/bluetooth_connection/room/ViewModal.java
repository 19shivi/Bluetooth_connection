package com.example.bluetooth_connection.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    // creating a new variable for course repository.
    private Repository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<DeviceModel>> allCourses;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allCourses = repository.getAllDevices();
    }

    // below method is use to insert the data to our repository.
    public void insert(DeviceModel model) {
        repository.insert(model);
    }

    // below line is to update data in our repository.
    public void update(DeviceModel model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(DeviceModel model) {
        repository.delete(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllDevices() {
        repository.deleteAllDevices();
    }

    // below method is to get all the courses in our list.
    public LiveData<List<DeviceModel>> getAllDevices() {
        return allCourses;
    }
}
