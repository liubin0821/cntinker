/**
 *2011-2-10 ����06:13:52
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
     * ��ͼƬӡˢ��ͼƬ��
     * 
     * @param pressImg
     *            -- ˮӡ�ļ�
     * @param targetImg
     *            -- Ŀ���ļ�
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

            // ˮӡ�ļ�
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
     * ��ӡ����ˮӡͼƬ
     * 
     * @param pressText
     *            --����
     * @param targetImg
     *            -- Ŀ��ͼƬ
     * @param fontName
     *            -- ������
     * @param fontStyle
     *            -- ������ʽ
     * @param color
     *            -- ������ɫ
     * @param fontSize
     *            -- �����С
     * @param x
     *            -- ƫ����
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
     * ���ٷֱȸı�ͼƬ��С
     * 
     * @param oldpath
     * @param newpath
     * @param proportion
     * @throws IOException
     */
    public static void scaleImage(String oldpath,String newpath,int proportion)
            throws IOException{
        // ��ȡ�ϵ�ͼƬ
        File oldimg = new File(oldpath);

        BufferedImage src = ImageIO.read(oldimg);

        int newW = (int) ( src.getWidth(null) * ( proportion * 0.01 ) );
        int newH = (int) ( src.getHeight(null) * ( proportion * 0.01 ) );

        Image Itemp = src.getScaledInstance(newW,newH,
                BufferedImage.SCALE_SMOOTH);
        BufferedImage thumbnail = new BufferedImage(newW,newH,
                BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(Itemp,0,0,null);
        // ���Ժ��ͼƬ·��
        File newimg = new File(newpath);
        FileOutputStream out = new FileOutputStream(newimg);

        // ��ͼ
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbnail);
        param.setQuality(1.0f,false);
        encoder.encode(thumbnail);
        out.close();
        src.flush();
        src = null;
    }

    /**
     * ����ͼƬ
     * 
     * @param oldpath
     *            ԭͼƬ
     * @param newpath
     *            �����ɵ�ͼƬ��ŵ�ַ
     * @param wdith
     *            ���Ժ�Ŀ�
     * @param height
     *            ���Ժ�ĸ�
     * @throws IOException
     */
    public static void scaleImage(String oldpath,String newpath,int wdith,
            int height) throws IOException{
        // ��ȡ�ϵ�ͼƬ
        File oldimg = new File(oldpath);

        BufferedImage bi = ImageIO.read(oldimg);
        Image Itemp = bi.getScaledInstance(wdith,height,
                BufferedImage.SCALE_SMOOTH);
        BufferedImage thumbnail = new BufferedImage(wdith,height,
                BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(Itemp,0,0,null);

        // ���Ժ��ͼƬ·��
        File newimg = new File(newpath);
        FileOutputStream out = new FileOutputStream(newimg);

        // ��ͼ
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbnail);
        param.setQuality(1.0f,false);
        encoder.encode(thumbnail);
        out.close();
        bi.flush();
        bi = null;

    }

    /**
     * �и�ͼƬ
     * 
     * @param x
     *            �ص������ (����ʼ����)
     * @param y
     *            �ص������� (���Ͽ�ʼ����)
     * @param width
     *            ��ȡ�Ŀ��
     * @param height
     *            ��ȡ�ĳ���
     * @param oldpath
     *            ͼƬλ��
     * @param newpath
     *            �����ɵ�ͼƬλ��
     * @throws IOException
     */
    public static void cutImage(int x,int y,int width,int height,
            String oldpath,String newpath) throws IOException{

        FileInputStream is = null;
        ImageInputStream iis = null;

        // ����ǻ�ȡͼƬ��չ���ķ��������磺jpg�����������ֳɵģ����û�У��Լ�ʵ��
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
