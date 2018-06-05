package com.calculator.hide.vault;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by Nishit on 13-03-2018.
 */

public class MyBackupAgent extends BackupAgentHelper {
        // The name of the SharedPreferences file
        static final String passData = "passworddata";
        static final String fileData = "userinfo";

        // A key to uniquely identify the set of backup data
        static final String PREFS_BACKUP_KEY = "prefs";

        // Allocate a helper and add it to the backup agent
        @Override
        public void onCreate() {

                Log.e("TAG","backup  asdasd asdad");
            SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, passData,fileData);
            addHelper(PREFS_BACKUP_KEY, helper);
        }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
    }
}

