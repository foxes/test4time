package com.foxes.capstone;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * Foxes 3/19/2017
 */

public class AccessibilityPermissionFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_accessibility_required, container, false);

        Button give_permission_button = (Button)view.findViewById(R.id.accessibility_permission_button);
        give_permission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPermissionSettings();
            }
        });

        return view;

    }



    public void openPermissionSettings(){

        startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 101);

    }

}
