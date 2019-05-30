package com.annkee.common.util;

import com.alibaba.fastjson.JSONObject;
import com.annkee.common.constant.ProjectConstant;
import com.annkee.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 远程访问工具
 *
 * @author wangan
 * @date 2018/1/9
 */
@Slf4j
public class HttpClientUtil {
    
    private static CloseableHttpClient httpClient = null;
    
    
    /**
     * get请求, 携带header
     *
     * @param url 目标地址
     * @return String
     */
    public static String get(String url, Map<String, Object> headers) {
        HttpGet httpGet = new HttpGet(url);
        if (headers != null && headers.size() > 0) {
            
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                
                httpGet.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return httpResult(httpGet);
    }
    
    /**
     * post 携带header，参数为 JsonObject，参数编码 UTF-8
     *
     * @param url        目标地址
     * @param headers    请求头
     * @param jsonObject 实体对象
     * @return String
     */
    public static String postJsonObject(String url, Map<String, Object> headers, JSONObject jsonObject) {
        
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            httpPost.setEntity(new StringEntity(jsonObject.toJSONString(), "UTF-8"));
            return httpResult(httpPost);
        } catch (Exception e) {
            log.error("postJsonObject error: {}, {}" + e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * post 携带头信息，参数为map键值对，参数编码 UTF-8，参数为键值对
     *
     * @param url     目标地址
     * @param headers 请求头
     * @param params  参数
     * @return String
     */
    public static String postKeyValue(String url, Map<String, Object> headers, Map<String, Object> params) {
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params2NVPs(params), "UTF-8"));
            return httpResult(httpPost);
        } catch (UnsupportedEncodingException e) {
            log.error("postKeyValue error: {}, {}" , e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 参数转换
     *
     * @param params 参数
     * @return List<NameValuePair>
     */
    private static List<NameValuePair> params2NVPs(Map<String, Object> params) {
        List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                nvpList.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
            }
        }
        return nvpList;
    }
    
    /**
     * 配置请求超时时间
     *
     * @param httpRequestBase
     */
    private static void configHttpRequest(HttpRequestBase httpRequestBase) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(ProjectConstant.TIME_OUT)
                .setConnectTimeout(ProjectConstant.TIME_OUT).setSocketTimeout(ProjectConstant.TIME_OUT).build();
        httpRequestBase.setConfig(requestConfig);
    }
    
    /**
     * 请求结果处理
     *
     * @param request
     * @return String
     */
    private static String httpResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            configHttpRequest(request);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                response.close();
                return result;
            }
        } catch (Exception e) {
            log.error("httpResult error：{}, {}" + e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取HttpClient
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        }
        httpClient = createHttpClient();
        return httpClient;
    }
    
    /**
     * 创建HttpClient
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createHttpClient() {
        ConnectionSocketFactory connectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSF = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", connectionSocketFactory).register("https", sslSF).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        
        // 最大连接数
        connectionManager.setMaxTotal(ProjectConstant.MAX_CONNECTIONS);
        // 每个路由基础的连接
        connectionManager.setDefaultMaxPerRoute(ProjectConstant.MAX_ROUTE_CONNECTIONS);
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= ProjectConstant.RETRY_NUM) {
                return false;
            }
            // 连接丢失，重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            // SSL握手异常，超时，目标服务器不可达，连接被拒绝，SSL异常不重试
            if (exception instanceof SSLHandshakeException || exception instanceof InterruptedIOException
                    || exception instanceof UnknownHostException || exception instanceof ConnectTimeoutException
                    || exception instanceof SSLException) {
                return false;
            }
            
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 请求幂等，再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        };
        
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .setRetryHandler(httpRequestRetryHandler).build();
        return httpClient;
    }
    
    /**
     * 上传安装包
     *
     * @param url
     * @param headers
     * @param multipartFile
     * @return String
     */
    public static ResultVO upload(HttpServletRequest request, String url, Map<String, Object> headers, MultipartFile multipartFile) {
        
        if (multipartFile.isEmpty()) {
            return null;
        }
        
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
        String filename = multipartFile.getOriginalFilename();
        String filePath = request.getSession().getServletContext().getRealPath("/upload/") + filename;
        File saveFile = new File(filePath);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        } catch (IOException e) {
            log.error("upload FileCopyUtils.copy error");
            e.printStackTrace();
        }
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", saveFile);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        String result = httpResult(httpPost);
        log.warn("result={}", result);
        
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(Integer.parseInt(code));
        
        resultVO.setMessage(jsonObject.getString("message"));
        JSONObject data = jsonObject.getJSONObject("data");
        String originalFilename = multipartFile.getOriginalFilename();
        
        data.put("name", originalFilename);
        
        int length = filename.length();
        
        //上传后删除文件
        saveFile.delete();
        log.warn("upload result={}", data);
        resultVO.setData(data);
        
        return resultVO;
    }
    
    
    /**
     * 上传
     *
     * @param url
     * @param headers
     * @param file
     * @return String
     */
    public static String uploadFile(String url, Map<String, Object> headers, File file) {
        
        
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", file);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        String result = httpResult(httpPost);
        //上传后删除文件
        file.delete();
        log.warn("upload result={}", result);
        return result;
    }
    
    
    /**
     * 上传
     *
     * @param url     必须
     * @param headers 头文件非必须，不设置传null
     * @param params  参数非必须，不设置传null
     * @param files   必须
     * @return String
     */
    public static String upload(String url, Map<String, Object> headers, Map<String, Object> params, List<File> files) {
        if (files == null || files.size() == 0) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (int i = 0; i < files.size(); i++) {
            builder.addBinaryBody("file_" + i, files.get(i), ContentType.DEFAULT_BINARY, files.get(i).getName());
        }
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), ContentType.DEFAULT_BINARY);
            }
        }
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        return httpResult(httpPost);
    }
    
}
