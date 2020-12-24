/**
 *2015年11月10日 下午5:47:05
 */
package com.cntinker.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author: liubin
 *
 */
public class GzipHelper {

	public static final int BUFFER = 1024;

	/**
	 * 数据压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		compress(bais, baos);
		byte[] output = baos.toByteArray();

		baos.flush();
		baos.close();

		bais.close();

		return output;
	}

	/**
	 * 数据压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os)
			throws Exception {

		GZIPOutputStream gos = new GZIPOutputStream(os);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}

		gos.finish();
		gos.flush();
		gos.close();
	}

	/**
	 * 数据解压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 解压缩

		decompress(bais, baos);

		data = baos.toByteArray();

		baos.flush();
		baos.close();

		bais.close();

		return data;
	}

	/**
	 * 数据解压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os)
			throws Exception {

		GZIPInputStream gis = new GZIPInputStream(is);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = gis.read(data, 0, BUFFER)) != -1) {
			os.write(data, 0, count);
		}

		gis.close();
	}

	public static void main(String[] args) throws Exception {
		String inputStr = "asdfhsdfpw8yhsiudhn;dsfgjsdn;jsidfjsdoifjepifjweoijfweibcdw@12390sf.com测试jf0238we8fhso[ifu[038hsefhoe[ir2308hsdif[siduf09u0w9eijfsodifjwe09fjw0e9fjw09ejfw09ejf0w9ejjfsi0djf0sejfwe0jf0wejf0w9ejf09wejf0wejfw0ejf0w9ejf09jwe90fjw09ejf09wjejsdifjweijf0wejf09wj3r0923r908jgfdijn3940t8[ru9hdoiodljxcnvxc;vgiue*&#^A#(%$*#&^@(*^#)(#*$(&*GYEBFPFSDF(YDUGBpiuygfbdsoifshd;uagfvasduh";
		// String inputStr = "sdufy237sf;soid";
		System.err.println("原文:\t" + inputStr);

		byte[] input = inputStr.getBytes();
		System.err.println("长度:\t" + input.length);

		byte[] data = compress(input);
		System.err.println("压缩后:\t");
		System.err.println("长度:\t" + data.length);

		byte[] output = decompress(data);
		String outputStr = new String(output);
		System.err.println("解压缩后:\t" + outputStr);
		System.err.println("长度:\t" + output.length);
	}
}
