package se.marcuslindblom.flickrsearchapp.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;

import se.marcuslindblom.flickrsearchapp.R;

public class ImageDetailsDialogFragment extends DialogFragment {

    private static final String TAG = ImageDetailsDialogFragment.class.getSimpleName();


    public ImageDetailsDialogFragment() {

    }

    public static ImageDetailsDialogFragment newInstance(Bitmap image, String title) {

        ImageDetailsDialogFragment frag = new ImageDetailsDialogFragment();

        Bundle args = new Bundle();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        args.putByteArray("image", byteArray);

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //		View view = inflater.inflate(R.layout.custom_manual_weight_dialog, container);
        View view = inflater.inflate(R.layout.image_details_dialog_fragment, null);

        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView detailDialogImage = view.findViewById(R.id.detail_dialog_image);
        TextView detailDialogTitle = view.findViewById(R.id.detail_dialog_title);
        ImageView detailDialogCloseButton = view.findViewById(R.id.detail_dialog_close_button);

        detailDialogCloseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        byte[] byteArray = getArguments().getByteArray("image");
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String title = getArguments().getString("title");

        detailDialogImage.setImageBitmap(image);
        detailDialogTitle.setText(title);


        return view;
    }

}
