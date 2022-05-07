package team.no.nextbeen.fragments.main;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import team.no.nextbeen.MainActivity;
import team.no.nextbeen.R;

public class PostFragment extends Fragment {

    private static final String REVIEW_SHARED_PREF = "REVIEW_SHARED_PREF";
    private static final String CONTENT_REVIEW = "CONTENT_REVIEW";
    public String currentAddress;
    private LocationManager locationManager;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public Uri imageUri;
    private Button button;
    private EditText editTextTitle;
    private EditText editTextContent;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;

    private SharedPreferences sharedPref;
    private EditText txtReviewContent;
    private EditText txtReviewAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = requireContext().getSharedPreferences(REVIEW_SHARED_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null) {
            currentAddress = getArguments().getString("CURRENT_ADDRESS", "");
            txtReviewAddress.setText(currentAddress);
            txtReviewContent.setText(sharedPref.getString(CONTENT_REVIEW, ""));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        txtReviewAddress = requireView().findViewById(R.id.txtReviewAddress);

        txtReviewContent = requireView().findViewById(R.id.txtReviewContent);
        txtReviewContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveContentReviewToSharedPref();
            }
        });

        ImageView btnAddPhoto = requireView().findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(view -> {

        });
    }

    private void saveContentReviewToSharedPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CONTENT_REVIEW", txtReviewContent.getText().toString());
        editor.apply();
    }
}