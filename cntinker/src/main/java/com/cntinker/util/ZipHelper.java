/**
 *2011-9-20 下午02:25:39
 */
package com.cntinker.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author : bin_liu
 */
public class ZipHelper{

    private static final String BASE_DIR = "";

    // 符号"/"用来作为目录标识判断符
    private static final String PATH = "/";

    private static final int BUFFER = 1024;

    /**
     * 压缩
     * 
     * @param srcFile
     *            源路径
     * @param destFile
     *            目标路径
     * @throws IOException
     */
    public static void compress(File srcFile,File destFile) throws IOException{

        // 对输出文件做CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
                destFile),new CRC32());

        ZipOutputStream zos = new ZipOutputStream(cos);

        compress(srcFile,zos,BASE_DIR);

        zos.flush();
        zos.close();
    }

    /**
     * 压缩文件
     * 
     * @param srcFile
     * @param destPath
     * @throws IOException
     * @throws Exception
     */
    public static void compress(File srcFile,String destPath)
            throws IOException{
        compress(srcFile,new File(destPath));
    }

    /**
     * 压缩
     * 
     * @param srcFile
     *            源路径
     * @param zos
     *            ZipOutputStream
     * @param basePath
     *            压缩包内相对路径
     * @throws IOException
     * @throws Exception
     */
    private static void compress(File srcFile,ZipOutputStream zos,
            String basePath) throws IOException{
        if(srcFile.isDirectory()){
            compressDir(srcFile,zos,basePath);
        }else{
            compressFile(srcFile,zos,basePath);
        }
    }

    /**
     * 文件压缩
     * 
     * @param srcPath
     *            源文件路径
     * @param destPath
     *            目标文件路径
     * @throws IOException
     */
    public static void compress(String srcPath,String destPath)
            throws IOException{
        File srcFile = new File(srcPath);

        compress(srcFile,destPath);
    }

    /**
     * 压缩目录
     * 
     * @param dir
     * @param zos
     * @param basePath
     * @throws IOException
     * @throws Exception
     */
    private static void compressDir(File dir,ZipOutputStream zos,String basePath)
            throws IOException{

        File[] files = dir.listFiles();

        // 构建空目录
        if(files.length < 1){
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);
            zos.putNextEntry(entry);
            zos.closeEntry();
        }

        for(File file : files){
            // 递归压缩
            compress(file,zos,basePath + dir.getName() + PATH);
        }
    }

    /**
     * 文件压缩
     * 
     * @param file
     *            待压缩文件
     * @param zos
     *            ZipOutputStream
     * @param dir
     *            压缩文件中的当前路径
     * @throws IOException
     * @throws Exception
     */
    private static void compressFile(File file,ZipOutputStream zos,String dir)
            throws IOException{

        /**
         * 压缩包内文件名定义
         * 
         * <pre>
         * 如果有多级目录，那么这里就需要给出包含目录的文件名 
         * 如果用WinRAR打开压缩包，中文名将显示为乱码
         * </pre>
         */
        ZipEntry entry = new ZipEntry(dir + file.getName());

        zos.putNextEntry(entry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));

        int count;
        byte data[] = new byte[BUFFER];
        while( ( count = bis.read(data,0,BUFFER) ) != -1){
            zos.write(data,0,count);
        }
        bis.close();

        zos.closeEntry();
    }

    /**
     * 解压文件到指定目录下
     * 
     * @param zipFile
     * @param outPath
     * @throws IOException
     */
    public static void unCompress(String zipFile,String outPath)
            throws IOException{

        if(!outPath.substring(outPath.length() - 1,outPath.length())
                .equals("/"))
            outPath += "/";
        FileHelper.mkdir(outPath);

        // Open the ZIP file
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));

        // Get the first entry
        ZipEntry entry = null;

        while( ( entry = in.getNextEntry() ) != null){
            String outName = outPath + entry.getName();
            // 创建结构内的目录
            FileHelper.mkdir(getPath(outName));

            // 写文件
            OutputStream out = new FileOutputStream(outName);
            byte[] buf = new byte[BUFFER];

            int len;
            while( ( len = in.read(buf) ) > 0){
                out.write(buf,0,len);
            }
            out.close();
        }

        // Close the streams
        in.close();

    }

    /**
     * 得到一个只到文件夹的路径
     * 
     * @param file
     * @return String
     */
    private static String getPath(String file){
        return file.substring(0,file.lastIndexOf("/") + 1);
    }

    public static void main(String[] args) throws Exception{
        // unZip();
        // File[] fList = new File[]{new File("d:/temp/test")};
        String input = "d:/temp/test";
        String output = "d:/temp/out.zip";

        // 压缩文件
        // compress(input,output);
        // System.out.println(getPath("d:/temp/source/test/fuqi.txt"));

        // 解压缩
        unCompress(output,"d:/temp/source");

        // ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
        // output));
        // out.putNextEntry(new ZipEntry(""));
    }
}
