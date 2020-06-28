package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse {
    @SerializedName("service_response")
    @Expose
    private ServiceResponse serviceResponse;
    @SerializedName("is_exception")
    @Expose
    private Boolean isException;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_ar")
    @Expose
    private String messageAr;

    public ServiceResponse getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public Boolean getIsException() {
        return isException;
    }

    public void setIsException(Boolean isException) {
        this.isException = isException;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageAr() {
        return messageAr;
    }

    public void setMessageAr(String messageAr) {
        this.messageAr = messageAr;
    }

}
