package team.no.nextbeen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import team.no.nextbeen.databinding.ActivityMainBinding;
import team.no.nextbeen.fragments.main.HomeFragment;
import team.no.nextbeen.fragments.main.NotificationFragment;
import team.no.nextbeen.fragments.main.PostFragment;
import team.no.nextbeen.fragments.main.ProfileFragment;
import team.no.nextbeen.fragments.main.SearchFragment;

public class MainActivity extends AppCompatActivity {

    public static final Integer RECYCLE_VIEW_HOME = 0; // Home frag
    public static final Integer RECYCLE_VIEW_PROFILE = 1; // Profile frag
    public static final Integer RECYCLE_VIEW_POST = 2; // Post frag


    ActivityMainBinding binding;
    private LocationManager locationManager;
    private String currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            processSelectFragment(item.getItemId());
            return true;
        });

        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        checkingPermission();
        getCurrentLocation();
    }

    private void processSelectFragment(int itemId) {
        if (itemId == R.id.menu_home) {
            replaceFragment(new HomeFragment());
        }
        else if (itemId == R.id.menu_search) {
            replaceFragment(new SearchFragment());
        }
        else if (itemId == R.id.menu_post) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.backend_post_current_address), currentAddress);
            PostFragment postFragment = new PostFragment();
            postFragment.setArguments(bundle);
            replaceFragment(postFragment);
        }
        else if (itemId == R.id.menu_notification) {
            replaceFragment(new NotificationFragment());
        }
        else { // menu_profile
            replaceFragment(new ProfileFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private String getAddressFromCoordinate(Location coordinate) {
        if (coordinate == null) {
            return "";
        }

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (paramDialogInterface, paramInt) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        // check permission access location
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}