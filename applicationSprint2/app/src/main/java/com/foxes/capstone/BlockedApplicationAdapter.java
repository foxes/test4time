package com.foxes.capstone;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;



public class BlockedApplicationAdapter extends RecyclerView.Adapter<BlockedApplicationAdapter.ApplicationViewHolder> {

    private static final String         TAG = "ApplicationAdapter";
    private PackageManager              mPackageManager = null;
    private List<ApplicationInfo>       mApplications = null;
    private final int                   mFocusedItem = 0;

    public BlockedApplicationAdapter(Activity activity, RecyclerView recyclerView, List<ApplicationInfo> applicationInfos) {

        mApplications = applicationInfos;
        mPackageManager = activity.getPackageManager();

    }

    public void addBlockedApplication(final Application app) {

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(app);
            }
        });
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_application_row, parent, false);

        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ApplicationViewHolder holder, int position) {
        ApplicationInfo app = mApplications.get(position);

        holder.itemView.setSelected(mFocusedItem == position);

        holder.getLayoutPosition();

        holder.appIcon.setImageDrawable(app.loadIcon(mPackageManager));
        holder.appName.setText(app.loadLabel(mPackageManager));
        holder.packageName.setText(app.packageName);
        holder.processName.setText(app.processName);

        // prevents app check from being removed from appSelected(hashmap) when scrolling out of view
        holder.checkBox.setOnCheckedChangeListener(null);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Application> blockedApplications = realm.where(Application.class).equalTo("name", holder.appName.getText().toString()).findAll();

        // check the appropriate apps (check apps that should be checked
        if (blockedApplications.size() > 0) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //holder.checkBox.setChecked(isChecked);
                if(isChecked) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insert(new Application(
                                    holder.appName.getText().toString(),
                                    holder.packageName.getText().toString(),
                                    holder.processName.getText().toString()
                            ));
                            Log.d("Realm", "Added " + holder.appName.getText().toString());
                        }
                    });
                } else {

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (Application app : realm.where(Application.class).equalTo("name", holder.appName.getText().toString()).findAll()) {
                                app.deleteFromRealm();
                                Log.d("Realm", "Removed " + holder.appName.getText().toString());
                            }
                        }
                    });

                }

                saveBlockedApplications();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    public class ApplicationViewHolder extends RecyclerView.ViewHolder {

        protected ImageView     appIcon;
        protected TextView      appName;
        protected TextView      packageName;
        protected TextView      processName;
        protected CheckBox      checkBox;
        protected LinearLayout  layout;

        public ApplicationViewHolder(View view) {
            super(view);
            this.appIcon = (ImageView) view.findViewById(R.id.app_icon);
            this.appName = (TextView) view.findViewById(R.id.app_name);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            this.layout = (LinearLayout) view.findViewById(R.id.rowLayout);
            this.packageName = (TextView) view.findViewById(R.id.packageName);
            this.processName = (TextView) view.findViewById(R.id.processName);
            view.setClickable(true);
        }

    }

    public void saveBlockedApplications() {

        /*Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all currently stored blocked applications
                for (Application storedBlockedApplications : realm.where(Application.class).findAll()){
                    storedBlockedApplications.deleteFromRealm();
                }
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Store new list of blocked applications
                for (Map.Entry<String,Application> app : mBlockedApplications.entrySet()) {
                    if (!app.getValue().isValid()) {
                        if (!app.getValue().getPackageName().contains("com.test4time.test4time")) { // don't store test4time
                            if (!realm.where(Application.class).equals(app)) {
                                realm.copyToRealm(app.getValue());
                            }
                        }
                    }
                }
            }
        });*/
    }




}
