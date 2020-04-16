package com.nachtgeistw.impurebird.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;

import com.jakewharton.disklrucache.DiskLruCache;

import javax.net.ssl.HttpsURLConnection;

//https://blog.csdn.net/guolin_blog/article/details/28863651

public class Cache {
    /**
     * Get cache directory
     */
    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Objects.requireNonNull(context.getExternalCacheDir()).getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Get the current version of application
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static DiskLruCache openDiskLruCache(Context context) {
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    public static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpsURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
//            if (url.getProtocol().toLowerCase().equals("https")) {
//                urlConnection = (HttpsURLConnection) url.openConnection();
//            }
            urlConnection = (HttpsURLConnection) url.openConnection();
            Log.e("Twitter", "cache > downloadUrlToStream > urlConnection = " + urlConnection);
            InputStream i = urlConnection.getInputStream();
            Log.e("Twitter", "cache > downloadUrlToStream > InputStream = " + i);
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            Log.e("Twitter", "cache > downloadUrlToStream > in = " + in);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            Log.e("Twitter", "cache > downloadUrlToStream > out = " + out);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @param imageUrlKey the MD5 key of image url
     * @param diskLruCache the DLC that stores of this image key
     * @param imageView the imageView to be set
     * @throws IOException if set failed
     */
    public static void setImageFromCache(String imageUrlKey, DiskLruCache diskLruCache, ImageView imageView) throws IOException {
        try {
            DiskLruCache.Snapshot snapShot = diskLruCache.get(imageUrlKey);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
