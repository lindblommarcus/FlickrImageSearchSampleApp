package se.marcuslindblom.flickrsearchapp.viewmodels;

import android.graphics.Bitmap;

public class ImageItemViewModel {

    private Bitmap image;
    private String title;

    public ImageItemViewModel(Bitmap image, String title) {
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}



