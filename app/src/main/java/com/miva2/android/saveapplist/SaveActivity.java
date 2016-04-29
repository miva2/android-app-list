package com.miva2.android.saveapplist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;
import java.util.List;

public class SaveActivity extends AppCompatActivity {

    private static final String FILENAME = "appslist.txt";
    private List<ResolveInfo> installedApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SaveAppsTask(view).execute();
                }
            });
        }
    }

    private boolean saveApps() {

        installedApps = getInstalledApps(getPackageManager());
        String text = FileUtils.generateTextFromList(this, installedApps);
        boolean result = false;
        try {
            result = FileUtils.saveTextFileToExternalStorage(text, FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<ResolveInfo> getInstalledApps(PackageManager manager) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        return manager.queryIntentActivities(intent, 0);
    }

    private class SaveAppsTask extends AsyncTask<Void, Void,Boolean> {

        View view;

        public SaveAppsTask(View view) {
            this.view = view;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            return saveApps();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Snackbar.make(view, R.string.save_success, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, R.string.save_failed, Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
