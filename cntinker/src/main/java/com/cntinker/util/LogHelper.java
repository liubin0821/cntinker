/**
 * 2013-7-21 下午12:47:50
 */
package com.cntinker.util;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @autohr: bin_liu
 */
public class LogHelper{

    static{
        PropertyConfigurator.configureAndWatch("log4j.xml",60000);
    }

    
    public static void reload(){
        PropertyConfigurator.configure("log4j.xml");
    }
    
    public static void debug(String logName,String msg){
        Logger exLogger = Logger.getLogger(logName);
    }
    
    private static void testReload() throws InterruptedException{
        int flag = 0;
        while(true){
            Thread.currentThread().sleep(10 * 1000);
            if(flag == 2){

            }
            flag ++ ;
        }
    }

    public static void main(String[] args) throws Exception{
        ConfigHelper c = new ConfigHelper(new HttpHelper());
        System.out.println(c.getCfgPath());
        
        System.out.println(c.getWorklassPath());
    }
}
