package team.no.nextbeen;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.LayoutInflater;
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

public class PostFragment extends Fragment {

    private String currentAddress;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentAddress = getArguments().getString("CURRENT_ADDRESS", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        checkingPermission();
        getCurrentLocation();

        imageView = getActivity().findViewById(R.id.postPhoto);
        button = getActivity().findViewById(R.id.postButton);
        editTextTitle = getActivity().findViewById(R.id.postTitle);
        editTextContent = getActivity().findViewById(R.id.postContent);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fDatabase = FirebaseDatabase.getInstance();

        dRef = fDatabase.getReference().child("Post");
        dRef.child("Title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    editTextTitle.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dRef.child("Content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    editTextContent.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

//        if (getView() != null) {
//            TextView textView = getView().findViewById(R.id.postTitle);
//            textView.setText(currentAddress);
//        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getAddressFromCoordinate(Location coordinate) {
        if (coordinate == null || !isAdded()) {
            return "";
        }

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addressList.size() > 0) {
                return  addressList.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            return "";
        }

        return "";
    }

    void getCurrentLocation() {
        try {
            currentAddress = getAddressFromCoordinate(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            int MIN_TIME_MS = 1000;
            int MIN_DISTANCE = 5;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_MS, MIN_DISTANCE, location -> {
                currentAddress = getAddressFromCoordinate(location);
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void checkingPermission() {
        // check location is turn on or off
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (paramDialogInterface, paramInt) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        // check permission access location
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(requireActivity(), new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = editTextTitle.getText().toString();
                    String content  = editTextContent.getText().toString();

                    dRef.child("Title").setValue(title);
                    dRef.child("Content").setValue(content);
                    uploadPicture();
                }
            });

        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("image/" + randomkey);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Imamge Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext().getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int)progressPercent + "%");
                    }
                });
    }
}