package com.example.news.application;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Okhttp3网络请求框架，工具类封装
 * Name: laodai
 * Time：2019.08.05
 */
public class OkhttpManager {

    private static final String FILE_PREFIX="CNIAO5_";
    private OkHttpClient client;
    private static OkhttpManager okhttpManager;
    private Handler handler;

    /**
     * 请求单例
     * @return
     */
    private static OkhttpManager getInstance() {
        if (okhttpManager == null) {
            okhttpManager = new OkhttpManager();
        }
        return okhttpManager;
    }

    /**
     * 构造方法
     */
    private OkhttpManager() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    //********************内部逻辑的请求处理方法********************
    /**
     * 同步Get请求方法
     * @param url
     * @return
     * @throws IOException
     */
    private Response p_getSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        return call.execute();
    }

    /**
     * 进行发送同步GET请求，并且返回String类型数据
     * @param url
     * @return
     * @throws IOException
     */
    private String p_getSyncString(String url) throws IOException {
        return Objects.requireNonNull(OkhttpManager.getSync(url).body()).string();
    }

    /**
     * 异步GET请求
     * @param url
     * @param callBack
     * @throws IOException
     */
    private void p_getAsync(String url, DataCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) {
                deliverSuccess(call, response, callBack);
            }
        });
    }

    /**
     * 内部POST请求
     * @param url
     * @param map
     * @param callBack
     */
    private void p_getPostAsync(String url, Map<String, String> map, DataCallBack callBack) {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            map = new HashMap<String, String>();
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = null;
            if (entry.getValue() == null) value = "";
            else value = entry.getValue().toString();
            builder.add(key, value);
        }
        requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                deliverSuccess(call, response, callBack);
            }
        });
    }

    /**
     * 内部执行异步下载文件逻辑处理
     * @param url 文件的地址
     * @param destDir 文件存储的绝对路径
     * @param callBack
     */
    private void p_downloadAsync(String url, String destDir, DataCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //开始写入文件
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try { //异常捕获
                    inputStream = response.body().byteStream();
                    byte[] bytes = new byte[2048];
                    int len = 0;
                    File file = new File(destDir, getFileName(url));
                    fileOutputStream = new FileOutputStream(file);
                    while ((len = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, 0);
                    }
                    //刷新
                    fileOutputStream.flush();
                    deliverSuccess(call, response, callBack);
                } catch (IOException e) {
                    deliverFailure(call, e, callBack);
                } finally { //最后执行
                    //关闭文件输出流
                    if (fileOutputStream != null) fileOutputStream.close();
                    //关闭输入流
                    if (inputStream != null) inputStream.close();
                }
            }
        });
    }

    /**
     * 根据文件URL地址获取文件的路径文件名
     * @param pUrl
     * @return
     */
    private String getFileName(String pUrl) {
        int separatorIndex = pUrl.lastIndexOf("/");
        String path = (separatorIndex < 0) ? pUrl : pUrl.substring(separatorIndex + 1, pUrl.length());
        return FILE_PREFIX + path;
    }

    //********************数据请求成功或者失败分发方法********************
    /**
     * 请求分发请求失败的数据情况
     * @param call
     * @param e
     * @param callBack
     */
    private void deliverFailure(Call call, IOException e, DataCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    //响应接口
                    callBack.requestFailure(call, e);
                }
            }
        });
    }

    /**
     * 请求分发请求成功的数据情况
     * @param call
     * @param response
     * @param callBack
     */
    private void deliverSuccess(Call call, Response response, DataCallBack callBack) {
        handler.post(() -> {
            if (callBack != null) {
                try {
                    //响应接口
                    callBack.requestSuccess(call, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //********************对外公布的请求处理方法********************
    /**
     * 外部同步GET请求方法
     * @param url
     * @return
     * @throws IOException
     */
    public static Response getSync(String url) throws IOException {
        return getInstance().p_getSync(url);
    }

    /**
     * 外部同步返回StringGET请求方法
     * @param url
     * @return
     * @throws IOException
     */
    public static String getSyncString(String url) throws IOException {
        return getInstance().p_getSyncString(url);
    }

    /**
     * 外部异步GET请求方法
     * @param url
     * @return
     * @throws IOException
     */
    public static void getASync(String url, DataCallBack callBack) {
        getInstance().p_getAsync(url, callBack);
    }

    /**
     * 外部发送POST请求
     * @param url
     * @param map
     * @param callBack
     */
    public static void getPostAsync(String url, Map<String, String> map, DataCallBack callBack) {
        getInstance().p_getPostAsync(url, map, callBack);
    }

    /**
     * 外部执行异步下载文件
     * @param url 文件的地址
     * @param destDir 文件存储的绝对路径
     * @param callBack
     */
    public static void downloadAsync(String url, String destDir, DataCallBack callBack) {
        getInstance().p_downloadAsync(url, destDir, callBack);
    }

    /**  数据回调接口 */
    public interface DataCallBack {
        /**
         * 请求失败
         * @param call
         * @param e
         */
        void requestFailure(Call call, IOException e);

        /**
         * 请求成功
         * @param call
         * @param response
         * @throws IOException
         */
        void requestSuccess(Call call, Response response) throws IOException;
    }

}
