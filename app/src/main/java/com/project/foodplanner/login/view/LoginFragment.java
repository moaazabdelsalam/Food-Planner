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

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodplanner.R;
import com.project.foodplanner.login.presenter.LoginPresenter;
import com.project.foodplanner.login.presenter.LoginPresenterInterface;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.User;

public class LoginFragment extends Fragment implements LoginViewInterface {
    public static final String TAG = "TAG login fragment";
    TextInputLayout emailTxtInLayout;
    TextInputEditText emailTxtInET;
    TextInputLayout passwordTxtInLayout;
    TextInputEditText passwordTxtInET;
    TextView registerBtn;
    AppCompatButton loginBtn;
    AppCompatButton loginWithGoogleBtn;
    LoginPresenterInterface presenter;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 74;
    GoogleSignInOptions gso;

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

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        //Log.i(TAG, "onViewCreated: current user: " + mAuth.getCurrentUser().getEmail());
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

        loginWithGoogleBtn.setOnClickListener(view1 -> {
            Log.i(TAG, "onViewCreated: login button clicked");
            loginWithGoogle();
        });
    }

    private void initializeViews(View view) {
        emailTxtInLayout = view.findViewById(R.id.loginEmailTxtInLayout);
        emailTxtInET = view.findViewById(R.id.loginEmailTxtInET);
        passwordTxtInLayout = view.findViewById(R.id.loginPasswordTxtInLayout);
        passwordTxtInET = view.findViewById(R.id.loginPasswordTxtInET);
        loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn = view.findViewById(R.id.underlineRegisterBtn);
        loginWithGoogleBtn = view.findViewById(R.id.loginWithGoogleBtn);

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

    void loginWithGoogle() {
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "onActivityResult: sign in request");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, "onActivityResult: " + account.getIdToken());
                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {
                Log.i(TAG, "onActivityResult: exception ");
                e.printStackTrace();
                // ...
            }
        }
    }

    private void firebaseAuth(String idToken) {
        Log.i(TAG, "firebaseAuth: ");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User users = new User();
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);

                            Log.i(TAG, "onComplete: logged in with google with: " + user.getEmail());
                        } else {
                            Log.i(TAG, "onComplete: logged in failed: " + task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

}