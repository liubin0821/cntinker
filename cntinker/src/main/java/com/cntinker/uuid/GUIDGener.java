package com.cntinker.uuid;


import java.io.BufferedWriter;
import java.io.FileWriter;

public class GUIDGener{
    private static UIDFactory uuid;

    static{
        try{
            uuid = UIDFactory.getInstance("GUID");
        }catch(UIDNotSupportException unsex){
        }
    }

    public GUIDGener(){
    }

    /**
     * @return String
     */
    public static String getGUID(){
        return uuid.getNextUID();
    }

    /**
     * @param args
     *            String[]
     */
    public static void main(String[] args){
        try{
            BufferedWriter fw = new BufferedWriter(new FileWriter("d:\\id.txt"));
            for(int i = 0;i <= 10000;i ++ ){
                fw.write(uuid.getNextUID());
                fw.write(System.getProperty("line.separator"));
            }
            fw.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
