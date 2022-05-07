package team.no.nextbeen;

import static team.no.nextbeen.MainActivity.RECYCLE_VIEW_HOME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import team.no.nextbeen.adapters.ReviewAdapter;
import team.no.nextbeen.daos.DAOReview;
import team.no.nextbeen.daos.DAOUser;
import team.no.nextbeen.models.UserModel;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class SearchResultActivity extends AppCompatActivity {

    private List<ReviewViewModel> reviews;
    private ViewPager2 vpResult;
    private String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        district = intent.getStringExtra("DISTRICT");

        LinearLayout llSearchResult = findViewById(R.id.llSearchResult);
        llSearchResult.bringToFront();

        TextView txtDistrict = findViewById(R.id.txtDistrict);
        txtDistrict.setText(district);

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(view -> finish());

        vpResult = findViewById(R.id.vpResult);
        DAOReview daoReview = new DAOReview();
        daoReview.getDatabaseReference().addValueEventListener(dataEventListener());
    }

    private ValueEventListener dataEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviews = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ReviewViewModel reviewViewModel = ds.getValue(ReviewViewModel.class);
                    if (reviewViewModel != null && reviewViewModel.getAddress().toLowerCase(Locale.ROOT).contains(district.toLowerCase(Locale.ROOT))) {
                        reviewViewModel.setReviewId(ds.getKey());
                        reviews.add(reviewViewModel);
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
                                    }
                                });
                    }

                    Collections.shuffle(reviews);
                    vpResult.setAdapter(new ReviewAdapter(reviews, getApplicationContext(), RECYCLE_VIEW_HOME));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                reviews = new ArrayList<>();
            }
        };
    }
}