/**
 * 2012-5-10 下午3:34:07
 */
package com.cntinker.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cntinker.data.HttpReturnData;

/**
 * @author: bin_liu
 */
public class HttpHelper {

	public static void main(String[] args) throws IOException {
		String url = "http://itunes.apple.com/lookup?id=284910350";
		System.out.println(postHttpRquest(url, "", "UTF-8"));
		System.out.println("----------- sending -------------");

	}

	/**
	 * 前置NGINX或者APACHE的时候获取IP用
	 * 
	 * @param request
	 * @return String
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

	public static String[] sendGetReturnHeaderRes(String destURL,
			String character) throws IOException {
		String res = "";
		String header = "";
		destURL = destURL.trim().replaceAll(" ", "%20");
		destURL = destURL.trim().replaceAll("  ", "%20");
		destURL = destURL.trim().replaceAll("   ", "%20");
		destURL = destURL.trim().replaceAll("\n", "");
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();
		try {
			url = new URL(destURL);
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			uc.setDoOutput(true);
			out = uc.getOutputStream();
			// 读取回流
			urlStream = uc.getInputStream();
			br = new BufferedReader(new InputStreamReader(urlStream, character));
			// 请求头
			Map m = uc.getHeaderFields();
			header = m.toString();

			// 回执请求体
			while (currentLine != null) {
				currentLine = br.readLine();
				if (!StringHelper.isNull(currentLine))
					tempStr.append(currentLine).append("\n");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new MalformedURLException("url error:"
					+ StringHelper.getStackInfo(e));
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null)
					br.close();
				if (uc != null)
					uc.disconnect();

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		res = tempStr.toString();

		String[] r = { header, res };

		return r;
	}

	public static String postXml(String url, String xml) throws IOException {
		return postXml(null, url, xml, "utf-8", false);
	}

	public static String postXml(String url, String xml, String character)
			throws IOException {
		return postXml(null, url, xml, character, false);
	}

	public static String postXml(Map<String, String> header, String url,
			String xml) throws IOException {
		return postXml(header, url, xml, "utf-8", false);
	}

	public static HttpReturnData postXmlData(Map<String, String> header,
			String url, String xml) throws IOException {
		HttpReturnData res = postXmlData(header, url, xml, "utf-8");
		return res;
	}

	public static HttpReturnData postXmlData(String url, String xml,
			String character) throws IOException {
		HttpReturnData res = postXmlData(null, url, xml, character);
		return res;
	}

	public static HttpReturnData postXmlData(String url, String xml)
			throws IOException {
		HttpReturnData res = postXmlData(null, url, xml, "utf-8");
		return res;
	}

	public static HttpReturnData postXmlData(Map<String, String> header,
			String url, String xml, String character) throws IOException {
		StringBuffer tempStr = new StringBuffer();
		BufferedReader br = null;
		URL u = new URL(url);
		URLConnection con = u.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);

		Map<String, String> returnHeader = new HashMap<String, String>();

		if (header != null && header.size() > 0) {
			Iterator<String> it = header.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				String v = header.get(k);
				con.setRequestProperty(k, v);
			}
		} else {
			con.setRequestProperty("Content-Type", "text/xml");
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-length",
					String.valueOf(xml.length()));
		}

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		// OutputStreamWriter out = new
		// OutputStreamWriter(con.getOutputStream());

		out.write(new String(xml.getBytes(character)).getBytes());

		InputStream urlStream = null;
		urlStream = con.getInputStream();
		Map m = con.getHeaderFields();

		Iterator it = m.keySet().iterator();

		while (it.hasNext()) {
			Object o = it.next();
			if(o==null)
				continue;
			String k = o.toString();
			String v = m.get(k).toString();

			returnHeader.put(k, v);
		}

		String currentLine = "";

		while (it.hasNext()) {
			String k = (String) it.next();
			tempStr.append(k).append(":").append(con.getHeaderField(k))
					.append("\n");
		}

		br = new BufferedReader(new InputStreamReader(urlStream));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
		}

		HttpReturnData resdata = new HttpReturnData();
		resdata.setContent(tempStr.toString());
		resdata.setHeader(returnHeader);
		return resdata;
	}

	public static String postXml(Map<String, String> header, String url,
			String xml, String character, boolean displayHeadInfo)
			throws IOException {
		StringBuffer tempStr = new StringBuffer();
		BufferedReader br = null;
		URL u = new URL(url);
		URLConnection con = u.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);

		if (header != null && header.size() > 0) {
			Iterator<String> it = header.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				String v = header.get(k);
				con.setRequestProperty(k, v);
			}
		} else {
			con.setRequestProperty("Content-Type", "text/xml");
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-length",
					String.valueOf(xml.length()));
		}

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		// OutputStreamWriter out = new
		// OutputStreamWriter(con.getOutputStream());

		out.write(new String(xml.getBytes(character)).getBytes());

		InputStream urlStream = null;
		urlStream = con.getInputStream();
		Map m = con.getHeaderFields();

		Iterator it = m.keySet().iterator();
		String currentLine = "";
		if (displayHeadInfo) {
			while (it.hasNext()) {
				String k = (String) it.next();
				tempStr.append(k).append(":").append(con.getHeaderField(k))
						.append("\n");
			}
		}
		br = new BufferedReader(new InputStreamReader(urlStream));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
		}

		return tempStr.toString();
	}

	/**
	 * 请求指定地址
	 * 
	 * @param destURL
	 *            (目标地址,http://xxx.xx.com)
	 * @param parameters
	 *            (POST请求体参数,parame_a=a&param_b=b)
	 * @return String(请求对方回写的内容)
	 * @throws IOException
	 */
	public static String postHttpRquest(String destURL, String parameters)
			throws IOException {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();

		url = new URL(destURL);
		uc = (HttpURLConnection) url.openConnection();

		uc.setRequestMethod("POST");
		uc.setDoOutput(true);
		out = uc.getOutputStream();
		if (!StringHelper.isNull(parameters))
			out.write(parameters.getBytes());
		urlStream = uc.getInputStream();
		Map m = uc.getHeaderFields();

		Iterator it = m.keySet().iterator();

		while (it.hasNext()) {
			String k = (String) it.next();
			tempStr.append(k).append(":").append(uc.getHeaderField(k))
					.append("\n");
		}
		tempStr.append("\n\n");
		br = new BufferedReader(new InputStreamReader(urlStream));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
		}

		return tempStr.toString();
	}

	/**
	 * 指定字符读回流
	 * 
	 * @param destURL
	 * @param parameters
	 * @param character
	 * @return String
	 * @throws IOException
	 */
	public static String postHttpRquest(String destURL, String parameters,
			String character) throws IOException {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();

		url = new URL(destURL);
		uc = (HttpURLConnection) url.openConnection();

		uc.setRequestMethod("POST");
		uc.setDoOutput(true);
		out = uc.getOutputStream();
		if (!StringHelper.isNull(parameters))
			out.write(parameters.getBytes());
		urlStream = uc.getInputStream();

		// Map m = uc.getHeaderFields();
		// Iterator it = m.keySet().iterator();
		// while(it.hasNext()){
		// String k = (String) it.next();
		// tempStr.append(k).append(":").append(uc.getHeaderField(k))
		// .append("\n");
		// }

		tempStr.append("\n\n");
		br = new BufferedReader(new InputStreamReader(urlStream, character));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
			tempStr.append("\n");
		}

		return tempStr.toString();
	}

	public static String sendGet(String destURL) throws MalformedURLException,
			IOException {
		return sendGet(destURL, "utf-8");
	}

	/**
	 * @param destURL
	 * @param parameters
	 * @return
	 */
	public static String sendGet(String destURL, String character)
			throws MalformedURLException, IOException {
		destURL = destURL.trim().replaceAll(" ", "%20");
		destURL = destURL.trim().replaceAll("  ", "%20");
		destURL = destURL.trim().replaceAll("   ", "%20");
		destURL = destURL.trim().replaceAll("\n", "");
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();
		try {
			url = new URL(destURL);
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			uc.setDoOutput(true);
			out = uc.getOutputStream();
			// 读取回流
			urlStream = uc.getInputStream();
			br = new BufferedReader(new InputStreamReader(urlStream, character));
			// 请求头
			// Map m = uc.getHeaderFields();
			// tempStr.append(m.toString()).append("\n");

			// 回执请求体
			while (currentLine != null) {
				currentLine = br.readLine();
				if (!StringHelper.isNull(currentLine))
					tempStr.append(currentLine).append("\n");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new MalformedURLException("url error:"
					+ StringHelper.getStackInfo(e));
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null)
					br.close();
				if (uc != null)
					uc.disconnect();

			} catch (IOException e) {
				e.printStackTrace();
				return "IO error:" + StringHelper.getStackInfo(e);
			}
		}

		return tempStr.toString();
	}

	public static String getHeader(String destURL) {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();
		try {
			url = new URL(destURL);
			uc = (HttpURLConnection) url.openConnection();
			Map m = uc.getHeaderFields();
			Iterator it = m.entrySet().iterator();

			while (it.hasNext()) {
				// System.out.println(it.next());
				tempStr.append(it.next()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempStr.toString();
	}
}
