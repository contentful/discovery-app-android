package com.contentful.discovery.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.contentful.discovery.CFApp;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.db.DBHelper;
import com.contentful.discovery.db.schema.CredentialsTable;
import com.contentful.discovery.utils.IntentConsts;

import org.apache.commons.lang3.StringUtils;

/**
 * Database IntentService.
 * An IntentService to use for performing operations against the local SQLite database.
 */
public class DBIntentService extends IntentService {
    public static Intent withAction(String action) {
        return new Intent(CFApp.getInstance(), DBIntentService.class)
                .setAction(action);
    }

    public DBIntentService() {
        super(DBIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (IntentConsts.DB.ACTION_SAVE_CREDENTIALS.equals(action)) {
            saveCredentials(intent);
        }
    }

    /**
     * Saves a set of Credentials to the database.
     */
    private void saveCredentials(Intent intent) {
        Credentials credentials = intent.getParcelableExtra(IntentConsts.EXTRA_CREDENTIALS);

        String space = credentials.getSpace();
        String accessToken = credentials.getAccessToken();

        // Validation
        if (credentials == null) {
            throw new IllegalArgumentException("No credentials supplied.");
        }

        if (StringUtils.isBlank(space) ||
                StringUtils.isBlank(accessToken)) {

            throw new IllegalArgumentException("Invalid credentials.");
        }

        // Insert
        ContentValues values = new ContentValues();

        values.put(CredentialsTable.Columns.SPACE_NAME, credentials.getSpaceName());
        values.put(CredentialsTable.Columns.SPACE, space);
        values.put(CredentialsTable.Columns.TOKEN, accessToken);
        values.put(CredentialsTable.Columns.LAST_LOGIN, credentials.getLastLogin());

        DBHelper.getInstance()
                .getWritableDatabase()
                .insertWithOnConflict(
                        CredentialsTable.NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
    }
}
