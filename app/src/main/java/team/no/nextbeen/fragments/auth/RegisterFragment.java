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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import team.no.nextbeen.R;
import team.no.nextbeen.daos.DAOUser;
import team.no.nextbeen.models.UserModel;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextRePassword;

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

        mAuth = FirebaseAuth.getInstance();

        editTextFullName = requireView().findViewById(R.id.editTextFullName);
        editTextEmail = requireView().findViewById(R.id.editTextEmail);
        editTextPassword = requireView().findViewById(R.id.editTextPassword);
        editTextRePassword = requireView().findViewById(R.id.editTextRePassword);

        Button buttonRegister = requireView().findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(view -> processRegisterAccount());

        TextView textViewLogin = requireView().findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(view -> replaceToLoginFragment());
    }

    private void processRegisterAccount() {
        String fullName = editTextFullName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextRePassword.getText().toString();

        if (fullName.length() == 0) {
            editTextEmail.setError(getString(R.string.auth_register_error_empty_fullname));
            editTextEmail.requestFocus();
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.auth_login_error_invalid_email));
            editTextEmail.requestFocus();
        }
        else if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.auth_login_error_invalid_password));
            editTextPassword.requestFocus();
        }
        else if (!password.equals(rePassword)) {
            editTextPassword.setError(getString(R.string.auth_login_error_password_repassword_not_match));
            editTextPassword.requestFocus();
            editTextRePassword.setError(getString(R.string.auth_login_error_password_repassword_not_match));
            editTextRePassword.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                       if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                           UserModel user = new UserModel();
                           user.setEmail(email);
                           user.setId(mAuth.getCurrentUser().getUid());
                           user.setShortId(mAuth.getCurrentUser().getUid());
                           user.setBio(getString(R.string.app_desc));
                           user.setFullName(fullName);

                           DAOUser daoUser = new DAOUser();
                           daoUser.addUserAsync(user).addOnSuccessListener(requireActivity(), unused -> {
                               showNotification("Register successfully!");
                               replaceToLoginFragment();
                           });
                       }
                       else {
                            showNotification("Register failed! Please try again later.");
                       }
                    });
        }
    }

    private void replaceToLoginFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_auth, new LoginFragment());
        fragmentTransaction.commit();
    }

    private void showNotification(String content) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show();
    }
}