package com.foxes.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class AccessTokenReceiver extends AppCompatActivity {




    String string;
    static String TokenParsed = "0000";


    @Override
    protected void onNewIntent(Intent intent) {
        string = intent.getDataString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        String accessToken = string.substring(string.indexOf("&access_token") + 14);
        String userId = string.substring(string.indexOf("&user_id")+9, string.indexOf("&token_type"));
        String tokenType = string.substring(string.indexOf("&token_type")+12,string.indexOf("&expires_in"));
        String parsedToken = string.substring(string.indexOf("access_token=")+13, string.indexOf("&user_id"));

        Log.i("TAG", string);
        Log.i("TAG", "Bearer " + parsedToken);
        TokenParsed = "Bearer " + parsedToken;
      //  Log.i("TAG", userId);
       // Log.i("TAG", tokenType);



        Intent intent = new Intent(AccessTokenReceiver.this, MainActivity.class);
        startActivity(intent);
    }
}
