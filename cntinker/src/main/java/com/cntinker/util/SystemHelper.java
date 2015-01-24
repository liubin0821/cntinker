/**
 * 2013-7-19 上午11:39:31
 */
package com.cntinker.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import com.sun.management.OperatingSystemMXBean;

/**
 * 本类中提供的获取系统数据的参数支持跨平台，但经由runOSCmd方法执行的返回需要自行判断OS<br>
 * 需要JRE1.6+<br>
 * 
 * @autohr: bin_liu
 */
@SuppressWarnings("unchecked")
public class SystemHelper{

    /**
     * system name
     * 
     * @return String
     */
    public static String getOsName(){
        String res = System.getProperty("os.name");

        if(StringHelper.isNull(res)){
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean();
            res = osmb.getName();
        }
        return res;
    }

    public static String getJVMVersion(){
        return System.getProperty("java.vm.specification.version");
    }

    public static String getJavaHome(){
        return System.getProperty("java.home");
    }

    /**
     * JVM实现供应商
     * 
     * @return String
     */
    public static String getJVMVendor(){
        return System.getProperty("java.vm.specification.vendor");
    }

    /**
     * JVM环境规范版本
     * 
     * @return
     */
    public static String getJVMSpec(){
        return System.getProperty("java.vm.specification.name");
    }

    public static String getJVMName(){
        return System.getProperty("java.vm.name");
    }

    public static String getJVMRuntimeVersion(){
        return System.getProperty("java.specification.version");
    }

    /**
     * JVM运行时环境规范版本
     * 
     * @return
     */
    public static String getJVMRuntimeSpec(){
        return System.getProperty("java.specification.name");
    }

    public static String getClasspath(){
        return replaceLineSeparator(System.getProperty("java.class.path"));
    }

    public static String getLibPath(){
        return replaceLineSeparator(System.getProperty("java.library.path"));
    }

    public static String getTempdir(){
        return replaceLineSeparator(System.getProperty("java.io.tmpdir"));
    }

    public static String getExtdir(){
        return replaceLineSeparator(System.getProperty("java.ext.dirs"));
    }

    public static String getOsarch(){
        return System.getProperty("os.arch");
    }

    public static String getFileSeparator(){
        return System.getProperty("file.separator");
    }

    public static String getPathSeparator(){
        return replaceLineSeparator(System.getProperty("path.separator"));
    }

    public static String getLineSeparator(){
        return System.getProperty("line.separator");
    }

    public static String getUsername(){
        return System.getProperty("user.name");
    }

    public static String getUserhome(){
        return replaceLineSeparator(System.getProperty("user.home"));
    }

    public static String getUserCurrentDir(){
        return replaceLineSeparator(System.getProperty("user.dir"));
    }

    public static String getMacAddress() throws SocketException,
            UnknownHostException{
        return StringHelper.byte2hex(NetworkInterface.getByInetAddress(
                InetAddress.getLocalHost()).getHardwareAddress());
    }

    public static String[] runOSCmd(String command) throws IOException{
        return runOSCmd(command,"utf-8");
    }

    public static String[] runOSCmd(String command,String character)
            throws IOException{
        String para = "/c";
        if(getOsName().indexOf("Linux") > -1){
            para = "-c";
        }
        return runOSCmd(command,character,para);
    }

    private static String replaceLineSeparator(String content){
        if(getOsName().indexOf("Win") > -1){
            content = content.replaceAll("/",getLineSeparator());
            //content = content.replaceAll("\\",getLineSeparator());
        }
        return content;
    }

    /**
     * 对于创建目录这类带分隔符操作，统一使用md d:/temp即可 ，已对WIN系统的分隔符做转义处理<br>
     * 
     * @param command
     * @param character
     *            指定返回信息编码,默认utf-8<br>
     * @return String[] 返回终端信息<br>
     * @throws IOException
     */
    public static String[] runOSCmd(String command,String character,String para)
            throws IOException{

        command = replaceLineSeparator(command);

        String binPro = "cmd.exe";

        if(getOsName().indexOf("Linux") > -1){
            binPro = "/bin/sh";
        }

        String[] cmd = {binPro,para,command};

        Process pid = Runtime.getRuntime().exec(cmd);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(
                pid.getInputStream(),character),1024);
        String str = "";

        List<String> result = new Vector<String>();
        while( ( str = bfr.readLine() ) != null){
            result.add(str);

        }
        bfr.close();

        return (String[]) result.toArray(new String[0]);
    }

    /**
     * type:<br>
     * 1:total <br>
     * 2:available <br>
     * 3:
     * 
     * @param type
     * @return Long
     */
    public static Long getMem(int type){
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        Long res = 0l;
        if(type == 1){
            res = osmb.getTotalPhysicalMemorySize() / 1024 / 1024;
        }else if(type == 2){
            res = osmb.getFreePhysicalMemorySize() / 1024 / 1024;
        }

        return res;
    }

    public static void main(String[] args) throws Exception{
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        System.out.println("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize()
                / 1024 / 1024 + "MB");
        System.out.println("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize()
                / 1024 / 1024 + "MB");

        System.out.println("JVM内存总计：" + osmb.getCommittedVirtualMemorySize()
                / 1024 / 1024 + "MB");
        System.out.println("os Name:" + osmb.getName());
        System.out.println("getAvailableProcessors:"
                + osmb.getAvailableProcessors());
        System.out.println("getFreeSwapSpaceSize:"
                + osmb.getFreeSwapSpaceSize() / 1024 / 1024 + "MB");

        System.out.println("getTotalSwapSpaceSize:"
                + osmb.getTotalSwapSpaceSize() / 1024 / 1024 + "MB");

        long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        System.out.println("totalMemory:: " + totalMemory);
        System.out.println("freeMemory:: " + freeMemory);
        System.out.println("maxMemory:: " + maxMemory);

        String[] s = runOSCmd("dir/w","gbk");
        for(String e : s){
            System.out.println(" : " + e);
        }

        String[] te = runOSCmd("dir/w","gbk");
        System.out.println(getMacAddress());

    }
}
