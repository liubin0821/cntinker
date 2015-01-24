/**
 * 2011-4-12 下午08:07:53
 */
package com.cntinker.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author bin_liu
 */
public class ConfigHelper{

    /**
     * 配置文件的绝对路径
     */
    private String cfgPath;

    private String worklassPath;

    public String getCfgPath(){
        return cfgPath;
    }

    public void setCfgPath(String cfgPath){
        this.cfgPath = cfgPath;
    }

    public String getWorklassPath(){
        return worklassPath;
    }

    public void setWorklassPath(String worklassPath){
        this.worklassPath = worklassPath;
    }

    public ConfigHelper(Object flagClass){

        String osName = System.getProperty("os.name");
        String path = flagClass.getClass().getResource("").toString();
        System.out.println("path:" + path);
        if(osName.indexOf("Win") > -1){
            path = path.substring(path.indexOf("/") + 1);

        }else{
            path = path.substring(path.indexOf("/"));
            path = path.replaceAll("%20", " ");
        }

        // worklassPath = path.substring(0,path.indexOf("classes") + 8);
        // cfgPath = path.substring(0,path.indexOf("classes") + 8) + "conf/";
        if(path.indexOf("classes") > -1){
            worklassPath = path.substring(0,path.indexOf("classes") + 8);
            cfgPath = path.substring(0,path.indexOf("classes") + 8) + "conf/";
        }else{
            worklassPath = path.substring(0,path.indexOf("WEB-INF") + 8)+"classes/";
            cfgPath = path.substring(0,path.indexOf("WEB-INF")+8) + "classes/conf/";
        }
        System.out.println("cfgPath: " + cfgPath);
    }

    /**
     * 指定配置文件，得到指定KEY的VALUE
     * 
     * @param cfgFile
     * @param key
     * @return String
     * @throws IOException
     */
    public String getValueByName(String cfgFile,String key) throws IOException{
        String res = "";
        String[] f = FileHelper.getLine(cfgPath + cfgFile);
        for(String e : f){
            if(e.trim().startsWith(key)){
                res = StringHelper.splitStr(e,"=")[1];
            }
        }
        return res;
    }

    public static void main(String[] args) throws Exception{
        ConfigHelper c = new ConfigHelper(StringHelper.getInstancle());
        System.out.println(c.getCfgPath());
        // 默认配置文件目录
        System.out.println(c.getValueByName("server.properties",
                "platform.server.businessServer"));
        // 变更配置文件目录
        System.out.println(c
                .getValueByName("client.properties","client.server"));

        String s = "file:/D:/source/hualu/smsplatform/smsplatform-web/target/smsplatform-web/WEB-INF/classes/com/cntinker/util/";
        System.out
                .println(s.substring(s.indexOf("/") + 1,s.indexOf("WEB-INF")));
        System.out.println(c.getCfgPath().substring(0,
                c.getCfgPath().indexOf("WEB-INF")));
        System.out.println(c.getCfgPath());
    }
}
