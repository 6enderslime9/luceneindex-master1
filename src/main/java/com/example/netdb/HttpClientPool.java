package com.example.netdb;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientPool {
	  //1.生成httpclient，相当于该打开一个浏览器
	public final static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    public static HttpGet getHttpConn(String Url) {
    	 //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet(Url);
        //设置请求头，将爬虫伪装成浏览器
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//        HttpHost proxy = new HttpHost("60.13.42.232", 9999);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(config);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        request.setConfig(requestConfig);
        return request;
    }
}
