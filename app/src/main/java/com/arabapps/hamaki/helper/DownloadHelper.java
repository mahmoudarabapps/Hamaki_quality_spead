package com.arabapps.hamaki.helper;

public class DownloadHelper {
   /* public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }*/

   /* public static void download(Context context, String url, String filename) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(filename);
        request.setTitle(filename);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);


// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager.enqueue(request) > 0)
            DialogHelper.Companion.printMessage(context, "Downloading in background");
    }*/

/*    private static final String TAG = "DownloadManager manager ";
    private static DownloadManager manager;

    public static String isFileDownloaded(Context context, String fileName) {

        DownloadManager.Query query = new DownloadManager.Query();

        if (manager == null)
            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = manager.query(query);
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (BuildConfig.DEBUG)  Log.d(TAG, "isFileDownloaded: file name " + fileName + " \t " + name);
                    if (name.contains(fileName.replaceAll(".pdf", "").replaceAll(".jpg", "")
                            .replaceAll(".png", "").replaceAll(".jpeg", ""))) {
                        return name;
                    }
                }
            } else
                return null;
        }

        if (BuildConfig.DEBUG)  Log.d(TAG, "isFileDownloaded: test finishyed ");

        return null;
    }*/
}
