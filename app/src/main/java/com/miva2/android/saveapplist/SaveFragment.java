package com.miva2.android.saveapplist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SaveFragment extends Fragment {
    private static final String TAG = SaveFragment.class.getSimpleName();

    private List<ResolveInfo> installedApps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button savebutton = (Button) view.findViewById(R.id.savebutton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveApps();
            }
        });
    }

    private void saveApps() {
        //this is currently happening in UI thread.
        // TODO: make a simple asynctask to offload the scanning and saving to a non-UI Thread.

        installedApps = getInstalledApps(getActivity().getPackageManager());
        String text = generateTextFromList(installedApps);
        saveTextFileToExternalStorage(text);

    }
    
    private String generateTextFromList(List<ResolveInfo> appsList) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSTALLED APPS\n\n\n");
        for (ResolveInfo app : appsList) {

            sb.append(app.resolvePackageName).append("\n")
                    .append(app.activityInfo.packageName).append("\n")
                    .append(app.loadLabel(getActivity().getPackageManager())).append("\n")
                    .append(app.nonLocalizedLabel).append("\n")
                    .append("\n=============================================\n");
        }
        return sb.toString();
    }

    private List<ResolveInfo> getInstalledApps(PackageManager manager) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        return manager.queryIntentActivities(intent, 0);
    }

    private void saveTextFileToExternalStorage(String text) {

        String fileName = "appList.txt";
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            boolean mkdirSuccess = directory.mkdirs();
            Log.d(TAG, "directories successfully created: " + mkdirSuccess);

            File appListTextFile = new File(directory, fileName);
            boolean newFileSuccess = appListTextFile.createNewFile();//fileOutputStream creates a file
            Log.d(TAG, "new file successfully created: " + newFileSuccess);

            if (mkdirSuccess && newFileSuccess) {
                FileOutputStream outputStream = new FileOutputStream(appListTextFile);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                writer.append(text);
                writer.close();
                outputStream.close();

                Log.d(TAG, "File " + fileName + " saved in directory " + directory);
            }
            Toast.makeText(getActivity(),
                    "Done writing SD 'mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
//*.iml
//        .gradle
//        /local.properties
//        /.idea/workspace.xml
//        /.idea/libraries
//        .DS_Store
//        /build
//        /captures
