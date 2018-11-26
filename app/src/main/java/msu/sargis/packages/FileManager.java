package msu.sargis.packages;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class FileManager {
    private static final String TAG = "FileManager";
    private File currentDirectory;
    private final File rootDirectory;

    public FileManager(Context context) {
        File directory;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            directory = Environment.getExternalStorageDirectory();
        }else {
            directory = ContextCompat.getDataDir(context);
        }

        rootDirectory = directory;
        navigateTo(directory);
    }

    boolean navigateTo(File directory) {
        if (!directory.isDirectory()) {
            Log.e(TAG, directory.getAbsolutePath() + " is not a directory!");
            return false;
        }
        if (!rootDirectory.equals(directory) && rootDirectory.getAbsolutePath()
                .contains(directory.getAbsolutePath())){
            Log.w(TAG, "Trying to navigate upper than root directory to " + directory.getAbsolutePath());
            return false;
        }
        currentDirectory = directory;
        return true;
    }

    boolean navigateUp(){
        return navigateTo(currentDirectory.getParentFile());
    }

    List<File> getFiles(){
        return new ArrayList<>(Arrays.asList(currentDirectory.listFiles()));
    }
}
