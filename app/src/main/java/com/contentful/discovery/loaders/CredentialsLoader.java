package com.contentful.discovery.loaders;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.db.DBHelper;
import com.contentful.discovery.db.schema.CredentialsTable;
import java.util.LinkedHashSet;

/**
 * Credentials Loader.
 * Use to load all CDA Content Types from the current Space.
 */
public class CredentialsLoader extends AbsAsyncTaskLoader<LinkedHashSet<Credentials>> {
  @Override protected LinkedHashSet<Credentials> performLoad() {
    SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
    String[] columns = new String[] {
        CredentialsTable.Columns.SPACE_NAME, CredentialsTable.Columns.SPACE,
        CredentialsTable.Columns.TOKEN, CredentialsTable.Columns.LAST_LOGIN
    };
    String order = CredentialsTable.Columns._ID + " DESC";
    Cursor cursor = null;
    LinkedHashSet<Credentials> loadResult = null;

    try {
      cursor = db.query(CredentialsTable.NAME, columns, null, null, null, null, order);
      if (cursor.moveToFirst()) {
        loadResult = new LinkedHashSet<>();
        int spaceNameIndex = cursor.getColumnIndex(CredentialsTable.Columns.SPACE_NAME);
        int spaceIndex = cursor.getColumnIndex(CredentialsTable.Columns.SPACE);
        int tokenIndex = cursor.getColumnIndex(CredentialsTable.Columns.TOKEN);
        int lastLoginIndex = cursor.getColumnIndex(CredentialsTable.Columns.LAST_LOGIN);

        do {
          loadResult.add(
              new Credentials(cursor.getString(spaceNameIndex), cursor.getString(spaceIndex),
                  cursor.getString(tokenIndex), cursor.getLong(lastLoginIndex)));
        } while (cursor.moveToNext());
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return loadResult;
  }
}
