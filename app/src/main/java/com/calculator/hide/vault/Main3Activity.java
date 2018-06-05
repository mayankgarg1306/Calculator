package com.calculator.hide.vault;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main3Activity extends Activity implements AdapterView.OnItemClickListener {

    private String directory= Environment.getExternalStorageDirectory().toString() +"/.data/.file/.drop/.hidden"+"/.ooooo";




    @Override
    public void onPause() {
        if (isApplicationSentToBackground(this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }else{
                ActivityCompat.finishAffinity(this);
            }

        }
        super.onPause();
    }
    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    List<String> gridItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        setGridAdapter(directory);

        AdView mAdView = (AdView) findViewById(R.id.banner_AdView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void setGridAdapter(String path) {
        // Create a new grid adapter
        gridItems = createGridItems(path);
        MyGridAdapter adapter = new MyGridAdapter(this, gridItems);

        // Set the grid adapter
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // Set the onClickListener
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }


    /**
     * Go through the specified directory, and create items to display in our
     * GridView
     */
    private List<String> createGridItems(String directoryPath) {
        List<String> items = new ArrayList<>();
        if(directoryPath==null||directoryPath.isEmpty()){
            directoryPath=directory;
        }
        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles();
        if(files!=null) {
            for (File file : files) {
                // Add the directories containing images or sub-directories
                items.add(file.toString());
            }
        }
        return items;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (new File(gridItems.get(position)).isDirectory()) {
            setGridAdapter(gridItems.get(position));
        }
        else {

            File file = new File(gridItems.get(position));
            String str=gridItems.get(position).substring(gridItems.get(position).lastIndexOf(".")+1);

            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getMimeTypeFromExtension(str);

          //  intent.setDataAndType(Uri.fromFile(file), type);

            Uri apkURI = FileProvider.getUriForFile(
                        this,BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(apkURI, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Select Appropriate app"));

        }

    }

}