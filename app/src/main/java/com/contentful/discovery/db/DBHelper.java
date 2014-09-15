package com.contentful.discovery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.db.schema.CredentialsTable;

/**
 * Database Helper.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;

    /**
     * Database configurations
     */
    private static final String DB_NAME = CFApp.getInstance()
            .getResources()
            .getString(R.string.db_name);

    private static final Integer DB_VERSION = Integer.valueOf(CFApp.getInstance()
            .getResources()
            .getString(R.string.db_version));

    public synchronized static DBHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DBHelper(CFApp.getInstance());
        }

        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();

            // create tables
            db.execSQL(CredentialsTable.CREATE);

            // insert static data
            insertStaticData(db);

            // commit
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    /**
     * Insert static data to the DB.
     *
     * @param db Writable {@code SQLiteDatabase} instance.
     */
    private void insertStaticData(SQLiteDatabase db) {
        Context context = CFApp.getInstance();
        ContentValues values = new ContentValues();

        values.put(CredentialsTable.Columns.SPACE_NAME,
                context.getString(R.string.demo_space_name));

        values.put(CredentialsTable.Columns.SPACE,
                context.getString(R.string.demo_space_key));

        values.put(CredentialsTable.Columns.TOKEN,
                context.getString(R.string.demo_space_token));

        db.insert(CredentialsTable.NAME, null, values);
    }
}
