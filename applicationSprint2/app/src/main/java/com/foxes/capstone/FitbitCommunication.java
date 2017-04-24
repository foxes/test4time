package com.foxes.capstone;

import android.os.Process;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import static android.R.attr.key;

/**
 * Created by fletcher on 3/28/17.
 */

public class FitbitCommunication implements Runnable{

    private int steps;
    private int year;
    private int month;
    private int date;

    private final String URL = AccessTokenReceiver.TokenParsed;

    public void connectToFitbit() {

        try {
            DateUtil dateUtil = new DateUtil();
            System.out.println("connectToFitbit started");
            //URL link = new URL("https://api.fitbit.com/1/user/-/activities/date/2017-04-10.json");
            URL link = new URL("https://api.fitbit.com/1/user/-/activities/date/" + dateUtil.getYear() + "-" + dateUtil.getMonth() + "-" + dateUtil.getDay() + ".json");
            System.out.println("Link: " + link);

            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", URL);
            conn.setDoOutput(true);

            int responseCode;
            try{
                responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = br.readLine()) != null){
                response.append(inputLine + '\n');
            }
            br.close();

            System.out.println(response.toString());
            parseJsonResponse(response.toString());

        } catch (MalformedURLException malformedURLException) {
            System.out.println("caught");
        }
        catch(IOException ioexception){

        }
    }

    public void parseJsonResponse(String response){

        System.out.println("inside parser");
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONObject summary = jsonObject.getJSONObject("summary");
            String steps = summary.getString("steps");

            int intSteps = Integer.parseInt(steps);
            System.out.println("intSteps: " + intSteps);
            setSteps(intSteps);
            System.out.println("steps: " + steps);
        }
        catch(JSONException j){
            j.printStackTrace();
        }
    }

    public void run(){

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        connectToFitbit();
    }

    public void setSteps(int steps){

        this.steps = steps;
    }

    public int getSteps(){

        return steps;
    }


}

