package com.foxes.capstone;



import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.DialogInterface;
import android.content.Context;






public class MainActivity extends AppCompatActivity {

    int stepCounter = 100;
    int stepGoal = 2500;
    int StepCounting;
    int sliderPercent;

    boolean lockStatus;

    Button buttonTimer;
    Button buttonLock;
    Button buttonUnLock;
    Button buttonRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView lockStatus = (TextView) findViewById(R.id.lockStatus);
        final TextView middleText = (TextView) findViewById(R.id.middleText);
        final TextView upperText = (TextView) findViewById(R.id.upperText);
        final TextView lowerText = (TextView) findViewById(R.id.lowerText);
        buttonTimer = (Button) findViewById(R.id.TimerButton);
        buttonLock = (Button) findViewById(R.id.LockButton);
        buttonUnLock = (Button) findViewById(R.id.UnlockButton);
        buttonRefresh = (Button) findViewById(R.id.refreshButton);


        middleText.setText( "" + stepGoal);

        final SeekBar seekBarProgress;
        seekBarProgress = (SeekBar) findViewById(R.id.seekBar_progress);

        final CircleProgressBar circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);

        seekBarProgress.setProgress((int) circleProgressBar.getProgress());
        seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    circleProgressBar.setProgressWithAnimation(i);
                    // middleText.setText( "" + (int)  circleProgressBar.getProgress() );

                    sliderPercent = i;
                    StepCounting = stepGoal - ( (int)(stepGoal*(i*0.01)) ) ;
                    lowerText.setText("" + i + "%");
                    middleText.setText( "" + StepCounting ) ;

                    if (i == 100){
                        lockStatus.setText("UNLOCKED");
                    }
                    if (i != 100){
                        lockStatus.setText("LOCKED");
                    }
                }
                else {
                    circleProgressBar.setProgressWithAnimation(i);
                    sliderPercent = i;
                    StepCounting = stepGoal - ( (int)(stepGoal*(i*0.01)) ) ;
                    lowerText.setText("" + i + "%");
                    middleText.setText( "" + StepCounting ) ;
                    if (i == 100){
                        lockStatus.setText("UNLOCKED");
                    }
                    if (i != 100){
                        lockStatus.setText("LOCKED");
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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

                mLogin.setOnClickListener(new View.OnClickListener(){
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
                                    lockStatus.setText("UNLOCKED");
                                    dialog.dismiss();

                                }
                            });





                            //this will call another dialog box that lets people put in time

                        }else{
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

                mLogin.setOnClickListener(new View.OnClickListener(){
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


                            //this is the action listener for the inner-menu on the unlock. it SHOULD NOT bring up list of apps. ONLY the "lock" trigger should do that.

                            mLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    seekBarProgress.setProgress(0);
                                    stepGoal = Integer.parseInt(mEmail.getText().toString());
                                    lowerText.setText("" + 0 + "%");
                                    middleText.setText("" + stepGoal);
                                    Toast.makeText(MainActivity.this, "step goal updated.", Toast.LENGTH_LONG).show();
                                    lockStatus.setText("LOCKED");
                                    dialog.dismiss();



                                }
                            });

                            //this will call an activity with the list of apps

                        }else{
                            Toast.makeText(MainActivity.this, "bad pin", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
        //this is all code for the unlock button button/////////////////////////////////////////////////
        final Context context = this;
        buttonUnLock.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                //List of items to be show in  alert Dialog are stored in array of strings/char sequences

                final String[] items = {"User1", "User2", "User3", "User4"};
                final Accounts account = new Accounts();
                account.setstepstozero();
                //final int[] steps = {1111, 2222, 333, 444 };
                //final List<String> myList = new ArrayList<String>();
                //myList.add("user");



                AlertDialog.Builder builder = new AlertDialog.Builder(context);



                //set the title for alert dialog

                builder.setTitle("Choose user: ");
                //String[] mFileList = new String[0];


                //set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog

                builder.setItems(items, new DialogInterface.OnClickListener() {



                    @Override

                    public void onClick(DialogInterface dialog, int item) {

                        TextView view = (TextView) findViewById(R.id.userName);
                        final String name = items[item];

                        view.setText(name);

                        int steps = account.getsteps(item);
                        int stepsleft = 2500 - steps;
                        middleText.setText("" + stepsleft + "");

                    }

                });




                //Creating CANCEL button in alert dialog, to dismiss the dialog box when nothing is selected

                builder.setCancelable(false);
                builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {



                            @Override

                            public void onClick(DialogInterface dialog, int id) {

                                String newuser = "NewUser";
                                //final String[] newitems = {"User1", "User2", "User3", "User4", newuser};


                            }

                        });
                //Creating ADD USER button to add user to the list
                builder.setNegativeButton("ADD USER", new DialogInterface.OnClickListener() {



                    @Override

                    public void onClick(DialogInterface dialog, int id) {

                        //When clicked on CANCEL button the dalog will be dismissed

                        dialog.dismiss();

                    }

                });



                // Creating alert dialog

                AlertDialog alert = builder.create();



                //Showing alert dialog

                alert.show();



            }

        });


    //@Override

    //public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        //getMenuInflater().inflate(R.menu.main, menu);

        //return true;

    //}

        /////////////////////////////////////////////////////////////////////////////////////////

        //this is all code for the Refresh button button/////////////////////////////////////////////////
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //this stuff is just for demoing since we dont have the api ready to pull yet
                Random rand = new Random();
                int n = rand.nextInt(5);


                seekBarProgress.setProgress(sliderPercent + n);


            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
    }
}


