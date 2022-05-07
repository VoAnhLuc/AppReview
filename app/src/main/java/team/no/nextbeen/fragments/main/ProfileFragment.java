package team.no.nextbeen.fragments.main;

import static team.no.nextbeen.MainActivity.RECYCLE_VIEW_PROFILE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.AuthorizationActivity;
import team.no.nextbeen.MainActivity;
import team.no.nextbeen.R;
import team.no.nextbeen.adapters.ReviewAdapter;
import team.no.nextbeen.daos.DAOReview;
import team.no.nextbeen.daos.DAOUser;
import team.no.nextbeen.viewmodels.ReviewViewModel;
import team.no.nextbeen.viewmodels.UserViewModel;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private UserViewModel user;
    private List<ReviewViewModel> reviews;

    private TextView txtUserName, txtUserFullName, txtUserPosts, txtUserFollowers, txtUserFollowing, txtUserBio;
    private ImageView imgUserAvatar;

    private ReviewAdapter reviewAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txtUserName = requireView().findViewById(R.id.txtUserName);
        txtUserFullName = requireView().findViewById(R.id.txtUserFullName);
        txtUserPosts = requireView().findViewById(R.id.txtUserPosts);
        txtUserFollowers = requireView().findViewById(R.id.txtUserFollowers);
        txtUserFollowing = requireView().findViewById(R.id.txtUserFollowing);
        txtUserBio = requireView().findViewById(R.id.txtUserBio);
        imgUserAvatar = requireView().findViewById(R.id.imgUserAvatar);

        ViewPager2 rcvPostList = requireView().findViewById(R.id.rcvPostList);
        rcvPostList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews, requireContext(), RECYCLE_VIEW_PROFILE);
        rcvPostList.setAdapter(reviewAdapter);

        ImageView btnLogout = requireView().findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            ((MainActivity)requireActivity()).replaceFragment(new ProfileFragment());
            Toast.makeText(requireContext(), "Logout successfully!", Toast.LENGTH_LONG).show();
        });

        if (mAuth.getCurrentUser() == null) {
            Button buttonAuthorize = requireView().findViewById(R.id.btnEditProfile);
            buttonAuthorize.setText(getString(R.string.auth_profile));
            buttonAuthorize.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), AuthorizationActivity.class);
                startActivity(intent);
            });
            rcvPostList.setVisibility(View.GONE);
            btnLogout.setVisibility(View.INVISIBLE);
            Picasso.get().load(getString(R.string.profile_default_avatar_url)).fit().centerCrop().into(imgUserAvatar);
        }
        else {
            DAOUser daoUser = new DAOUser();
            daoUser.getUserInforByUserIdAsync(mAuth.getCurrentUser().getUid())
                    .addOnSuccessListener(dataSnapshot -> {
                        user = dataSnapshot.getValue(UserViewModel.class);
                        if (user != null) {
                            txtUserName.setText(user.getShortId().substring(Math.min(user.getShortId().length(), 15)));
                            txtUserFullName.setText(user.getFullName());
                            txtUserBio.setText(user.getBio());

                            String avatarURL = user.getAvatar() == null ? getString(R.string.profile_default_avatar_url) : user.getAvatar();
                            Picasso.get().load(avatarURL).fit().centerCrop().into(imgUserAvatar);

                            getListPostFromRealTimeDatabase();
                        }
                    });
        }
    }
    private void getListPostFromRealTimeDatabase()
    {
        DAOReview daoReview = new DAOReview();
        daoReview.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ReviewViewModel review = dataSnapshot.getValue(ReviewViewModel.class);
                    if (review != null && user.getId() != null
                            && review.getAuthor().equals(user.getId())) {
                        reviews.add(review);
                        reviewAdapter.notifyDataSetChanged();
                        txtUserPosts.setText(String.valueOf(reviews.size()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}