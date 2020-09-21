package se.marcuslindblom.flickrsearchapp;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import se.marcuslindblom.flickrsearchapp.callbacks.ImageItemListDoneCallback;
import se.marcuslindblom.flickrsearchapp.callbacks.ImageItemViewModelListDoneCallback;
import se.marcuslindblom.flickrsearchapp.data.DataHandler;
import se.marcuslindblom.flickrsearchapp.models.ImageItem;
import se.marcuslindblom.flickrsearchapp.viewmodels.ImageItemViewModel;

public class Facade {

    private ImageItemViewModelListDoneCallback imageItemViewModelListDoneCallbackListener;

    public void getFlickrImagesByKeyword(SharedPreferences sharedPreferences, String keyword, int pageNumber, ImageItemViewModelListDoneCallback callback) {
        imageItemViewModelListDoneCallbackListener = callback;
        DataHandler.getFlickrPhotosByKeyword(sharedPreferences, keyword, pageNumber, getImageItemListDoneCallback());
    }

    private ImageItemListDoneCallback getImageItemListDoneCallback() {
        return new ImageItemListDoneCallback() {
            @Override
            public void onDone(List<ImageItem> imageItemList) {
                List<ImageItemViewModel> imageItemViewModelList = new ArrayList<>();
                ImageItemViewModel imageItemViewModel;
                for (ImageItem imageItem : imageItemList) {
                    imageItemViewModel = new ImageItemViewModel(imageItem.getImage(), imageItem.getTitle());
                    imageItemViewModelList.add(imageItemViewModel);
                }
                imageItemViewModelListDoneCallbackListener.onDone(imageItemViewModelList);
            }

        };
    }
}