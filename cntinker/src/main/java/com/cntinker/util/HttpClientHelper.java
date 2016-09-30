/**
 *2016年2月5日 下午3:46:26
 */
package com.cntinker.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author: liubin
 *
 */
public class HttpClientHelper {

	private static Log exLog = LogFactory.getLog("ex");

	private static MultiThreadedHttpConnectionManager conmgr = null;
	private static HttpClient client = null;

	static {
		conmgr = new MultiThreadedHttpConnectionManager();
		conmgr.getParams().setDefaultMaxConnectionsPerHost(200);
		conmgr.getParams().setMaxTotalConnections(350);
		client = new HttpClient(conmgr);

		//屏蔽httpClient的日志
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime",
				"true");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient",
				"stdout");
	}

	public static HttpClient getClient() {
		return client;
	}

	public static String post(String url, Map<String, String> params,
			String charset) throws HttpException, IOException {
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		if (params != null) {
			Iterator<Entry<String, String>> p = params.entrySet().iterator();
			Entry<String, String> e;
			while (p.hasNext()) {
				e = p.next();
				postMethod.addParameter(e.getKey(), e.getValue());
			}
		}
		String result = "";

		int status = client.executeMethod(postMethod);
		result = postMethod.getResponseBodyAsString();

		postMethod.releaseConnection();

		return result;
	}

	public static String postContent(String url, String xmlInfo,
			boolean hasReturnContent, String charset)
			throws IllegalAccessException {
		PostMethod postMethod = new PostMethod(url);
		String result = "";
		if (StringHelper.isNull(charset))
			charset = "utf-8";
		if (xmlInfo != null) {
			try {
				postMethod.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
				postMethod.setRequestEntity(new StringRequestEntity(xmlInfo,
						"application/x-www-form-urlencoded", charset));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			postMethod.setRequestBody(xmlInfo);// 这里添加xml字符串

		}
		try {
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				if (!hasReturnContent) {
					result = postMethod.getResponseBodyAsString();
				} else {
					BufferedInputStream bis = new BufferedInputStream(
							postMethod.getResponseBodyAsStream());
					byte[] bytes = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int count = 0;
					while ((count = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, count);
					}
					byte[] strByte = bos.toByteArray();
					result = new String(strByte, 0, strByte.length, charset);
					bos.close();
					bis.close();
				}

			}
		} catch (IOException e) {
			result = "";
			exLog.error("url:" + url + StringHelper.getStackInfo(e));
			throw new IllegalAccessException("http request exception : "
					+ StringHelper.getStackInfo(e));
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	public static String get(String url, boolean hashReturnXml, String charset) {
		GetMethod getMethod = new GetMethod(url);
		String result = "";
		try {
			int status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				if (!hashReturnXml) {
					result = getMethod.getResponseBodyAsString();
				} else {
					BufferedInputStream bis = new BufferedInputStream(
							getMethod.getResponseBodyAsStream());
					byte[] bytes = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int count = 0;
					while ((count = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, count);
					}
					byte[] strByte = bos.toByteArray();
					result = new String(strByte, 0, strByte.length, charset);
					bos.close();
					bis.close();
				}

			} else {
				result = getMethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			exLog.error("url:" + url + StringHelper.getStackInfo(e));
		} catch (IOException e) {
			exLog.error("url:" + url + StringHelper.getStackInfo(e));
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://api.itrigo.net/batchmt.jsp";

		String user = "111111";
		String pwd = "111111";
		String content = "走一个测试呗【天信博易】";
		String phone = "13888888888";

		String json = "{\"cpid\":\"" + user + "\",\"cppwd\":\"" + pwd
				+ "\",\"items\":[{\"content\":\"" + content + "\",\"phone\":\""
				+ phone + "\",\"extend\":\"123123\",\"spnumber\":\"11111\"}]}";

		Map map = new HashMap();
		map.put("pack", URLEncoder.encode(json, "GBK"));

		String res = post(url, map, "GBK");

		System.out.println("res : " + res);
	}
}
