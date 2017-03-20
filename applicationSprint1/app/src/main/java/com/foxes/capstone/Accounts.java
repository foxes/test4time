package com.foxes.capstone;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

/**
 * Created by trevor on 3/18/17.
 */


public class Accounts extends AppCompatActivity {
    final int numofUsers = 10;
    int[] stepsleft = new int[numofUsers];
    int[] stepstaken = new int[numofUsers];

    public void setstepstozero(){
        for(int i = 0; i < numofUsers; i++){
            stepstaken[i] = 100 + (20 * i);
        }
    }
    public int getsteps(int user){
        int steps = stepstaken[user];
        return steps;
    }
    //public void updatetaken(int user){

    //}
    //public void updateneeded(int user){

   // }
    //public void updatetime(int user){

    //}


}