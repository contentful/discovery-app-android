package com.contentful.discovery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.StringRes;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.db.schema.CredentialsTable;
import org.apache.commons.lang3.StringUtils;

/**
 * Database Helper.
 */
public class DBHelper extends SQLiteOpenHelper {
  private static DBHelper sInstance;

  /**
   * Database configurations
   */
  private static final String DB_NAME =
      CFApp.getInstance().getResources().getString(R.string.db_name);

  private static final Integer DB_VERSION =
      Integer.valueOf(CFApp.getInstance().getResources().getString(R.string.db_version));

  public synchronized static DBHelper getInstance() {
    if (sInstance == null) {
      sInstance = new DBHelper(CFApp.getInstance());
    }

    return sInstance;
  }

  private DBHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
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

  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
  }

  /**
   * Insert static data to the DB.
   *
   * @param db Writable {@code SQLiteDatabase} instance.
   */
  private void insertStaticData(SQLiteDatabase db) {
    ContentValues values = new ContentValues();

    // Ancient Myths Space
    updateValuesFromStrings(values, R.string.demo_space_name, R.string.demo_space_key,
        R.string.demo_space_token);

    db.insert(CredentialsTable.NAME, null, values);
  }

  private static void updateValues(ContentValues values, String spaceName, String spaceKey,
      String accessToken) {

    if (StringUtils.isNotBlank(spaceName)) {
      values.put(CredentialsTable.Columns.SPACE_NAME, spaceName);
    }

    values.put(CredentialsTable.Columns.SPACE, spaceKey);
    values.put(CredentialsTable.Columns.TOKEN, accessToken);
  }

  private static void updateValuesFromStrings(ContentValues values, @StringRes int spaceName,
      @StringRes int spaceKey, @StringRes int accessToken) {
    Context context = CFApp.getInstance();
    updateValues(values, context.getString(spaceName), context.getString(spaceKey),
        context.getString(accessToken));
  }
}
