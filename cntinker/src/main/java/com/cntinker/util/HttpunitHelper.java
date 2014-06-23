/**
 * 2008-8-29 17:11:07
 */
package com.cntinker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;

import org.xml.sax.SAXException;

import com.meterware.httpunit.ClientProperties;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * @author bin_liu
 */
public class HttpunitHelper {

	public Vector v1 = new Vector();// ����COOkie������

	public Vector v2 = new Vector();// ����COOkie��ֵ

	String location = "";// �������ɵ������

	private WebConversation wc = null; // ��COOKIE��ģ��ͻ���

	// WebConversation wc = null;

	// ClientProperties cp = null;

	// WebRequest req = null;

	// WebResponse resp = null;

	// WebForm form = null;

	/**
	 * @param ua
	 * @param autoRedirect
	 *            һ����վ����Ϊtrue
	 */
	public WebConversation init(String ua, boolean autoRedirect) {
		WebConversation wc = new WebConversation();
		ClientProperties cp = wc.getClientProperties();

		ua = StringHelper.isNull(ua) ? "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6) Gecko/20050223 Firefox/1.0.1"
				: ua;
		cp.setUserAgent(ua);
		cp.setAcceptCookies(true);
		cp.setAutoRedirect(autoRedirect);
		cp.setIframeSupported(true);
		cp.setAcceptGzip(true);
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		HttpUnitOptions.setScriptingEnabled(true);
		return wc;
	}

	/**
	 * access a url and get Response
	 * 
	 * @param url
	 * @return WebResponse
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	public WebResponse getWebResponse(WebConversation wc, String url)
			throws MalformedURLException, IOException, SAXException {
		WebRequest req = new GetMethodWebRequest(url);
		WebResponse resp = wc.getResponse(req);
		return resp;
	}

	/**
	 * get a form by url
	 * 
	 * @param wc
	 * @param url
	 * @param formName
	 * @return WebForm
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	public WebForm getWebForm(WebConversation wc, String url, String formName)
			throws MalformedURLException, IOException, SAXException {

		WebForm w = getWebResponse(wc, url).getFormWithName(formName);

		// SubmitButton[] sb = w.getSubmitButtons();
		// System.out.println("submit count: "+sb.length);
		// for(SubmitButton sb1 : sb){
		// System.out.println("___ head ___");
		// System.out.println(sb1.getName());
		// System.out.println(sb1.getText());
		// System.out.println(sb1.getID());
		// System.out.println(sb1.getType());
		// System.out.println(sb1.getValue());
		// System.out.println(sb1.getNode());
		// System.out.println("___ end ___");
		// }
		return w;
	}

	/**
	 * get a file name by respose
	 * 
	 * @param resp
	 * @return String
	 */
	public String getFileName(WebResponse resp) {
		return resp.getURL().toString()
				.substring(resp.getURL().toString().lastIndexOf("/") + 1);
	}

	/**
	 * �õ�����������
	 * 
	 * @param page
	 * @return String[]
	 */
	public String[] getLink(String page) {
		List l = new ArrayList();
		int star = 0;
		int end = 0;

		while (page.indexOf("<a href=\"") > -1) {
			star = page.indexOf("<a href=\"") + 9;

			page = page.substring(star, page.length());

			end = page.indexOf("\">");

			l.add(page.substring(0, end));

			page = page.substring(end + 2, page.length());
		}

		return (String[]) l.toArray(new String[0]);
	}

	/**
	 * download binary file
	 * 
	 * @param res
	 * @param file
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	public void downloadBinary(WebResponse res, String file)
			throws MalformedURLException, IOException, SAXException {

		// System.out.println("File: "+res.getURL());

		File f = new File(file);
		FileOutputStream fos = new FileOutputStream(f);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				res.getInputStream(), res.getCharacterSet()));

		int b;
		while ((b = res.getInputStream().read()) != -1) {
			fos.write(b);
		}

		res.close();
		fos.close();
		in.close();
	}

	/**
	 * get login response and save cookie to WebConversation
	 * 
	 * @param resp
	 * @param wc
	 * @param loginUrl
	 * @param loginForm
	 * @param parameter
	 * @param subType
	 *            : 0->formSubmit 1->no formSubmit
	 * @return WebResponse
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	public WebResponse getLoginRes(WebResponse resp, WebConversation wc,
			String loginUrl, String loginForm, Map parameter, int subType)
			throws MalformedURLException, IOException, SAXException {
		WebForm form = getWebForm(wc, loginUrl, loginForm);

		// System.out.println("form is null: " + ( form == null ));

		Iterator it = parameter.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) parameter.get(key);
			form.setParameter(key, value);

			// System.out.println("key: " + key + " | value"+value);
		}

		// ��½��Ϊ���ж���/��BUTTON�ύFORM
		WebResponse rs = null;

		if (subType == 0)
			rs = form.submit();
		else
			rs = form.submitNoButton();

		// System.out.println(rs.getCharacterSet());
		// System.out.println(rs.getResponseMessage());
		// System.out.println(Arrays.deepToString(rs.getNewCookieNames()));
		// System.out.println("_________");

		saveCookie(rs, wc);

		return rs;
	}

	/**
	 * save cookie
	 * 
	 * @param rs
	 * @param wc
	 */
	private void saveCookie(WebResponse rs, WebConversation wc) {
		/**
		 * �õ�cookie,�ұ���
		 */
		String[] str = rs.getNewCookieNames();
		// System.out.println("��дCOOKIE���ȣ� " + str.length);
		for (int i = 0; i < str.length; i++) {
			v1.add(str[i]);
			v2.add(rs.getNewCookieValue(str[i]));
		}
		/**
		 * ȡ��LOCATION,�ұ���
		 */
		String[] str1 = rs.getHeaderFieldNames();
		for (int i = 0; i < str1.length; i++) {
			if (str1[i].equalsIgnoreCase("LOCATION")) {
				location = rs.getHeaderField(str1[i]);
			}
		}
		this.wc = wc;
		/**
		 * Ϊģ�����������cookie
		 */
		for (int i = 0; i < v1.size(); i++) {
			// System.out.println("key: " + (String) v1.get(i) + " | values: "
			// + (String) v2.get(i));
			this.wc.putCookie((String) v1.get(i), (String) v2.get(i));
		}
	}

	/**
	 * get a save cookie WebConversation
	 * 
	 * @return WebConversation
	 */
	public WebConversation getWcCookie() {
		return this.wc;
	}

	/**
	 * login success?
	 * 
	 * @param loginSuccessTitle
	 * @param rs
	 * @param wc
	 * @return boolean
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	public boolean login(String loginSuccessTitle, WebResponse rs)
			throws MalformedURLException, IOException, SAXException {
		if (rs.getTitle().indexOf(loginSuccessTitle) > -1)
			return true;
		return false;
	}

	/**
	 * test method
	 * 
	 * @param args
	 * @throws MalformedURLException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void main(String[] args) throws MalformedURLException,
			SAXException, IOException, ServletException {
		HttpunitHelper webLogin = new HttpunitHelper();
		WebConversation wc = webLogin.init("bin_liu", true);

		WebResponse r = wc.getResponse("http://211.94.187.165:8090/test.jsp");
		System.out.println(r.getText());
		// System.out.println(HttpHelper.postHttpRquest(login,"","UTF-8"));
	}
}
