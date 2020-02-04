package dm.sime.com.kharetati.datas.models;

import java.util.ArrayList;
import java.util.List;

public class RetrieveMyMapResponse {

    private String email_id;
    private String callback_url;
    private long parcel_id;
    private String message_ar;

    private String voucher_amount_text;

    private String voucher_no;

    private String mobile;

    private String message_en;

    private int voucher_amount;

    private String customer_name;

    private String request_id;

    private String erad_payment_url;

    private List<MyMapResults> lstMyMap;

    public RetrieveMyMapResponse(){

    }
    public List<MyMapResults> getMyMapList(){
        lstMyMap = new ArrayList<>();
        populateMyMap();
        return lstMyMap;
    }

    private void populateMyMap(){
        MyMapResults obj;
        for (int x = 0; x < lstMyMap.size(); x++){
            obj = new MyMapResults();

            obj.setParcelId(String.valueOf(11120 + x));
            obj.setArea(String.valueOf(PlotDetails.area));
            obj.setArea(String.valueOf("area "+x));
            obj.setReqCreatedDate("12/05/2020");

            lstMyMap.add(obj);
        }
    }


    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getVoucher_amount_text() {
        return voucher_amount_text;
    }

    public void setVoucher_amount_text(String voucher_amount_text) {
        this.voucher_amount_text = voucher_amount_text;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public void setVoucher_no(String voucher_no) {
        this.voucher_no = voucher_no;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getVoucher_amount() {
        return voucher_amount;
    }

    public void setVoucher_amount(int voucher_amount) {
        this.voucher_amount = voucher_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getErad_payment_url() {
        return erad_payment_url;
    }

    public void setErad_payment_url(String erad_payment_url) {
        this.erad_payment_url = erad_payment_url;
    }



    public long getParcel_id() {
        return parcel_id;
    }

    public void setParcel_id(long parcel_id) {
        this.parcel_id = parcel_id;
    }




    private MyMapResults[] mymapresults;

    private String status;

    public String getMessage_ar ()
    {
        return message_ar;
    }

    public void setMessage_ar (String message_ar)
    {
        this.message_ar = message_ar;
    }

    public String getMessage_en ()
    {
        return message_en;
    }

    public void setMessage_en (String message_en)
    {
        this.message_en = message_en;
    }

    public MyMapResults[] getMyMapResults ()
    {
        return mymapresults;
    }

    public void setMyMapResults (MyMapResults[] mymapresults)
    {
        this.mymapresults = mymapresults;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
