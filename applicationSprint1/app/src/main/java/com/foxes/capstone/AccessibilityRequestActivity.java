package com.foxes.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Foxes 3/19/17
 */

public class AccessibilityRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_accessibility_required);

        Button give_permission_button = (Button)this.findViewById(R.id.accessibility_permission_button);
        give_permission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPermissionSettings();
            }
        });

    }



    public void openPermissionSettings(){

        startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 101);

    }

}
