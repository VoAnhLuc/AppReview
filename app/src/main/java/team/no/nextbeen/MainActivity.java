package team.no.nextbeen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import team.no.nextbeen.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    ActivityMainBinding binding;
    private String currentAddress;
    private final Integer MIN_TIME_MS = 1000;
    private final Integer MIN_DISTANCE = 5;

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkingPermission();
        getCurrentLocation();
    }

    private void checkingPermission() {
        // check location is turn on or off
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                new AlertDialog.Builder(MainActivity.this)
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    void getCurrentLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_MS, MIN_DISTANCE, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentAddress = getAddressFromCoordinate(location);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
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
            bundle.putString("CURRENT_ADDRESS", currentAddress);

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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private String getAddressFromCoordinate(Location coordinate) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addressList.size() > 0) {
                return  addressList.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}