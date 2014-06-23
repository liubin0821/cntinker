package com.cntinker.uuid;


import java.io.BufferedWriter;
import java.io.FileWriter;

public class UUIDGener{
    private static UIDFactory uuid;
    static{
        try{
            uuid = UIDFactory.getInstance("UUID");
        }catch(UIDNotSupportException unsex){
        }
        ;
    }

    public UUIDGener(){
    }

    /**
     * @return String
     */
    public static String getUUID(){
        return uuid.getNextUID();
    }

    /**
     * @param len
     *            int
     * @return String
     */
    public static String getUUID(int len){
        return uuid.getNextUID(len);
    }

    /**
     * @param args
     *            String[]
     */
    public static void main(String[] args){
        try{
            BufferedWriter fw = new BufferedWriter(new FileWriter("d:\\id2.txt"));
            for(int i = 1;i < 10000;i ++ ){
                fw.write(uuid.getNextUID());
                fw.write(System.getProperty("line.separator"));
            }
            fw.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
