package com.fangdean.minilife.data.remote;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by fda on 2017/7/14.
 */

public class OkHttpUtil {
    private static OkHttpClient okHttpClient;
    private static OkHttpClient longOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpUtil.createOkHttpClient(10);
        }
        return okHttpClient;
    }

    public static OkHttpClient getLongOkHttpClient() {
        if (longOkHttpClient == null) {
            longOkHttpClient = OkHttpUtil.createOkHttpClient(30);
        }
        return longOkHttpClient;
    }

    public static OkHttpClient createOkHttpClient(long timeout) {
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        return builder.build();
    }

    public static Map<String, RequestBody> createMultiFileMultipart(List<String> names, List<File> files) {
        Map<String, RequestBody> fileMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), files.get(i));
            fileMap.put(names.get(i), fileRequestBody);
        }
        return fileMap;
    }

    /**
     * 不带进度的单文件上传
     *
     * @param name
     * @param file
     * @return
     */
    public static MultipartBody.Part createSingleFilePart(String name, File file) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(name, file.getName(), fileRequestBody);
        return filePart;
    }

    /**
     * 不带进度的单图片上传
     *
     * @param name
     * @param bitmap
     * @return
     */
    public static MultipartBody.Part createBitmapPart(String name, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(name, System.currentTimeMillis() + ".jpeg", fileRequestBody);
        return filePart;
    }

}
