package team.no.nextbeen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import team.no.nextbeen.R;

public class ImageAdapter extends BaseAdapter {
    private final List<String> images;
    private final Context context;

    public ImageAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images != null ? images.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
        }
        ImageView imageView = view.findViewById(R.id.myImage);
        Picasso.get().load(images.get(i)).fit().centerCrop().into(imageView);
        return view;
    }
}
