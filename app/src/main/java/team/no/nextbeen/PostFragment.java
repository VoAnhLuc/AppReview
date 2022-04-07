package team.no.nextbeen;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostFragment extends Fragment {

    private String currentAddress;
    private LocationManager locationManager;

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

        if (getView() != null) {
            TextView textView = getView().findViewById(R.id.testlocal);
            textView.setText(currentAddress);
        }
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
}