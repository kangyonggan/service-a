package com.kangyonggan.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kyg
 */
public final class HttpUtil {

    private HttpUtil() {
    }

    /**
     * http get
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            return getResponseContent(httpClient.execute(get));
        } catch (Exception e) {
            throw e;
        } finally {
            get.abort();
        }
    }

    /**
     * http get
     *
     * @param url
     * @param httpClient
     * @return
     * @throws Exception
     */
    public static String get(String url, CloseableHttpClient httpClient) throws Exception {
        HttpGet get = new HttpGet(url);
        try {
            return getResponseContent(httpClient.execute(get));
        } catch (Exception e) {
            throw e;
        } finally {
            get.abort();
        }
    }

    /**
     * http post
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params) throws Exception {
        HttpPost post = new HttpPost(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> list = new ArrayList<>();
                for (String key : params.keySet()) {
                    list.add(new BasicNameValuePair(key, params.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(list, StandardCharsets.UTF_8));
            }

            return getResponseContent(httpClient.execute(post));
        } catch (Exception e) {
            throw e;
        } finally {
            post.abort();
        }
    }

    /**
     * 获取响应内容
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String getResponseContent(HttpResponse response) throws Exception {
        return getResponseContent(response, StandardCharsets.UTF_8.name());
    }

    /**
     * 获取响应内容
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String getResponseContent(HttpResponse response, String charsetName) throws Exception {
        HttpEntity entity = response.getEntity();
        StringBuilder buff = new StringBuilder();
        String line;

        try (InputStream in = entity.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, charsetName))) {
            while ((line = reader.readLine()) != null) {
                buff.append(line).append("\n");
            }
            buff.deleteCharAt(buff.lastIndexOf("\n"));
        }
        return buff.toString();
    }

}
