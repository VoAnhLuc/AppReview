package team.no.nextbeen.fragments.main;

import static team.no.nextbeen.MainActivity.RECYCLE_VIEW_HOME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.adapters.ReviewAdapter;
import team.no.nextbeen.daos.DAOReview;
import team.no.nextbeen.daos.DAOUser;
import team.no.nextbeen.models.UserModel;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class HomeFragment extends Fragment {
    ViewPager2 homeViewPager;
    private List<ReviewViewModel> reviews;
    private ReviewAdapter reviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        homeViewPager = requireView().findViewById(R.id.homeViewPager);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews, getContext(), RECYCLE_VIEW_HOME);
        homeViewPager.setAdapter(reviewAdapter);

        DAOReview daoReview = new DAOReview();
        daoReview.getDatabaseReference().addValueEventListener(dataEventListener());

    }

    private ValueEventListener dataEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ReviewViewModel reviewViewModel = ds.getValue(ReviewViewModel.class);
                    if (reviewViewModel != null) {
                        reviewViewModel.setReviewId(ds.getKey());
                        reviews.add(reviewViewModel);
                        reviewAdapter.notifyDataSetChanged();
                    }
                }

                if (reviews != null) {
                    for (ReviewViewModel review : reviews) {
                        DAOUser daoUser = new DAOUser();
                        daoUser.getUserInforByUserIdAsync(review.getAuthor())
                                .addOnSuccessListener(dataSnapshot -> {
                                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                    if (userModel != null) {
                                        review.setShortId(userModel.getShortId());
                                        review.setFullName(userModel.getFullName());
                                        reviewAdapter.notifyDataSetChanged();
                                    }
                                });
                    }

                    Collections.shuffle(reviews);
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                reviews = new ArrayList<>();
            }
        };
    }
}