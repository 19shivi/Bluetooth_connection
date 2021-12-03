package com.example.bluetooth_connection.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    // below line is the create a variable
    // for dao and list for all courses.
    private DaoAccess dao;
    private LiveData<List<DeviceModel>> alldevices;

    // creating a constructor for our variables
    // and passing the variables to it.
    public Repository(Application application) {
        DataBase database = DataBase.getInstance(application);
        dao = database.DaoAccess();
        alldevices = dao.getAllDevices();
    }

    // creating a method to insert the data to our database.
    public void insert(DeviceModel model) {
        new InsertCourseAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(DeviceModel model) {
        new UpdateCourseAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(DeviceModel model) {
        new DeleteCourseAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the courses.
    public void deleteAllDevices() {
        new DeleteAllCoursesAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public LiveData<List<DeviceModel>> getAllDevices() {
        return alldevices;
    }

    // we are creating a async task method to insert new course.
    private static class InsertCourseAsyncTask extends AsyncTask<DeviceModel, Void, Void> {
        private DaoAccess dao;

        private InsertCourseAsyncTask(DaoAccess dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DeviceModel... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our course.
    private static class UpdateCourseAsyncTask extends AsyncTask<DeviceModel, Void, Void> {
        private DaoAccess dao;

        private UpdateCourseAsyncTask(DaoAccess dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DeviceModel... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteCourseAsyncTask extends AsyncTask<DeviceModel, Void, Void> {
        private DaoAccess dao;

        private DeleteCourseAsyncTask(DaoAccess dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DeviceModel... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoAccess dao;
        private DeleteAllCoursesAsyncTask(DaoAccess dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteAllDevices();
            return null;
        }
    }
}




