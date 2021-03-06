package com.jinghuan.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

	private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 7000;
	private static final int MAX_SOCKET_TIMEOUT = 300000;

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		//configBuilder.setSocketTimeout(MAX_TIMEOUT);
		configBuilder.setSocketTimeout(MAX_SOCKET_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}

	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, new HashMap<String, Object>());
	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params) {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			}else {
				param.append("&");
			}
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		String result = null;
		try(CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpGet httpPost = new HttpGet(apiUrl);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
		return result;
	}

	
	/**
	 * 发送 POST 请求（HTTP），不带输入数据
	 * 
	 * @param apiUrl
	 * @return
	 */
	public static String doPost(String apiUrl) {
		return doPost(apiUrl, new HashMap<String, Object>());
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @return
	 */
	public static String doPost(String apiUrl, Map<String, Object> params) {
		String httpStr = null;
		try(CloseableHttpClient httpClient = HttpClients.createDefault()){
			HttpPost httpPost = new HttpPost(apiUrl);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			try(CloseableHttpResponse response = httpClient.execute(httpPost)){
				HttpEntity entity = response.getEntity();
				httpStr = EntityUtils.toString(entity, "UTF-8");
				consume(response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return httpStr;
	}

	
	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl
	 * @param json
	 *            json对象
	 * @return
	 */
	public static String doPost(String apiUrl, String json) {
		String httpStr = null;
		try(CloseableHttpClient httpClient = HttpClients.createDefault()){
			HttpPost httpPost = new HttpPost(apiUrl);
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			try(CloseableHttpResponse response = httpClient.execute(httpPost)){
				HttpEntity entity = response.getEntity();
				httpStr = EntityUtils.toString(entity, "UTF-8");
				consume(response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return httpStr;
	}
	
	

	/**
	 * 发送 POST 请求（HTTP），json形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @return
	 */
	public static String doPostHeader(String apiUrl, String json,Map<String, String> headers) {
		String httpStr = null;
		try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(apiUrl);

			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			//设置headers
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			try(CloseableHttpResponse response = httpClient.execute(httpPost)){
				HttpEntity entity = response.getEntity();
				httpStr = EntityUtils.toString(entity, "UTF-8");
				consume(response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return httpStr;
	}
	
	
	
	

	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @return
	 */
	public static String doPostSSL(String apiUrl, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;

		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			consume(response);
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），JSON形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param json
	 *            JSON对象
	 * @return
	 */
	public static String doPostSSL(String apiUrl, Object json) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;

		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			consume(response);
		}
		return httpStr;
	}

	/**
	 * 发送 POST 请求（HTTP），传递cookie
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @param headers
	 * @return
	 */
	public static String doPostHeader(String apiUrl, Map<String, Object> params, Map<String, String> headers) {
		String httpStr = null;
		try(CloseableHttpClient httpClient = HttpClients.createDefault()){
			HttpPost httpPost = new HttpPost(apiUrl);

			httpPost.setConfig(requestConfig);
			if (params != null) {
				List<NameValuePair> pairList = new ArrayList<>(params.size());
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
						pairList.add(pair);
					}
				}
				httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			}
			//设置headers
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}

			try(CloseableHttpResponse response = httpClient.execute(httpPost)){
                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
                consume(response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return httpStr;
	}

	/**
	 * 发送 GET 请求（HTTP），传递cookie
	 * @param url API接口URL
	 * @param params 参数map
	 * @param headers
	 * @return
	 */
	public static String doGetHeader(String url, Map<String, Object> params, Map<String, String> headers) {
		String apiUrl = url;
		if (params != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : params.keySet()) {
				if (i == 0) {
					param.append("?");
				}else {
					param.append("&");
				}
				param.append(key).append("=");
				if (params.get(key) != null) {
					param.append(params.get(key));
				}
				i++;
			}
			apiUrl += param;
		}
		String result = null;
		try(CloseableHttpClient httpclient = HttpClients.createDefault();){
            HttpGet httpGet = new HttpGet(apiUrl);
            //设置headers
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
		return result;
	}

	/**
	 * 创建SSL安全连接
	 * 
	 * @return
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

				@Override
				public void verify(String host, SSLSocket ssl) throws IOException {
				}

				@Override
				public void verify(String host, X509Certificate cert) throws SSLException {
				}

				@Override
				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				}
			});
		} catch (GeneralSecurityException e) {
			logger.error(e.getMessage(), e);
		}
		return sslsf;
	}

	private static void consume(CloseableHttpResponse response) {
		if (response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
