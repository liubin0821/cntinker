/*
 * Created on 2005-6-21 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package com.cntinker.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * @author bin_liu
 *
 */
public class ByteCommon{

    public final static int BUFF_SIZE_2M = 2 * 1024;

    public final static int BUFF_SIZE_512KB = 512;

    public static byte[] object2Bytes(Object o) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    public static Object bytes2Object(byte raw[]) throws IOException,
            ClassNotFoundException{
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        ois.close();
        bais.close();
        return o;
    }

}
