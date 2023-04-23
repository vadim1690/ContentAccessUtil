package com.vadim.dev.mobile.contentaccessutil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Utility class that contains methods for taking pictures, capturing videos,
 * selecting files, and previewing taken pictures. These methods are tied to
 * the lifecycle of an activity using a lifecycle observer. The class also
 * defines interfaces for callbacks that can be implemented when these actions
 * are completed.
 */
public class ContentAccessLifeCycleObserver implements DefaultLifecycleObserver {

    // Constants for keys used to store results in the activity result registry
    private final String TAKE_PICTURE_PREVIEW_KEY = "TAKE_PICTURE_PREVIEW_KEY";
    private final String SELECT_FILE_KEY = "SELECT_FILE_KEY";

    // Activity result launchers for each type of action
    private ActivityResultLauncher<Void> takePicturePreviewResult;
    private ActivityResultLauncher<String> selectImageFromGalleryResult;

    // MIME types for various file types
    public static final String ALL_FILES = "*/*";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_WAV = "audio/wav";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/jpeg";
    public static final String AUDIO_MP3 = "audio/mpeg";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String FILE_CSV = "text/csv";
    public static final String FILE_WORD = "application/msword";
    public static final String FILE_HTML = "text/html";
    public static final String FILE_JSON = "application/json";
    public static final String FILE_PDF = "application/pdf";
    public static final String FILE_PPT = "application/vnd.ms-powerpoint";
    public static final String FILE_XLS = "application/vnd.ms-excel";

    // Activity result registry
    private final ActivityResultRegistry mRegistry;


    /**
     * Callback interface for when a picture preview is taken.
     */
    public interface TakePicturePreviewCallback {
        void onPicturePreviewTaken(Bitmap bitmapResult);
    }

    /**
     * Callback interface for when a file is selected.
     */
    public interface SelectFileCallback {
        void onFileSelected(Uri uri);
    }

    /**
     * Constructor that takes an activity result registry.
     *
     * @param registry the activity result registry
     */
    public ContentAccessLifeCycleObserver(@NonNull ActivityResultRegistry registry) {
        mRegistry = registry;
    }

    /**
     * Called when the owner's onCreate method is called.
     *
     * @param owner the lifecycle owner
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
    }

    /**
     * Takes a picture preview and returns the Bitmap to the callback function.
     *
     * @param callback the callback function that takes the Bitmap of the picture preview
     */
    public void takePicturePreview(TakePicturePreviewCallback callback) {
        takePicturePreviewResult = mRegistry.register(TAKE_PICTURE_PREVIEW_KEY, new ActivityResultContracts.TakePicturePreview(), resultBitMap -> {
            if (callback != null) {
                callback.onPicturePreviewTaken(resultBitMap);
            }
        });
        takePicturePreviewResult.launch(null);
    }

    /**
     * Launches an intent to allow the user to select a file of a specific mime type.
     *
     * @param mimeType the desired mime type of the file to be selected
     * @param callback the callback to be invoked after the user has selected a file
     */
    public void selectFile(String mimeType, SelectFileCallback callback) {
        selectImageFromGalleryResult = mRegistry.register(SELECT_FILE_KEY, new ActivityResultContracts.GetContent(), uri -> {
            if (callback != null) {
                callback.onFileSelected(uri);
            }
        });
        selectImageFromGalleryResult.launch(mimeType);
    }


    // Method to save a file to external storage
    public void saveFileToExternalStorage(Activity activity, Uri fileUri, String fileName, String mimeType) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(fileUri);
        OutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), fileName));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();

        // Add the saved file to the media scanner so it shows up in the gallery
        MediaScannerConnection.scanFile(activity, new String[]{Environment.getExternalStorageDirectory() + "/" + fileName}, new String[]{mimeType}, null);
    }

    // Method to get the metadata of a file
    public Bundle getFileMetadata(Activity activity, Uri fileUri) {
        Cursor cursor = activity.getContentResolver().query(fileUri, null, null, null, null, null);
        Bundle metadata = new Bundle();
        if (cursor != null && cursor.moveToFirst()) {
            int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            String displayName = cursor.getString(displayNameIndex);
            long size = cursor.getLong(sizeIndex);
            metadata.putString("name", displayName);
            metadata.putLong("size", size);
            cursor.close();
        }
        return metadata;
    }

    // Method to share a file
    public void shareFile(Activity activity, Uri fileUri, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(activity.getContentResolver().getType(fileUri));
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        activity.startActivity(Intent.createChooser(intent, title));
    }


    // Method to get the file extension from a URI
    public String getFileExtension(Context context, Uri uri) {
        String extension = null;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (contentResolver != null) {
            String mimeType = contentResolver.getType(uri);
            if (mimeType != null) {
                extension = mimeTypeMap.getExtensionFromMimeType(mimeType);
            }
        }
        if (extension == null) {
            extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        }
        return extension;
    }

    // Method to resize an image and save it to the cache directory
    public Uri resizeImage(Context context, Uri uri, int maxWidth, int maxHeight, int quality) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        File file = new File(context.getCacheDir(), "image_" + System.currentTimeMillis() + ".jpg");
        OutputStream outputStream = new FileOutputStream(file);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        outputStream.close();
        return Uri.fromFile(file);
    }

}


