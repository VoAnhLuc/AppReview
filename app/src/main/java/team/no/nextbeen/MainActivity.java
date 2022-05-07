package team.no.nextbeen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import team.no.nextbeen.databinding.ActivityMainBinding;
import team.no.nextbeen.fragments.main.HomeFragment;
import team.no.nextbeen.fragments.main.NotificationFragment;
import team.no.nextbeen.fragments.main.PostFragment;
import team.no.nextbeen.fragments.main.ProfileFragment;
import team.no.nextbeen.fragments.main.SearchFragment;

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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}