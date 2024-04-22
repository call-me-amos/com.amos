package com.test.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Throwables;
import com.to8to.rpc.common.exception.RPCException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Ivan.luo
 * @Description
 * @date 2021/03/23
 */
@Component
@Slf4j
public class HttpUtils {
    /**
     * 读取超时时间
     */
    @Value("${socket.time.out:2000}")
    private int soTimeout;

    /**
     * 连接超时时间
     */
    @Value("${connection.time.out:2000}")
    private int connectionTimeout;

    /**
     * 连接请求超时时间
     */
    private int connectionRequestTimeout = 10000;

    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connMgr;
    /**
     * 重试机制
     */
    private static HttpRequestRetryHandler retryHandler;

    /**
     * 超时时间
     */
    private static final int MAX_TIMEOUT = 5000;
    /**
     * socket读取超时时间
     */
    private static final int MAX_SOCKET_TIMEOUT = 7000;

    public String doGet(String url) throws IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result;
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(soTimeout)
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);

            if (response.getStatusLine().getStatusCode() == 200) {
                return result;
            } else {
                log.error("HttpGetException http-get,request={}，状态码:{},内容：{}", url, response.getStatusLine().getStatusCode(), result);
                return null;
            }
        } catch (Exception e) {
            log.warn("HttpGetException http-get执行时出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("HttpGetException http-get关闭response出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("HttpGetException http-get关闭httpClient出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
        }
    }

    public String doPost(String url, Map<String, Object> paramMap) throws IOException {
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;
        String result;
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(soTimeout)
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //组装参数
        assembleFormData(paramMap, httpPost);
        //获取post结果
        return getPostResult(httpClient, httpPost, url);
    }

    public String doPost(String url, Map<String, Object> paramMap, Map<String, String> headerMap, ContentType contentType) throws IOException {
        return doPost(url, paramMap, headerMap, contentType, soTimeout);
    }

    public String doPost(String url, Map<String, Object> paramMap, Map<String, String> headerMap, ContentType contentType, int socketTimeout) throws IOException {
        CloseableHttpClient httpClient;
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        for (String key : headerMap.keySet()) {
            httpPost.addHeader(key, headerMap.get(key));
        }
        //组装参数
        if (ContentType.APPLICATION_JSON.getMimeType().equals(contentType.getMimeType())) {
            assembleJson(paramMap, httpPost);
        } else if (ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(contentType.getMimeType())) {
            assembleFormData(paramMap, httpPost);
        }
        //获取post结果
        return getPostResult(httpClient, httpPost, url);
    }

    private void assembleJson(Map<String, Object> paramMap, HttpPost httpPost) throws UnsupportedEncodingException {
        JSONObject jsonParam = new JSONObject();
        Iterator<Map.Entry<String, Object>> iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> mapEntry = iterator.next();
            jsonParam.put(mapEntry.getKey(), mapEntry.getValue());
        }
        String string = jsonParam.toString();
        String encodeString = URLEncoder.encode(string, "UTF-8");
        StringEntity entity = new StringEntity(encodeString, ContentType.APPLICATION_JSON);

        // 为httpPost设置封装好的请求参数
        httpPost.setEntity(entity);
    }

    private void assembleFormData(Map<String, Object> paramMap, HttpPost httpPost) {
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPostResult(CloseableHttpClient httpClient, HttpPost httpPost, String url) throws IOException {
        String result;
        CloseableHttpResponse httpResponse = null;
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            log.error("HttpPostException http-post执行时出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("HttpPostException http-post关闭httpResponse出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("HttpPostException http-post关闭httpClient出现异常 url = {}, error.e = {}", url, Throwables.getStackTraceAsString(e));
                    throw e;
                }
            }
        }
        return result;
    }

    /**
     * 调用php接口发送post请求
     *
     * @param url
     * @param body
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T sendPostRequest(String url, Map<String, Object> body, Map<String, String> headerMap, TypeReference<T> typeReference, ContentType contentType) {
        String result = null;
        try {
            log.debug(">>>>>>>>>>> start send post request... body : {} ,url : {}", JSON.toJSONString(body), url);
            long start = System.currentTimeMillis();
            result = doPost(url, body, headerMap, contentType);
            T t = JSON.parseObject(result, typeReference);
            long end = System.currentTimeMillis();
            log.debug(">>>>>>>>>>> send post request success. url : {}, body : {}, costTime : {}, res : {}", url, JSON.toJSONString(body), (end - start), result);
            return t;
        } catch (Exception e) {
            log.error(">>>>>>>>>>>url:{},result:{} send post request exception : {}", url, result, Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     * 发起HTTP／HTTPS请求
     *
     * @param reqType 请求类型GET、POST、DELETE、PUT
     * @param url     请求url
     * @param headers
     * @param param
     * @return
     * @throws RPCException
     */
    public static <T> T sendHttp(RequestTypeEnum reqType, String url, Map<String, String> headers, Object param, Class<T> tClass) throws RPCException {
        HttpRequestBase reqBase = reqType.getHttpType(url);
        log.info("@@@ 开始向地址[{}]发起[{}]请求，请求头为{}, 请求参数:[{}]", url, reqBase.getMethod(), JSON.toJSONString(headers), param);
        long start = System.currentTimeMillis();
        CloseableHttpClient httpClient = getHttpClient();
        //请求头和超时时间配置
        config(reqBase);

        //设置请求头
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                reqBase.setHeader(entry.getKey(), entry.getValue());
            }
        }

        //添加参数，参数是json字符串
        if (param != null && param instanceof String) {
            String paramStr = String.valueOf(param);
            log.info("@@@ 请求参数为：{}", paramStr);
            ((HttpEntityEnclosingRequest) reqBase).setEntity(new StringEntity(String.valueOf(paramStr), ContentType.create("application/json", "UTF-8")));
        } else if (param != null && param instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) param;
            log.info("@@@ 请求参数为：{}", JSONObject.toJSONString(paramMap));
            if (null != paramMap && paramMap.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                // 通过map集成entrySet方法获取entity
                Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
                // 循环遍历，获取迭代器
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> mapEntry = iterator.next();
                    nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
                }

                // 为httpPost设置封装好的请求参数
                try {
                    // ContentType.create("application/x-www-form-urlencoded","UTF-8"
                    ((HttpEntityEnclosingRequest) reqBase).setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error(">>> UrlEncodedFormEntity error:{}", Throwables.getStackTraceAsString(e));
                }
            }
        } else if (param != null && param instanceof byte[]) {
            log.info("@@@ 请求参数为文件流");
            byte[] paramBytes = (byte[]) param;
            ((HttpEntityEnclosingRequest) reqBase).setEntity(new ByteArrayEntity(paramBytes));
        }

        //响应对象和响应内容
        CloseableHttpResponse res = null;
        String resCtx = null;
        try {
            //执行请求
            res = httpClient.execute(reqBase);
            log.info("@@@ 执行请求完毕，响应状态码：{}", res.getStatusLine());
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RPCException("@@@ HTTP访问异常：" + res.getStatusLine());
            }

            //获取请求响应对象和响应entity
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                resCtx = EntityUtils.toString(entity, "utf-8");
                log.info("@@@ 结束向地址[{}]发起[{}]请求，请求头为{}, result:[{}]", url, reqBase.getMethod(), JSON.toJSONString(headers), resCtx);
            }
        } catch (Exception e) {
            throw new RPCException("请求失败", e);
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (IOException e) {
                    throw new RPCException("@@@ 关闭请求响应失败", e);
                }
            }
        }
        long end = System.currentTimeMillis();
        log.info("@@@ 请求执行完毕，耗费时长：{}ms", (end - start));
        return JSONObject.parseObject(resCtx, tClass);
    }

    /**
     * 请求头和超时时间配置
     *
     * @param httpReqBase
     */
    private static void config(HttpRequestBase httpReqBase) {
        //配置请求的超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(MAX_TIMEOUT)
                .setConnectTimeout(MAX_TIMEOUT)
                .setSocketTimeout(MAX_SOCKET_TIMEOUT)
                .build();
        httpReqBase.setConfig(requestConfig);
    }

    /**
     * 获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connMgr)
                .setRetryHandler(retryHandler)
                .build();
    }
}

