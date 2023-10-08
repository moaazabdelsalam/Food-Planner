package com.project.foodplanner.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.foodplanner.LoginActivity;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.GoogleSingInConfigs;
import com.project.foodplanner.model.MealsRepository;
import com.project.foodplanner.model.NotLoggedInMessage;
import com.project.foodplanner.network.MealClient;
import com.project.foodplanner.profile.presenter.ProfilePresenter;
import com.project.foodplanner.profile.presenter.ProfilePresenterInterface;

public class ProfileFragment extends Fragment implements ProfileViewInterface {

    AppCompatButton logoutBtn;
    ProfilePresenterInterface presenter;
    View _view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _view = view;
        initializeViews(view);

        logoutBtn.setOnClickListener(view1 -> presenter.logoutUser());
    }

    private void initializeViews(View view) {
        logoutBtn = view.findViewById(R.id.logoutBtn);

        presenter = new ProfilePresenter(this, CloudRepo.getInstance(MealsRepository.getInstance(MealClient.getInstance(), ConcreteLocalSource.getInstance(requireContext()))));
    }

    @Override
    public void logoutMessage(int status) {
        if (status == 1) {
            Navigation.findNavController(_view).navigateUp();
            GoogleSignIn.getClient(requireContext(), GoogleSingInConfigs.getInstance().getGso()).signOut();
        } else
            Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotLoggedInMessage() {
        NotLoggedInMessage.showNotLoggedInDialogue(getContext(), _view);
    }
}