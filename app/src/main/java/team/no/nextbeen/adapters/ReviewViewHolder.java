package team.no.nextbeen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import team.no.nextbeen.DetailActivity;
import team.no.nextbeen.R;
import team.no.nextbeen.models.ImageModel;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private final ImageView homeImageView;
    private final TextView homeImageTitle, homeImageDesc;
    private final LinearLayout homeLinearLayout;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        homeImageView = itemView.findViewById(R.id.homeImageView);
        homeImageTitle = itemView.findViewById(R.id.homeImageTitle);
        homeImageDesc = itemView.findViewById(R.id.homeImageDesc);

        homeLinearLayout = itemView.findViewById(R.id.homeLinearLayout);
        homeLinearLayout.bringToFront();
    }

    public void setData(ReviewViewModel reviewViewModel, Context context, Integer type) {
        if (type == 0) {
            Picasso.get().load(reviewViewModel.getImages().get(0)).into(homeImageView);
            homeImageTitle.setVisibility(View.VISIBLE);
        }
        else if (type == 1) {
            Picasso.get().load(reviewViewModel.getImages().get(0)).fit().centerCrop().into(homeImageView);
            homeImageTitle.setVisibility(View.GONE);
        }

        homeImageTitle.setText(reviewViewModel.getFullName());
        homeImageDesc.setText(reviewViewModel.getShortDesc(40));

        if (reviewViewModel.getContent().isEmpty()) {
            homeImageDesc.setVisibility(View.GONE);
        }

        homeImageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REVIEW", reviewViewModel);
            context.startActivity(intent);
        });
    }
}
