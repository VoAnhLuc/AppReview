package team.no.nextbeen.fragments.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.adapters.ImageAdapter;
import team.no.nextbeen.models.ImageModel;

public class HomeFragment extends Fragment {
    ViewPager2 homeViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        List<ImageModel> imageModels = new ArrayList<>();
        imageModels.add(new ImageModel("Marry me", "None", "https://i.imgur.com/9qtD36C.jpeg"));
        imageModels.add(new ImageModel("Wild plants", "", "https://i.imgur.com/wrfFdVL.jpeg"));
        imageModels.add(new ImageModel("The old house", "yeah sure, chắc chắn rồi homie okie con dề alogaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "https://i.imgur.com/zAXNEBD.jpeg"));

        homeViewPager = requireView().findViewById(R.id.homeViewPager);
        homeViewPager.setAdapter(new ImageAdapter(imageModels));
    }
}