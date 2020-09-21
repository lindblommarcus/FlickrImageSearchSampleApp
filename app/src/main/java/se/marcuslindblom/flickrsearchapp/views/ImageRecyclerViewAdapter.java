package se.marcuslindblom.flickrsearchapp.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import se.marcuslindblom.flickrsearchapp.R;
import se.marcuslindblom.flickrsearchapp.viewmodels.ImageItemViewModel;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;

    private List<ImageItemViewModel> imageItemList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView cardImage;
        private TextView cardTitle;

        public MyViewHolder(MaterialCardView view, final OnItemClickListener clickCistener) {
            super(view);

            cardImage = view.findViewById(R.id.cardImage);
            cardTitle = view.findViewById(R.id.cardTitle);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickCistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickCistener.onItemClick(position);
                        }
                    }
                }
            });

        }

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public ImageRecyclerViewAdapter(List<ImageItemViewModel> imageItemList) {
        this.imageItemList = imageItemList;
    }

    @NonNull
    @Override
    public ImageRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageItemViewModel currentItem = imageItemList.get(position);

        holder.cardImage.setImageBitmap(currentItem.getImage());

        String titleText;
        if(currentItem.getTitle().length() > 25){
            titleText = currentItem.getTitle().substring(0, 25) + "...";
        } else {
            titleText = currentItem.getTitle();
        }
        holder.cardTitle.setText(titleText);
    }

    @Override
    public int getItemCount() {
        return imageItemList.size();
    }
}
