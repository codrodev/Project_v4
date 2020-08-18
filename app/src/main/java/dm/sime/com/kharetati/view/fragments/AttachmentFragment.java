package dm.sime.com.kharetati.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.DoubleBuffer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentAttachmentBinding;
import dm.sime.com.kharetati.datas.models.AttachedDoc;
import dm.sime.com.kharetati.datas.models.AttachmentBitmap;
import dm.sime.com.kharetati.datas.models.DeliveryDetails;
import dm.sime.com.kharetati.datas.models.DocArr;
import dm.sime.com.kharetati.datas.models.Docs;
import dm.sime.com.kharetati.datas.models.LicenceDocs;
import dm.sime.com.kharetati.datas.models.NocDocs;
import dm.sime.com.kharetati.datas.models.PassportDocs;
import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.AttachmentRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FileDownloader;
import dm.sime.com.kharetati.utility.Files.DialogConfigs;
import dm.sime.com.kharetati.utility.Files.DialogProperties;
import dm.sime.com.kharetati.utility.Files.DialogSelectionListener;
import dm.sime.com.kharetati.utility.Files.DocumentUtility;
import dm.sime.com.kharetati.utility.Files.FilePickerDialog;
import dm.sime.com.kharetati.utility.Files.ListItem;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.ImageCropActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.activities.ViewImage;
import dm.sime.com.kharetati.view.customview.CameraPermissionInterface;
import dm.sime.com.kharetati.view.customview.DataCallback;
import dm.sime.com.kharetati.view.navigators.AttachmentNavigator;
import dm.sime.com.kharetati.view.viewModels.AttachmentViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.AttachmentViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.licenseData;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_ATTACHMENT;


public class AttachmentFragment extends Fragment implements AttachmentNavigator, CameraPermissionInterface, MainActivity.onPermissionResult, ImageCropActivity.onCropListener {

    private static final String TAG = "AttachmentFragment";
    public static String paymentUrl;
    public static String callBackURL;
    FragmentAttachmentBinding binding;
    AttachmentViewModel model;
    private View mRootView;
    private AttachmentRepository repository;
    private AttachmentViewModelFactory factory;
    private FilePickerDialog filePickerDialog;
    final DialogProperties properties=new DialogProperties();
    private int GALLERY = 1, CAMERA = 2, VIEW = 3, CHOOSER = 4;
    public static String currentSelection = "";
    public static final String LAND_OWNER_CERTIFICATE = "LAND_OWNER_CERTIFICATE";
    public static final String VISA_PASSPORT = "VISA_PASSPORT";
    public static final String COMPANY_LICENCE = "COMPANY_LICENSE";
    public static final String PASSPORT = "PASSPORT";
    public static final String LETTER_FROM_OWNER = "LETTER_FROM_OWNER";
    private ArrayList<ListItem> listItem;
    public static List<AttachedDoc> lstAttachedDoc = new ArrayList<AttachedDoc>();

    private Bitmap retrieved_license,retrieved_noc,retrieved_passport,retrieved_visa;
    public static Uri photoURI;
    public static Uri contentURI;
    public static Bitmap thumbnail;
    public Bitmap viewBitmap,myBitmap,pdfBitmap;
    public static boolean isGallery,isCamera=false;
    private int GALLERY_CROP=111;
    private Uri galleryURI;
    private String mCurrentPhotoPath;
    private AttachedDoc attachedDoc;
    private File dwldsPath;
    private Bitmap front;
    private String imageType;
    public static AttachmentViewModel attachmentModel;
    private int docid_passort,docid_visaPassport;
    private Tracker mTracker;
    private ArrayList<Integer> previousUploadDocIds;
    private boolean isDocumentAdded;


    public static AttachmentFragment newInstance(){
        AttachmentFragment fragment = new AttachmentFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new AttachmentRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new AttachmentViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(AttachmentViewModel.class);
        model.attachmentNavigator =this;
        ImageCropActivity.onCropListener =this;
        attachmentModel =model;
        listItem = new ArrayList<>();

        /*if(!ImageCropActivity.isImageCropped)
        {
            if (al == null) {
                al = new ArrayList<DocArr>();
            }
            if(oldDoc == null) {
                oldDoc = new ArrayList<DocArr>();
            }

            //clearImage();
        }*/
        lstAttachedDoc = new ArrayList<AttachedDoc>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), AppConstants.ALL_PERMISSIONS, AppConstants.REQUEST_READ_EXTERNAL_STORAGE);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FR_ATTACHMENT;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attachment, container, false);
        binding.setFragmentAttachmentVM(model);
        mRootView = binding.getRoot();
        initializePage();

        setRetainInstance(true);
        if(CURRENT_LOCALE.equals("en")) binding.rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializePage() {
        MainActivity.listner = this;
        clearAttachments();
        /*if(!DeliveryFragment.isDeliveryFragment)
            ParentSiteplanViewModel.getDownloadedDoc().clear();*/
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_ATTACHMENT);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        previousUploadDocIds = new ArrayList();

        /*for(int i=0;i<Global.docArr.length;i++){
            previousUploadDocIds.add(Integer.valueOf(Global.docArr[i].getDocid()));
        }
        Collections.sort(previousUploadDocIds);*/


        //clearBitMap();

        int permission = ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            if (Global.docArr != null && Global.docArr.length > 0)
            {
                if((ParentSiteplanViewModel.getDownloadedDoc() == null || ParentSiteplanViewModel.getDownloadedDoc().size() == 0)&&(!ImageCropActivity.isImageCropped))
                {
                    if (Global.isPerson && Global.rbIsOwner  ) {
                        //if (!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport))
                        downloadDocs(Global.docArr);
                    }
                    else if(Global.isPerson && !Global.rbIsOwner ){
                        //if(!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport) ||!getBitmapFromView(binding.imgLetterFromOwner))
                        downloadDocs(Global.docArr);
                    }
                    else if(!Global.isPerson && !Global.rbIsOwner ){
                        //if(!getBitmapFromView(binding.imgCompanyLicense) ||!getBitmapFromView(binding.imgLetterFromOwner))
                        downloadDocs(Global.docArr);
                    }
                }
            }
            else
                imageAlignment();
        }

        attachmentState();

        filePickerDialog=new FilePickerDialog(getActivity(),properties);
        filePickerDialog.setTitle("Select a File");
        filePickerDialog.setPositiveBtnName("Select");
        filePickerDialog.setNegativeBtnName("Cancel");

        setDialogProperties();

        binding.imgPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = PASSPORT;
                if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                    showPictureDialog();
                else
                    showPictureDialog1();

            }
        });
        binding.imgVisaPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = VISA_PASSPORT;
                if(((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                    showPictureDialog();
                else
                    showPictureDialog1();
            }
        });
        binding.imgCompanyLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = COMPANY_LICENCE;
                if(((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                    showPictureDialog();
                else
                    showPictureDialog1();

            }
        });
        binding.imgLetterFromOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = LETTER_FROM_OWNER;
                if(((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                    showPictureDialog();
                else
                    showPictureDialog1();

            }
        });
        /*binding.imgLandOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = LAND_OWNER_CERTIFICATE;
                if(((BitmapDrawable) binding.imgLandOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                    showPictureDialog();
                else
                    showPictureDialog1();

            }
        });*/
        binding.fragmentAttachmentLblDownloadNoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else
                    AlertDialogUtil.downloaNocAlert(getActivity().getResources().getString(R.string.confirmation_openlink),getActivity().getResources().getString(R.string.ok),getActivity().getResources().getString(R.string.cancel),getActivity());

            }
        });


        binding.personalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentSelection = PASSPORT;
                    viewImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.visaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentSelection=VISA_PASSPORT;
                    viewImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.licenseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentSelection=COMPANY_LICENCE;
                    viewImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.nocView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentSelection=LETTER_FROM_OWNER;
                    viewImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        binding.personalChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection=PASSPORT;
                showPictureDialog();

            }
        });
        binding.visaChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection=VISA_PASSPORT;
                showPictureDialog();

            }
        });
        binding.licenseChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection=COMPANY_LICENCE;
                showPictureDialog();

            }
        });
        binding.nocChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection=LETTER_FROM_OWNER;
                showPictureDialog();

            }
        });

        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of paths selected by the App User.
                int size = listItem.size();
                listItem.clear();

                for(String path:files) {
                    File myFile=new File(path);
                    String fileExtension = DocumentUtility.getFileExtension(myFile.getName()).toLowerCase();
                    if (DocumentUtility.isCorrectDocExtension(fileExtension)) {


                        createAttachedDoc(encodeFileToBase64Binary(myFile), "application/pdf",
                                getCurrentKey(), myFile.getName(), getDocId(currentSelection), currentSelection);
                        AddDoc(currentSelection, myFile.getPath(), myFile.getName(), "application/pdf", 0);

                        if (currentSelection == PASSPORT) {

                            isCamera = false;
                            isGallery =false;

                            binding.imgPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                            binding.personalView.setVisibility(View.VISIBLE);
                            binding.personalChange.setVisibility(View.VISIBLE);

                        } else if (currentSelection == LETTER_FROM_OWNER) {

                            isCamera = false;
                            isGallery =false;
                            binding.imgLetterFromOwner.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                            binding.nocView.setVisibility(View.VISIBLE);
                            binding.nocChange.setVisibility(View.VISIBLE);
                        } else if (currentSelection == VISA_PASSPORT) {

                            isCamera = false;
                            isGallery =false;
                            binding.imgVisaPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                            binding.visaView.setVisibility(View.VISIBLE);
                            binding.visaChange.setVisibility(View.VISIBLE);
                        } else if (currentSelection == COMPANY_LICENCE) {

                            isCamera = false;
                            isGallery =false;
                            binding.imgCompanyLicense.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                            binding.licenseView.setVisibility(View.VISIBLE);
                            binding.licenseChange.setVisibility(View.VISIBLE);
                        }

                    }
                    imageAlignment();
                }

            }
        });

        if(Global.docArr!=null){

            for(int i=0;i<Global.docArr.length;i++){

                Global.docID=Global.docArr[i].getDocid();

                if(Global.docID!=null && Global.docArr[i].getDoctype().equals("passport"))
                {
                    binding.passportButtons.setVisibility(View.VISIBLE);
                    binding.visaPassportButtons.setVisibility(View.VISIBLE);
                }
                if(Global.docID!=null && Global.docArr[i].getDoctype().equals("passport"))
                {
                    binding.passportButtons.setVisibility(View.VISIBLE);
                    binding.visaPassportButtons.setVisibility(View.VISIBLE);
                }
                else if(Global.docID!=null && Global.docArr[i].getDoctype().equals("license"))
                {
                    binding.companyLicenseButtons.setVisibility(View.VISIBLE);
                }
                else if(Global.docID!=null && Global.docArr[i].getDoctype().equals("noc"))
                {
                    binding.nocButtons.setVisibility(View.VISIBLE);
                }

            }

        }

        if(retrieved_passport!=null)
            imageAlignment();
        else
            binding.imgPassport.setImageResource(R.drawable.photo);
        if(retrieved_visa!=null)
            imageAlignment();
        else
            binding.imgVisaPassport.setImageResource(R.drawable.photo);
        if(retrieved_license!=null)
            imageAlignment();
        else
            binding.imgCompanyLicense.setImageResource(R.drawable.photo);
        if(retrieved_noc!=null)
            imageAlignment();
        else
            binding.imgLetterFromOwner.setImageResource(R.drawable.photo);


        if(ParentSiteplanViewModel.status==501){
            // I am Owner (person)
            if(retrieved_passport!=null)
                binding.imgPassport.setImageBitmap(retrieved_passport);
            else if(AttachmentBitmap.passport_copy!=null)
                binding.imgPassport.setImageBitmap(AttachmentBitmap.passport_copy);
            else
                binding.imgPassport.setImageResource(R.drawable.photo);
            if(retrieved_visa!=null)
                binding.imgVisaPassport.setImageBitmap(retrieved_visa);
            else if(AttachmentBitmap.visa_passport!=null)
                binding.imgVisaPassport.setImageBitmap(AttachmentBitmap.visa_passport);
            else
                binding.imgVisaPassport.setImageResource(R.drawable.photo);
            if(getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);

            retrieved_passport=null;
            retrieved_visa=null;


        }
        else if(ParentSiteplanViewModel.status==502){
            // I am Not owner (person)
            if(retrieved_passport!=null)
                binding.imgPassport.setImageBitmap(retrieved_passport);
            else if(AttachmentBitmap.passport_copy!=null)
                binding.imgPassport.setImageBitmap(AttachmentBitmap.passport_copy);
            else
                binding.imgPassport.setImageResource(R.drawable.photo);
            if(retrieved_visa!=null)
                binding.imgVisaPassport.setImageBitmap(retrieved_visa);
            else if(AttachmentBitmap.visa_passport!=null)
                binding.imgVisaPassport.setImageBitmap(AttachmentBitmap.visa_passport);
            else
                binding.imgVisaPassport.setImageResource(R.drawable.photo);
            if(retrieved_noc!=null)
                binding.imgLetterFromOwner.setImageBitmap(retrieved_noc);
            else if(AttachmentBitmap.letter_from_owner!=null)
                binding.imgLetterFromOwner.setImageBitmap(AttachmentBitmap.letter_from_owner);
            else
                binding.imgLetterFromOwner.setImageResource(R.drawable.photo);

            if(getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport) && getBitmapFromView(binding.imgLetterFromOwner))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
            retrieved_passport=null;
            retrieved_visa=null;
            retrieved_noc=null;


        }
        else if(ParentSiteplanViewModel.status==503){
            // Company

            if(retrieved_license!=null)
                binding.imgCompanyLicense.setImageBitmap(retrieved_license);
            else if(AttachmentBitmap.company_license!=null)
                binding.imgCompanyLicense.setImageBitmap(AttachmentBitmap.company_license);
            else
                binding.imgCompanyLicense.setImageResource(R.drawable.photo);
            if(retrieved_noc!=null)
                binding.imgLetterFromOwner.setImageBitmap(retrieved_noc);
            else if(AttachmentBitmap.letter_from_owner!=null)
                binding.imgLetterFromOwner.setImageBitmap(AttachmentBitmap.letter_from_owner);
            else
                binding.imgLetterFromOwner.setImageResource(R.drawable.photo);

            if(getBitmapFromView(binding.imgCompanyLicense) && getBitmapFromView(binding.imgLetterFromOwner))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);

            retrieved_license=null;
            retrieved_noc=null;

        }
        else if(ParentSiteplanViewModel.status==504){
            // No previously uploaded documents
            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        }




    }
    public Boolean getBitmapFromView(ImageView imageView){

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        if(getActivity() !=null)
            return (bitmap!=null && !(bitmap==((BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.photo)).getBitmap()));
        else
            return false;

    }

    public void attachmentState() {
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        if(Global.isPerson && Global.rbIsOwner){


            binding.cardPassport.setVisibility(View.VISIBLE);
            binding.cardVisaPassport.setVisibility(View.VISIBLE);
            binding.fragmentAttachementDownloadnocLayout.setVisibility(View.GONE);
            binding.cardLetterFromOwner.setVisibility(View.GONE);
            binding.cardCompanyLicense.setVisibility(View.GONE);
            //payButtonsLayout.setVisibility(View.VISIBLE);
            //submit.setVisibility(View.GONE);

        }
        else if(Global.isPerson && Global.rbNotOwner ){

            binding.cardPassport.setVisibility(View.VISIBLE);
            binding.cardVisaPassport.setVisibility(View.VISIBLE);
            binding.fragmentAttachementDownloadnocLayout.setVisibility(View.VISIBLE);
            binding.cardLetterFromOwner.setVisibility(View.VISIBLE);
            binding.cardCompanyLicense.setVisibility(View.GONE);
            //payButtonsLayout.setVisibility(View.VISIBLE);
            //submit.setVisibility(View.GONE);
            //ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);

        }
        else if(Global.isCompany){

            binding.cardPassport.setVisibility(View.GONE);
            binding.cardVisaPassport.setVisibility(View.GONE);
            binding.cardCompanyLicense.setVisibility(View.VISIBLE);
            binding.fragmentAttachementDownloadnocLayout.setVisibility(View.VISIBLE);
            binding.cardLetterFromOwner.setVisibility(View.VISIBLE);
            //payButtonsLayout.setVisibility(View.VISIBLE);
            //submit.setVisibility(View.GONE);
            //ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);

        }


    }




    private void showPictureDialog() {


        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle(getActivity().getResources().getString(R.string.CHOOSETHEOPTION));

        ArrayList al=new ArrayList();
        al.add(getActivity().getResources().getString(R.string.view));
        al.add(getActivity().getResources().getString(R.string.select_photo_gallery));
        al.add(getActivity().getResources().getString(R.string.capture_photo_camera));
        al.add(getActivity().getResources().getString(R.string.choose_from_explorer));

        String[] pictureDialogItems = {al.get(0).toString(), al.get(1).toString(), al.get(2).toString(), al.get(3).toString()};
        final List<String> myList = new ArrayList<String>(Arrays.asList(pictureDialogItems));


        myList.remove(al.get(0).toString());

        pictureDialogItems = myList.toArray(new String[0]);
        pictureDialogItems[0]=al.get(1).toString();
        pictureDialogItems[1]=al.get(2).toString();
        pictureDialogItems[2]=al.get(3).toString();
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        requestPermissions(AppConstants.PERMISSIONS_STORAGE, AppConstants.REQUEST_READ_EXTERNAL_STORAGE);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                                } else {
                                    choosePhotoFromGallary();
                                }

                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_CAMERA_PERMISSION);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                                } else {
                                    takePhotoFromCamera();
                                }
                                break;
                            case 2:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        ActivityCompat.requestPermissions(getActivity(), AppConstants.PERMISSIONS_STORAGE, AppConstants.REQUEST_READ_EXTERNAL_STORAGE);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                                } else {

                                    filePickerDialog.show();

                                }
                                break;
                        }

                        if(ImageCropActivity.resultBitmap!=null) myBitmap=ImageCropActivity.resultBitmap;
                    }
                });
        pictureDialog.show();

    }

    public void showPictureDialog1(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle(getActivity().getResources().getString(R.string.CHOOSETHEOPTION));
        String[] pictureDialogItems={/*getActivity().getResources().getString(R.string.clear),*/getActivity().getResources().getString(R.string.select_photo_gallery),getActivity().getResources().getString(R.string.capture_photo_camera),getActivity().getResources().getString(R.string.choose_from_explorer)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        ActivityCompat.requestPermissions(getActivity(), AppConstants.PERMISSIONS_STORAGE, AppConstants.REQUEST_READ_EXTERNAL_STORAGE);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                                } else {
                                    choosePhotoFromGallary();
                                }

                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_CAMERA_PERMISSION);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                                } else {
                                    takePhotoFromCamera();
                                }
                                break;
                            case 2:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    if (Global.isConnected(getActivity()))
                                        ActivityCompat.requestPermissions(getActivity(), AppConstants.PERMISSIONS_STORAGE, AppConstants.REQUEST_READ_EXTERNAL_STORAGE);
                                    else
                                        AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                                } else {
                                    filePickerDialog.show();
                                }
                                break;
                        }
                        myBitmap=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap();
                    }
                });
        pictureDialog.show();

    }

    /*private void deleteCurrent(String key){
        if(al != null && al.size() > 0){
            for (int i =0; i < al.size(); i++){
                if(al.get(i).getDocKey() != null && al.get(i).getDocKey().equals(key)){
                    al.remove(i);
                    break;
                }
            }
        }
    }*/

    private void setDialogProperties(){
        String fextension = AppConstants.DOC_TYPE_PDF;
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.extensions=new String[]{fextension};
        properties.selection_type=DialogConfigs.FILE_SELECT;
        properties.offset=new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir=new File("/mnt");
        //Set new properties of dialog.
        filePickerDialog.setProperties(properties);
    }



   /* private DocArr getDoc(String key){
        if(oldDoc != null && oldDoc.size() > 0){
            for (int i =0; i < oldDoc.size(); i++){
                if(oldDoc.get(i).getDocKey() != null && oldDoc.get(i).getDocKey().equals(key)){
                    return oldDoc.get(i);
                }
            }
        }
        return new DocArr();
    }*/

    /*public void clearBitMap(){
        retrieved_license=null;
        retrieved_noc=null;
        retrieved_passport = null;
        retrieved_visa = null;
    }*/


    public void takePhotoFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.kharetati.android.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA);


            }

        }

    }




    FileInputStream getSourceStream(Uri u) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    getActivity().getBaseContext().getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            out = new FileInputStream(fileDescriptor);
        } else {
            out = (FileInputStream) getActivity().getBaseContext().getContentResolver().openInputStream(u);
        }
        return out;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        Global.paymentStatus = null;
        Global.hideSoftKeyboard(getActivity());
        ParentSiteplanFragment.currentIndex =1;
        //setEmailAndMobileField();


       /* int permission = ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            if (Global.docArr != null && Global.docArr.length > 0)
            {
                if(ParentSiteplanViewModel.getDownloadedDoc() == null || ParentSiteplanViewModel.getDownloadedDoc().size() == 0)
                {
                    if (Global.isPerson && Global.rbIsOwner && Global.docArr.length<=2 ) {
                        if (!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport))
                            downloadDocs(Global.docArr);
                    }
                    else if(Global.isPerson && !Global.rbIsOwner && Global.docArr.length<=3 ){
                        if(!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport) ||!getBitmapFromView(binding.imgLetterFromOwner))
                            downloadDocs(Global.docArr);
                    }
                    else if(!Global.isPerson && !Global.rbIsOwner && Global.docArr.length<=2 ){
                        if(!getBitmapFromView(binding.imgCompanyLicense) ||!getBitmapFromView(binding.imgLetterFromOwner))
                            downloadDocs(Global.docArr);
                    }
                }
            }
            else
                imageAlignment();
        }*/

        if (ParentSiteplanViewModel.getNewlyAttachedDoc() != null &&
                ParentSiteplanViewModel.getNewlyAttachedDoc().size() > 0)
        {

            for (int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++)
            {
                if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat() == null)
                {

                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null)
                    {
                        if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(PASSPORT) == 0)
                        {
                            binding.imgPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                        }
                        else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(VISA_PASSPORT) == 0)
                        {
                            binding.imgVisaPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                        }
                        else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(COMPANY_LICENCE) == 0)
                        {
                            binding.imgCompanyLicense.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                        }
                        else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(LETTER_FROM_OWNER) == 0)
                        {
                            binding.imgLetterFromOwner.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                        }
                    }
                }
                else
                {
                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("pdf") == 0 ||
                            ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("application/pdf") == 0)
                    {
                        if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null)
                        {
                            if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(PASSPORT) == 0)
                            {
                                binding.imgPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                binding.personalView.setVisibility(View.VISIBLE);
                                binding.personalChange.setVisibility(View.VISIBLE);
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(VISA_PASSPORT) == 0)
                            {
                                binding.imgVisaPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                binding.visaView.setVisibility(View.VISIBLE);
                                binding.visaChange.setVisibility(View.VISIBLE);
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(COMPANY_LICENCE) == 0)
                            {
                                binding.imgCompanyLicense.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                binding.licenseView.setVisibility(View.VISIBLE);
                                binding.licenseChange.setVisibility(View.VISIBLE);
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(LETTER_FROM_OWNER) == 0)
                            {
                                binding.imgLetterFromOwner.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                binding.nocView.setVisibility(View.VISIBLE);
                                binding.nocChange.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    else if (model.isImageFormat(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat()))
                    {
                        if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null)
                        {
                            if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(PASSPORT) == 0)
                            {
                                if (AttachmentBitmap.passport_copy != null)
                                {
                                    binding.imgPassport.setImageBitmap(AttachmentBitmap.passport_copy);
                                }
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(VISA_PASSPORT) == 0)
                            {
                                if (AttachmentBitmap.visa_passport != null) {
                                    binding.imgVisaPassport.setImageBitmap(AttachmentBitmap.visa_passport);
                                }
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(COMPANY_LICENCE) == 0)
                            {
                                if (AttachmentBitmap.company_license != null) {
                                    binding.imgCompanyLicense.setImageBitmap(AttachmentBitmap.company_license);
                                }
                            }
                            else if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().compareToIgnoreCase(LETTER_FROM_OWNER) == 0)
                            {
                                if (AttachmentBitmap.letter_from_owner != null)
                                {
                                    binding.imgLetterFromOwner.setImageBitmap(AttachmentBitmap.letter_from_owner);
                                }
                            }
                        }
                    }

                }
            }
        }
        nextButtonStatus();
        imageAlignment();

    }
    public void nextButtonStatus(){

        if(ParentSiteplanViewModel.status==501){

            if(getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        }
        else if(ParentSiteplanViewModel.status==502){
            if(getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport) && getBitmapFromView(binding.imgLetterFromOwner))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);

        }
        else if(ParentSiteplanViewModel.status==503){
            if(getBitmapFromView(binding.imgCompanyLicense) && getBitmapFromView(binding.imgLetterFromOwner))
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
            else
                ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        }
        else {
            if(Global.isPerson && Global.rbIsOwner){
                if((getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport)))
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
            }
            else if(Global.isPerson && !Global.rbIsOwner){
                if(getBitmapFromView(binding.imgPassport) && getBitmapFromView(binding.imgVisaPassport) && getBitmapFromView(binding.imgLetterFromOwner))
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
            }
            else if(Global.isCompany){
                if(getBitmapFromView(binding.imgCompanyLicense) && getBitmapFromView(binding.imgLetterFromOwner))
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
            }

        }
    }

    @Override
    public void onStarted() {
        if(getActivity()!=null)
            AlertDialogUtil.showProgressBar(getActivity(),true);
        Log.d(getClass().getSimpleName(),"onStarted");

    }

    @Override
    public void onSuccess() {
        if(getActivity()!=null)
            AlertDialogUtil.showProgressBar(getActivity(),false);
        Log.d(getClass().getSimpleName(),"onSuccess");

    }

    @Override
    public void onFailure(String Msg) {
        if(getActivity()!=null){
            AlertDialogUtil.showProgressBar(getActivity(),false);
            AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());
            Log.d(getClass().getSimpleName(),"onFailure");
        }

    }

    @Override
    public void navigateToPay() throws JSONException {
        prepareDoc();
    }

    @Override
    public void nextButtonStatusForAttachment() {
        nextButtonStatus();
    }

    @Override
    public void updateUI(RetrieveDocStreamResponse retrieveDocStreamResponse, int docId) {

       /* if(retrieveDocStreamResponse.getDoc_details() != null)
        {
            try
            {


                if (retrieveDocStreamResponse.getDoc_details().getDocKey() != null && retrieveDocStreamResponse.getDoc_details().getDocformat() != null)
                {
                    DocArr dr = new DocArr();
                    dr.setDocFormat(retrieveDocStreamResponse.getDoc_details().getDocformat());

                    dr.setDocId(docId);

                    if (dr.getDocFormat().compareToIgnoreCase("pdf") == 0 || dr.getDocFormat().compareToIgnoreCase("application/pdf") == 0)
                    {
                        final int random = new Random().nextInt(100);

                        dwldsPath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Doc" + "_" + random + ".pdf");
                        byte[] pdfAsBytes = Base64.decode(retrieveDocStreamResponse.getDoc_details().getDoc().trim().getBytes(), Base64.DEFAULT);
                        FileOutputStream os;

                        try {
                            os = new FileOutputStream(dwldsPath, false);
                            os.write(pdfAsBytes);

                            os.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dr.setDocPath(Environment.getExternalStorageDirectory().getPath() + "/" + dwldsPath.getName());
                        dr.setDocName(dwldsPath.getName());
                        dr.setDocDta(retrieveDocStreamResponse.getDoc_details().getDoc().trim());
                        boolean isPassportExists = false;


                        if (retrieveDocStreamResponse.getDoc_details().getDocKey().compareToIgnoreCase("passport") == 0) {
                            isPassportExists = isOldDocContainsPassport(PASSPORT);
                            isDocumentAdded =false;
                        }
                        if (retrieveDocStreamResponse.getDoc_details().getDocKey().compareToIgnoreCase("passport") == 0 && isPassportExists == false) {
                            *//*if(!getBitmapFromView(binding.imgPassport)){*//*
                                binding.imgPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                Log.d(getClass().getSimpleName(),"passport:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                                *//*}*//*
                                binding.passportButtons.setVisibility(View.VISIBLE);
                                dr.setDocKey(PASSPORT);
                                docid_passort =dr.getDocId();
                        } else if (retrieveDocStreamResponse.getDoc_details().getDocKey().compareToIgnoreCase("passport") == 0 && isPassportExists == true) {
                            *//*if(!getBitmapFromView(binding.imgVisaPassport)){*//*
                            binding.imgVisaPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                Log.d(getClass().getSimpleName(),"visaPassport:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            binding.visaPassportButtons.setVisibility(View.VISIBLE);
                            dr.setDocKey(VISA_PASSPORT);
                            docid_visaPassport = dr.getDocId();
                        } else if (retrieveDocStreamResponse.getDoc_details().getDocKey().compareToIgnoreCase("license") == 0) {
                            *//*if(!getBitmapFromView(binding.imgCompanyLicense)){*//*
                            binding.imgCompanyLicense.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                Log.d(getClass().getSimpleName(),"companyLicense:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            binding.companyLicenseButtons.setVisibility(View.VISIBLE);

                            dr.setDocKey(COMPANY_LICENCE);
                        } else if (retrieveDocStreamResponse.getDoc_details().getDocKey().compareToIgnoreCase("noc") == 0) {
                            *//*if(!getBitmapFromView(binding.imgLetterFromOwner)){*//*
                            binding.imgLetterFromOwner.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdf_icon));
                                Log.d(getClass().getSimpleName(),"letterFromOwner:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            binding.nocButtons.setVisibility(View.VISIBLE);
                            dr.setDocKey(LETTER_FROM_OWNER);

                        }
                        nextButtonStatus();

                    }
                    else if (model.isImageFormat(dr.getDocFormat()))
                    {
                        InputStream stream = new ByteArrayInputStream(Base64.decode(retrieveDocStreamResponse.getDoc_details().getDoc().trim().getBytes(), Base64.DEFAULT));
                        front = BitmapFactory.decodeStream(stream);
                        dr.setDocDta(retrieveDocStreamResponse.getDoc_details().getDoc().trim());
                        dr.setDocId(docId);
                        imageType = retrieveDocStreamResponse.getDoc_details().getDocKey();
                        boolean isPassportExists = false;
                        if (imageType.compareToIgnoreCase("passport") == 0) {
                            isPassportExists = isOldDocContainsPassport(PASSPORT);
                        }

                        if (imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == false) {
                            dr.setDocKey(PASSPORT);
                            dr.setDocName(PASSPORT + "." + getDocFormat(dr.getDocFormat()));

                            AttachmentBitmap.passport_copy = front;
                            retrieved_passport = front;

                                *//*if (!getBitmapFromView(binding.imgPassport)){*//*
                                    binding.imgPassport.setImageBitmap(AttachmentBitmap.passport_copy);
                                    Log.d(getClass().getSimpleName(),"passport:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                                *//*}*//*

                            docid_passort =dr.getDocId();
                            imageAlignment();
                        } else if (imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == true) {

                            AttachmentBitmap.visa_passport = front;
                            dr.setDocName(VISA_PASSPORT + "." + getDocFormat(dr.getDocFormat()));
                            retrieved_visa = front;
                            dr.setDocKey(VISA_PASSPORT);

                            *//*if(!getBitmapFromView(binding.imgVisaPassport)){*//*
                                binding.imgVisaPassport.setImageBitmap(AttachmentBitmap.visa_passport);
                                Log.d(getClass().getSimpleName(),"visaPassport:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            imageAlignment();
                            docid_visaPassport = dr.getDocId();


                        } else if (imageType.compareToIgnoreCase("license") == 0) {

                            AttachmentBitmap.company_license = front;
                            retrieved_license = front;

                            dr.setDocName(COMPANY_LICENCE + "." + getDocFormat(dr.getDocFormat()));
                            *//*if(!getBitmapFromView(binding.imgCompanyLicense)){*//*
                                binding.imgCompanyLicense.setImageBitmap(AttachmentBitmap.company_license);
                                Log.d(getClass().getSimpleName(),"companyLicense:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            imageAlignment();
                            dr.setDocKey(COMPANY_LICENCE);

                        } else if (imageType.compareToIgnoreCase("noc") == 0) {

                            AttachmentBitmap.letter_from_owner = front;
                            retrieved_noc = front;
                            dr.setDocName(LETTER_FROM_OWNER + "." + getDocFormat(dr.getDocFormat()));
                            *//*if(!getBitmapFromView(binding.imgLetterFromOwner)){*//*
                                binding.imgLetterFromOwner.setImageBitmap(AttachmentBitmap.letter_from_owner);
                                Log.d(getClass().getSimpleName(),"letterFromOwner:"+dr.getDocKey()+"."+dr.getDocFormat()+"["+dr.getDocId()+"]");
                            *//*}*//*
                            imageAlignment();
                            dr.setDocKey(LETTER_FROM_OWNER);

                        }
                        nextButtonStatus();

                    } else {
                        //imageType = retrieveDocStreamResponse.getDoc_details().getDocKey();
                        imageType = retrieveDocStreamResponse.getDoc_details().getDocKey();
                        boolean isPassportExists = false;
                        if (imageType.compareToIgnoreCase("passport") == 0) {

                            isPassportExists = isOldDocContainsPassport(PASSPORT);
                        }
                        if (imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == false) {
                            dr.setDocKey(PASSPORT);

                            binding.imgPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                            imageAlignment();
                        } else if (imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == true) {


                            dr.setDocKey(VISA_PASSPORT);
                            binding.imgVisaPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                            imageAlignment();


                        } else if (imageType.compareToIgnoreCase("license") == 0) {


                            binding.imgCompanyLicense.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                            imageAlignment();
                            dr.setDocKey(COMPANY_LICENCE);

                        } else if (imageType.compareToIgnoreCase("noc") == 0) {


                            binding.imgLetterFromOwner.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                            imageAlignment();
                            dr.setDocKey(LETTER_FROM_OWNER);

                        }
                        nextButtonStatus();

                    }

                    if (!isOldDocExistInAttachment(dr.getDocKey())) {
                        ParentSiteplanViewModel.addDownloadedDoc(dr);
                    }
                    AddDoc(dr.getDocKey(), dr.getDocPath(), dr.getDocName(), dr.getDocFormat(), dr.getDocId());

                    if (ParentSiteplanViewModel.getDownloadedDoc() != null && ParentSiteplanViewModel.getDownloadedDoc().size() > 0) {
                        *//*if (ParentSiteplanViewModel.getDownloadedDoc().size() == Global.docArr.length) {
                            onSuccess();

                        }*//*
                        if(Global.isPerson && Global.rbIsOwner){
                            if(ParentSiteplanViewModel.getDownloadedDoc().size()==Global.docArr.length){
                                onSuccess();
                                nextButtonStatus();
                                Log.d(getClass().getSimpleName(),"IamOwner Success:"+ParentSiteplanViewModel.getDownloadedDoc().size());

                            }
                            *//*else{
                                if(getBitmapFromView(binding.imgPassport)&&getBitmapFromView(binding.imgVisaPassport)){
                                    onSuccess();
                                    nextButtonStatus();
                                }

                            }*//*
                        }
                        else if(Global.isPerson && !Global.rbIsOwner){
                            if(ParentSiteplanViewModel.getDownloadedDoc().size()==Global.docArr.length){
                                onSuccess();
                                Log.d(getClass().getSimpleName(),"IamNotOwner Success:"+ParentSiteplanViewModel.getDownloadedDoc().size());
                                nextButtonStatus();

                            }
                            *//*else{
                                if(getBitmapFromView(binding.imgPassport)&&getBitmapFromView(binding.imgVisaPassport)&&getBitmapFromView(binding.imgLetterFromOwner)){
                                    onSuccess();
                                    nextButtonStatus();
                                }

                            }*//*
                        }
                        else if(!Global.isPerson && !Global.rbIsOwner){
                            if(ParentSiteplanViewModel.getDownloadedDoc().size()==Global.docArr.length){
                                Log.d(getClass().getSimpleName(),"Company Success:"+ParentSiteplanViewModel.getDownloadedDoc().size());
                                onSuccess();
                                nextButtonStatus();

                            }
                            *//*else{
                                if(getBitmapFromView(binding.imgCompanyLicense)&&getBitmapFromView(binding.imgLetterFromOwner)){
                                    onSuccess();
                                    nextButtonStatus();
                                }
                            }*//*
                        }
                        *//*else if(ParentSiteplanViewModel.getDownloadedDoc().size() == Global.docArr.length){
                            onSuccess();
                            nextButtonStatus();
                        }*//*
         *//*else{
                            onSuccess();
                            nextButtonStatus();
                        }*//*

                    }


                   // onSuccess();

                }
            }
            catch (Exception e){
                onFailure(e.getMessage());
            }
        }
        *//*if(Global.isPerson && Global.rbIsOwner){
            if(getBitmapFromView(binding.imgPassport)&&getBitmapFromView(binding.imgVisaPassport)){
                onSuccess();
                nextButtonStatus();
            }
        }
        else if(Global.isPerson && !Global.rbIsOwner){
            if(getBitmapFromView(binding.imgPassport)&&getBitmapFromView(binding.imgVisaPassport)&&getBitmapFromView(binding.imgLetterFromOwner)){
                onSuccess();
                nextButtonStatus();
            }
        }*//*
         *//*if(!Global.isPerson && !Global.rbIsOwner){
            if(getBitmapFromView(binding.imgCompanyLicense)&&getBitmapFromView(binding.imgLetterFromOwner)){
                onSuccess();
                nextButtonStatus();
            }
        }
        else if(Objects.requireNonNull(ParentSiteplanViewModel.getDownloadedDoc()).size() == Global.docArr.length){
            onSuccess();
            nextButtonStatus();
        }*/

    }

    public void retrieveDoc(final int  docId){
        int permission = ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            int checkPermissionCode = AppConstants.REQUEST_READ_EXTERNAL_STORAGE;
            requestPermissions(
                    AppConstants.PERMISSIONS_STORAGE,checkPermissionCode

            );
            return;
        }


        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("doc_id",docId);
            jsonBody.put("token",Global.site_plan_token);
            jsonBody.put("locale", CURRENT_LOCALE);


            JsonObjectRequest req = new JsonObjectRequest(Global.base_url_site_plan+"/retrieveDocStream",jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response != null){

                                    Gson gson = new GsonBuilder().serializeNulls().create();

                                    RetrieveDocStreamResponse docResponse =  gson.fromJson(response.toString(),RetrieveDocStreamResponse.class);
                                    boolean isError=false;
                                    String resStatus=docResponse.getStatus();
                                    int status=Integer.parseInt(resStatus);

                                    String msg=CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? response.getString("message_en"):response.getString("message_ar");
                                    if( status == 403){
                                        isError= true;
                                        if(msg!=null||msg.equals("")) AlertDialogUtil.errorAlertDialog("",msg,getActivity().getResources().getString(R.string.ok),getActivity());
                                    } else{
                                        if(docResponse.getDoc_details() != null)
                                        {

                                            if(docResponse.getDoc_details().getDoctype() != null && docResponse.getDoc_details().getDocformat() != null) {
                                                DocArr dr = new DocArr();
                                                dr.docFormat = docResponse.getDoc_details().getDocformat();

                                                dr.docId = docId;

                                                if (dr.docFormat.compareToIgnoreCase("pdf") == 0||dr.docFormat.compareToIgnoreCase("application/pdf") == 0) {
                                                    final int random = new Random().nextInt(100);

                                                    dwldsPath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Doc" + "_" + random + ".pdf");
                                                    byte[] pdfAsBytes = Base64.decode(docResponse.getDoc_details().getDoc().trim().getBytes(), Base64.DEFAULT);
                                                    FileOutputStream os;
                                                    os = new FileOutputStream(dwldsPath, false);
                                                    os.write(pdfAsBytes);

                                                    os.close();
                                                    dr.docPath = Environment.getExternalStorageDirectory().getPath() + "/" + dwldsPath.getName();
                                                    dr.docName = dwldsPath.getName();
                                                    dr.docDta = docResponse.getDoc_details().getDoc().trim();
                                                    boolean isPassportExists = false;
                                                    if(docResponse.getDoc_details().getDoctype().compareToIgnoreCase("passport") == 0){
                                                        isPassportExists = isOldDocContainsPassport(PASSPORT);
                                                    }
                                                    if(docResponse.getDoc_details().getDoctype().compareToIgnoreCase("passport") == 0 && isPassportExists == false){
                                                        binding.imgPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                                        binding.passportButtons.setVisibility(View.VISIBLE);
                                                        dr.docKey = PASSPORT;
                                                    } else if(docResponse.getDoc_details().getDoctype().compareToIgnoreCase("passport") == 0 && isPassportExists == true){
                                                        binding.imgVisaPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                                        binding.visaPassportButtons.setVisibility(View.VISIBLE);
                                                        dr.docKey = VISA_PASSPORT;
                                                    } else if(docResponse.getDoc_details().getDoctype().compareToIgnoreCase("license") == 0){
                                                        binding.imgCompanyLicense.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                                        binding.companyLicenseButtons.setVisibility(View.VISIBLE);
                                                        dr.docKey = COMPANY_LICENCE;
                                                    } else if(docResponse.getDoc_details().getDoctype().compareToIgnoreCase("noc") == 0){
                                                        binding.imgLetterFromOwner.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                                        binding.nocButtons.setVisibility(View.VISIBLE);
                                                        dr.docKey = LETTER_FROM_OWNER;
                                                    }

                                                } else if(dr.docFormat.compareToIgnoreCase("jpg") == 0 || dr.docFormat.compareToIgnoreCase("png") == 0|| dr.docFormat.compareToIgnoreCase("image/png") == 0|| dr.docFormat.compareToIgnoreCase("image/jpg") == 0|| dr.docFormat.compareToIgnoreCase("image/jpeg") == 0|| dr.docFormat.compareToIgnoreCase("jpeg") == 0){
                                                    InputStream stream = new ByteArrayInputStream(Base64.decode(docResponse.getDoc_details().getDoc().trim().getBytes(), Base64.DEFAULT));
                                                    front = BitmapFactory.decodeStream(stream);
                                                    dr.docDta = docResponse.getDoc_details().getDoc().trim();
                                                    imageType= docResponse.getDoc_details().getDoctype();
                                                    boolean isPassportExists = false;
                                                    if(imageType.compareToIgnoreCase("passport") == 0){
                                                        isPassportExists = isOldDocContainsPassport(PASSPORT);
                                                    }
                                                    if(imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == false){
                                                        dr.docKey = PASSPORT;
                                                        dr.docName = PASSPORT + "." +getDocFormat(dr.docFormat);

                                                        AttachmentBitmap.passport_copy=front;
                                                        retrieved_passport=front;
                                                        binding.imgPassport.setImageBitmap(AttachmentBitmap.passport_copy);
                                                        imageAlignment();
                                                    }
                                                    else if(imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == true){

                                                        AttachmentBitmap.visa_passport=front;
                                                        dr.docName = VISA_PASSPORT + "." +getDocFormat(dr.docFormat);
                                                        retrieved_visa=front;
                                                        dr.docKey = VISA_PASSPORT;
                                                        binding.imgVisaPassport.setImageBitmap(AttachmentBitmap.visa_passport);
                                                        imageAlignment();


                                                    }else if(imageType.compareToIgnoreCase("license") == 0){

                                                        AttachmentBitmap.company_license=front;
                                                        retrieved_license=front;
                                                        dr.docName = COMPANY_LICENCE + "." +getDocFormat(dr.docFormat);
                                                        binding.imgCompanyLicense.setImageBitmap(AttachmentBitmap.company_license);
                                                        imageAlignment();
                                                        dr.docKey = COMPANY_LICENCE;

                                                    }else if(imageType.compareToIgnoreCase("noc") == 0){

                                                        AttachmentBitmap.letter_from_owner=front;
                                                        retrieved_noc=front;
                                                        dr.docName = LETTER_FROM_OWNER + "." +getDocFormat(dr.docFormat);
                                                        binding.imgLetterFromOwner.setImageBitmap(AttachmentBitmap.letter_from_owner);
                                                        imageAlignment();
                                                        dr.docKey = LETTER_FROM_OWNER;

                                                    }
                                                    else{

                                                    }

                                                } else {
                                                    imageType= docResponse.getDoc_details().getDoctype();
                                                    imageType= docResponse.getDoc_details().getDoctype();
                                                    boolean isPassportExists = false;
                                                    if(imageType.compareToIgnoreCase("passport") == 0){
                                                        isPassportExists = isOldDocContainsPassport(PASSPORT);
                                                    }
                                                    if(imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == false){
                                                        dr.docKey = PASSPORT;

                                                        binding.imgPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                                                        imageAlignment();
                                                    }
                                                    else if(imageType.compareToIgnoreCase("passport") == 0 && isPassportExists == false){


                                                        dr.docKey = VISA_PASSPORT;
                                                        binding.imgVisaPassport.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                                                        imageAlignment();


                                                    }else if(imageType.compareToIgnoreCase("license") == 0){


                                                        binding.imgCompanyLicense.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                                                        imageAlignment();
                                                        dr.docKey = COMPANY_LICENCE;

                                                    }else if(imageType.compareToIgnoreCase("noc") == 0){


                                                        binding.imgLetterFromOwner.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unsupported));
                                                        imageAlignment();
                                                        dr.docKey = LETTER_FROM_OWNER;;

                                                    }
                                                }

                                                if(isOldDocExistInAttachment(dr.getDocKey()) == false) {
                                                    ParentSiteplanViewModel.getDownloadedDoc().add(dr);
                                                }
                                                AddDoc(dr.docKey, dr.docPath, dr.docName, dr.docFormat, dr.docId);

                                                if(ParentSiteplanViewModel.getDownloadedDoc() != null) {
                                                    if (ParentSiteplanViewModel.getDownloadedDoc().size() == Global.docArr.length) {
                                                        onSuccess();
                                                        //ParentSiteplanViewModel.getDownloadedDoc().clear();
                                                        nextButtonStatus();
                                                    }
                                                }
                                            }

                                        }

                                    }

                                } else {
                                    /*if (progressDialog != null)
                                        progressDialog.hide();*/
                                }
                            } catch (Exception e) {
                                onFailure(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onFailure(error.getMessage());
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }};

            onStarted();
            Volley.newRequestQueue(getActivity()).add(req);

            req.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(300),0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void OnPermissionAccepted(Boolean isAccepted) {
        //System.console().printf("permission");
        if (isAccepted) {
            if (Global.docArr != null && Global.docArr.length > 0) {
                if (ParentSiteplanViewModel.getDownloadedDoc() == null || ParentSiteplanViewModel.getDownloadedDoc().size() == 0) {
                    if (Global.isPerson && Global.rbIsOwner) {
                        //if (!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport))
                        downloadDocs(Global.docArr);
                    } else if (Global.isPerson && !Global.rbIsOwner) {
                        //if(!getBitmapFromView(binding.imgPassport) || !getBitmapFromView(binding.imgVisaPassport) ||!getBitmapFromView(binding.imgLetterFromOwner))
                        downloadDocs(Global.docArr);
                    } else if (!Global.isPerson && !Global.rbIsOwner) {
                        //if(!getBitmapFromView(binding.imgCompanyLicense) ||!getBitmapFromView(binding.imgLetterFromOwner))
                        downloadDocs(Global.docArr);
                    }
                }
            } else
                imageAlignment();
        } else
            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
    }

    @Override
    public void onCrop(String uri) {



        galleryURI=Uri.parse(uri);
        //AddDoc(currentSelection, "", "", "jpg", 0);
        if (currentSelection.equals(PASSPORT)) {
            binding.imgPassport.setImageURI(galleryURI);
            //AddDoc(currentSelection, galleryURI.toString(), PASSPORT+".jpg", "jpg", 0);
            AttachmentBitmap.passport_copy=((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap();
            createAttachedDoc(encodeImage(AttachmentBitmap.passport_copy), "image/jpg",
                    getCurrentKey(),PASSPORT+".jpg", getDocId(currentSelection), PASSPORT);
            AddDoc(currentSelection, galleryURI.toString(), PASSPORT+".jpg", "image/jpg", getDocId(currentSelection));
        } else if (currentSelection.equals(LETTER_FROM_OWNER)) {
            binding.imgLetterFromOwner.setImageURI(galleryURI);
            AttachmentBitmap.letter_from_owner=((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap();
            createAttachedDoc(encodeImage(AttachmentBitmap.letter_from_owner), "image/jpg",
                    getCurrentKey(),LETTER_FROM_OWNER+".jpg", getDocId(currentSelection), LETTER_FROM_OWNER);
            AddDoc(currentSelection, galleryURI.toString(), LETTER_FROM_OWNER+".jpg", "image/jpg", getDocId(currentSelection));
        }else if (currentSelection.equals(VISA_PASSPORT)) {
            binding.imgVisaPassport.setImageURI(galleryURI);
            AttachmentBitmap.visa_passport=((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap();
            createAttachedDoc(encodeImage(AttachmentBitmap.visa_passport), "image/jpg",
                    getCurrentKey(),VISA_PASSPORT+".jpg", getDocId(currentSelection), VISA_PASSPORT);
            AddDoc(currentSelection, galleryURI.toString(), VISA_PASSPORT+".jpg", "image/jpg", getDocId(currentSelection));
        }else if (currentSelection.equals(COMPANY_LICENCE)) {
            binding.imgCompanyLicense.setImageURI(galleryURI);
            AttachmentBitmap.company_license=((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap();
            createAttachedDoc(encodeImage(AttachmentBitmap.company_license), "image/jpg",
                    getCurrentKey(),COMPANY_LICENCE+".jpg", getDocId(currentSelection), COMPANY_LICENCE);
            AddDoc(currentSelection, galleryURI.toString(), COMPANY_LICENCE+".jpg", "image/jpg", getDocId(currentSelection));
        }
        nextButtonStatus();

    }




    public static class DownloadFile extends AsyncTask<String, Void, Void> {

        DataCallback dataCallback;
        Object dataExecution;

        private String downlodedFilename;
        Activity act;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dataCallback.onDownloadFinish(dataExecution);
            AlertDialogUtil.showProgressBar(act,false);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialogUtil.showProgressBar(act,true);


        }


        public DownloadFile(Activity activity) {

            dataCallback = (DataCallback) activity;
            act = activity;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            downlodedFilename = fileName;
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                if (pdfFile.exists())
                    pdfFile.delete();
                pdfFile.createNewFile();
                FileDownloader.downloadFile(fileUrl, pdfFile);
                viewPdf();
            } catch (IOException e) {
                e.printStackTrace();
                dataExecution = e;
            }
            dataExecution = "Success";
            return null;
        }

        public void viewPdf() {
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + downlodedFilename);
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                act.startActivity(pdfIntent);
            } catch (Exception e) {
                dataExecution = e.getMessage();


            }
        }
    }

    private String getCurrentKey(){
        if(currentSelection.equals(PASSPORT)){
            return "passport";
        } else if(currentSelection.equals(LETTER_FROM_OWNER)){
            return "noc";
        } else if(currentSelection.equals(VISA_PASSPORT)){
            return "passport";
        }else if(currentSelection.equals(COMPANY_LICENCE)){
            return "license";
        } else {
            return "";
        }
    }

    private int getDocId(String key){
        if(ParentSiteplanViewModel.getNewlyAttachedDoc() != null &&
                ParentSiteplanViewModel.getNewlyAttachedDoc().size() > 0){
            for (DocArr ar : ParentSiteplanViewModel.getNewlyAttachedDoc()){
                if(ar.getDocKey() != null && ar.getDocKey().equals(key)){
                    return ar.getDocId();
                }
            }
        }
        return 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSER) {
            // Get the Uri of the selected file
            if(data != null){
                Uri uri = data.getData();
                if(!DocumentUtility.isGoogleStorageDocument(getActivity(),uri)){
                    String uriString = uri.toString();
                    String path = DocumentUtility.getPath(getActivity(), uri);
                    String fileExtension = "";
                    if(path != null && path.length() > 0) {
                        File myFile = new File(path);

                        fileExtension = DocumentUtility.getFileExtension(myFile.getName()).toLowerCase();
                        if (DocumentUtility.isCorrectDocExtension(fileExtension)) {


                            createAttachedDoc(encodeFileToBase64Binary(myFile), "application/pdf",
                                    getCurrentKey(), myFile.getName(), getDocId(currentSelection), currentSelection);
                            AddDoc(currentSelection, myFile.getPath(), myFile.getName(), "application/pdf", 0);

                            if (currentSelection.equals(PASSPORT)) {

                                isCamera = false;

                                binding.imgPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                binding.personalView.setVisibility(View.VISIBLE);
                                binding.personalChange.setVisibility(View.VISIBLE);
                                nextButtonStatus();

                            } else if (currentSelection.equals(LETTER_FROM_OWNER)) {

                                isCamera = false;
                                binding.imgLetterFromOwner.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                binding.nocView.setVisibility(View.VISIBLE);
                                binding.nocChange.setVisibility(View.VISIBLE);
                                nextButtonStatus();
                            } else if (currentSelection.equals(VISA_PASSPORT)) {

                                isCamera = false;
                                binding.imgVisaPassport.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                binding.visaView.setVisibility(View.VISIBLE);
                                binding.visaChange.setVisibility(View.VISIBLE);
                                nextButtonStatus();
                            } else if (currentSelection.equals(COMPANY_LICENCE)) {

                                isCamera = false;
                                binding.imgCompanyLicense.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                                binding.licenseView.setVisibility(View.VISIBLE);
                                binding.licenseChange.setVisibility(View.VISIBLE);
                                nextButtonStatus();
                            }

                        }
                    }
                }
            }
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                System.out.println(contentURI.toString());
                System.out.println(contentURI.getPath());
                //AddDoc(currentSelection, "", "", "jpg", 0);
                try {
                    isCamera = false;
                    isGallery = true;
                    Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                    crop.putExtra("uri", contentURI.toString());
                    startActivityForResult(crop, GALLERY_CROP);
                    /*Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                    crop.putExtra("uri", contentURI.toString());
                    startActivityForResult(crop, GALLERY_CROP);*/
                    /*if (currentSelection == LAND_OWNER_CERTIFICATE) {


                    } else if (currentSelection == PASSPORT) {

                        isCamera = false;
                        Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                        crop.putExtra("uri", contentURI.toString());
                        startActivityForResult(crop, GALLERY_CROP);

                    } else if (currentSelection == LETTER_FROM_OWNER) {

                        isCamera = false;
                        Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                        crop.putExtra("uri", contentURI.toString());
                        startActivityForResult(crop, GALLERY_CROP);
                    }else if (currentSelection == VISA_PASSPORT) {
                        isCamera = false;
                        Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                        crop.putExtra("uri", contentURI.toString());
                        startActivityForResult(crop, GALLERY_CROP);

                    }else if (currentSelection == COMPANY_LICENCE) {

                        isCamera = false;
                        Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                        crop.putExtra("uri", contentURI.toString());
                        startActivityForResult(crop, GALLERY_CROP);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if (requestCode == CAMERA) {

            isCamera = true;
            isGallery = false;
            try {
                thumbnail = (Bitmap) compressImage(Uri.parse(mCurrentPhotoPath), "camera_image");
            } catch (Exception ex){

            }
            //AddDoc(currentSelection, "", "", "jpg", 0);
            /*Intent crop = new Intent(getActivity(), ImageCropActivity.class);
            crop.putExtra("uri", photoURI);
            startActivityForResult(crop, GALLERY_CROP);*/
            Intent crop = new Intent(getActivity(), ImageCropActivity.class);
            crop.putExtra("uri", photoURI.toString());

            startActivityForResult(crop, GALLERY_CROP);
            /*if (currentSelection == LAND_OWNER_CERTIFICATE) {
                Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                crop.putExtra("uri", photoURI);

                startActivityForResult(crop, GALLERY_CROP);
            } else if (currentSelection == PASSPORT) {

            } else if (currentSelection == LETTER_FROM_OWNER) {
                Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                crop.putExtra("uri", photoURI);

                startActivityForResult(crop, GALLERY_CROP);
            }else if (currentSelection == VISA_PASSPORT) {
                Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                crop.putExtra("uri", photoURI);

                startActivityForResult(crop, GALLERY_CROP);
            }else if (currentSelection == COMPANY_LICENCE) {
                Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                crop.putExtra("uri", photoURI);

                startActivityForResult(crop, GALLERY_CROP);
            }*/

        }
        else if (requestCode == GALLERY_CROP && resultCode == -1) {

            galleryURI=Uri.parse(data.getExtras().getString("uri"));
            AddDoc(currentSelection, "", "", "jpg", 0);
           if (currentSelection.equals(PASSPORT)) {
                binding.imgPassport.setImageURI(galleryURI);
                //AddDoc(currentSelection, galleryURI.toString(), PASSPORT+".jpg", "jpg", 0);
                AttachmentBitmap.passport_copy=((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.passport_copy), "image/jpg",
                        getCurrentKey(),PASSPORT+".jpg", getDocId(currentSelection), PASSPORT);
            } else if (currentSelection.equals(LETTER_FROM_OWNER)) {
                binding.imgLetterFromOwner.setImageURI(galleryURI);
                AttachmentBitmap.letter_from_owner=((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.letter_from_owner), "image/jpg",
                        getCurrentKey(),LETTER_FROM_OWNER+".jpg", getDocId(currentSelection), LETTER_FROM_OWNER);
            }else if (currentSelection.equals(VISA_PASSPORT)) {
                binding.imgVisaPassport.setImageURI(galleryURI);
                AttachmentBitmap.visa_passport=((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.visa_passport), "image/jpg",
                        getCurrentKey(),VISA_PASSPORT+".jpg", getDocId(currentSelection), VISA_PASSPORT);
            }else if (currentSelection.equals(COMPANY_LICENCE)) {
                binding.imgCompanyLicense.setImageURI(galleryURI);
                AttachmentBitmap.company_license=((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.company_license), "image/jpg",
                        getCurrentKey(),COMPANY_LICENCE+".jpg", getDocId(currentSelection), COMPANY_LICENCE);
            }
            nextButtonStatus();

        }
    }


    private void AddDoc(String key, String path, String name, String format, int docID){
        boolean isAdded = false;
        ActivateViewAndChange(key);
        if(ParentSiteplanViewModel.getNewlyAttachedDoc() != null && ParentSiteplanViewModel.getNewlyAttachedDoc().size() > 0){
            for(int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++){
                if(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().equals(key)){
                    isAdded = true;
                    ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).setDocFormat(format);
                    ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).setDocPath(path);
                    ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).setDocKey(key);
                    ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).setDocName(name);
                    ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).setDocId(docID);
                }
            }
            if(isAdded == false){
                DocArr ar = new DocArr();
                ar.setDocKey(key);
                ar.setDocPath(path);
                ar.setDocName(name);
                ar.setDocFormat(format);
                ar.setDocId(0);
                ParentSiteplanViewModel.addNewlyAttachedDoc(ar);
            }
        } else {

            /*if(ParentSiteplanViewModel.getNewlyAttachedDoc()==null)
                al= new ArrayList<>();*/

            DocArr ar = new DocArr();
            ar.setDocKey(key);
            ar.setDocPath(path);
            ar.setDocName(name);
            ar.setDocFormat(format);
            ar.setDocId(0);
            ParentSiteplanViewModel.addNewlyAttachedDoc(ar);

        }
    }

    private String getDocType(String key){
        if (key.equals(PASSPORT) || key.equals(VISA_PASSPORT)) {
            return "passport";
        } else if(key.equals(COMPANY_LICENCE)){
            return  "license";
        } else if(key.equals(LETTER_FROM_OWNER)){
            return  "noc";
        }
        return key;
    }

    public void clearAttachments(){
        binding.imgPassport.setImageDrawable(getResources().getDrawable(R.drawable.photo));
        binding.imgVisaPassport.setImageDrawable(getResources().getDrawable(R.drawable.photo));
        binding.imgCompanyLicense.setImageDrawable(getResources().getDrawable(R.drawable.photo));
        binding.imgLetterFromOwner.setImageDrawable(getResources().getDrawable(R.drawable.photo));
    }

    private boolean isDocExistInAttachment(String key){
        boolean isExists = false;
        if(lstAttachedDoc != null && lstAttachedDoc.size() > 0) {
            for (int i = 0; i < lstAttachedDoc.size(); i++){
                if(lstAttachedDoc.get(i).getKey().equals(key)){
                    isExists = true;
                    return isExists;
                }
            }
        }
        return isExists;
    }

    private boolean paylaterValidation() {

        boolean isValid = true;
        if(Global.rbIsOwner){

            if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {

                if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()){
                    isValid=true;
                }
                else{
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());
                    isValid = false;
                }

            }

        }
        else if (Global.rbNotOwner)
        {

            if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {

                if((((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                        || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                        && ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()){

                    isValid = true;
                }
                else{
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());
                    isValid = false;
                }
            }

        }
        else if(Global.isCompany)
        {
            if(((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {
                AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());

                isValid = false;
            }



        }
        return isValid;
    }

    private boolean paymentValidation() {
        boolean isValid = true;
        if(Global.rbIsOwner){

            if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {

                if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()){
                    isValid=true;
                }
                else{
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());
                    isValid = false;
                }
            }


        }
        else if (Global.rbNotOwner)
        {

            if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {
                if((((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                        || ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                        && ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()!=((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()){

                    isValid = true;
                }
                else{
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());
                    isValid = false;
                }

            }


        }
        else if(Global.isCompany)
        {
            if(((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap()
                    || ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
            {
                AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.empty_document),getActivity().getResources().getString(R.string.ok),getActivity());
                isValid = false;

            }


        }
        return isValid;
    }

    private void imageAlignment() {


        if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() && ((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=null)

            binding.passportButtons.setVisibility(View.INVISIBLE);
        else
            binding.passportButtons.setVisibility(View.VISIBLE);

        if(((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() && ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=null)

            binding.visaPassportButtons.setVisibility(View.INVISIBLE);
        else
            binding.visaPassportButtons.setVisibility(View.VISIBLE);

        if(((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() && ((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()!=null)

            binding.companyLicenseButtons.setVisibility(View.INVISIBLE);
        else
            binding.companyLicenseButtons.setVisibility(View.VISIBLE);
        if(((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap() && ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()!=null)

            binding.nocButtons.setVisibility(View.INVISIBLE);
        else
            binding.nocButtons.setVisibility(View.VISIBLE);

        viewUiUpdate();


    }

    private void viewImage() throws IOException {
        Intent intent = new Intent(this.getActivity(), ViewImage.class);
        File file = null;

        pdfBitmap =((BitmapDrawable) getResources().getDrawable(R.drawable.pdf_icon)).getBitmap();


        if (currentSelection == COMPANY_LICENCE && ParentSiteplanViewModel.getNewlyAttachedDoc() != null) {
            for (int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++) {
                if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().equals(COMPANY_LICENCE)) {
                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("pdf") == 0||
                            ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("application/pdf") == 0) {

                        //viewPdf(Uri.parse(dwldsPath.getAbsolutePath()), COMPANY_LICENCE);
                        DocumentUtility.previewDocument(getActivity(), ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocPath(), "pdf");

                    } else if (model.isImageFormat(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat())) {
                        viewBitmap = ((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap();
                        file = storeImage(viewBitmap);
                        intent.putExtra("bitmap", file.getAbsolutePath());
                        if (viewBitmap == ((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                            Toast.makeText(getActivity(), getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                        else if (!(viewBitmap == pdfBitmap))
                            startActivity(intent);
                    }

                }
            }
        }
        else if (currentSelection == VISA_PASSPORT && ParentSiteplanViewModel.getNewlyAttachedDoc() != null) {
            for (int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++) {
                if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().equals(VISA_PASSPORT)) {
                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("pdf") == 0||
                            ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("application/pdf") == 0) {
                        DocumentUtility.previewDocument(getActivity(), ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocPath(), "pdf");

                    } else if (model.isImageFormat(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat())) {
                        viewBitmap = ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap();
                        file = storeImage(viewBitmap);
                        intent.putExtra("bitmap", file.getAbsolutePath());
                        if (viewBitmap == ((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                            Toast.makeText(getActivity(), getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                        else if (!(viewBitmap == pdfBitmap))
                            startActivity(intent);
                    }
                }
            }

        } else if (currentSelection == PASSPORT && ParentSiteplanViewModel.getNewlyAttachedDoc() != null)
        {
            for (int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++)
            {
                if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().equals(PASSPORT)) {
                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("pdf") == 0||
                            ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("application/pdf") == 0) {
                        DocumentUtility.previewDocument(getActivity(),
                                ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocPath(), "pdf");

                    } else if (model.isImageFormat(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat())) {
                        viewBitmap = ((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap();
                        file = storeImage(viewBitmap);
                        intent.putExtra("bitmap", file.getAbsolutePath());
                        if (viewBitmap == ((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                            Toast.makeText(getActivity(), getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                        else if (!(viewBitmap == pdfBitmap))
                            startActivity(intent);
                    }
                }
            }

        }
        else if (currentSelection == LETTER_FROM_OWNER && ParentSiteplanViewModel.getNewlyAttachedDoc() != null)
        {
            for (int i = 0; i < ParentSiteplanViewModel.getNewlyAttachedDoc().size(); i++)
            {
                if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocKey().equals(LETTER_FROM_OWNER)) {
                    if (ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("pdf") == 0||
                            ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat().compareToIgnoreCase("application/pdf") == 0) {
                        DocumentUtility.previewDocument(getActivity(),
                                ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocPath(), "pdf");


                    } else if (model.isImageFormat(ParentSiteplanViewModel.getNewlyAttachedDoc().get(i).getDocFormat())) {
                        viewBitmap = ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap();
                        file = storeImage(viewBitmap);
                        intent.putExtra("bitmap", file.getAbsolutePath());
                        if (viewBitmap == ((BitmapDrawable) getResources().getDrawable(R.drawable.photo)).getBitmap())
                            Toast.makeText(getActivity(), getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                        else if (!(viewBitmap == pdfBitmap))
                            startActivity(intent);
                    }
                }
            }
        }
    }


    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);

    }

    public void prepareDoc() throws JSONException {

        populateLstDoc();
        if(lstAttachedDoc!=null && lstAttachedDoc.size()>0){
            for(int i=0;i<lstAttachedDoc.size();i++){
                if(isValidDocFormat(lstAttachedDoc.get(i).getDoc_format()) == false){
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.valid_doc),
                            getActivity().getResources().getString(R.string.ok),getActivity());
                    return;
                }
            }
        }


        //User user=Global.getUser(getActivity());

        if(Global.isDeliveryByCourier) {
            Global.deliveryDetails=new DeliveryDetails();
            if(ParentSiteplanViewModel.deliveryDetails != null){
                Global.deliveryDetails.setNameEn(ParentSiteplanViewModel.deliveryDetails.getNameEn());
                Global.deliveryDetails.setNameAr( ParentSiteplanViewModel.deliveryDetails.getNameAr());
                Global.deliveryDetails.setEmailId( ParentSiteplanViewModel.deliveryDetails.getEmailId());
                Global.deliveryDetails.setMobileNo( ParentSiteplanViewModel.deliveryDetails.getMobileNo());
                Global.deliveryDetails.setBldgName(ParentSiteplanViewModel.deliveryDetails.getBldgName());
                Global.deliveryDetails.setBldgNo( ParentSiteplanViewModel.deliveryDetails.getBldgNo());
                Global.deliveryDetails.setNearestLandmark( ParentSiteplanViewModel.deliveryDetails.getNearestLandmark());
                Global.deliveryDetails.setStreetAddress( ParentSiteplanViewModel.deliveryDetails.getStreetAddress());
                Global.deliveryDetails.setMainAddress(ParentSiteplanViewModel.deliveryDetails.getMainAddress());
                Global.deliveryDetails.setEmirate(Integer.parseInt(ParentSiteplanViewModel.deliveryDetails.getEmirate()));
                Global.deliveryDetails.setMakaniNo( ParentSiteplanViewModel.deliveryDetails.getMakaniNo());
            }

            //Global.deliveryDetails.setEmID(DeliveryFragment.emId);
        }
        if (Global.isPerson && Global.rbIsOwner || Global.isPerson && !Global.rbIsOwner)
        {
            Global.passportData = new ArrayList<>();
            for (int i = 0; i < lstAttachedDoc.size(); i++) {
                if (lstAttachedDoc.get(i).getDoc_type().equals("passport")) {
                    PassportDocs obj = new PassportDocs();
                    String file = lstAttachedDoc.get(i).getDoc();
                    if (!(lstAttachedDoc.get(i).getDoc_format().equals("pdf")
                            || lstAttachedDoc.get(i).getDoc_format().equals("application/pdf")
                            || lstAttachedDoc.get(i).getDoc_format().equals("jpg")
                            || lstAttachedDoc.get(i).getDoc_format().equals("png")
                            || lstAttachedDoc.get(i).getDoc_format().equals("image/png")
                            || lstAttachedDoc.get(i).getDoc_format().equals("image/jpg")
                            || lstAttachedDoc.get(i).getDoc_format().equals("image/jpeg")
                            || lstAttachedDoc.get(i).getDoc_format().equals("jpeg")))
                        file = encodeImage(((BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.unsupported)).getBitmap());
                    obj.setDoc_desc_en(lstAttachedDoc.get(i).getDoc_desc_en());
                    obj.setDoc_type(lstAttachedDoc.get(i).getDoc_type());
                    obj.setDoc_format(lstAttachedDoc.get(i).getDoc_format());
                    obj.setDoc_id(lstAttachedDoc.get(i).getDoc_id());
                    obj.setDoc_name(lstAttachedDoc.get(i).getDoc_name());
                    obj.setDoc(file);


                    Global.passportData.add(obj);
                }
            }
        }
        if (Global.isCompany)
        {
            /*if(getBitmapFromView(binding.imgLetterFromOwner)){
                AttachmentBitmap.company_license=((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.company_license), "image/jpg",
                        "license",COMPANY_LICENCE+".jpg", getDocId(currentSelection), COMPANY_LICENCE);
            }*/

            licenseData = new ArrayList<>();
            for (int k = 0; k < lstAttachedDoc.size(); k++) {
                if (lstAttachedDoc.get(k).getDoc_type().equals("license")) {
                    LicenceDocs obj = new LicenceDocs();
                    String file = lstAttachedDoc.get(k).getDoc();
                    if (!(lstAttachedDoc.get(k).getDoc_format().equals("pdf")
                            || lstAttachedDoc.get(k).getDoc_format().equals("application/pdf")
                            || lstAttachedDoc.get(k).getDoc_format().equals("jpg")
                            || lstAttachedDoc.get(k).getDoc_format().equals("png")
                            || lstAttachedDoc.get(k).getDoc_format().equals("image/png")
                            || lstAttachedDoc.get(k).getDoc_format().equals("image/jpg")
                            || lstAttachedDoc.get(k).getDoc_format().equals("image/jpeg")
                            || lstAttachedDoc.get(k).getDoc_format().equals("jpeg")))
                        file = encodeImage(((BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.unsupported)).getBitmap());
                    obj.setDoc_desc_en(lstAttachedDoc.get(k).getDoc_desc_en());
                    obj.setDoc_type(lstAttachedDoc.get(k).getDoc_type());
                    obj.setDoc_format(lstAttachedDoc.get(k).getDoc_format());
                    obj.setDoc_id(lstAttachedDoc.get(k).getDoc_id());
                    obj.setDoc_name(lstAttachedDoc.get(k).getDoc_name());
                    obj.setDoc(file);
                    licenseData.add(obj);
                }
            }
        }
        if (Global.isCompany || Global.isPerson && !Global.rbIsOwner)
        {
            /*if(getBitmapFromView(binding.imgLetterFromOwner)){
                AttachmentBitmap.letter_from_owner=((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap();
                createAttachedDoc(encodeImage(AttachmentBitmap.letter_from_owner), "image/jpg",
                        "noc",COMPANY_LICENCE+".jpg", getDocId(currentSelection), COMPANY_LICENCE);
            }*/
            Global.nocData = new ArrayList<>();
            for (int j = 0; j < lstAttachedDoc.size(); j++) {
                if (lstAttachedDoc.get(j).getDoc_type().toLowerCase().equals("noc")) {
                    NocDocs obj = new NocDocs();
                    String file = lstAttachedDoc.get(j).getDoc();
                    if (!(lstAttachedDoc.get(j).getDoc_format().equals("pdf")
                            || lstAttachedDoc.get(j).getDoc_format().equals("application/pdf")
                            || lstAttachedDoc.get(j).getDoc_format().equals("jpg")
                            || lstAttachedDoc.get(j).getDoc_format().equals("png")
                            || lstAttachedDoc.get(j).getDoc_format().equals("image/png")
                            || lstAttachedDoc.get(j).getDoc_format().equals("image/jpg")
                            || lstAttachedDoc.get(j).getDoc_format().equals("image/jpeg")
                            || lstAttachedDoc.get(j).getDoc_format().equals("jpeg")))
                        file = encodeImage(((BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.unsupported)).getBitmap());
                    obj.setDoc_desc_en(lstAttachedDoc.get(j).getDoc_desc_en());
                    obj.setDoc_type(lstAttachedDoc.get(j).getDoc_type());
                    obj.setDoc_format(lstAttachedDoc.get(j).getDoc_format());
                    obj.setDoc_id(lstAttachedDoc.get(j).getDoc_id());
                    obj.setDoc_name(lstAttachedDoc.get(j).getDoc_name());
                    obj.setDoc(file);
                    Global.nocData.add(obj);
                }
            }
        }
    }

    private void populateLstDoc(){
        if(ParentSiteplanViewModel.getDownloadedDoc() != null && ParentSiteplanViewModel.getDownloadedDoc().size() > 0) {
            for (int i = 0; i < ParentSiteplanViewModel.getDownloadedDoc().size(); i++) {
                if (isDocExistInAttachment(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey()) == false) {
                    if(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocName() != null &&
                            ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocName().length() > 0 &&
                            ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocFormat() != null &&
                            ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocFormat().length() > 0 &&
                            ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey() != null &&
                            ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey().length() > 0)
                    {
                        AttachedDoc doc = new AttachedDoc();
                        doc.setDoc(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocDta());
                        doc.setDoc_id(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocId());
                        doc.setDoc_name(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocName());
                        doc.setDoc_desc_en("TestNameEn");
                        doc.setDoc_format(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocFormat());
                        doc.setKey(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey());
                        doc.setDoc_type(getDocType(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey()));
                        lstAttachedDoc.add(doc);
                        nextButtonStatus();
                    }
                }
            }
        }
    }



    public void viewUiUpdate(){

        if(((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.unsupported)).getBitmap() && ((BitmapDrawable) binding.imgPassport.getDrawable()).getBitmap()!=null){

            binding.personalView.setEnabled(false);
            binding.personalView.setAlpha(0.5f);
        }
        else {
            binding.personalView.setEnabled(true);
            binding.personalView.setAlpha(1f);
        }

        if(((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.unsupported)).getBitmap() && ((BitmapDrawable) binding.imgVisaPassport.getDrawable()).getBitmap()!=null){

            binding.visaView.setEnabled(false);
            binding.visaView.setAlpha(0.5f);
        }
        else {
            binding.visaView.setEnabled(true);
            binding.visaView.setAlpha(1f);
        }

        if(((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.unsupported)).getBitmap() && ((BitmapDrawable) binding.imgCompanyLicense.getDrawable()).getBitmap()!=null){

            binding.licenseView.setEnabled(false);
            binding.licenseView.setAlpha(0.5f);
        }
        else {
            binding.licenseView.setEnabled(true);
            binding.licenseView.setAlpha(1f);
        }
        if(((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()==((BitmapDrawable) getResources().getDrawable(R.drawable.unsupported)).getBitmap() && ((BitmapDrawable) binding.imgLetterFromOwner.getDrawable()).getBitmap()!=null){

            binding.nocView.setEnabled(false);
            binding.nocView.setAlpha(0.5f);
        }
        else {
            binding.nocView.setEnabled(true);
            binding.nocView.setAlpha(1f);
        }
    }
    private void ActivateViewAndChange(String key){
        if(key != null){
            if(key.equals(COMPANY_LICENCE)){
                binding.licenseView.setVisibility(View.VISIBLE);
                binding.licenseChange.setVisibility(View.VISIBLE);
            } else if(key.equals(VISA_PASSPORT)){
                binding.visaView.setVisibility(View.VISIBLE);
                binding.visaChange.setVisibility(View.VISIBLE);
            } else if(key.equals(LAND_OWNER_CERTIFICATE)){
                binding.nocView.setVisibility(View.VISIBLE);
                binding.nocChange.setVisibility(View.VISIBLE);
            } else if(key.equals(PASSPORT)){
                binding.personalView.setVisibility(View.VISIBLE);
                binding.personalChange.setVisibility(View.VISIBLE);
            }
        }
    }








    public Bitmap compressImage(Uri imageUri, String imagenameName) {

        String filePath = getRealPathFromURI(imageUri.toString());
        String fileName = imagenameName;
        Bitmap scaledBitmap = null;
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        //max Height and width values of the compressed image is taken as 816x612
        if (actualHeight == 0 || actualWidth == 0) {
            try {
                return BitmapFactory.decodeStream(getSourceStream(imageUri));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        //width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        if(scaledBitmap != null) {
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            //check the rotation of the image and display it properly
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return scaledBitmap;
    }

    private String getRealPathFromURI(String contentURI) {

        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void permissionAllowed(int contant) {
        if (contant == AppConstants.REQUEST_READ_EXTERNAL_STORAGE) {

        } else if (contant == AppConstants.REQUEST_CAMERA_PERMISSION) {
            takePhotoFromCamera();
        }else if (contant == AppConstants.REQUEST_READ_EXTERNAL_STORAGE_GALLERY) {
            if(Global.docArr!=null)downloadDocs(Global.docArr);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void downloadDocs(Docs[] docs) {

        for (int i=0; i < docs.length; i++) {
            retrieveDoc(Integer.parseInt(docs[i].getDocid()));
            Log.d(getClass().getSimpleName(),"Downloading Documents with DocumentID:"+Integer.parseInt(docs[i].getDocid()));
            Log.d(getClass().getSimpleName(),"Downloading Documents format:"+docs[i].getDocformat()+"."+docs[i].getDocformat());
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);
        return encImage;
    }

    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        return encoded;
    }

    public static boolean isOldDocContainsPassport(String key){
        boolean isExists = false;
        if(ParentSiteplanViewModel.getDownloadedDoc() != null && ParentSiteplanViewModel.getDownloadedDoc().size() > 0) {
            for (int i = 0; i < ParentSiteplanViewModel.getDownloadedDoc().size(); i++){
                if(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey() != null &&
                        ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey().equals(key)){
                    isExists = true;
                    return isExists;
                }
            }
        }
        return isExists;
    }

    private boolean isOldDocExistInAttachment(String key){
        boolean isExists = false;
        if(ParentSiteplanViewModel.getDownloadedDoc() != null && ParentSiteplanViewModel.getDownloadedDoc().size() > 0) {
            for (int i = 0; i < ParentSiteplanViewModel.getDownloadedDoc().size(); i++){
                if(ParentSiteplanViewModel.getDownloadedDoc().get(i).getDocKey().equals(key)){
                    isExists = true;
                    return isExists;
                }
            }
        }
        return isExists;
    }
    /*private boolean isValidEmailId(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etEmailaddress.getText().toString())) {

            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if(!binding.etEmailaddress.getText().toString().contains("@")||!binding.etEmailaddress.getText().toString().contains(".")) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());
            isValid=false;
            return isValid;
        }
        return isValid;
    }

    private boolean isValidMobile(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etMobile.getText().toString())) {

            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (mobileNoInitialValidation() == false) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (binding.etMobile.length() != 12) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        return isValid;
    }

    private boolean mobileNoInitialValidation(){
        boolean isValid = false;
        if(binding.etMobile.getText().toString().startsWith("971")){
            if(binding.etMobile.getText().toString().length() == 12){
                try {
                    String st = String.valueOf(binding.etMobile.getText().toString().charAt(3));
                    int val = Integer.parseInt(st);
                    if(val > 0){
                        isValid = true;
                    }
                } catch (Exception ex){

                }
            }
        }
        return isValid;
    }
*/
    private void createAttachedDoc(String doc, String docFormat, String docType, String docName, int docId, String currentKey) {
        boolean isAdded = false;
        if(lstAttachedDoc != null && lstAttachedDoc.size() > 0){
            for(int i = 0; i < lstAttachedDoc.size(); i++){
                if(lstAttachedDoc.get(i).getKey() != null && lstAttachedDoc.get(i).getKey().equals(currentKey)){
                    isAdded = true;
                    lstAttachedDoc.get(i).setDoc_desc_en("TestNameEn");
                    lstAttachedDoc.get(i).setDoc_name(docName);
                    lstAttachedDoc.get(i).setDoc_type(docType);
                    lstAttachedDoc.get(i).setDoc(doc);
                    lstAttachedDoc.get(i).setDoc_format(docFormat);
                }
            }
            if(isAdded == false){
                attachedDoc = new AttachedDoc();
                attachedDoc.setDoc_desc_en("TestNameEn");
                attachedDoc.setDoc(doc);
                attachedDoc.setDoc_id(docId);
                attachedDoc.setDoc_format(docFormat);
                attachedDoc.setDoc_name(docName);
                attachedDoc.setDoc_type(docType);
                attachedDoc.setKey(currentKey);
                lstAttachedDoc.add(attachedDoc);
            }
        } else {
            attachedDoc = new AttachedDoc();
            attachedDoc.setDoc_desc_en("TestNameEn");
            attachedDoc.setDoc(doc);
            attachedDoc.setDoc_id(docId);
            attachedDoc.setDoc_format(docFormat);
            attachedDoc.setDoc_name(docName);
            attachedDoc.setDoc_type(docType);
            attachedDoc.setKey(currentKey);
            lstAttachedDoc.add(attachedDoc);
        }
    }

    private boolean isValidDocFormat(String key){
        boolean isValid = false;
        if(key.toLowerCase().equals("pdf")){
            isValid = true;
        } else if(key.toLowerCase().equals("application/pdf")){
            isValid = true;
        }else if(key.toLowerCase().equals("jpg")){
            isValid = true;
        }else if(key.toLowerCase().equals("jpeg")){
            isValid = true;
        }else if(key.toLowerCase().equals("png")){
            isValid = true;
        }else if(key.toLowerCase().equals("image/png")){
            isValid = true;
        }else if(key.toLowerCase().equals("image/jpg")){
            isValid = true;
        }else if(key.toLowerCase().equals("image/jpeg")){
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }


    private String getDocFormat(String type){
        if(type.equals("image/png")){
            return "png";
        } else if(type.equals("image/jpg")){
            return "jpg";
        } else if(type.equals("image/jpeg")){
            return "jpg";
        } else if(type.equals("jpeg")){
            return "jpg";
        } else if(type.equals("jpg")){
            return "jpg";
        } else {
            return "png";
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File storeImage(Bitmap image) throws IOException {
        File pictureFile = createImageFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }

}