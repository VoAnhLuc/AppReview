package team.no.nextbeen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import team.no.nextbeen.adapters.ImageAdapter;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(view -> {
            finish();
        });

        Intent intent = getIntent();
        ReviewViewModel reviewViewModel = (ReviewViewModel)intent.getSerializableExtra("REVIEW");

        ImageAdapter imageAdapter = new ImageAdapter(reviewViewModel.getImages(), getApplicationContext());

        GridView gvImages = findViewById(R.id.gvImages);
        gvImages.setAdapter(imageAdapter);
        gvImages.setOnItemClickListener((adapterView, view, i, l) -> {
            // handle show image later
        });

        FrameLayout frameLayout = findViewById(R.id.flHeader);
        frameLayout.bringToFront();

        TextView txtUserFullName = findViewById(R.id.txtUserFullName);
        TextView txtContentReview = findViewById(R.id.txtContentReview);
        TextView txtLocation = findViewById(R.id.txtLocation);
        txtUserFullName.setText(reviewViewModel.getFullName());
        txtContentReview.setText(reviewViewModel.getContent());
        txtLocation.setText(reviewViewModel.getAddress());
    }
}