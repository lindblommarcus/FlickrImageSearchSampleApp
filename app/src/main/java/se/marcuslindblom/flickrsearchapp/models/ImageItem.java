package se.marcuslindblom.flickrsearchapp.models;

import android.graphics.Bitmap;

import com.flickr4java.flickr.people.User;


public class ImageItem {
    private Bitmap image;
    private String title;
    private User owner;

    public ImageItem(Bitmap image, String title, User owner) {
        this.image = image;
        this.title = title;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "image=" + image +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                '}';
    }
}
