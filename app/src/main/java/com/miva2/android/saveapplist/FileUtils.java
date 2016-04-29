package com.miva2.android.saveapplist;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileUtils {

    private static final String TAG = "FILE";
    private static final String SEPARATOR = "======================";
    private static final String HEADER = "INSTALLED APPS";

    public static String generateTextFromList(Context context, List<ResolveInfo> appsList) {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER + "\n\n\n");

        for (ResolveInfo app : appsList) {

            sb.append(app.resolvePackageName).append("\n")
                    .append(app.activityInfo.packageName).append("\n")
                    .append(app.loadLabel(context.getPackageManager())).append("\n")
                    .append(app.nonLocalizedLabel).append("\n")
                    .append("\n" + SEPARATOR + "\n");
        }
        return sb.toString();
    }

    public static boolean saveTextFileToExternalStorage(String text, String filename) throws IOException {

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        boolean mkdirSuccess = directory.mkdirs();
        Log.d(TAG, "directories successfully created: " + mkdirSuccess);

        File appListTextFile = new File(directory, filename);
        if (!appListTextFile.exists()) {
            boolean newFileSuccess = appListTextFile.createNewFile();
            Log.d(TAG, "new file successfully created: " + newFileSuccess);
        }

        if (appListTextFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(appListTextFile);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.append(text);
            writer.close();
            outputStream.close();

            Log.d(TAG, "File " + filename + " saved in directory " + directory);
            return true;
        }

        return false;
    }
}
