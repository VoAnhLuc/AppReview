package team.no.nextbeen.fragments.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.AuthorizationActivity;
import team.no.nextbeen.R;
import team.no.nextbeen.adapters.PostAdapter;
import team.no.nextbeen.models.Post;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView rcvPostList;
    private List<Post> posts;
    private PostAdapter postAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        rcvPostList=view.findViewById(R.id.rcvPostList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rcvPostList.setLayoutManager(linearLayoutManager);
        posts=new ArrayList<>();
        postAdapter=new PostAdapter(posts);
        rcvPostList.setAdapter(postAdapter);
        getListPostFromRealTimeDatabase();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            Button buttonAuthorize = requireView().findViewById(R.id.buttonAuthorize);
            buttonAuthorize.setVisibility(View.VISIBLE);
            buttonAuthorize.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), AuthorizationActivity.class);
                startActivity(intent);
            });
        }
    }
    private void getListPostFromRealTimeDatabase()
    {
        FirebaseDatabase database=FirebaseDatabase.getInstance("https://nextbeen-notech-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference=database.getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Post post=dataSnapshot.getValue(Post.class);
                    posts.add(post);
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}