package com.dewii.mpeapp.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.dewii.mpeapp.constants.Directory;
import com.dewii.mpeapp.managers.FileManager;
import com.dewii.mpeapp.models.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public final class FileUtils {
    public static final String TAG = FileUtils.class.getCanonicalName();

    private FileUtils() {

    }

    public static ArrayList<FileModel> getFileModels(String[] imagePaths) {
        ArrayList<FileModel> fileModels = new ArrayList<>();
        for (int i = 0; i < imagePaths.length; i++) {
            fileModels.add(new FileModel(new File(imagePaths[i]).getName(), imagePaths[i], ""));
        }
        return fileModels;
    }

    public static FileModel toFileModel(String imagePath) {
        return new FileModel(new File(imagePath).getName(), imagePath, "");
    }

    public static FileModel toFileModel(String name, String path, String extra) {
        return new FileModel(name, path, extra);
    }

    public static File getDirectory(Directory directory) {
        return mkDir(new File(
                mkDir(new File(
                        Environment.getExternalStorageDirectory()
                                + "/" + Directory.ROOT.getDirName()))
                        + "/" + directory.getDirName()));
    }

    public static synchronized File createImageFile(Activity activity, String prefix, byte[] bytes) {
        try {
            File destFile = createImageFile(activity, null, prefix);
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            fileOutputStream.flush();

            return destFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static synchronized File createImageFile(Activity activity, File storageDest, String namePrefix) throws IOException {
        storageDest = storageDest == null ?
                activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) : storageDest;

        namePrefix = namePrefix == null ? "IMG_" : namePrefix;

        String fileName = namePrefix + DateUtils.fileTimeStamp();
        String fullFileName = fileName + ".jpg";

        File imageFile = new File(storageDest, fullFileName);
        Log.i(TAG, "createImageFile: imageFile = " + imageFile.getAbsolutePath());

        return imageFile;
    }

    public static String getFileRealName(String filepath) {
        String[] fileNameParts = filepath.split("/");
        return fileNameParts[fileNameParts.length - 1];
    }

    /**
     * This method is called to create one, if file does not exist
     *
     * @param directory name for the folder to be created
     */
    public static File mkDir(File directory) {
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

    public static ArrayList<File> getCompressedFiles(Context context, ArrayList<FileModel> fileModels) {
        ArrayList<File> compressedFiles = new ArrayList<>();

        int fileListSize = fileModels.size();
        for (int i = 0; i < fileListSize; i++)
            compressedFiles.add(getCompressedFile(context, fileModels.get(i)));

        return compressedFiles;
    }

    public static File getCompressedFile(Context context, FileModel fileModel) {
        return compressFile(context, fileModel.getFilePath());
    }

    public static ArrayList<File> getCompressedFiles(Context context, List<String> filePaths) {
        ArrayList<File> compressedFiles = new ArrayList<>();

        int pathListSize = filePaths.size();
        for (int i = 0; i < pathListSize; i++)
            compressedFiles.add(compressFile(context, filePaths.get(i)));

        return compressedFiles;
    }

    public static ArrayList<File> compressFiles(Context context, String[] filePaths) {
        ArrayList<File> compressedFiles = new ArrayList<>();

        for (String filePath : filePaths)
            if (!StringUtils.isInvalid(filePath))
                compressedFiles.add(compressFile(context, filePath));

        return compressedFiles;
    }

    public static File compressFile(Context context, String filePath) {
        File compressedFile = new File(filePath);
        try {
            compressedFile = Compressor.getDefault(context).compressToFile(compressedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressedFile;
    }

    public static void createImageThumbnail(Context context, Uri uri, File file) {
        try {
            final int THUMBNAIL_SIZE = 64;
            File compressedFile = compressFile(context, uri.getPath());

            FileInputStream fis = new FileInputStream(compressedFile.getAbsolutePath());
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            Float width = (float) imageBitmap.getWidth();
            Float height = (float) imageBitmap.getHeight();
            Float ratio = width / height;
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);

            saveThumbnail(file, imageBitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createVideoThumbnail(Context context, Uri uri, File file) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
        saveThumbnail(file, bitmap);
    }

//    public static void createDocumentThumbnail(Context context, Uri uri, File file) {
//        int pageNumber = 0;
//        PdfiumCore pdfiumCore = new PdfiumCore(context);
//        try {
//            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "r");
//            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
//            pdfiumCore.openPage(pdfDocument, pageNumber);
//            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
//            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
//            pdfiumCore.closeDocument(pdfDocument);
//
//            saveThumbnail(file, bmp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static void saveThumbnail(File file, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTempFileSuffix(String uri) {
        String[] splitUri = uri.split("\\.");
        Log.i(TAG, "getTempFileSuffix: ." + splitUri[splitUri.length - 1]);
        return "." + splitUri[splitUri.length - 1];
    }

    public static String getPlaybackDuration(int milliseconds) {
        String min, sec;
        int seconds = milliseconds / 1000;
        int mins = seconds / 60, secs = seconds % 60;
        if (mins < 10)
            min = "0" + mins;
        else
            min = "" + mins;

        if (secs < 10)
            sec = "0" + secs;
        else
            sec = "" + secs;

        return min + ":" + sec;
    }

    public static String getFileSize(long size) {
        long oneMB = 1024 * 1024, oneKB = 1024, oneMBThousandth = 1000 / oneMB, oneKBThousandth = 1000 / oneKB;
        if (size / oneMB > 0) {
            return size / oneMB + "." + (oneMBThousandth * (size % oneMB)) + " MB";
        } else
            return size / oneKB + "." + (oneKBThousandth * (size % oneKB)) + " KB";
    }

    public static String getInternalStoragePath(final Context context, final Uri uri) throws IOException {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isSDCardAvailable(Context context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        return storages.length > 1 && storages[0] != null && storages[1] != null;
    }

    private static String getSDCardRotPath(Context context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        File storageInSDCard = storages[1];
        File removableSDCardAsRootFile = storageInSDCard.getParentFile().getParentFile().getParentFile().getParentFile();
        Log.i(TAG, "getSDCardRotPath: " + String.valueOf(removableSDCardAsRootFile));
        return removableSDCardAsRootFile.getAbsolutePath();
    }

    public static String getExternalStoragePath(Context context, Uri uri) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            String sdcardPath = getSDCardRotPath(context);
            return sdcardPath.concat("/").concat(split[1]);
        } else
            return null;
    }

    public static String getMimeType(String uri) {
        String[] fileNameParts = new File(uri).getName().split("\\.");

        int filePartsLength = fileNameParts.length;
        Log.i(TAG, "getMimeType: " + fileNameParts[filePartsLength - 1]);
        switch (fileNameParts[filePartsLength - 1]) {
            // text extensions
            case "txt":
                return com.dewii.mpeapp.constants.File.Mime.txt;
            // image extensions
            case "bmp":
                return com.dewii.mpeapp.constants.File.Mime.bmp;
            case "cgm":
                return com.dewii.mpeapp.constants.File.Mime.cgm;
            case "gif":
                return com.dewii.mpeapp.constants.File.Mime.gif;
            case "jpeg":
                return com.dewii.mpeapp.constants.File.Mime.jpeg;
            case "jpg":
                return com.dewii.mpeapp.constants.File.Mime.jpg;
            case "mdi":
                return com.dewii.mpeapp.constants.File.Mime.mdi;
            case "psd":
                return com.dewii.mpeapp.constants.File.Mime.psd;
            case "png":
                return com.dewii.mpeapp.constants.File.Mime.png;
            case "svg":
                return com.dewii.mpeapp.constants.File.Mime.svg;
            // audio extensions
            case "adp":
                return com.dewii.mpeapp.constants.File.Mime.adp;
            case "aac":
                return com.dewii.mpeapp.constants.File.Mime.aac;
            case "mpga":
                return com.dewii.mpeapp.constants.File.Mime.mpga;
            case "mp4a":
                return com.dewii.mpeapp.constants.File.Mime.mp4a;
            case "oga":
                return com.dewii.mpeapp.constants.File.Mime.oga;
            case "wav":
                return com.dewii.mpeapp.constants.File.Mime.wav;
            case "mp3":
                return com.dewii.mpeapp.constants.File.Mime.mp3;
            // video extensions
            case "3gp":
                return com.dewii.mpeapp.constants.File.Mime.a3gp;
            case "3g2":
                return com.dewii.mpeapp.constants.File.Mime.a3g2;
            case "avi":
                return com.dewii.mpeapp.constants.File.Mime.avi;
            case "xlv":
                return com.dewii.mpeapp.constants.File.Mime.xlv;
            case "m4v":
                return com.dewii.mpeapp.constants.File.Mime.m4v;
            case "mp4":
                return com.dewii.mpeapp.constants.File.Mime.mp4;
            // documents extension
            case "rar":
                return com.dewii.mpeapp.constants.File.Mime.rar;
            case "zip":
                return com.dewii.mpeapp.constants.File.Mime.zip;
            case "pdf":
                return com.dewii.mpeapp.constants.File.Mime.pdf;
            case "dvi":
                return com.dewii.mpeapp.constants.File.Mime.dvi;
            case "karbon":
                return com.dewii.mpeapp.constants.File.Mime.karbon;
            case "mdb":
                return com.dewii.mpeapp.constants.File.Mime.mdb;
            case "xls":
                return com.dewii.mpeapp.constants.File.Mime.xls;
            case "pptx":
                return com.dewii.mpeapp.constants.File.Mime.pptx;
            case "docx":
                return com.dewii.mpeapp.constants.File.Mime.docx;
            case "ppt":
                return com.dewii.mpeapp.constants.File.Mime.ppt;
            case "doc":
                return com.dewii.mpeapp.constants.File.Mime.doc;
            case "oxt":
                return com.dewii.mpeapp.constants.File.Mime.oxt;
            default:
                return "file/*";
        }
    }

    public static String getFileCategory(String uri) {
        String mimeType = getMimeType(uri);
        Log.i(TAG, "getFileCategory. " + mimeType.split("/")[0]);
        return mimeType.split("/")[0];
    }

    public static boolean deleteFileFromStorage(ArrayList<File> files) {
        boolean deleted = true;
        try {
            for (File file : files)
                deleted = deleted && deleteFileFromStorage(file);
        } catch (Exception e) {
            Log.e(FileManager.TAG, "deleteFileFromStorage: " + e.getMessage());
        }
        return deleted;
    }

    public static boolean deleteFileFromStorage(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            Log.e(FileManager.TAG, "deleteFileFromStorage: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile - FileInputStream for the file to copy from.
     * @param toFile   - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}
