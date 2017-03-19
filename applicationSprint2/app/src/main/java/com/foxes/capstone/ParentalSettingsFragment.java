package com.foxes.capstone;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;



public class ParentalSettingsFragment extends Fragment {

    private static final String TAG = "ParentalSettingsFragment";

    private MainActivity mActivity;

    //@BindView(R.id.button_new_user)       Button button_new_user;
    @BindView(R.id.button_edit_users)     Button button_edit_users;
    @BindView(R.id.button_edit_applist)   Button button_edit_applist;
    //@BindView(R.id.button_delete_appdata) Button button_delete_appdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mActivity = (MainActivity)getActivity();

        View view = inflater.inflate(R.layout.fragment_parental_settings, container, false);
        ButterKnife.bind(this, view);

        button_edit_applist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new BlockedApplicationsFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, newFragment)
                        .addToBackStack(TAG)
                        .commit();

            }
        });

        return view;
    }


}
