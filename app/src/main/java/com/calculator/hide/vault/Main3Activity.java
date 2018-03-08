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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends Activity implements AdapterView.OnItemClickListener {

    static int count;
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

    }

    @Override
    protected void onResume() {
        count++;
        if(count%2==0){
            NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder().build();
            adView.loadAd(request);
        }
        super.onResume();
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

            intent.setDataAndType(Uri.fromFile(file), type);
            startActivity(Intent.createChooser(intent, "Select Appropriate app"));

        }

    }

}