package com.vadim.dev.mobile.contentaccessutil;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentAccessLifeCycleObserver contentAccessLifeCycleObserver = new ContentAccessLifeCycleObserver(getActivityResultRegistry());
        getLifecycle().addObserver(contentAccessLifeCycleObserver);
        contentAccessLifeCycleObserver.selectFile(ContentAccessLifeCycleObserver.FILE_PDF, new ContentAccessLifeCycleObserver.SelectFileCallback() {
            @Override
            public void onFileSelected(Uri uri) {
                Toast.makeText(MainActivity.this, "File selected, URI: " + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}