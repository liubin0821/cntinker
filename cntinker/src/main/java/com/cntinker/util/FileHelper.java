/**
 * 2010-6-17 下午12:13:11
 */
package com.cntinker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author bin_liu
 */
public class FileHelper {

	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

	public static void copyFileByInputStearm(InputStream inputStream, File outFile) throws IOException {
		try (
				FileInputStream fi = (FileInputStream) inputStream;
				FileOutputStream fo = new FileOutputStream(outFile);
				FileChannel out = fo.getChannel();// 得到对应的文件通道
				FileChannel in = fi.getChannel();// 得到对应的文件通道

		) {
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}

	}

	public static void copyFile(File inFile, File outFile) throws IOException {
		try (
				FileInputStream fi = new FileInputStream(inFile);
				FileOutputStream fo = new FileOutputStream(outFile);
				FileChannel out = fo.getChannel();// 得到对应的文件通道
				FileChannel in = fi.getChannel();// 得到对应的文件通道
		) {
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * 得到文件大小
	 *
	 * @param file
	 * @param type (0:K 1:M 2:G)
	 * @return String
	 * @throws IOException
	 */
	public static String getFileSize(String file, int type) throws IOException {

		String size = "0";

		switch (type) {
			case -1:
				size = getLongFileSize(file, type) + " Byte";
				break;
			case 0:
				size = getDoubleFileSize(file, type) + " KB";
				break;
			case 1:
				size = getDoubleFileSize(file, type) + " MB";
				break;
			case 2:
				size = getDoubleFileSize(file, type) + " GB";
				break;
			default:
				size = getDoubleFileSize(file, 1) + " M";
		}

		return size;
	}

	/**
	 * @param file
	 * @param type (-1:byte 0:kb 1:mb 2:gb)
	 * @return long
	 * @throws IOException
	 */
	public static double getDoubleFileSize(String file, int type)
			throws IOException {
		File f = new File(file);
		Long sz = Long.valueOf(f.length());
		double size = sz.doubleValue();

		switch (type) {
			case 0:
				size = size / 1024;
				break;
			case 1:
				size = size / 1024 / 1024;
				break;
			case 2:
				size = size / 1024 / 1024 / 1024;
				break;
			default:
				size = size / 1024 / 1024 / 1024;
		}

		return StringHelper.formartDecimalToDouble(size);
	}

	/**
	 * 得到文件大小
	 *
	 * @param file
	 * @param type (0:K 1:M 2:G)
	 * @return String
	 * @throws IOException
	 */
	public static Long getLongFileSize(String file, int type)
			throws IOException {
		File f = new File(file);

		return f.length();
	}

	/**
	 * 切割文件，不过重复行
	 *
	 * @param sourceFile
	 * @param line
	 * @throws IOException
	 */
	public static void splitFileByLineFilter(String sourceFile, int line)
			throws IOException {
		splitFileByLine(sourceFile, line, false);
	}

	/**
	 * 切割文件，过滤重复行
	 *
	 * @param sourceFile
	 * @param line
	 * @throws IOException
	 */
	public static void splitFileByLineNoFilter(String sourceFile, int line)
			throws IOException {
		splitFileByLine(sourceFile, line, true);
	}

	/**
	 * 按行数切割文件，指定多少行分一个文件
	 *
	 * @param sourceFile
	 * @param line
	 * @param isFilter   是否过重复行
	 * @throws IOException
	 */
	public static void splitFileByLine(String sourceFile, int line,
									   boolean isFilter) throws IOException {
		String[] temp = null;
		if (isFilter) {
			temp = getLine(sourceFile, true);
		} else {
			temp = getLine(sourceFile, false);
		}
		int flag = 0;

		Collection<String> l = new ArrayList<String>();
		int fileCount = 0;
		for (String e : temp) {
			l.add(e);
			if (flag % line == 0) {
				FileHelper.recordFetchToFile(sourceFile + "_" + fileCount
						+ ".tmp", l.toArray(new String[0]));
				fileCount++;
				l.clear();
			}
			flag++;
		}
		FileHelper.recordFetchToFile(sourceFile + "_" + fileCount + ".tmp",
				l.toArray(new String[0]));
		l.clear();
	}

	/**
	 * 按大小切割文件，生成在源文件的相同目录下
	 *
	 * @param sourceFile
	 * @param size       (单位M)
	 * @throws IOException
	 */
	public static void splitFile(String sourceFile, int size)
			throws IOException {

		File file = new File(sourceFile);
		long c = file.length();
		long cSize = size * 1024L * 1024L;
		// 小于需要分割的大小则不处理
		if (c <= size) {
			return;
		}
		// 切割多少个文件
		int count = (int) (c / cSize);
		if (c % cSize > 0) {
			count++;
		}
		long splitSize = c / count;

		splitFileByCount(sourceFile, count);

	}

	/**
	 * 按指定文件数分割文件
	 *
	 * @param sourceFile
	 * @param count
	 * @throws IOException
	 */
	public static void splitFileByCount(String sourceFile, int count)
			throws IOException {
		// 定义输出路径，文件名
		String outPath = new File(sourceFile).getAbsolutePath();
		String outSuffix = getSuffix(sourceFile);

		try (RandomAccessFile raf = new RandomAccessFile(new File(sourceFile), "r")) {
			long length = raf.length();

			long theadMaxSize = length / count; // 每份的大小 1024 * 1000L;

			long offset = 0L;
			for (int i = 0; i < count - 1; i++) // 这里不去处理最后一份
			{
				long fbegin = offset;
				long fend = (i + 1) * theadMaxSize;
				logger.info("offset:" + offset + " | i:" + i + " | fbegin:"
						+ fbegin + " | fend:" + fend);
				offset = write(sourceFile, i, fbegin, fend);
			}

			if (length - offset > 0) { // 将剩余的都写入最后一份
				write(sourceFile, count - 1, offset, length);
			}
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}

	}

	/**
	 * <p>
	 * 指定每份文件的范围写入不同文件
	 * </p>
	 *
	 * @param file  源文件
	 * @param index 文件顺序标识
	 * @param begin 开始指针位置
	 * @param end   结束指针位置
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private static long write(String file, int index, long begin, long end)
			throws IOException {
		try (RandomAccessFile in = new RandomAccessFile(new File(file), "r");
			 RandomAccessFile out = new RandomAccessFile(new File(file + "_" + index
					 + ".tmp"), "rw");
		) {
			byte[] b = new byte[1024];
			int n = 0;
			in.seek(begin);// 从指定位置读取

			while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			long endPointer = in.getFilePointer();
			return endPointer;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * <p>
	 * 合并文件
	 * </p>
	 *
	 * @param file      指定合并后的文件
	 * @param tempFiles 分割前的文件名
	 * @param tempCount 文件个数
	 * @throws Exception
	 */
	public static void merge(String file, String tempFiles, int tempCount)
			throws IOException {
		try (RandomAccessFile ok = new RandomAccessFile(new File(file), "rw")) {

			for (int i = 0; i < tempCount; i++) {
				try (RandomAccessFile read = new RandomAccessFile(new File(tempFiles
						+ "_" + i + ".tmp"), "r")) {
					byte[] b = new byte[1024];
					int n = 0;
					while ((n = read.read(b)) != -1) {
						ok.write(b, 0, n);
					}
				} catch (IOException e) {
					throw new IOException(e);
				}
			}
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	/**
	 * 得到文件后缀
	 *
	 * @param file
	 * @return String
	 * @throws IOException
	 */
	public static String getSuffix(String file) throws IOException {
		String suffix = "";
		File f = new File(file);
		if (f.getName().indexOf(".") < 0) {
			return "";
		}
		suffix = f.getName().substring(f.getName().indexOf(".") + 1);

		return suffix;
	}

	/**
	 * 读文件进一个BYTE[]
	 *
	 * @param file
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {

		try (InputStream is = new FileInputStream(file)) {
			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				// File is too large
				throw new IOException("file too large");
			}

			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * 得到文件类型
	 *
	 * @param file
	 * @return String
	 */
	public static String getFiletype(String file) throws IOException {
		if (file.indexOf(".") < 0)
			return "";

		return file.substring(file.lastIndexOf(".") + 1);
	}

	/**
	 * 返回指定文件切割好的每一段内容 注意每一行的分割符号必须一致,元素个数可以不一致
	 *
	 * @param file
	 * @param split
	 * @return String[][] (int:序号,String[] : 所有内容)
	 */
	public static String[][] getFileBySplit(String file, String split)
			throws FileNotFoundException, IOException {
		int page = 0;
		String str;
		try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
			List list = new LinkedList();
			String temp = "";
			while ((str = bfr.readLine()) != null) {
				list.add(str);
				if (page == 0) {
					temp = str;
				}
				page++;
			}
			String[][] result = new String[page][temp.length()];

			for (int i = 0; i < page; i++) {
				result[i] = StringHelper.splitStr((String) list.get(i), split);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * 得到一个文本中的所有行
	 *
	 * @param file
	 * @return String[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] getLine(String file) throws FileNotFoundException,
			IOException {
		return getLine(file, false);
	}

	/**
	 * 得到一个文本中的所有行
	 *
	 * @param file
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getLineFilter(String file) throws IOException {
		return getLine(file, true);
	}

	public static String[] getLine(String file, boolean isFilter)
			throws IOException {
		String str;
		try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
			Collection<String> result = null;
			if (isFilter) {
				result = new HashSet<String>();
			} else {
				result = new Vector<String>();
			}
			while ((str = bfr.readLine()) != null) {
				result.add(str);
			}
			return (String[]) result.toArray(new String[0]);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static List<String> getLineToList(String file) throws IOException {
		String str;
		try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
			List<String> result = new Vector<String>();
			while ((str = bfr.readLine()) != null) {
				result.add(str);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static String getContent(String file) throws FileNotFoundException,
			IOException {
		try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
			String str;
			StringBuffer res = new StringBuffer();
			while ((str = bfr.readLine()) != null) {
				res.append(str).append("\n");
			}
			return res.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * 指定文件下，按指定字符获取内容
	 *
	 * @param file
	 * @param character 指定字符
	 * @return String[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] getLine(String file, String character)
			throws FileNotFoundException, IOException {
		String str;
		try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
			List result = new Vector();
			while ((str = bfr.readLine()) != null) {
				result.add(new String(str.getBytes(), character));
			}
			return (String[]) result.toArray(new String[0]);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	/**
	 * 批量写文件
	 *
	 * @param file
	 * @param content
	 * @return int
	 */
	public static int recordFetchToFile(String file, String[] content) {
		File f = new File(file);
		try (
				FileWriter fw = new FileWriter(f, true);
				BufferedWriter bw = new BufferedWriter(fw);
		) {
			for (int i = 0; i < content.length; i++) {
				bw.write(content[i]);
				bw.newLine();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		}
		return 0;
	}

	/**
	 * 批量写文件(可选择是否可追加)
	 *
	 * @param file
	 * @param content
	 * @param append
	 * @return int
	 */
	public static int recordFetchToFile(String file, String[] content,
										boolean append) {
		File f = new File(file);
		try (
				FileWriter fw = new FileWriter(f, append);
				BufferedWriter bw = new BufferedWriter(fw);
		) {
			for (int i = 0; i < content.length; i++) {
				bw.write(content[i]);
				bw.newLine();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		}
		return 0;
	}

	/**
	 * 追加一行进指定文本
	 *
	 * @param file    (文件位置，要包含文件名)
	 * @param content (内容，写完后会自动换行)
	 * @param append  (是否追加记录)
	 * @return int (0是正常,2是IO错误,9是未知错误)
	 */
	public static int recordLineToFile(String file, String content,
									   boolean append) {
		File f = new File(file);
		try (FileWriter fw = new FileWriter(f, append);
			 BufferedWriter bw = new BufferedWriter(fw);) {
			bw.write(content);
			bw.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			logger.error("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		}
		return 0;
	}

	/**
	 * 可设置保存文件的字符编码(由于JDK BUG，在WIN OS下文件名或内容没有中文时会出现编码依然是ANSI的情况)
	 *
	 * @param file
	 * @param content
	 * @param character
	 * @throws IOException
	 */
	public static void recordLineToFile(String file, String content,
										String character) throws IOException {
		try (
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos, character);
		) {
			osw.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IOException("不能写入文件,错误原因: " + ex.getMessage());
		}
	}

	public static File[] getAllFile(String path) throws IOException {
		return getAllFile(path, null);
	}

	// ToT
	public static File[] getAllFile(String path, List<File> fileList)
			throws IOException {
		if (fileList == null) {
			fileList = new ArrayList<File>();
		}
		File[] fList = getFilelist(path);
		for (File f : fList) {
			if (f.isDirectory()) {
				getAllFile(f.getAbsolutePath(), fileList);
				continue;
			} else {
				fileList.add(f);
			}
		}
		return (File[]) fileList.toArray(new File[0]);
	}

	/**
	 * 得到一个目录下的所有子目录
	 *
	 * @param path
	 * @return File[]
	 * @throws IOException
	 */
	public static File[] getSubdirName(String path) throws IOException {
		List l = new Vector();
		File[] f = new File(path).listFiles();

		if (f == null) {
			throw new IOException("Error path: " + path);
		}
		for (int i = 0; i < f.length; i++) {
			if (f[i].isDirectory()) {
				l.add(f[i]);
			}
		}
		return (File[]) l.toArray(new File[0]);
	}

	/**
	 * 得到一个目录下所有文件和目录
	 *
	 * @param path
	 * @return String[]
	 * @throws IOException
	 */
	public static File[] getFilelist(String path) throws IOException {
		File[] f = new File(path).listFiles();
		List l = new Vector();

		if (f == null) {
			throw new IOException("Error path: " + path);
		}

		for (int i = 0; i < f.length; i++) {
			// if(!f[i].isDirectory())
			l.add(f[i]);
		}
		return (File[]) l.toArray(new File[0]);
	}

	/**
	 * 深度建立文件夹，如果中间哪个文件夹不存在则建立
	 *
	 * @param path
	 */
	public static void mkdir(String path) {
		String[] dir = StringHelper.splitStr(path, "/");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dir.length; i++) {

			sb.append(dir[i] + "/");
			File f = new File(sb.toString());
			if (!f.exists()) {
				f.mkdir();
			}
		}
	}

	public static void clearDir(String dir) throws IOException {
		File[] f = getFilelist(dir);
		for (File e : f) {
			if (e.isDirectory()) {
				clearDir(e.getAbsolutePath());
			}
			if(!e.delete()){
				continue;
			}
		}
	}
}
