package team.no.nextbeen.fragments.auth;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import team.no.nextbeen.MainActivity;
import team.no.nextbeen.R;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = requireView().findViewById(R.id.editTextEmail);
        editTextPassword = requireView().findViewById(R.id.editTextPassword);

        Button buttonLogin = requireView().findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(view -> processLogin());

        TextView textViewRegister = requireView().findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(view -> replaceRegisterFragment());
    }

    private void processLogin() {
        String emailAddress = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            editTextEmail.setError(getString(R.string.auth_login_error_invalid_email));
            editTextEmail.requestFocus();
        }
        else if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.auth_login_error_invalid_password));
            editTextPassword.requestFocus();
        }
        else {
            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            showNotification(getString(R.string.auth_login_notification_success));
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            showNotification(getString(R.string.auth_login_notification_fail));
                        }
                    });
        }
    }

    private void replaceRegisterFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_auth, new RegisterFragment());
        fragmentTransaction.commit();
    }

    private void showNotification(String content) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show();
    }
}