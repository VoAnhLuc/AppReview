package team.no.nextbeen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.adapters.ImageAdapter;
import team.no.nextbeen.databinding.ActivityMainBinding;
import team.no.nextbeen.models.ImageModel;

public class    MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ViewPager2 homeViewPager;

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