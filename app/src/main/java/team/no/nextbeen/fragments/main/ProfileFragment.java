package team.no.nextbeen.fragments.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import team.no.nextbeen.AuthorizationActivity;
import team.no.nextbeen.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button buttonAuthorize = requireView().findViewById(R.id.buttonAuthorize);
        buttonAuthorize.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), AuthorizationActivity.class);
            startActivity(intent);
        });
    }
}