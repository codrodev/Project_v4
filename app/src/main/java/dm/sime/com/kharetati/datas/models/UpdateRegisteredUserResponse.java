package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

/**
 * Created by hasham on 9/13/2017.
 */

public class UpdateRegisteredUserResponse extends BaseObservable {
  public boolean isError;
  public String message;
  public int userID;
  public String deviceID;

  public boolean isError() {
    return isError;
  }

  public void setError(boolean error) {
    isError = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }
}
