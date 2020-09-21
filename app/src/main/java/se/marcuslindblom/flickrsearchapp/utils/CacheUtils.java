package se.marcuslindblom.flickrsearchapp.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import se.marcuslindblom.flickrsearchapp.models.ImageItem;

public class CacheUtils {

    private static final String TAG = CacheUtils.class.getSimpleName();
    public static final String SHARED_PREFERENCES_NAME = "image_cache_shared_preferences";

    public static String buildCacheKeyString(String keyword, int pageNumber) {
        return keyword + pageNumber;
    }

    public static void savePhotos(SharedPreferences sharedPreferences, List<ImageItem> imageItemList, String key) {
        if (!imageItemList.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(imageItemList);
            editor.putString(key, json);
            editor.apply();
        }
    }

    public static List<ImageItem> loadData(SharedPreferences sharedPreferences, String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<ImageItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


}
