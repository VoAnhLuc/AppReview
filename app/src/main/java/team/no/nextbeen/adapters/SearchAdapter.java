package team.no.nextbeen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.fragments.main.MainSearch;

public class SearchAdapter extends ArrayAdapter<MainSearch> {
    private Context ct;
    private ArrayList<MainSearch> arr;
    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<MainSearch> objects){
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.fragment_search, null);
        }
        if(arr.size()>0){
            MainSearch d = arr.get(position);
        }
        return convertView;
    }
}
