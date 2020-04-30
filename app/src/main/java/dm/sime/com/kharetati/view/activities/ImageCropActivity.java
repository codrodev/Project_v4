package dm.sime.com.kharetati.view.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.MYPREFERENCES;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_IMAGE_CROP;


public class ImageCropActivity extends AppCompatActivity {


    private static final String TAG = "ImageCropActivity";
    public static Uri uri;
    public static String URI;
    public static Bitmap resultBitmap;
    public static boolean isImageCropped=false;
    //private CropImageView cropImageView;
    public Bitmap bitmap;

    private File croppedFile;
    public CropImageView cropView;
    private Button button_choose;
    private Button button_cancel;
    private String cameraUri;
    private Intent resultIntent;
    private String mCurrentPhotoPath;
    private File path;
    private ProgressDialog progressDialog;
    private Tracker mTracker;
    private SharedPreferences sharedpreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharedpreferences = newBase.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
            if(!locale.equals("defaultStringIfNothingFound"))
                CURRENT_LOCALE =locale;
            else
                CURRENT_LOCALE ="en";
            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_IMAGE_CROP);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        cropView = (CropImageView) findViewById(R.id.cropImageView);
        button_choose = (Button) findViewById(R.id.button_choose);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        cameraUri =getIntent().getStringExtra("uri");


        //cropView.setImageBitmap(bitmap);
        if(AttachmentFragment.isCamera) {
            if(AttachmentFragment.thumbnail != null) {
                cropView.setImageBitmap(AttachmentFragment.thumbnail);
            } else {
                onBackPressed();
            }
        }
        else
            cropView.setImageUriAsync(Uri.parse(cameraUri));
        resultIntent= new Intent();



        button_choose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialog.show();
                    }
                });
                resultBitmap = cropView.getCroppedImage();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {



                        isImageCropped=true;


                        try
                        {
                            URI=storeImage(resultBitmap).getAbsolutePath();
                            Bitmap cropped=compressImage(Uri.parse(URI),"cropped_image");
                            path=storeImage(cropped);

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        resultIntent.putExtra("uri",path.getAbsolutePath());

                        setResult(RESULT_OK,resultIntent);
                        AttachmentFragment.thumbnail=null;
                /*if(progressDialog!=null)
                    progressDialog.cancel();*/

                        finish();

                    }
                });



            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isImageCropped=false;
                setResult(RESULT_CANCELED,resultIntent);
                finish();

            }
        });





        /*try {
            bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(img_uri));
            cropView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //cropView.setImageBitmap(AttachmentFragment.bitmap);

        //cropView.setImageUriAsync(Uri.parse(img_uri));

        //Glide.with(this).load(AttachmentFragment.contentURI).into(cropView);

        //cropView.setImageURI(AttachmentFragment.contentURI);


        /*resultBitmap=cropView.crop();
        cropView.extensions()
                .crop()
                .quality(87)
                .format(PNG)
                .into(croppedFile);*/
        //uri=Uri.parse(croppedFile.getAbsolutePath());
        //new MainActivity().createAndLoadFragment(Constant.FR_ATTACHMENT,true,null);
       /*cropImageView=(com.edmodo.cropper.CropImageView)findViewById(R.id.CropImageView);

        uri= Uri.parse(getIntent().getStringExtra("uri"));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImageView.setImageBitmap(bitmap);
       resultBitmap=cropImageView.getCroppedImage();*/




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog!=null)
            progressDialog.cancel();
        progressDialog=null;
    }

    private File storeImage(Bitmap image) throws IOException {
        File pictureFile = createImageFile(image);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }
    public File createImageFile(Bitmap bitmap) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        //check the rotation of the image and display it properly
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

//    FileOutputStream out = null;
//    File file = new File(Environment.getExternalStorageDirectory().getPath(), "ProfilePic");
//    if (!file.exists()) {
//      file.mkdirs();
//    }
//    String uriSting = (file.getAbsolutePath() + "/" + fileName + ".jpg");
//    String filename = uriSting;
//    try {
//      out = new FileOutputStream(filename);
//      scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    }

        //  File tempFile = new File("" + imagePath);
        // imageSize = tempFile.length() / GALLERY_CROP24;
        return scaledBitmap;
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
    FileInputStream getSourceStream(Uri u) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    this.getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            out = new FileInputStream(fileDescriptor);
        } else {
            out = (FileInputStream) this.getContentResolver().openInputStream(u);
        }
        return out;
    }
    private String getRealPathFromURI(String contentURI) {


        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

}