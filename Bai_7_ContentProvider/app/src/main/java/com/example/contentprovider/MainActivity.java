package com.example.contentprovider;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.contentprovider.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView1.setOnClickListener(view -> {

        });

        binding.textView2.setOnClickListener(view -> {
            acceptPermission();
            List<String> list = readCallLog();
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            binding.list.setAdapter(adapter);
        });

        binding.textView3.setOnClickListener(view -> {
            acceptPermission();
            List<String> list = loadMediaFiles();
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            binding.list.setAdapter(adapter);
        });

        binding.textView4.setOnClickListener(view -> {

        });

        binding.textView5.setOnClickListener(view -> {
            acceptPermission();
            List<TinNhan> list = readSms();
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list.stream().map(item -> item.getMessage()).collect(Collectors.toList()));
            binding.list.setAdapter(adapter);
        });
    }

    private void loadMessage() {


    }

    public void acceptPermission(){
        if(!hasStoragePermissions()){
            requestStoragePermissions();
        }
    }

    private boolean hasStoragePermissions() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int readPermission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int readPermission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && readPermission2 == PackageManager.PERMISSION_GRANTED && readPermission3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{ Manifest.permission.READ_SMS,  Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSIONS
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms();
            } else {
                Toast.makeText(this, "Cần quyền truy cập vào bộ nhớ để tiếp tục.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<TinNhan> readSms() {
        Cursor cursor = null;
        List<TinNhan> list = new ArrayList<>();
        try {
            Uri uri = Uri.parse("content://sms/");
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex("body"));
                    @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                    list.add(new TinNhan(id, body, address));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private List<String> readCallLog() {
        List<String> callLogs = new ArrayList<>();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                callLogs.add(number);
            }
            cursor.close();
        }
        return callLogs;
    }

    private List<String> loadMediaFiles() {
        List<String> mediaFiles = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                mediaFiles.add(name);
            }
            cursor.close();
        }
        return mediaFiles;
    }

}