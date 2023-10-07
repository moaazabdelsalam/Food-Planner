package com.project.foodplanner.login.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.foodplanner.MainActivity;
import com.project.foodplanner.R;
import com.project.foodplanner.login.presenter.LoginPresenter;
import com.project.foodplanner.login.presenter.LoginPresenterInterface;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.register.presenter.RegisterPresenter;
import com.project.foodplanner.register.presenter.RegisterPresenterInterface;

public class LoginFragment extends Fragment implements LoginViewInterface {
    public static final String TAG = "TAG login fragment";
    TextInputLayout emailTxtInLayout;
    TextInputEditText emailTxtInET;
    TextInputLayout passwordTxtInLayout;
    TextInputEditText passwordTxtInET;
    TextView registerBtn;
    AppCompatButton loginBtn;
    LoginPresenterInterface presenter;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

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

        registerBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        loginBtn.setOnClickListener(view1 -> {
            if (checkAllFields(
                    emailTxtInET.getText().toString(),
                    passwordTxtInET.getText().toString())
            ) {
                Log.i(TAG, "onViewCreated: registering user: " + emailTxtInET.getText().toString());
                presenter.loginWithEmailAndPass(emailTxtInET.getText().toString(), passwordTxtInET.getText().toString());
            } else {
                Toast.makeText(getContext(), "error, check you input data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews(View view) {
        emailTxtInLayout = view.findViewById(R.id.loginEmailTxtInLayout);
        emailTxtInET = view.findViewById(R.id.loginEmailTxtInET);
        passwordTxtInLayout = view.findViewById(R.id.loginPasswordTxtInLayout);
        passwordTxtInET = view.findViewById(R.id.loginPasswordTxtInET);
        loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn = view.findViewById(R.id.underlineRegisterBtn);

        presenter = new LoginPresenter(this, CloudRepo.getInstance());
    }

    boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_&$-]+@[A-Za-z0-9_&$-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    boolean validatePassword(String password) {
        String passwordRegex = "^[A-Za-z0-9].{6,10}$";
        return password.matches(passwordRegex);
    }

    boolean checkAllFields(String email, String password) {
        return validateEmail(email) && validatePassword(password);
    }

    @Override
    public void loginStatus(int status) {
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