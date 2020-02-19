package dm.sime.com.kharetati.utility.Files;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import dm.sime.com.kharetati.utility.constants.AppConstants;

public class DocumentUtility {

    public static String getPath(Context context, Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if (AppConstants.DOC_TYPE_ID_PRIMARY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    if(split[1] != null && split[1].length() > 0) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }

                /*try {
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } catch (NumberFormatException e) {
                    return null;
                }*/

               /* final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);*/
            } /*else if (isGoogleStorageDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }*/ else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if (AppConstants.DOC_TYPE_ID_IMAGE.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (AppConstants.DOC_TYPE_ID_VIDEO.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AppConstants.DOC_TYPE_ID_AUDIO.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if (AppConstants.DOC_INITIALS_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if (AppConstants.DOC_INITIALS_FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception es){
            if (cursor != null)
                cursor.close();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static void previewDocument(Activity activity, String extrenalPath, String mimeType){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + extrenalPath);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent chooser = Intent.createChooser(intent, "Open with");

        try {
            activity.startActivity(chooser);
        } catch (Exception e) {
            //logger.error("No Application available to view " + resume.getFileMimeType(), e);
            String ex = e.getStackTrace().toString();
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return AppConstants.EXTERNAL_STORAGE_DOC_PATH.equals(uri.getAuthority());
    }

    public static boolean isGoogleStorageDocument(Context context, Uri uri) {
        boolean isGoogleDrive = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            return AppConstants.GOOGLE_STORAGE_DOC_PATH.equals(uri.getAuthority());
        }
        return isGoogleDrive;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return AppConstants.DOWNLOAD_STORAGE_DOC_PATH.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return AppConstants.MEDIA_STORAGE_DOC_PATH.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return AppConstants.GOOGLE_STORAGE_PHOTOS_PATH.equals(uri.getAuthority());
    }



    public static String getMimeType(String fileExtension){
        String mimeType = "";
        if (fileExtension.equals(AppConstants.DOC_TYPE_PDF)) {
            mimeType = AppConstants.DOC_TYPE_PDF_MIME;
        }
        return  mimeType;
    }

    public static String getFileName(String fileName) {
        String fileGUID = null;
        if (null != fileName) {
            int lastIndex = fileName.lastIndexOf(".");
            return fileName.substring(0, lastIndex);
        }
        return fileGUID;
    }

    public static String getFileExtension(String fileName) {
        String fileExtension = null;
        if (null != fileName) {
            String[] extensionFinder = fileName.split("\\.");
            fileExtension = extensionFinder[extensionFinder.length - 1];
        }
        return fileExtension;
    }


    public static boolean isCorrectDocExtension(String fileExtension){
        boolean isCorrect = false;
        if (fileExtension.toLowerCase().equals(AppConstants.DOC_TYPE_PDF)) {
            isCorrect = true;
        }
        return isCorrect;
    }
}
