/**
 * 2014年3月4日 下午4:17:59
 */
package com.cntinker.util;


import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @author: bin_liu
 */
public class QrcodeUtil{

    public static void createImg(String content,String file,int width,
            int height,String imgType,String charset) throws IOException,
            WriterException{

        String format = imgType;
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET,charset);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE,width,height,hints);
        File outputFile = new File(file);
        MatrixToImageWriter.writeToFile(bitMatrix,format,outputFile);
    }

    public static void createImg(String content,String file)
            throws IOException,WriterException{
        createImg(content,file,300,300,"png","utf-8");
    }

    public static void createImg(String content,String file,int width,int height)
            throws IOException,WriterException{
        createImg(content,file,width,width,"png","utf-8");
    }

    public static void createImg(String content,String file,int width,
            int height,String imgType) throws IOException,WriterException{
        createImg(content,file,width,width,imgType,"utf-8");
    }

    public static void main(String[] args) throws Exception{
        String text = "http://weixin.qq.com/q/wXSYEg3mdYUMJ3C_1lpC";
        int width = 500;
        int height = 500;
        String format = "png";
        String file = "/Users/liubin/temp/xnr/temp.png";
        String charset = "utf-8";

        createImg(text,file,width,height,format,charset);
        
        //createImg("嗒嗒洗车","d:/new.png",400,400,"png",charset);
    }
}
