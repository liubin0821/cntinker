/**
 *2011-2-10 下午06:13:52
 */
package com.cntinker.util;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.poi.util.StringUtil;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @autohr: bin_liu
 */
public class ImgHelper{

    /**
     * 把图片印刷到图片上
     * 
     * @param pressImg
     *            -- 水印文件
     * @param targetImg
     *            -- 目标文件
     * @param x
     * @param y
     */
    public final static void pressImage(String pressImg,String targetImg,int x,
            int y){
        try{
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth,height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src,0,0,wideth,height,null);

            // 水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao,wideth - wideth_biao - x,height - height_biao
                    - y,wideth_biao,height_biao,null);
            // /
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印文字水印图片
     * 
     * @param pressText
     *            --文字
     * @param targetImg
     *            -- 目标图片
     * @param fontName
     *            -- 字体名
     * @param fontStyle
     *            -- 字体样式
     * @param color
     *            -- 字体颜色
     * @param fontSize
     *            -- 字体大小
     * @param x
     *            -- 偏移量
     * @param y
     */

    public static void pressText(String pressText,String targetImg,
            String outImg,String fontName,Color fontColor,int fontStyle,
            int fontSize,int x,int y){
        try{
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth,height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src,0,0,wideth,height,null);
            // String s="www.qhd.com.cn";
            g.setColor(fontColor);
            g.setFont(new Font(fontName,fontStyle,fontSize));

            g.drawString(pressText,wideth - fontSize - x,height - fontSize / 2
                    - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(outImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        }catch(Exception e){
            System.out.println(StringHelper.getStackInfo(e));
            System.out.println(e);
        }
    }

    /**
     * 按百分比改变图片大小
     * 
     * @param oldpath
     * @param newpath
     * @param proportion
     * @throws IOException
     */
    public static void scaleImage(String oldpath,String newpath,int proportion)
            throws IOException{
        // 获取老的图片
        File oldimg = new File(oldpath);

        BufferedImage src = ImageIO.read(oldimg);

        int newW = (int) ( src.getWidth(null) * ( proportion * 0.01 ) );
        int newH = (int) ( src.getHeight(null) * ( proportion * 0.01 ) );

        Image Itemp = src.getScaledInstance(newW,newH,
                BufferedImage.SCALE_SMOOTH);
        BufferedImage thumbnail = new BufferedImage(newW,newH,
                BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(Itemp,0,0,null);
        // 缩略后的图片路径
        File newimg = new File(newpath);
        FileOutputStream out = new FileOutputStream(newimg);

        // 绘图
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbnail);
        param.setQuality(1.0f,false);
        encoder.encode(thumbnail);
        out.close();
        src.flush();
        src = null;
    }

    /**
     * 缩略图片
     * 
     * @param oldpath
     *            原图片
     * @param newpath
     *            新生成的图片存放地址
     * @param wdith
     *            缩略后的宽
     * @param height
     *            缩略后的高
     * @throws IOException
     */
    public static void scaleImage(String oldpath,String newpath,int wdith,
            int height) throws IOException{
        // 获取老的图片
        File oldimg = new File(oldpath);

        BufferedImage bi = ImageIO.read(oldimg);
        Image Itemp = bi.getScaledInstance(wdith,height,
                BufferedImage.SCALE_SMOOTH);
        BufferedImage thumbnail = new BufferedImage(wdith,height,
                BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(Itemp,0,0,null);

        // 缩略后的图片路径
        File newimg = new File(newpath);
        FileOutputStream out = new FileOutputStream(newimg);

        // 绘图
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbnail);
        param.setQuality(1.0f,false);
        encoder.encode(thumbnail);
        out.close();
        bi.flush();
        bi = null;

    }

    /**
     * 切割图片
     * 
     * @param x
     *            截点横坐标 (从左开始计数)
     * @param y
     *            截点纵坐标 (从上开始计数)
     * @param width
     *            截取的宽度
     * @param height
     *            截取的长度
     * @param oldpath
     *            图片位置
     * @param newpath
     *            新生成的图片位置
     * @throws IOException
     */
    public static void cutImage(int x,int y,int width,int height,
            String oldpath,String newpath) throws IOException{

        FileInputStream is = null;
        ImageInputStream iis = null;

        // 这个是获取图片扩展名的方法，比如：jpg。我这里有现成的，如果没有，自己实现
        String imgType = FileHelper.getFiletype(oldpath);

        is = new FileInputStream(oldpath);
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(imgType);
        ImageReader reader = it.next();
        iis = ImageIO.createImageInputStream(is);
        reader.setInput(iis,true);
        ImageReadParam param = reader.getDefaultReadParam();
        Point p = new Point();
        p.setLocation(x,y);

        Dimension d = new Dimension();
        d.setSize(width,height);
        Rectangle rect = new Rectangle(p,d);
        param.setSourceRegion(rect);

        BufferedImage bi = reader.read(0,param);
        ImageIO.write(bi,imgType,new File(newpath));

        is.close();
        iis.close();

    }

    public static void main(String[] args){
        // pressImage("C:/shuiyin/shuiyin.gif","d:/omen_Pc30.jpg",20,20);
        // pressText("www.liutaitai.com","d:/Temp/174308x44z14wblbri4k4j.png.thumb.jpg",
        // "d:/Temp/174308x44z14wblbri4k4j.png.thumb_1.jpg","Serief",Color.BLACK,
        // Font.ITALIC,22,200,200);

        // pressText("www.liutaitai.com","d:/Temp/omen_Pc30.jpg",
        // "d:/Temp/omen_Pc30_1.jpg","Serief",Color.BLACK,
        // Font.ITALIC,22,200,200);

    }
}
