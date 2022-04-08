package team.no.nextbeen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.models.ImageModel;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private final List<ImageModel> imageModels;

    public ImageAdapter(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setData(imageModels.get(position));
    }

    @Override
    public int getItemCount() {
        return imageModels == null ? 0 : imageModels.size();
    }
}
