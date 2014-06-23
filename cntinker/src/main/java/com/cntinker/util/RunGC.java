/*
 * Created on 2005-7-27 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package com.cntinker.util;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author peng_wei TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class RunGC extends Thread{

    public final static int CYCTIME = 15 * 60;

    public static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private int cycTime;

    private RunGC(int cycT){
        this.cycTime = cycT;
    }

    public static RunGC getInstance(){
        return getInstance(CYCTIME);
    }

    public static RunGC getInstance(int mi){
        return new RunGC(mi);
    }

    public void run(){
        while(true){
            try{
                Thread.sleep(cycTime * 1000);
            }catch(InterruptedException ie){
            }

            Runtime.getRuntime().gc();

            System.out.println(formatter.format(new Date()) + " ---- Run GC!");
        }
    }

    public static void main(String[] args){
        // RunGC.getInstance(3).start();
    }
}
