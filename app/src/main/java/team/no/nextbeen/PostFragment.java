package team.no.nextbeen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PostFragment extends Fragment {

    private String currentAddress;

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

        if (getView() != null) {
            TextView textView = getView().findViewById(R.id.testlocal);
            textView.setText(currentAddress);
        }
    }
}