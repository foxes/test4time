package com.foxes.capstone;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.test4time.test4time.adapter.BlockedApplicationAdapter;
//import com.test4time.test4time.data.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class BlockedApplicationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BlockedApplicationAdapter mListAdapter;
    private PackageManager mPackageManager = null;
    private MainActivity mActivity;
    private ProgressDialog mProgressDialog = null;
    //private JsonDatabase mJsonDatabase = null;
    private List<ApplicationInfo> mApplications = null;
    private LinearLayoutManager mLinearLayoutManager = null;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPackageManager = getActivity().getPackageManager();
        mActivity = (MainActivity)getActivity();

        new LoadApplications().execute(); // start thread to generate List of apps
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_blocked_applications, container, false);



        mRecyclerView = (RecyclerView) mView.findViewById(R.id.blocked_applications_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();

        /*if (mListAdapter != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mListAdapter.saveBlockedApplications();
                }
            });
        }*/
    }

    private Comparator<ApplicationInfo> ALPHABETICAL_ORDER = new Comparator<ApplicationInfo>() {
        public int compare(ApplicationInfo app1, ApplicationInfo app2) {

            String appLabel1 = app1.loadLabel(mPackageManager).toString();
            String appLabel2 = app2.loadLabel(mPackageManager).toString();

            int res = String.CASE_INSENSITIVE_ORDER.compare(appLabel1, appLabel2);
            if (res == 0) {
                res = appLabel1.compareTo(appLabel2);
            }
            return res;
        }
    };


    /*
     * generate list of installed apps
     */
    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {

        final ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != mPackageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);

                    if (mProgressDialog != null){

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.setMessage(applist.size() + " Applications Loaded");
                            }
                        });
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mProgressDialog != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.setMessage("Sorting " + applist.size() + " Applications");
                }
            });
        }

        //alphabetically
        Collections.sort(applist, ALPHABETICAL_ORDER);

        return applist;
    }

    /*
     * inner class creates a thread to populated the RecyclerView
     * Data retrieved (applications installed on device, applications in BLOCKAPPS table)
     */
    private class LoadApplications extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            mApplications = checkForLaunchIntent(mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA));

            mListAdapter = new BlockedApplicationAdapter(mActivity, mRecyclerView, mApplications);


            Snackbar.make(mView, "Loaded " + mApplications.size() + " applications", Snackbar.LENGTH_SHORT).show();

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mRecyclerView.setAdapter(mListAdapter);

            Realm realm = Realm.getDefaultInstance();
            RealmResults<Application> blockedApplications = realm.where(Application.class).findAll();

            for (Application app : blockedApplications){
                mListAdapter.addBlockedApplication(app);
            }

            mListAdapter.notifyDataSetChanged();

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            mProgressDialog = ProgressDialog.show(mActivity, "", "Loading Applications", true, false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
