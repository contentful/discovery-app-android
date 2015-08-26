package com.contentful.discovery.db.schema;

import android.provider.BaseColumns;

public final class CredentialsTable {
  private CredentialsTable() {
  }

  public static final String NAME = "credentials";

  public static final class Columns implements BaseColumns {
    public static final String SPACE_NAME = "space_name";
    public static final String SPACE = "space";
    public static final String TOKEN = "token";
    public static final String LAST_LOGIN = "last_login";
  }

  public static final String CREATE = "CREATE TABLE " + NAME + " ("
      + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + Columns.SPACE_NAME + " STRING NOT NULL, "
      + Columns.SPACE + " STRING NOT NULL, "
      + Columns.TOKEN + " STRING NOT NULL, "
      + Columns.LAST_LOGIN + " STRING, "
      + "UNIQUE (" + Columns.SPACE + ", " + Columns.TOKEN + ")"
      + ");";
}
