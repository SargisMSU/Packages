package msu.sargis.packages;

import android.support.annotation.Nullable;

import java.io.File;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by sargis on 11/26/18.
 */

public class RootHelper {

    public static boolean uninstall(String packagename){
        String output = executeCommand("pm uninstall " + packagename);
        if (output != null && output.toLowerCase().contains("success")){
            return true;
        }else
            return false;
    }

    public static boolean uninstallSystem(File fileApk){
        executeCommand("mount -o rw,remount /system");
        executeCommand("rm " + fileApk.getAbsolutePath());
        executeCommand("mount -o ro,remount /system");

        String output = executeCommand("ls " + fileApk.getAbsolutePath());
        if (output != null && output.trim().isEmpty()){
            return true;
        }else
            return false;
    }

    @Nullable
    private static String executeCommand(String command){
        List<String> stdout = Shell.SU.run(command);
        if (stdout == null){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String line :stdout) {
            builder.append(line).append('\n');
        }

        return builder.toString();
    }
}
