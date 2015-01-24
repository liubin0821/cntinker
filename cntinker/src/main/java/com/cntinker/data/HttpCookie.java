/**
 *2014年12月18日 下午2:54:22
 */
package com.cntinker.data;

import java.io.Serializable;
import java.util.List;

import com.cntinker.util.ReflectHelper;
import com.cntinker.util.StringHelper;

/**
 * @author: liubin
 *
 */
public class HttpCookie implements Serializable {

	private String key;

	private String value;
	
	private List<HttpCookie> res;

	public static HttpCookie processHttpHeader(String headerReturn) {
		HttpCookie d = new HttpCookie();
		String c1 = headerReturn.substring(1,headerReturn.length()-1);
		return d;
	}

	public static void main(String[] args) throws Exception {
		String c = "[ECS[visit_times]=1; expires=Thu, 17-Dec-2015 22:49:20 GMT; path=/, ECS_ID=d9a5043e005679e7d9343356a574fd30e82a82ed; path=/]";
		String c1 = c.substring(1,c.length()-1);
		String[] res = StringHelper.splitStr(c1, ";");
		System.out.println(c1);
		for(String e : res){
			System.out.println(e);
		}
	}

	public String toString() {
		String res = "";
		try {
			res = ReflectHelper.makeToString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<HttpCookie> getRes() {
		return res;
	}

	public void setRes(List<HttpCookie> res) {
		this.res = res;
	}
}
