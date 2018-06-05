package com.calculator.hide.vault;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity  {

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


    private static final int REQUEST_INVITE = 0;
    private InterstitialAd interstitial;
    Button buttonOpenDialog;
    Button buttonUp;
    Button buttonok;
    Button buttoncancel;
    Button unhide;
    Button cancelButton;
    TextView textFolder;
    String TAG = "message";
    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    static final int CUSTOM_DIALOG = 10;
    int dismiss=1;
    static int x=0;
    static int count;

    ListView dialog_ListView;
    ListView listView;
    ArrayAdapter listadapter;
    ArrayAdapter listadapterhide;

    RewardedVideoAd mRewardedVideoAd;

    String hiddenDirectory;

    File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    File force_retrieve=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Calculator_Media");
    File curFolder=root;
    File selected;
    File hiddenfolder;

    private List<String> fileList = new ArrayList<String>();
    static final List<String> filesss=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listadapter=new customlist(this, (ArrayList) fileList);
        listadapterhide=new customlist(this, (ArrayList) fileList);
        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    request_permission();
                } else {
                    showDialog(CUSTOM_DIALOG_ID);
                }
            }
        });

        unhide = (Button) findViewById(R.id.Unhide);
        unhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    request_permission();
                }
                else
                    showDialog(CUSTOM_DIALOG);
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;

        hiddenfolder = new File(Environment.getExternalStorageDirectory().toString()+"/.data/.file/.drop/.hidden"+"/.ooooo");
        File extrafolder = new File(Environment.getExternalStorageDirectory().toString()+"/.data/.data/.file/.drop/.hidden");
        if(!extrafolder.isDirectory()){
            extrafolder.mkdirs();
        }
        if (!hiddenfolder.isDirectory()) {
            hiddenfolder.mkdirs();//create storage directories, if they don't exist
        }
        hiddenDirectory = hiddenfolder.toString();
        AdView mAdView = (AdView) findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


        void request_permission(){
                    ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            adload();
            dialog_passwordchange();
            return true;
        }
        else if (id==R.id.video){
            mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());

            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem rewardItem) {
                }

                @Override
                public void onRewardedVideoAdLoaded() {
                     showRewardedVideo();
                }

                @Override
                public void onRewardedVideoAdOpened() {
                      }

                @Override
                public void onRewardedVideoStarted() {
                   }

                @Override
                public void onRewardedVideoAdClosed() {
                     }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                }
            });

        }
        else if(id==R.id.feedback)
        {
            adload();
            Intent i=new Intent(this,Main4Activity.class);
            startActivity(i);
            return true;

        }
        else if(id==R.id.appinfo)
        {
            Intent i=new Intent(this,infoActivity.class);
            startActivity(i);
            return true;
        }
        else if(id==R.id.rateus)
        {   try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            startActivity(rateIntent);
            return true;
            }catch (Exception e){
                return false;
            }
        }
        else if(id==R.id.invite){
            adload();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out this amazing app for hiding photos, videos and everything else in a calculator " +
                            "at: https://play.google.com/store/apps/details?id=com.calculator.hide.vault&hl=en");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if(id==R.id.exit){
            adload();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }else{
                ActivityCompat.finishAffinity(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(Main2Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonok = (Button) dialog.findViewById(R.id.button);
                buttoncancel=(Button) dialog.findViewById(R.id.buttoncancel);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(final DialogInterface arg0) {
                        // do something
                        if (dismiss == 1) {
                            copytohidden();
                        }
                    }


                });

                buttonok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                           dismiss=1;
                        dismissDialog(CUSTOM_DIALOG_ID);

                    }
                });
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(curFolder.equals(root)||curFolder.getParentFile()==null){
                            dismiss=0;
                           dismissDialog(CUSTOM_DIALOG_ID);
                        }
                        else
                        ListDir(curFolder.getParentFile());
                    }
                });
                buttoncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(Main2Activity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        dismiss=0;
                        dismissDialog(CUSTOM_DIALOG_ID);

                    }
                });

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                            if(curFolder.getPath().equals("/storage/emulated/0")||curFolder.getParentFile()==null||curFolder==null) {
                                dismiss=0;
                                dismissDialog(CUSTOM_DIALOG_ID);
                                Log.i(TAG,"enter in if");
                            }
                            else
                                ListDir(curFolder.getParentFile());
                            Log.i(TAG,"enter in else");
                        }
                        return true;
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selected = new File(fileList.get(position));
                        if (selected.isDirectory()) {
                            x=0;
                            ListDir(selected);
                        } else {
                            copytohidden();
                            x=1;
                            if(selected.getParentFile()!=null)
                            ListDir(selected);

                        }
                    }
                });

                break;

            case CUSTOM_DIALOG:
                dialog = new Dialog(Main2Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialoglayout2);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                cancelButton=(Button) dialog.findViewById(R.id.cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       dismissDialog(CUSTOM_DIALOG);
                    }
                });

                listView = (ListView) dialog.findViewById(R.id.listView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selected = new File(fileList.get(position));
                        String filename=selected.getPath().substring(selected.getPath().lastIndexOf("/")+1);
                        //retrieve original path from hidden directory
                        String filepath = RetrieveData(filename);//original file path
                        File f = null;
                        File f1 = null;
                        boolean bool1 = false;

                        try {
                            // create new File objects
                            f = new File(hiddenDirectory+"/"+filename);//hidden file
                            if(filepath.equals("")) {
                                f1 = new File(force_retrieve.toString() + "/" + filename);
                                Toast.makeText(Main2Activity.this,"Your file will get stored in Calculator_Media folder of Internal Storage",Toast.LENGTH_LONG).show();
                            }
                            else
                                f1 = new File(filepath);                   //original file
                            if (!force_retrieve.isDirectory())
                                 force_retrieve.mkdirs();//create storage directories, if they don't exist
                            if (!f1.getParentFile().isDirectory())
                                f1.getParentFile().mkdirs();
                            //new DoThing().execute(f,f1);
                            Boolean bool=false;
                            bool = f.renameTo(f1);
                            media_scanning(f1);
                            scanning();
                        } catch (Exception e) {

                            // if any error occurs
                            e.printStackTrace();
                        }
                            String file=selected.getPath();
                        ListDir_1(file);
                    }
                });
                break;
        }
        return dialog;
    }

    private void media_scanning(File mediafile) {
            filesss.clear();
            if(mediafile.isDirectory()){
                listf(mediafile.getAbsolutePath());
            }
            else{
                filesss.add(mediafile.getAbsolutePath());
            }
        }
        public void listf(String directoryName) {

            File directory = new File(directoryName);
            File[] fList = directory.listFiles();
            if(fList!=null) {
                for (File file : fList) {
                    if (file.isFile()) {
                        filesss.add(file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        listf(file.getAbsolutePath());
                    }
                }
            }
        }
        public void scanning(){
            Toast.makeText(this, ""+filesss.size(), Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for(int i=0;i<filesss.size();i++)
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + new File(filesss.get(i)))));
            } else {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
        }

    private void copytohidden() {
        File f = null;
        File f1 = null;

        try {
            // create new File objects
            f = selected;//file selected to hide
            String path = selected.toString();//it contain original path of our file
            String filename = path.substring(path.lastIndexOf("/") + 1);
            String path1 = hiddenDirectory + '/' + filename;//hidden path of our file
            f1 = new File(path1);
            SaveData(filename, path); //saving data in sharedpreferences
            Boolean bool=false;
            media_scanning(f);
            bool = f.renameTo(f1);
            scanning();
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(root);
                break;
            case CUSTOM_DIALOG:
                ListDir_2();

        }
    }

    void ListDir(File f) {
        if(x==1) {
                curFolder = f.getParentFile();
                File[] files = curFolder.listFiles();
                fileList.clear();
                if(files!=null) {
                    for (File file : files) {
                        fileList.add(file.getPath());
                    }
                }
                fileList.remove(f.getPath());
                x = 0;
        }
        else{
                String name=f.getPath().substring(f.getPath().lastIndexOf("/") + 1);
                if(name.equals("0")){
                    textFolder.setText("Internal Storage");
                }
                else{
                    textFolder.setText("Hide folder : "+name);
                }
                curFolder=f;
                File[] files = f.listFiles();

                fileList.clear();
                if(files!=null) {
                    Arrays.sort(files, new Comparator<File>(){   //Sorting so that files modified recently appear on top
                        public int compare(File f1, File f2)
                        {
                            return -Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                        }
                    });
                    for (File file : files) {
                        if (!file.getPath().substring(file.getPath().lastIndexOf("/") + 1).startsWith("."))
                            fileList.add(file.getPath());
                    }
                }

        }
        try {
            if(dialog_ListView.getAdapter()==null)
                dialog_ListView.setAdapter(listadapter);
            else
                listadapter.notifyDataSetChanged();
        }catch(Exception e){
            Log.w("tag",e+"");
        }
    }

    void ListDir_1(String file1) {

        File[] files = hiddenfolder.listFiles();
        fileList.clear();
        if(files!=null) {
            for (File file : files) {
                String filename = file.getPath();    //give the name of hidden file
                fileList.add(filename);
            }
        }
        fileList.remove(file1);
        listadapterhide.notifyDataSetChanged();
    }

    void ListDir_2() {
        File[] files = hiddenfolder.listFiles();
        fileList.clear();
        if(files!=null) {
            for (File file : files) {
                String filename = file.getPath();    //give the name of hidden file
                fileList.add(filename);
            }
        }
        try {
                 if(listView.getAdapter()==null)
                    listView.setAdapter(listadapterhide);
                else
                    listadapterhide.notifyDataSetChanged();
        }catch(Exception e){
            Log.w("tag",e+"");
        }
    }


    private void SaveData(String name,String path){
        SharedPreferences sharedpref=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpref.edit();
        editor.putString(name,path);
        editor.apply();
       // Log.w("backup","backup");
        new BackupManager(this).dataChanged();

    }
    private String RetrieveData(String name){
        SharedPreferences sharedpref=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        String path=sharedpref.getString(name,"");
        Log.i(TAG,"path:"+path);
        SharedPreferences.Editor editor=sharedpref.edit();
        editor.remove(name);
        editor.apply();
        new BackupManager(this).dataChanged();
        return path;
    }
   public void image(View view){
       if (ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {
           request_permission();
       }
       count++;
       if(count%2==0){
           adload();
       }
       Intent i=new Intent(this,Main3Activity.class);
       i.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
       startActivity(i);
    }

 /*   private class DoThing extends AsyncTask<Object,File , File> {
        private ProgressDialog progress;
        @Override
        protected File doInBackground(Object... params) {
            File file = (File) params[0];
            File file1 = (File) params[1];
            Boolean bool=false;
            bool = file.renameTo(file1);
            //scanFile(file.toString(),true);
            return file;
        }
        @Override
        protected void onPostExecute(final File file) {
            Toast.makeText(Main2Activity.this, "Done", Toast.LENGTH_SHORT).show();
        }
    }
*/

    //for changing password
    void dialog_passwordchange(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final ViewGroup nullParent = null;
        final View dialogView = inflater.inflate(R.layout.change_password, nullParent);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.editText);
        editText.setHint("Old Password");
        final EditText editText1 = (EditText) dialogView.findViewById(R.id.editText2);
        final EditText editText2 = (EditText) dialogView.findViewById(R.id.editText3);
        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean allset=false;
                //Do stuff, possibly set allset to true then...
                String passkey=editText.getText().toString();
                if(passkey.equals(RetrieveData())){
                    if(editText1.getText().toString().equals("")){
                        editText1.setError("please enter a valid password");
                        editText1.requestFocus();
                    }
                    else if(!isValidPassword(editText1.getText().toString())){
                        editText1.setError("Password contain only numbers (0-9)");
                        editText1.requestFocus();
                    }
                    else if(editText1.getText().toString().equals(editText2.getText().toString())){
                        allset=true;
                    }
                    else{
                        editText2.setError("password does not match");
                        editText2.requestFocus();
                    }
                }
                else{
                    editText.setError("input correct password");
                    editText.requestFocus();
                }
                if(allset) {
                    SharedPreferences sharedpref=getSharedPreferences("passworddata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedpref.edit();
                    editor.putString("passkey",editText1.getText().toString());
                    editor.apply();
                    dialog.dismiss();
                    new BackupManager(Main2Activity.this).dataChanged();
                }


            }


        });


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                    dialog.dismiss();
            }
        });
    }

    boolean isValidPassword(String s) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private String RetrieveData(){
        SharedPreferences sharedpref=getSharedPreferences("passworddata", Context.MODE_PRIVATE);
        return sharedpref.getString("passkey","");

    }


    public void adload(){
        AdRequest adRequest = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(Main2Activity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void showRewardedVideo() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }



}
