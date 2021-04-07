package org.artqq.utils;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    public int READ_TIMEOUT = 100;
    // 读取超时时间
    public int CONNECT_TIMEOUT = 60;
    // 连接超时时间
    public int WRITE_TIMEOUT = 60;
    // 写入超时时间
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mOkHttpClient;
    public HashMap<String, String> header = new HashMap<>();
    public static final String defsult_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36";

    /**
     * 自定义网络回调接口
     */
    public interface NetCall {
        void success(Call call, Response response) throws IOException;

        void failed(Call call, IOException e);
    }

    public OkHttpUtil useDefault_User_Agent(){
        header.put("User-Agent", defsult_user_agent);
        return this;
    }

    public OkHttpUtil setUserAgent(String userAgent){
        header.put("User-Agent", userAgent);
        return this;
    }

    public OkHttpUtil(){
        init();
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @return
     */
    public Response getData(String url) {
        init();
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            builder.addHeader(key, v);
        }
        Request request = builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param netCall
     * @return
     */
    public void getDataAsyn(String url, final NetCall netCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            builder.addHeader(key, v);
        }
        Request request = builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    netCall.failed(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    netCall.success(call, response);

                }
            });
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param netCall
     */
    public void postDataAsyn(String url, Map<String, String> bodyParams, final NetCall netCall) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    netCall.failed(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    netCall.success(call, response);

                }
            });
    }

    public void postDataAsyn(String url, String data, final NetCall netCall) {
        //1构造RequestBody
        RequestBody body = RequestBody.create(MediaType.parse("text/html;charset=utf-8"), data);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    netCall.failed(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    netCall.success(call, response);
                }
            });
    }

    public Response postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public void postJsonAsyn(String url, String json, final NetCall netCall) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    netCall.failed(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    netCall.success(call, response);
                }
            });
    }

    /**
     * post请求，同步方式，提交数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     * @param url 链接
     * @param bodyParams post内容
     */
    public Response postData(String url, Map<String, String> bodyParams) {
        init();
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response postData(String url, String data) {
        init();
        //1构造RequestBody
        RequestBody body = RequestBody.create(MediaType.parse("text/html;charset=utf-8"), data);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Iterator<String> iterator = header.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            String v = header.get(key);
            requestBuilder.addHeader(key, v);
        }
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * post的请求参数，构造RequestBody
     *
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }

    /**
     * 准备工作
     * @return OkHttpUtil
     */
    public OkHttpUtil init(){
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 读取超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        // 连接超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        // 写入超时
        // 支持HTTPS请求，跳过证书验证
        ClientBuilder.sslSocketFactory(createSSLSocketFactory());
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        mOkHttpClient = ClientBuilder.build();
        return this;
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    /**
     * 设置连接超时时间
     * @param CONNECT_TIMEOUT
     * @return
     */
    public OkHttpUtil setCONNECT_TIMEOUT(int CONNECT_TIMEOUT) {
        this.CONNECT_TIMEOUT = CONNECT_TIMEOUT;
        return this;
    }

    /**
     * 设置读取超时时间
     * @param READ_TIMEOUT
     * @return
     */
    public OkHttpUtil setREAD_TIMEOUT(int READ_TIMEOUT) {
        this.READ_TIMEOUT = READ_TIMEOUT;
        return this;
    }

    /**
     * 设置写入超时时间
     * @param WRITE_TIMEOUT
     * @return
     */
    public OkHttpUtil setWRITE_TIMEOUT(int WRITE_TIMEOUT) {
        this.WRITE_TIMEOUT = WRITE_TIMEOUT;
        return this;
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public OkHttpUtil addHeader(String key, String value){
        header.put(key, value);
        return this;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public OkHttpUtil delHeader(String key){
        header.remove(key);
        return this;
    }

}
