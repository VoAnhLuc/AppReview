package team.no.nextbeen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import team.no.nextbeen.adapters.ImageAdapter;
import team.no.nextbeen.viewmodels.ReviewViewModel;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        txtUserFullName.setText(reviewViewModel.getFullName());
        txtContentReview.setText(reviewViewModel.getContent());
    }
}