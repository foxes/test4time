package com.foxes.capstone;


import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    public FitbitCommunication comm = new FitbitCommunication();
    public DateUtil dateUtil = new DateUtil();

    public String timeStamp = "";



    //this is the set stepGoal by the parents via the "lock" function
    static int stepGoal = 0;

    //this is the current steps that the kid is on, should pull from fitbit API when possible
    static int stepCounter = 0;

    //this is the baseline steps, pull from the API when a lock is set
    static int baselineSteps = 0;

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
    Button button;

    boolean accessibilityEnabled = false;

    public static TextView lockStatus;
    public TextView middleText;
    public TextView upperText;
    public ServiceStatuses serviceStatuses;

    private int mSelectedColor;


    public static boolean isRunningLockingService;
    public static boolean needToUpdateWhiteList ;
    public static boolean needToStopLockingService ;
    public static boolean isTemporarilyUnlocked ;
    public static long endOfUnlockTimestamp;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        //pulling variables from sharedPreferences. basically memory
         preferences = this.getSharedPreferences("myPrefs", MODE_MULTI_PROCESS); //.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        stepGoal = preferences.getInt("stepGoal",stepGoal);
        baselineSteps = preferences.getInt("baselineSteps",baselineSteps);
        stepCounter = preferences.getInt("stepCounter",stepCounter);
        sliderPercent = preferences.getInt("sliderPercent",sliderPercent);
        stepCounting = preferences.getInt("stepCounting",stepCounting);
        lockOn = preferences.getBoolean("lockOn",false);
        LockString = preferences.getString("LockString","Unlocked");
        mSelectedColor = preferences.getInt("mSelectedColor",mSelectedColor);

        serviceStatuses = new ServiceStatuses();

        isRunningLockingService = preferences.getBoolean("isRunningLockingService",false);
        needToUpdateWhiteList  = preferences.getBoolean("needToUpdateWhiteList", true);
        needToStopLockingService  = preferences.getBoolean ("needToStopLockingService" ,false);
        isTemporarilyUnlocked      = preferences.getBoolean (   "isTemporarilyUnlocked", false);
        endOfUnlockTimestamp = preferences.getLong("endOfUnlockTimestamp"  ,0);




        final CircleProgressBar circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        final TextView lowerText = (TextView) findViewById(R.id.lowerText);
        lockStatus = (TextView) findViewById(R.id.lockStatus);
        final TextView middleText = (TextView) findViewById(R.id.middleText);
        final TextView upperText = (TextView) findViewById(R.id.upperText);
        buttonTimer = (Button) findViewById(R.id.TimerButton);
        buttonLock = (Button) findViewById(R.id.LockButton);
        buttonUnLock = (Button) findViewById(R.id.UnlockButton);
        buttonRefresh = (Button) findViewById(R.id.refreshButton);
        button = (Button) findViewById(R.id.button);


        //this is what makes the prompt for accessibility at the beginning, right now itll appear
        //everytime it starts, but can change that once we find a better spot

        if (needPermissionForBlocking(this)) {
            Intent dialogIntent = new Intent(this, AccessibilityRequestActivity.class);
            dialogIntent.putExtra(MainActivity.OUT_OF_TIME, true);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

            accessibilityEnabled = true;

        }


        if(mSelectedColor != 0){
            circleProgressBar.setColor(mSelectedColor);
        }

        //to refresh the values after you close the app

        if (stepCounting == 0){
            middleText.setText("" + stepGoal);
        }

        if (stepCounting != 0){
            middleText.setText("" + stepCounting);
        }


        circleProgressBar.setProgressWithAnimation(sliderPercent);
        lowerText.setText("" + sliderPercent + "%");
        lockStatus.setText("" + LockString);


        //mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);
        final int[] mColors = getResources().getIntArray(R.array.default_rainbow);





        final ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL);


        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                circleProgressBar.setColor(mSelectedColor);
                saveEVERYTHING();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show(getFragmentManager(), "color_dialog_test");
            }
        });




        //this is all code for the timer button button/////////////////////////////////////////////////
        buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRunningLockingService = preferences.getBoolean("isRunningLockingService", false);
                if(isRunningLockingService /*serviceStatuses.isRunningLockingService.get()*/){
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

                                    lockDisableTime = Integer.parseInt(mEmail.getText().toString());

                                    isTemporarilyUnlocked = true;
                                    endOfUnlockTimestamp = Calendar.getInstance().getTimeInMillis() + lockDisableTime * 60000;


                                    //starting the notification "alarm", this is the only function that uses this currently but it could be
                                    //used on other ones later
                                    startAlarm(true, false);

                                    lockOn = false;
                                    LockString = "UNLOCKED";
                                    lockStatus.setText("" + LockString);

                                    saveEVERYTHING();


                                    dialog.dismiss();

                                }
                            });


                        } else {
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
            else{
                    Toast.makeText(MainActivity.this, "Everything is already unlocked right now.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////

        //this is all code for the lock button button/////////////////////////////////////////////////
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTemporarilyUnlocked = preferences.getBoolean("isTemporarilyUnlocked", false);
                // if(!isTemporarilyUnlocked /*!serviceStatuses.isTemporarilyUnlocked.get()*/){
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

                                    //pull from fitbit API when lock is set to set a baseline for later
                                    comm.connectToFitbit();
                                    baselineSteps = comm.getSteps();

                                    //delete this later keep above ^
                                    //baselineSteps = 0;

                                    lowerText.setText("" + 0 + "%");
                                    middleText.setText("" + stepGoal);
                                    circleProgressBar.setProgressWithAnimation(0);
                                    stepCounter = 0;
                                    stepCounting = 0;
                                    sliderPercent = 0;


                                    Toast.makeText(MainActivity.this, "step goal updated.", Toast.LENGTH_LONG).show();


                                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    Intent myIntent;
                                    PendingIntent pendingIntent;
                                    myIntent = new Intent(getApplicationContext(), AlarmNotificationReceiver.class);
                                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                    manager.cancel(pendingIntent);



                                    isRunningLockingService = true;
                                    isTemporarilyUnlocked = false;



                                    lockOn = true;
                                    LockString = "LOCKED";
                                    lockStatus.setText("" + LockString);
                                    dialog.dismiss();


                                    startService(new Intent(getApplicationContext(), LockingService.class));

                                    saveEVERYTHING();


                                   // editor.apply();


                                }
                            });

                            //this will call an activity with the list of apps
                        } else {
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }


                });
           /* }
                 else{
                     Toast.makeText(MainActivity.this, "The apps will lock after the timer is up.", Toast.LENGTH_SHORT).show();
                 }*/
            }
        });


        /////////////////////////////////////////////////////////////////////////////////////////
        //this is all code for the unlock button button/////////////////////////////////////////////////
        buttonUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRunningLockingService = preferences.getBoolean("isRunningLockingService", false);
                if(isRunningLockingService) {
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
                                        //TODO
                                        Toast.makeText(MainActivity.this, "lock disabled. Use lock to reactivate.", Toast.LENGTH_LONG).show();
                                        needToStopLockingService = true;
                                        editor.putBoolean("needToStopLockingService", needToStopLockingService);
                                        //serviceStatuses.needToStopLockingService.set(true);
                                        editor.commit();
                                        lockOn = false;
                                        LockString = "UNLOCKED";
                                        lockStatus.setText("" + LockString);
                                        dialog.dismiss();

                                        saveEVERYTHING();



                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Everything is alreaady unlocked.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////

        //this is all code for the Refresh button button/////////////////////////////////////////////////
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //this stuff is just for demoing since we dont have the api ready to pull yet
                //Random rand = new Random();
                //int n = rand.nextInt(5);

                //for later
                comm.connectToFitbit();
                 int tmpStep = comm.getSteps();
                 stepCounter = stepCounter + (tmpStep - baselineSteps);
                 stepCounting = stepGoal - stepCounter;
                 baselineSteps = tmpStep;

                //delete these, keep above ^
                //stepCounter = stepCounter + n;
                //stepCounting = stepGoal - stepCounter;

                if (stepCounter < stepGoal) {

                    //stepCounting = stepGoal - stepCounter;
                    middleText.setText("" + stepCounting);
                    sliderPercent = ( (int)(stepCounter / (stepGoal*.01) ) );
                    circleProgressBar.setProgressWithAnimation(sliderPercent);
                    lowerText.setText("" + sliderPercent + "%");

                    lockOn = true;
                    LockString = "LOCKED";
                    lockStatus.setText("" + LockString);
                    saveEVERYTHING();


                }

                if (stepCounter >= stepGoal){
                    middleText.setText("" + 0);
                    circleProgressBar.setProgressWithAnimation(100);
                    lowerText.setText("" + 100 + "%");

                    needToStopLockingService = true;
                    editor.putBoolean("needToStopLockingService", needToStopLockingService);
                    //serviceStatuses.needToStopLockingService.set(true);

                    lockOn = false;
                    LockString = "UNLOCKED";
                    lockStatus.setText("" + LockString);
                    stepCounting = 0;

                    sliderPercent = 100;
                    saveEVERYTHING();
                    editor.commit();


                }

                saveEVERYTHING();





            }
        });





    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // USED IN API 21
    // Check if service has access to usage Data
    //
    private static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
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


    public void StopAlarm(Context c){

        AlarmManager manager = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;
        myIntent = new Intent(c.getApplicationContext(),AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(c.getApplicationContext(),1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        manager.cancel(pendingIntent);
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


    public void saveEVERYTHING(){

        editor.putInt("stepGoal", stepGoal);
        editor.putInt("baselineSteps", baselineSteps);
        editor.putInt("stepCounter", stepCounter);
        editor.putInt("sliderPercent", sliderPercent);
        editor.putInt("stepCounting", stepCounting);
        editor.putBoolean("lockOn", lockOn);
        editor.putString("LockString", LockString);
        editor.putBoolean("isRunningLockingService", isRunningLockingService);
        editor.putBoolean("isTemporarilyUnlocked", isTemporarilyUnlocked);
        editor.putBoolean("needToStopLockingService", needToStopLockingService);
        editor.putLong("endOfUnlockTimestamp", endOfUnlockTimestamp);
        editor.putInt("mSelectedColor", mSelectedColor);
        editor.commit();





    }

}


