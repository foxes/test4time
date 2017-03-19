package com.foxes.capstone;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity {





    //this is the set stepGoal by the parents
    static int stepGoal = 1500;

    //this is the current steps that the kid is on, should pull from fitbit API when possible
    static int stepCounter = 0;


    //this is the time that we'll disable the lock for, if we decide on a default time it could do stuff like unlocking for 60 minutes once step goal is reached
    //but right now it should just unlock indefinetly until the lock is reset by the parent
    static int lockDisableTime;


    //these are just for demoing prior to getting access to fitbit API, so delete them later
    int stepCounting;

    int sliderPercent;

    public static final String OUT_OF_TIME = "outoftime";

    //for setting if lock is on or off
    static boolean lockOn = true;
    static String LockString = "LOCKED";

    Button buttonTimer;
    Button buttonLock;
    Button buttonUnLock;
    Button buttonRefresh;

    boolean accessibilityEnabled = false;

    public static TextView lockStatus;
    public TextView middleText;
    public TextView upperText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //pulling variables from sharedPreferences. basically memory
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        stepGoal = preferences.getInt("stepGoal",stepGoal);
        stepCounter = preferences.getInt("stepCounter",stepCounter);
        sliderPercent = preferences.getInt("sliderPercent",sliderPercent);
        stepCounting = preferences.getInt("stepCounting",stepCounting);
        lockOn = preferences.getBoolean("lockOn",lockOn);
        LockString = preferences.getString("LockString",LockString);



        final CircleProgressBar circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        final TextView lowerText = (TextView) findViewById(R.id.lowerText);
        lockStatus = (TextView) findViewById(R.id.lockStatus);
        final TextView middleText = (TextView) findViewById(R.id.middleText);
        final TextView upperText = (TextView) findViewById(R.id.upperText);
        //final TextView lowerText = (TextView) findViewById(R.id.lowerText);
        buttonTimer = (Button) findViewById(R.id.TimerButton);
        buttonLock = (Button) findViewById(R.id.LockButton);
        buttonUnLock = (Button) findViewById(R.id.UnlockButton);
        buttonRefresh = (Button) findViewById(R.id.refreshButton);



        //this is what makes the prompt for accessibility at the beginning, right now itll appear
        //everytime it starts, but can change that once we find a better spot
        if (!accessibilityEnabled) {

            Intent dialogIntent = new Intent(this, AccessibilityRequestActivity.class);
            dialogIntent.putExtra(MainActivity.OUT_OF_TIME, true);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

            accessibilityEnabled = true;

        }


        //to refresh the values after you close the app
        middleText.setText("" + stepCounting);
        circleProgressBar.setProgressWithAnimation(sliderPercent);
        lowerText.setText("" + sliderPercent + "%");
        lockStatus.setText("" + LockString);




        //this is all code for the timer button button/////////////////////////////////////////////////
        buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mEmail.getText().toString().isEmpty()) {

                            Toast.makeText(MainActivity.this, "Admin privileges granted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            final View mView = getLayoutInflater().inflate(R.layout.timerdialog, null);
                            final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                            Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();
                            // final String unlockTime = mEmail.getText().toString();


                            //this is all code for the timer dialog menu "confirm". in the future we can put the "lock" functionality here hopefully.
                            mLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Toast.makeText(MainActivity.this, "lock disabled for " + mEmail.getText().toString() + " minutes", Toast.LENGTH_LONG).show();

                                    lockDisableTime = Integer.parseInt( mEmail.getText().toString() );

                                    //starting the notification "alarm", this is the only function that uses this currently but it could be
                                    //used on other ones later
                                    startAlarm(true, false);

                                    lockOn = false;
                                    LockString = "UNLOCKED";
                                    lockStatus.setText(""+LockString);
                                    dialog.dismiss();

                                }
                            });



                        } else {
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////

        //this is all code for the lock button button/////////////////////////////////////////////////
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mEmail.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Admin privileges granted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            final View mView = getLayoutInflater().inflate(R.layout.stepquestiondialog, null);
                            final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                            Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();


                            //this is the action listener for the inner-menu on the lock. .

                            mLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    stepGoal = Integer.parseInt(mEmail.getText().toString());
                                    editor.putInt("stepGoal",stepGoal);
                                    editor.putInt("stepCounter",stepCounter);
                                    editor.putInt("sliderPercent",sliderPercent);
                                    editor.putInt("stepCounting",stepCounting);
                                    editor.putBoolean("lockOn",lockOn);
                                    editor.putString("LockString",LockString);
                                    editor.apply();

                                    lowerText.setText("" + 0 + "%");
                                    middleText.setText("" + stepGoal);
                                    circleProgressBar.setProgressWithAnimation(0);
                                    stepCounting = 0;
                                    stepCounter = 0;


                                    Toast.makeText(MainActivity.this, "step goal updated.", Toast.LENGTH_LONG).show();

                                    LockApps();
                                    //lockStatus.setText("LOCKED");
                                    dialog.dismiss();


                                }
                            });

                            //this will call an activity with the list of apps

                        } else {
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
        //this is all code for the unlock button button/////////////////////////////////////////////////
        buttonUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mEmail.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Admin privileges granted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            final View mView = getLayoutInflater().inflate(R.layout.confirmdialog, null);
                            final EditText mEmail = (EditText) mView.findViewById(R.id.etPassword);
                            Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();


                            //this is the action listener for the inner-menu on the unlock. it SHOULD NOT bring up list of apps. ONLY the "lock" trigger should do that.

                            mLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Toast.makeText(MainActivity.this, "lock disabled. Use lock to reactivate.", Toast.LENGTH_LONG).show();
                                    lockOn = false;
                                    LockString = "UNLOCKED";
                                    lockStatus.setText(""+LockString);
                                    dialog.dismiss();

                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////

        //this is all code for the Refresh button button/////////////////////////////////////////////////
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //this stuff is just for demoing since we dont have the api ready to pull yet
                Random rand = new Random();
                int n = rand.nextInt(5);

                stepCounter = stepCounter + n;
                stepCounting = stepGoal - stepCounter;

                if (stepCounter < stepGoal) {

                    stepCounting = stepGoal - stepCounter;
                    middleText.setText("" + stepCounting);
                    sliderPercent = ( (int)(stepCounter / (stepGoal*.01) ) );
                    circleProgressBar.setProgressWithAnimation(sliderPercent);
                    lowerText.setText("" + sliderPercent + "%");

                    lockOn = true;
                    LockString = "LOCKED";
                    lockStatus.setText("" + LockString);


                }

                if (stepCounter > stepGoal){
                    middleText.setText("" + 0);
                    circleProgressBar.setProgressWithAnimation(100);
                    lowerText.setText("" + 100 + "%");

                    lockOn = false;
                    LockString = "UNLOCKED";
                    lockStatus.setText("" + LockString);


                }


                editor.putInt("stepGoal",stepGoal);
                editor.putInt("stepCounter",stepCounter);
                editor.putInt("sliderPercent",sliderPercent);
                editor.putInt("stepCounting",stepCounting);
                editor.putBoolean("lockOn",lockOn);
                editor.putString("LockString",LockString);
                editor.apply();





            }
        });

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    public void startAlarm(boolean isOn, boolean isKill) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;


        myIntent = new Intent(getApplicationContext(),AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //60,000 milliseconds is the fastest this can go, if you set it less than 60k (1 minute)
        //itll literally set itself to 60,000.
        manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);


        //if(!isRepeat)



    }


    /////////////////////////////////////////////////////////////////////////////////////////


    //this doesnt work because its outside of a click thing :(((
    public void updateStepsAndCircle() {

        stepCounting = stepGoal - stepCounter;
        middleText.setText("" + stepCounting);
        sliderPercent = (int) ((stepGoal * 100.0f) / stepCounter);

      //  circleProgressBar.setProgressWithAnimation(sliderPercent);
      //  lowerText.setText("" + sliderPercent + "%");

        if (sliderPercent == 100) {

            lockOn = false;
            LockString = "UNLOCKED";
            lockStatus.setText("UNLOCKED");
        }
        if (sliderPercent != 100) {
            lockOn = true;
            LockString = "LOCKED";
            lockStatus.setText("" + LockString);
        }

    }

    public static void StopAlarm(Context c){

    }

    public static void LockApps(){

        lockOn = true;
        LockString = "LOCKED";
        lockStatus.setText(""+LockString);

    }

    public static void updateUI(){

        lockOn = true;
        LockString = "LOCKED";
        lockStatus.setText(""+LockString);

    }



}


