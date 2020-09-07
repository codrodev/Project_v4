package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerializableParcelDetails {

@SerializedName("service_response")
private List<ParcelDetails> service_response;
@SerializedName("is_exception")
private boolean is_exception;
@SerializedName("message")
private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_ar() {
        return message_ar;
    }

    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }

    @SerializedName("message_ar")
private String message_ar;

    public List<ParcelDetails> getService_response() {
        return service_response;
    }

    public void setService_response(List<ParcelDetails> service_response) {
        this.service_response = service_response;
    }

    public boolean isIs_exception() {
        return is_exception;
    }

    public void setIs_exception(boolean is_exception) {
        this.is_exception = is_exception;
    }


}
