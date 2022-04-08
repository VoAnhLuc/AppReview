package team.no.nextbeen.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import team.no.nextbeen.R;
import team.no.nextbeen.models.ImageModel;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView homeImageView;
    private final TextView homeImageTitle, homeImageDesc;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        homeImageView = itemView.findViewById(R.id.homeImageView);
        homeImageTitle = itemView.findViewById(R.id.homeImageTitle);
        homeImageDesc = itemView.findViewById(R.id.homeImageDesc);

        LinearLayout homeLinearLayout = itemView.findViewById(R.id.homeLinearLayout);
        homeLinearLayout.bringToFront();
    }

    public void setData(ImageModel imageModel) {
        Picasso.get().load(imageModel.getUrl()).into(homeImageView);
        homeImageTitle.setText(imageModel.getTitle());
        homeImageDesc.setText(imageModel.getDesc());

        if (imageModel.getDesc().isEmpty()) {
            homeImageDesc.setVisibility(View.GONE);
        }
    }
}
