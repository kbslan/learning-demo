package com.kbslan.esl.service.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chao.lan
 */
@Service
@Component
public class HttpComponent implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(HttpComponent.class);
    public static final MediaType JSON_TYPE  = MediaType.parse("application/json; charset=utf-8");

    @Value("${http.connectTimeout:3000}")
    private Long connectTimeout;
    @Value("${http.readTime:2000}")
    private Long readTime;
    @Value("${http.writeTime:2000}")
    private Long writeTime;
    @Value("${http.retry:2}")
    private Integer retry;

    private static OkHttpClient client;

    private static OkHttpClient ignoreCertClient;

    public String postHttpsIgnoreCert(String url, String json) throws IOException {
        return postHttpsIgnoreCert(url, json, Collections.emptyMap());
    }

    public String postHttpsIgnoreCert(String url, String json, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        String doUrl = this.straightUrl(url);
        Request.Builder builder = new Request.Builder()
                .url(doUrl)
                .post(body);
        if (Objects.nonNull(headers)) {
            headers.forEach(builder::header);
        }
        Request request = builder.build();
        Response response = ignoreCertClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("postIgnore url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    public String put(String url, String json) throws IOException {
        return put(url, json, Collections.emptyMap());
    }

    public String put(String url, String json, Map<String, String> headers) throws IOException {
        return doPut(url, json, Headers.of(headers));
    }

    public String doPut(String url, String json, Headers headers) throws IOException {
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        String doUrl = this.straightUrl(url);
        Request.Builder builder = new Request.Builder()
                .url(doUrl)
                .headers(headers)
                .put(body);

        Request request = builder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("put url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String post(String url, String param) throws IOException {
        return post(url, param, Collections.emptyMap());
    }

    public void postSync(String url, String param, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON_TYPE, param);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public String post(String url, String param, Map<String, String> headers) throws IOException {
        return doPost(url, param, Headers.of(headers));
    }

    public String doPost(String url, String param, Headers headers) throws IOException {
        RequestBody body = RequestBody.create(JSON_TYPE, param);
        String doUrl = this.straightUrl(url);
        Request.Builder builder = new Request.Builder()
                .url(doUrl)
                .headers(headers)
                .post(body);

        Request request = builder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("post url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String postForm(String url, Map<String, Object> map, Map<String, String> headers) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key).toString());
        }
        FormBody body = builder.build();
        String doUrl = this.straightUrl(url);
        Request.Builder requestBuilder = new Request.Builder()
                .url(doUrl)
                .post(body);
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("postForm url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String get(String url) throws IOException {
        return get(url, Collections.emptyMap(), Collections.emptyMap());
    }

    public String get(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return doGet(url, params, Headers.of(Objects.isNull(headers) ? Collections.emptyMap() : headers));
    }

    public String doGet(String url, Map<String, Object> params, Headers headers) throws IOException {
        if (MapUtils.isNotEmpty(params)) {
            url = url + "?" + buildGetParams(params);
        }
        String doUrl = this.straightUrl(url);
        Request.Builder builder = new Request.Builder().url(doUrl).headers(headers).get();
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("get url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String delete(String url) throws IOException {
        return doDelete(url, null, Headers.of(Collections.emptyMap()));
    }

    public String delete(String url, Object param) throws IOException {
        return doDelete(url, param, Headers.of(Collections.emptyMap()));
    }

    public String doDelete(String url, Object param, Headers headers) throws IOException {
        Request request;
        String doUrl = this.straightUrl(url);
        if (Objects.isNull(param)) {
            request = new Request.Builder().url(doUrl).headers(headers).delete().build();
        } else {
            RequestBody body = RequestBody.create(JSON_TYPE, JSON.toJSONString(param));
            request = new Request.Builder().url(doUrl).headers(headers).delete(body).build();
        }
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            logger.info("delete url={} result={}", doUrl, result);
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 构建get请求参数
     * @param params
     * @return
     */
    private String buildGetParams(Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            List<String> paramsList = new ArrayList<>();
            for (String key : params.keySet()) {
                paramsList.add(key + "=" + params.get(key).toString());
            }
            return StringUtils.join(paramsList,"&");
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTime, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTime, TimeUnit.MILLISECONDS);
        builder.interceptors().add(new Retry(retry));
        client = builder.build();

        OkHttpClient.Builder ignoreBuilder =new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), (X509TrustManager) SSLSocketClient.getTrustManager()[0])
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        ignoreBuilder.interceptors().add(new Retry(retry));
        ignoreCertClient = ignoreBuilder.build();
    }

    /**
     * 纠错url:  //-> /
     */
    public String straightUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        return url.replace("//", "/").replace(":/", "://");
    }

    public static class Retry implements Interceptor {

        int maxRetry;//最大重试次数

        Retry(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            int retryNum = 1;
            Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                response.close();
                response = chain.proceed(request);
            }
            return response;
        }

    }
}
