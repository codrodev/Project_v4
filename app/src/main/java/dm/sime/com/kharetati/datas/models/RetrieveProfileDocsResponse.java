package dm.sime.com.kharetati.datas.models;

public class RetrieveProfileDocsResponse {
    private String emirates_id;
    private String nationality;

    public String getEmirates_id() {
        return emirates_id;
    }

    public void setEmirates_id(String emirates_id) {
        this.emirates_id = emirates_id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    private String name_en;
    private String name_ar;
    private String email_id;
    private String mobile_no;


    private DeliveryDetails delivery_details;

    private String messageAr;

    private Docs[] docs;

    private String messageEn;

    private String status;

    public String getMessageAr ()
    {
        return messageAr;
    }

    public void setMessageAr (String messageAr)
    {
        this.messageAr = messageAr;
    }

    public Docs[] getDocs ()
    {
        return docs;
    }

    public void setDocs (Docs[] docs)
    {
        this.docs = docs;
    }

    public String getMessageEn ()
    {
        return messageEn;
    }

    public void setMessageEn (String messageEn)
    {
        this.messageEn = messageEn;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public DeliveryDetails getDeliveryDetails() {
        return delivery_details;
    }

    public void setDeliveryDetails(DeliveryDetails delivery_details) {
        this.delivery_details = delivery_details;
    }

}
