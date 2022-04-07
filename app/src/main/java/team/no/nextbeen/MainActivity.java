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

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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


    private void processSelectFragment(int itemId) {
        if (itemId == R.id.menu_home) {
            replaceFragment(new HomeFragment());
        }
        else if (itemId == R.id.menu_search) {
            replaceFragment(new SearchFragment());
        }
        else if (itemId == R.id.menu_post) {
            replaceFragment(new PostFragment());
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
}