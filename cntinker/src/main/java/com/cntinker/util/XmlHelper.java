/**
 *2011-2-27 上午02:48:29
 */
package com.cntinker.util;


import java.io.StringReader;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author : bin_liu
 */
public class XmlHelper{

    /**
     * 得到输入XML内容的所有节点
     * 
     * @param xmlContent
     * @return Iterator<Element>
     * @throws IllegalAccessException
     * @throws DocumentException
     */
    public static Element getRoot(String xmlContent)
            throws IllegalAccessException,DocumentException{
        SAXReader reader = new SAXReader();
        // System.out.println(this.om.getXml());
        Document doc = null;
        if(xmlContent != null && xmlContent.trim().length() != 0){
            doc = reader.read(new StringReader(xmlContent));
        }else{
            throw new IllegalAccessException("错误的输入");
        }

        // Element root = doc.getRootElement();
        return doc.getRootElement();
    }

    /**
     * 得到一个指定节点的队列
     * 
     * @param root
     * @param flag
     * @return Element
     */
    public static Element getRootList(Element root,String flag){
        Element res = null;
        Iterator<Element> it = root.elementIterator();

        while(it.hasNext()){
            Element e = it.next();
            if(e.getName().equals(flag)){
                res = e;
            }
        }
        return res;
    }

    /**
     * 根据输入的Element得到某个指定名称的节点列表
     * 
     * @param root
     * @param name
     * @return Iterator<Element>
     */
    public static Iterator<Element> getRootListByNodeName(Element root,
            String name){
        return root.elementIterator(name);
    }

    /**
     * 得到某ROOT下的指定属性值
     * 
     * @param root
     * @param key
     * @return String
     */
    public static String getAttributeValue(Element root,String key){
        return root.attributeValue(key);
    }

    /**
     * 得到一个节点下指定的二级属性值
     * 
     * @param root
     * @param flag
     * @param keyword
     * @return String
     */
    public static String getElementInfoSec(Element root,String flag,
            String keyword){
        String res = "";
        Iterator<Element> it = root.elementIterator();
        while(it.hasNext()){
            Element e = it.next();
            if(e.getName().equals(flag)){
                res = e.elementText(keyword);
            }
        }
        return res;
    }

    /**
     * 得到一个节点下指定的单级属性值
     * 
     * @param root
     * @param keyword
     * @return String
     */
    public static String getElementInfo(Element root,String keyword){
        return root.elementText(keyword);
    }

    public static void main(String[] args) throws Exception{
        String testXml = "D:/source/fx_project/cntinker/testxml.xml";
        String[] c = FileHelper.getLine(testXml);
        StringBuffer sb = new StringBuffer();
        for(String e : c){
            sb.append(e);
        }
        System.out.println(sb.toString());
        System.out.println("--------------------------------");
        Element root = XmlHelper.getRoot(sb.toString());
        Iterator<Element> eList = root.elementIterator();
        while(eList.hasNext()){
            Element e = eList.next();
            System.out.println("flag: " + e.getName());

        }
        System.out.println(XmlHelper.getElementInfoSec(root,"head","sub"));
        System.out.println(XmlHelper.getElementInfo(root,"foot"));
        System.out.println(XmlHelper.getElementInfoSec(root,"body","subSec"));
    }
}
