package msu.sargis.packages;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by sargis on 11/26/18.
 */

public class UninstallAsyncTask extends AsyncTask<AppInfo, Void, Boolean> {

    private final WeakReference<UninstallListener> uninstallListenerWeakReference;

    public UninstallAsyncTask(UninstallListener uninstallListener) {
        this.uninstallListenerWeakReference = new WeakReference<>(uninstallListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(AppInfo... appInfos) {
        AppInfo appInfo = appInfos[0];
        if (appInfo.isSystem()){
            return RootHelper.uninstallSystem(appInfo.getApkFile());
        }else {
            return RootHelper.uninstall(appInfo.getPackageName());
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        UninstallListener uninstallListener = uninstallListenerWeakReference.get();

        if (uninstallListener != null){
            if (aBoolean){
                uninstallListener.onUninstall();
            }else {
                uninstallListener.onFailed();
            }
        }
    }

    public interface UninstallListener{
        void onUninstall();
        void onFailed();
    }
}
