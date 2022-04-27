package team.no.nextbeen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private final List<ReviewViewModel> reviewViewModels;

    public ReviewAdapter(List<ReviewViewModel> reviewViewModels) {
        this.reviewViewModels = reviewViewModels;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.setData(reviewViewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewViewModels == null ? 0 : reviewViewModels.size();
    }
}
