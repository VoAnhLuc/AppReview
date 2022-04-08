package team.no.nextbeen.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import team.no.nextbeen.R;
import team.no.nextbeen.models.ImageModel;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView homeImageView;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        homeImageView = itemView.findViewById(R.id.homeImageView);
    }

    public void setData(ImageModel imageModel) {
        Picasso.get().load(imageModel.getUrl()).into(homeImageView);
    }
}
