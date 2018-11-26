package msu.sargis.packages;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_PICK_APK = 1;
    AppManager appManager;
    RecyclerView recyclerView;
    AppsAdapter appsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.apps_rv);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        appManager = new AppManager(this);


        /*for (int i = 0; i < appInfoList.size(); i++) {
            Log.i(TAG, appInfoList.get(i).toString());

        }*/

        appsAdapter = new AppsAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(appsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemToucheHelperCallback);
        recyclerView.addItemDecoration(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        reloadApps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                appsAdapter.setQuery(newText.toLowerCase().trim());
                appsAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.install_item:
                startFilePickerActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener =  new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reloadApps();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void reloadApps(){
        List<AppInfo> appInfoList = appManager.getInstalledApps();
        appsAdapter.setApps(appInfoList);
        appsAdapter.notifyDataSetChanged();
    }

    private void showToast(){
        Toast toast = Toast.makeText(this,"Hello", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void startFilePickerActivity(){
        Intent intent = new Intent(this, FilePickerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICK_APK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_PICK_APK && resultCode == RESULT_OK){
            String apkPath = data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);
            Log.i(TAG, "APK: " + apkPath);
            startAppInstallation(apkPath);
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startAppInstallation(String apkPath){
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID
                                            +".provider", new File(apkPath));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uri = Uri.fromFile(new File(apkPath));
        }

        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(installIntent);
    }


    private final ItemTouchHelper.Callback itemToucheHelperCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            AppInfo appInfo = (AppInfo) viewHolder.itemView.getTag();
            startAppUninstallation(appInfo);
        }
    };

    private void startAppUninstallation(AppInfo appInfo){
        uninstallWithRoot(appInfo);
    }

    private void uninstallWithRoot(AppInfo appInfo){
        UninstallAsyncTask asyncTask = new UninstallAsyncTask();
        asyncTask.execute(appInfo.getPackageName());
    }
}