package msu.sargis.packages;

import android.os.AsyncTask;

/**
 * Created by sargis on 11/26/18.
 */

public class UninstallAsyncTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String packageName = strings[0];
        boolean result = RootHelper.uninstall(packageName);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
