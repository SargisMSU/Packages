package msu.sargis.packages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.File;
import java.util.List;

public class FilePickerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "FilePickerActivity";
    static final String EXTRA_FILE_PATH = "file_path";
    private FileManager fileManager;
    private FilesAdapter filesAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        filesAdapter.setOnFileClickListener(onFileClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker_activity);

        RecyclerView recyclerView = findViewById(R.id.files_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        filesAdapter = new FilesAdapter();
        recyclerView.setAdapter(filesAdapter);

        initFileManager();
    }

    @Override
    protected void onStop() {
        filesAdapter.setOnFileClickListener(null);
        super.onStop();
    }

    private void updateFileList() {
        List<File> files = fileManager.getFiles();
        filesAdapter.setFiles(files);
        filesAdapter.notifyDataSetChanged();
    }

    private void initFileManager(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            fileManager = new FileManager(this);
            updateFileList();
        }else {
            requestPermissions();
        }
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Permission granted!");
                initFileManager();
            }else {
                Log.i(TAG, "Permission denied!");
                requestPermissions();
            }
        }
    }

    private final FilesAdapter.OnFileClickListener onFileClickListener = new FilesAdapter.OnFileClickListener() {
        @Override
        public void onFileClick(File file) {
            if (file.isDirectory()){
                fileManager.navigateTo(file);
                updateFileList();
            }else if (file.getName().endsWith(".apk")){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_FILE_PATH, file.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (fileManager != null && fileManager.navigateUp()){
            updateFileList();
        }else {
            super.onBackPressed();
        }
    }
}
