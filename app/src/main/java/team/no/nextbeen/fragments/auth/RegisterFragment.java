package team.no.nextbeen.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import team.no.nextbeen.R;

public class RegisterFragment extends Fragment {

    private EditText editTextEmail, editTextPassword, editTextRePassword;
    private Button buttonRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        editTextEmail = requireView().findViewById(R.id.editTextEmail);
        editTextPassword = requireView().findViewById(R.id.editTextPassword);
        editTextRePassword = requireView().findViewById(R.id.editTextRePassword);
        buttonRegister = requireView().findViewById(R.id.buttonRegister);

        TextView textViewLogin = requireView().findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(view -> replaceRegisterFragment());
    }

    private void replaceRegisterFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_auth, new LoginFragment());
        fragmentTransaction.commit();
    }
}