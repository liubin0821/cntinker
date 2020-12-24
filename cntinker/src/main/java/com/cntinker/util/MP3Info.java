package com.cntinker.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * mp3info
 * 
 */
public class MP3Info {

	public static void main(String[] args) {

		File MP3FILE = new File("/Volumes/Macintosh HD/Users/liubin/Music/douban/f_000aea.mp3");
		try {
			MP3Info info = new MP3Info(MP3FILE);
			info.setCharset("UTF-8");
			System.out.println(info.getSongName());
			System.out.println(info.getArtist());
			System.out.println(info.getAlbum());
			System.out.println(info.getYear());
			System.out.println(info.getComment());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String charset = "utf-8";

	private byte[] buf;

	public MP3Info(File mp3) throws IOException {

		buf = new byte[128];// 初始化标签信息的byte数组

		RandomAccessFile raf = new RandomAccessFile(mp3, "r");// 随机读写方式打开MP3文件
		raf.seek(raf.length() - 128);// 移动到文件MP3末尾
		raf.read(buf);// 读取标签信息

		raf.close();// 关闭文件

		if (buf.length != 128) {// 数据长度是否合法
			throw new IOException("MP3标签信息数据长度不合法!");
		}

		if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {// 标签头是否存在
			throw new IOException("MP3标签信息数据格式不正确!");
		}

	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSongName() {
		try {
			return new String(buf, 3, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 3, 30).trim();
		}
	}

	public String getArtist() {
		try {
			return new String(buf, 33, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 33, 30).trim();
		}
	}

	public String getAlbum() {
		try {
			return new String(buf, 63, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 63, 30).trim();
		}
	}

	public String getYear() {
		try {
			return new String(buf, 93, 4, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 93, 4).trim();
		}
	}

	public String getComment() {
		try {
			return new String(buf, 97, 28, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 97, 28).trim();
		}
	}

}
