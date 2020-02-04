package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

public class CreateUpdateRequestResponse extends BaseObservable {
    private String email_id;

    private String callback_url;

    private String message_ar;

    private String voucher_amount_text;

    private String voucher_no;

    private String mobile;

    private String message_en;

    private int voucher_amount;

    private String customer_name;

    private String request_id;

    private String erad_payment_url;

    private int status;

    private long parcel_id;

    public String getEmailId ()
    {
        return email_id;
    }

    public void setEmailId (String email_id)
    {
        this.email_id = email_id;
    }

    public String getCallbackUrl ()
    {
        return callback_url;
    }

    public void setCallbackUrl (String callback_url)
    {
        this.callback_url = callback_url;
    }

    public String getMessageAr ()
    {
        return message_ar;
    }

    public void setMessageAr (String message_ar)
    {
        this.message_ar = message_ar;
    }

    public String getVoucherAmountText ()
    {
        return voucher_amount_text;
    }

    public void setVoucherAmountText (String voucher_amount_text)
    {
        this.voucher_amount_text = voucher_amount_text;
    }

    public String getVoucherNo ()
    {
        return voucher_no;
    }

    public void setVoucherNo (String voucher_no)
    {
        this.voucher_no = voucher_no;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getMessageEn ()
    {
        return message_en;
    }

    public void setMessageEn (String message_en)
    {
        this.message_en = message_en;
    }

    public int getVoucherAmount ()
    {
        return voucher_amount;
    }

    public void setVoucherAmount (int voucher_amount)
    {
        this.voucher_amount = voucher_amount;
    }

    public String getCustomerName ()
    {
        return customer_name;
    }

    public void setCustomerName (String customer_name)
    {
        this.customer_name = customer_name;
    }

    public String getRequestId ()
    {
        return request_id;
    }

    public void setRequestId (String request_id)
    {
        this.request_id = request_id;
    }

    public String getEradPaymentUrl ()
    {
        return erad_payment_url;
    }

    public void setEradPaymentUrl (String erad_payment_url)
    {
        this.erad_payment_url = erad_payment_url;
    }

    public int getStatus ()
    {
        return status;
    }

    public void setStatus (int status)
    {
        this.status = status;
    }

    public long getparcelID() {
        return parcel_id;
    }
}
