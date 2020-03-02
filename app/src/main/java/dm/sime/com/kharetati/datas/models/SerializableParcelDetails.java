package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerializableParcelDetails {

@SerializedName("service_response")
private List<ParcelDetails> service_response;
@SerializedName("is_exception")
private boolean is_exception;
@SerializedName("message")
private boolean message;
@SerializedName("message_ar")
private boolean message_ar;

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

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public boolean isMessage_ar() {
        return message_ar;
    }

    public void setMessage_ar(boolean message_ar) {
        this.message_ar = message_ar;
    }
}
