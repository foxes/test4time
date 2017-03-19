package edu.gvsu.cis.buncha.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/*a demo app to work on app locking features */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*a button calls this via xml on click*/
public void launchAppLocker(View v){
    //Log.d("MainActivity", "oncreate running, before launching service");
    Intent intent = new Intent(this, LockingService.class);
    this.startService(intent);
}

    /*a button calls this via xml on click*/
    public void launchWhiteList(View v){
        //Log.d("MainActivity", "oncreate running, before launching service");
        Intent intent = new Intent(this, WhiteListView.class);
        this.startActivity(intent);
    }


}
