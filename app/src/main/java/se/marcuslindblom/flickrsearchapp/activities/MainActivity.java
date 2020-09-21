package se.marcuslindblom.flickrsearchapp.activities;

import android.Manifest;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import se.marcuslindblom.flickrsearchapp.Facade;
import se.marcuslindblom.flickrsearchapp.R;
import se.marcuslindblom.flickrsearchapp.callbacks.ImageItemViewModelListDoneCallback;
import se.marcuslindblom.flickrsearchapp.fragments.ImageDetailsDialogFragment;
import se.marcuslindblom.flickrsearchapp.utils.CacheUtils;
import se.marcuslindblom.flickrsearchapp.viewmodels.ImageItemViewModel;
import se.marcuslindblom.flickrsearchapp.views.ImageRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final String IMAGE_DETAIL_DIALOG_FRAGMENT_TAG = "image_detail_dialog";


    private TextView errorText;
    private EditText searchField;
    private ProgressBar progressBarTop;
    private ProgressBar progressBarBottom;
    private Button searchButton;

    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private int pageNumber;
    private String lastKeyword;
    private Facade facade;
    private SharedPreferences sharedPreferences;
    private List<ImageItemViewModel> imageItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        pageNumber = 0;
        imageItemList = new ArrayList<>();
        facade = new Facade();
        sharedPreferences = getSharedPreferences(CacheUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sharedPreferences.edit().clear().apply();

        initializeViews();
        buildRecyclerView();
        hideProgressBars();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastKeyword == null || !lastKeyword.equals(searchField.getText().toString())) {
                    pageNumber = 0;
                    progressBarTop.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    lastKeyword = searchField.getText().toString();
                    facade.getFlickrImagesByKeyword(sharedPreferences, searchField.getText().toString(), pageNumber, getImageItemViewModelListDoneCallback());
                }
            }
        });
    }

    private void hideProgressBars() {
        progressBarTop.setVisibility(View.GONE);
        progressBarBottom.setVisibility(View.GONE);
    }

    private void initializeViews() {
        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        errorText = findViewById(R.id.error_text);
        progressBarTop = findViewById(R.id.progress_bar_top);
        progressBarBottom = findViewById(R.id.progress_bar_bottom);
        errorText.setVisibility(View.GONE);
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.image_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(imageItemList);
        recyclerView.setAdapter(imageRecyclerViewAdapter);

        imageRecyclerViewAdapter.setOnItemClickListener(new ImageRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showImageDetailDialog(imageItemList.get(position).getImage(), imageItemList.get(position).getTitle());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && imageRecyclerViewAdapter.getItemCount() > 0) {
                    progressBarBottom.setVisibility(View.VISIBLE);
                    pageNumber++;
                    facade.getFlickrImagesByKeyword(sharedPreferences, searchField.getText().toString(), pageNumber, getImageItemViewModelListDoneCallback());
                }
            }
        });

    }

    private void showImageDetailDialog(Bitmap image, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ImageDetailsDialogFragment imageDetailsDialogFragment = ImageDetailsDialogFragment.newInstance(image, title);
        if (fragmentManager.findFragmentByTag(IMAGE_DETAIL_DIALOG_FRAGMENT_TAG) == null) {
            imageDetailsDialogFragment.show(fragmentManager, IMAGE_DETAIL_DIALOG_FRAGMENT_TAG);
        }
    }


    private void checkPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.permissions_rationale), PERMISSIONS_REQUEST_CODE, perms);
        }/* else {
            //Close app or do something else that won't bother doing now
        }*/
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    hideSystemUI();
                }
            }
        });
    }

    public ImageItemViewModelListDoneCallback getImageItemViewModelListDoneCallback() {
        return new ImageItemViewModelListDoneCallback() {
            @Override
            public void onDone(List<ImageItemViewModel> imageItemViewModelList) {
                if (pageNumber == 0) {
                    imageItemList.clear();
                    recyclerView.scrollToPosition(0);
                }

                imageItemList.addAll(imageItemViewModelList);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                hideProgressBars();

                if (imageItemViewModelList.size() == 0) {
                    errorText.setVisibility(View.VISIBLE);
                } else {
                    errorText.setVisibility(View.GONE);
                }

            }
        };
    }
}
