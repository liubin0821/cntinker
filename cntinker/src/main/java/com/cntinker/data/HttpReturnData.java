/**
 *2014年12月18日 下午2:22:31
 */
package com.cntinker.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.cntinker.util.ReflectHelper;

/**
 * @author: liubin
 *
 */
public class HttpReturnData implements Serializable {

	private String content;

	private Map<String, String> header;

	public String toString() {
		String res = "";
		try {
			res = ReflectHelper.makeToString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getContent() {
		return content;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
}
