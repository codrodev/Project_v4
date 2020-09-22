package dm.sime.com.kharetati.datas.models;

import android.annotation.SuppressLint;

import androidx.databinding.BaseObservable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dm.sime.com.kharetati.utility.Global;

public class MyMapResults extends BaseObservable {
    private String request_status;

    public String getRequestStatusAr() {
        return request_status_ar;
    }

    public void setRequestStatusAr(String request_status_ar) {
        this.request_status_ar = request_status_ar;
    }

    private String request_status_ar;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = String.valueOf(PlotDetails.area);
    }

    private String area;// temp

    private String email_id;

    private String is_payment_pending;

    private String payment_status;

    private String voucher_no;

    private String mobile;

    private String parcel_id;

    private String req_created_date_time;

    private String callback_url;

    private String group_request_id;

    private String is_plan_ready;

    private int voucher_amount;

    private String customer_name;

    private String request_id;

    private String erad_payment_url;

    public String getRequest_status() {
        return request_status;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getIs_payment_pending() {
        return is_payment_pending;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public String getParcel_id() {
        return parcel_id;
    }

    public String getReq_created_date() {

        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",new Locale("en"));
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = Global.CURRENT_LOCALE.equals("en")? new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa",new Locale("en")):new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa ", new Locale("en"));

        String resultDate = "";
        try {
            resultDate=outputFormat.format(inputFormat.parse(req_created_date_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return resultDate;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public String getGroup_request_id() {
        return group_request_id;
    }

    public String getIs_plan_ready() {
        return is_plan_ready;
    }

    public int getVoucher_amount() {
        return voucher_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getErad_payment_url() {
        return erad_payment_url;
    }

    public String getRequestStatus ()
    {
        return request_status;
    }

    public void setRequestStatus (String request_status)
    {
        this.request_status = request_status;
    }

    public String getEmailId ()
    {
        return email_id;
    }

    public void setEmailId (String email_id)
    {
        this.email_id = email_id;
    }

    public String getIsPaymentPending ()
    {
        return is_payment_pending;
    }

    public void setIsPaymentPending (String is_payment_pending)
    {
        this.is_payment_pending = is_payment_pending;
    }

    public String getPaymentStatus ()
    {
        return payment_status;
    }

    public void setPaymentStatus (String payment_status)
    {
        this.payment_status = payment_status;
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

    public String getParcelId ()
    {
        return parcel_id;
    }

    public void setParcelId (String parcel_id)
    {
        this.parcel_id = parcel_id;
    }

    public String getReqCreatedDate ()
    {
        return req_created_date_time;
    }

    public void setReqCreatedDate (String req_created_date)
    {
        this.req_created_date_time = req_created_date;
    }

    public String getCallbackUrl ()
    {
        return callback_url;
    }

    public void setCallbackUrl (String callback_url)
    {
        this.callback_url = callback_url;
    }

    public String getGroupRequestId ()
    {
        return group_request_id;
    }

    public void setGroupRequestId (String group_request_id)
    {
        this.group_request_id = group_request_id;
    }

    public String getIsPlanReady ()
    {
        return is_plan_ready;
    }

    public void setIsPlanReady (String is_plan_ready)
    {
        this.is_plan_ready = is_plan_ready;
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


}
