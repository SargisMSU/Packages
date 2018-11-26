package msu.sargis.packages;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, получающий список пакетов
 */

public class AppManager {

    private final PackageManager packageManager;

    public AppManager(Context context) {
        packageManager = context.getPackageManager();
    }

    public List<AppInfo> getInstalledApps(){

        List<AppInfo> installedApps = new ArrayList<>();

        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo :installedPackages) {
            AppInfo appInfo = new AppInfo(packageInfo.packageName,
                    packageInfo.versionCode, packageInfo.versionName,
                    packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                    packageInfo.applicationInfo.loadIcon(packageManager));
            installedApps.add(appInfo);
        }

        return installedApps;
    }
}


