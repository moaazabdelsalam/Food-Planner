package com.project.foodplanner.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.GoogleSingInConfigs;
import com.project.foodplanner.model.MealsRepository;
import com.project.foodplanner.network.MealClient;
import com.project.foodplanner.register.presenter.RegisterPresenter;
import com.project.foodplanner.register.presenter.RegisterPresenterInterface;

public class RegisterFragment extends Fragment implements RegisterViewInterface {

    public static final String TAG = "TAG register fragment";
    TextInputLayout emailTxtInLayout;
    TextInputEditText emailTxtInET;
    TextInputLayout passwordTxtInLayout;
    TextInputEditText passwordTxtInET;
    TextInputLayout confirmPasswordTxtInLayout;
    TextInputEditText confirmPasswordTxtInET;
    TextView loginBtn;
    AppCompatButton registerBtn;
    AppCompatButton signInWithGoogleBtn;
    RegisterPresenterInterface presenter;
    private static final int RC_SIGN_IN = 74;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        loginBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigateUp();
        });
        emailTxtInET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!validateEmail(charSequence.toString())) {
                    //Log.i(TAG, "onTextChanged: invalid");
                    emailTxtInLayout.setError("must be \"example12@mail.com\", allowed symbols _&$-");
                } else {
                    //Log.i(TAG, "onTextChanged: valid");
                    emailTxtInLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordTxtInET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!validatePassword(charSequence.toString())) {
                    passwordTxtInLayout.setError("must have upper, lower character with length 6:10");
                } else {
                    passwordTxtInLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPasswordTxtInET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!validateConfirmPassword(charSequence.toString())) {
                    confirmPasswordTxtInLayout.setError("password doesn't match");
                } else {
                    confirmPasswordTxtInLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        registerBtn.setOnClickListener(view1 -> {
            if (checkAllFields(
                    emailTxtInET.getText().toString(),
                    passwordTxtInET.getText().toString(),
                    confirmPasswordTxtInET.getText().toString())
            ) {
                Log.i(TAG, "onViewCreated: registering user: " + emailTxtInET.getText().toString());
                presenter.registerUserWithEmailAndPass(emailTxtInET.getText().toString(), passwordTxtInET.getText().toString());
            } else {
                Toast.makeText(getContext(), "error, check you input data", Toast.LENGTH_SHORT).show();
            }

        });
        signInWithGoogleBtn.setOnClickListener(view1 -> {
            loginWithGoogle();
        });
    }

    private void initializeViews(View view) {
        emailTxtInLayout = view.findViewById(R.id.registerEmailTxtInLayout);
        emailTxtInET = view.findViewById(R.id.registerEmailTxtInET);
        passwordTxtInLayout = view.findViewById(R.id.registerPasswordTxtInLayout);
        passwordTxtInET = view.findViewById(R.id.registerPasswordTxtInET);
        confirmPasswordTxtInLayout = view.findViewById(R.id.confirmPasswordTxtInLayout);
        confirmPasswordTxtInET = view.findViewById(R.id.confirmPasswordTxtInET);
        loginBtn = view.findViewById(R.id.underlineLoginBtn);
        registerBtn = view.findViewById(R.id.registerBtn);
        signInWithGoogleBtn = view.findViewById(R.id.signInWithGoogleBtn);

        presenter = new RegisterPresenter(this, CloudRepo.getInstance(MealsRepository.getInstance(
                MealClient.getInstance(),
                ConcreteLocalSource.getInstance(getContext())
        )));
    }

    void loginWithGoogle() {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSingInConfigs.getInstance().getGso());
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "onActivityResult: sign in request");
            presenter.validateLoginWithGoogle(data);
        }
    }

    boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_&$-]+@[A-Za-z0-9_&$-]+\\.[A-Za-z]{2,}$";
//        String emailRegex = "^[A-Za-z0-9_-](\\S+)@(.+)(\\S+)\\.$";
//        String emailRegex = "(?i)^[A-Za-z0-9+_.-](?=\\S+$)@(.+)(?=\\S+$)$";
        return email.matches(emailRegex);
    }

    boolean validatePassword(String password) {
        String passwordRegex = "^[A-Za-z0-9].{6,10}$";
        return password.matches(passwordRegex);
    }

    boolean validateConfirmPassword(String confirmPassword) {
        return confirmPassword.equals(passwordTxtInET.getText().toString());
    }

    boolean checkAllFields(String email, String password, String confirmPassword) {
        return validateEmail(email) && validatePassword(password) && validateConfirmPassword(confirmPassword);
    }

    @Override
    public void registeredState(int status) {
        if (status == 1)
            requireActivity().finish();
        else
            Toast.makeText(getContext(), "something wrong happened please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String error) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}