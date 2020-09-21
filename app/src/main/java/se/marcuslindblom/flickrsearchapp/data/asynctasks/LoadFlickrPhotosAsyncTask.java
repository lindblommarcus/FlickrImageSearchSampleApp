package se.marcuslindblom.flickrsearchapp.data.asynctasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

import java.util.ArrayList;
import java.util.List;

import se.marcuslindblom.flickrsearchapp.callbacks.ImageItemListDoneCallback;
import se.marcuslindblom.flickrsearchapp.models.ImageItem;
import se.marcuslindblom.flickrsearchapp.utils.FlickrUtils;
import se.marcuslindblom.flickrsearchapp.utils.ImageUtils;

public class LoadFlickrPhotosAsyncTask extends AsyncTask<String, Void, List<ImageItem>> {

    private ImageItemListDoneCallback callback;

    private SharedPreferences sharedPreferences;

    private int pageNumber;

    public LoadFlickrPhotosAsyncTask(SharedPreferences sharedPreferences, int pageNumber) {
        this.sharedPreferences = sharedPreferences;
        this.pageNumber = pageNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ImageItem> doInBackground(String... strings) {
        Flickr f = new Flickr(FlickrUtils.API_KEY, FlickrUtils.SHARED_SECRET, new REST());
        String keyword = strings[0];
        PhotosInterface photos = f.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        List<ImageItem> imageItemList = new ArrayList<>();

//        List<ImageItem> imageItemList = CacheUtils.loadData(sharedPreferences, CacheUtils.buildCacheKeyString(keyword, pageNumber));

//        if (imageItemList == null) { //Sought images weren't cached.
        try {
            params.setMedia(FlickrUtils.PHOTOS);
            params.setText(keyword);
            List<Photo> photoList = photos.search(params, FlickrUtils.IMAGES_PER_LOAD_PAGE, pageNumber);
            for (Photo photo : photoList) {
                imageItemList.add(new ImageItem(ImageUtils.getBitmapFromURL(photo.getMedium640Url()), photo.getTitle(), photo.getOwner()));
            }
//            CacheUtils.savePhotos(sharedPreferences, imageItemList, CacheUtils.buildCacheKeyString(keyword, pageNumber));

        } catch (FlickrException e) {
            e.printStackTrace();
        }
//        }
        return imageItemList;
    }


    @Override
    protected void onPostExecute(List<ImageItem> imageItemList) {
        super.onPostExecute(imageItemList);
        callback.onDone(imageItemList);
    }

    public void setCallback(ImageItemListDoneCallback listener) {
        this.callback = listener;
    }


}

