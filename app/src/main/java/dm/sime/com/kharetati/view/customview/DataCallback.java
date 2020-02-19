package dm.sime.com.kharetati.view.customview;

import org.json.JSONObject;

/**
 * Created by hasham on 8/11/2017.
 */

public interface DataCallback {
  void onSuccess(JSONObject result);
  void onDownloadFinish(Object data);
}
