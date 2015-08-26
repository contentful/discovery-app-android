package com.contentful.discovery.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {
  private String spaceName;
  private String space;
  private String accessToken;
  private Long lastLogin;

  public Credentials(String spaceName, String space, String accessToken, Long lastLogin) {
    this.spaceName = spaceName;
    this.space = space;
    this.accessToken = accessToken;
    this.lastLogin = lastLogin;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel out, int i) {
    out.writeString(spaceName);
    out.writeString(space);
    out.writeString(accessToken);
    out.writeValue(lastLogin);
  }

  public static final Parcelable.Creator<Credentials> CREATOR =
      new Parcelable.Creator<Credentials>() {
        public Credentials createFromParcel(Parcel in) {
          return new Credentials(in);
        }

        public Credentials[] newArray(int size) {
          return new Credentials[size];
        }
      };

  private Credentials(Parcel in) {
    spaceName = in.readString();
    space = in.readString();
    accessToken = in.readString();
    lastLogin = (Long) in.readValue(Long.class.getClassLoader());
  }

  public void setSpaceName(String spaceName) {
    this.spaceName = spaceName;
  }

  public void setLastLogin(Long lastLogin) {
    this.lastLogin = lastLogin;
  }

  public String getSpaceName() {
    return spaceName;
  }

  public String getSpace() {
    return space;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Long getLastLogin() {
    return lastLogin;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || ((Object) this).getClass() != o.getClass()) return false;

    Credentials that = (Credentials) o;

    if (!accessToken.equals(that.accessToken)) return false;
    if (!space.equals(that.space)) return false;

    return true;
  }

  @Override public int hashCode() {
    int result = space.hashCode();
    result = 31 * result + accessToken.hashCode();
    return result;
  }
}
