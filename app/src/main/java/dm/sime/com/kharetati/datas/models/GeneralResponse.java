package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

/**
 * Created by hasham on 9/20/2017.
 */

public class GeneralResponse extends BaseObservable {
  private boolean isError;
  private String message;
  private int status;

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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
