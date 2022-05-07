package team.no.nextbeen.fragments.main;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.MainActivity;
import team.no.nextbeen.R;
import team.no.nextbeen.adapters.ImageAdapter;
import team.no.nextbeen.daos.DAOPhoto;
import team.no.nextbeen.daos.DAOReview;
import team.no.nextbeen.models.ReviewModel;

public class PostFragment extends Fragment {

    private static final String REVIEW_SHARED_PREF = "REVIEW_SHARED_PREF";
    private static final String CONTENT_REVIEW = "CONTENT_REVIEW";

    private FirebaseAuth mAuth;

    public String currentAddress;

    private SharedPreferences sharedPref;
    private EditText txtReviewContent;
    private EditText txtReviewAddress;
    private ImageAdapter imageAdapter;

    private List<String> photos = new ArrayList<>();

    private final ActivityResultLauncher<Intent> photoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null && intent.getClipData() != null) {
                        int sizeOfClipData = intent.getClipData().getItemCount();
                        int pos = 0;
                        while (pos < sizeOfClipData) {
                            Uri uri = intent.getClipData().getItemAt(pos).getUri();
                            photos.add(uri.toString());
                            pos++;
                        }

                        imageAdapter.notifyDataSetChanged();
                        savePhotosToSharePref();
                    }
                }
            });;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

        GridView gvImages = requireView().findViewById(R.id.gvImages);
        String json = sharedPref.getString("PHOTOS", "");
        photos = new Gson().fromJson(json, new TypeToken<List<String>>() {}.getType());
        if (photos == null) {
            photos = new ArrayList<>();
        }
        imageAdapter = new ImageAdapter(photos, requireContext());
        gvImages.setAdapter(imageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(requireContext(), "Please login to share your feeling!", Toast.LENGTH_LONG).show();
            ((MainActivity)requireActivity()).replaceFragment(new ProfileFragment());
        }

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
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoActivityResultLauncher.launch(intent);
        });

        Button btnPostReview = requireView().findViewById(R.id.btnPostReview);
        btnPostReview.setOnClickListener(view -> {
            List<String> images = new ArrayList<>();
            DAOPhoto daoPhoto = new DAOPhoto();

            if (photos == null || photos.size() == 0) {
                Toast.makeText(getContext(), "Please select at least one image to post review", Toast.LENGTH_LONG).show();
            }
            else {
                for (String photo : photos) {

                    Uri uri = Uri.parse(photo);
                    String fileName =  System.currentTimeMillis() + "_" + getFileName(requireContext().getContentResolver(), uri);

                    daoPhoto.uploadPhotoAsync(uri, fileName).addOnSuccessListener(taskSnapshot -> {
                        daoPhoto.getPhotoURL(fileName).addOnSuccessListener(uri1 -> {
                            images.add(uri1.toString());

                            if (images.size() == photos.size()) {
                                String address = txtReviewAddress.getText().toString();
                                String content = txtReviewContent.getText().toString();
                                ReviewModel reviewModel = new ReviewModel(mAuth.getUid(), content, address, images);
                                DAOReview daoReview = new DAOReview();
                                daoReview.addReviewAsync(reviewModel).addOnSuccessListener(unused -> {
                                    sharedPref.edit().clear().apply();
                                    Toast.makeText(getContext(), "Your review is on air now!", Toast.LENGTH_LONG).show();
                                    ((MainActivity)requireActivity()).replaceFragment(new HomeFragment());
                                });
                            }
                        });
                    });
                }
            }
        });
    }

    private void saveContentReviewToSharedPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CONTENT_REVIEW", txtReviewContent.getText().toString());
        editor.apply();
    }

    private void savePhotosToSharePref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("PHOTOS", new Gson().toJson(photos));
        editor.apply();
    }

    private String getFileName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}