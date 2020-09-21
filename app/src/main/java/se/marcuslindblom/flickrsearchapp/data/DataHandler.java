package se.marcuslindblom.flickrsearchapp.data;

import android.content.SharedPreferences;

import se.marcuslindblom.flickrsearchapp.callbacks.ImageItemListDoneCallback;
import se.marcuslindblom.flickrsearchapp.data.asynctasks.LoadFlickrPhotosAsyncTask;

public class DataHandler {

    public static void getFlickrPhotosByKeyword(SharedPreferences sharedPreferences, String keyword, int pageNumber, ImageItemListDoneCallback callback) {
        LoadFlickrPhotosAsyncTask loadImagesAsyncTask = new LoadFlickrPhotosAsyncTask(sharedPreferences, pageNumber);
        loadImagesAsyncTask.setCallback(callback);
        loadImagesAsyncTask.execute(keyword);
    }


}
