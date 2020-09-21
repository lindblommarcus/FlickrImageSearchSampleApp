package se.marcuslindblom.flickrsearchapp.callbacks;

import java.util.List;

import se.marcuslindblom.flickrsearchapp.models.ImageItem;

public interface ImageItemListDoneCallback {
    void onDone(List<ImageItem> imageItemList);
}
