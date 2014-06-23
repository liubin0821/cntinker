/**
 * 2011-2-22 上午11:13:39
 */
package com.cntinker.util;


import java.io.FileNotFoundException;
import java.io.IOException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @autohr: bin_liu
 */
public class HtmlParse{

    private String url;

    private Parser parser;

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public Parser getParser(){
        return parser;
    }

    public void setParser(Parser parser){
        this.parser = parser;
    }

    public HtmlParse(String url,String encoding) throws FileNotFoundException,
            IOException,ParserException{
        this.url = url;
        this.parser = new Parser(url);
        this.parser.setEncoding(encoding);
    }

    public HtmlParse(String stringInput) throws ParserException{
        this.parser = new Parser(stringInput);
    }

    /**
     * 得到节点队列
     * 
     * @param nodeFilter
     * @return NodeList
     * @throws ParserException
     */
    public NodeList getNodeList(NodeFilter nodeFilter) throws ParserException{
        this.parser.reset();
        NodeList nList = (NodeList) parser.parse(nodeFilter);
        return nList;
    }

    /**
     * 得到某个节点列表里符合指定所有关键字的所有子节点
     * 
     * @param nodeList
     * @param keywords
     * @return NodeList
     */
    public NodeList getNodeListByKeywords(NodeList nodeList,String[] keywords){
        NodeList res = new NodeList();
        for(int i = 0;i < nodeList.size();i ++ ){
            Node node = nodeList.elementAt(i);
            String nodeHtml = node.toHtml();
            boolean isAdd = true;
            for(int j = 0;j < keywords.length;j ++ ){
                if(nodeHtml.indexOf(keywords[j]) < 0){
                    isAdd = false;
                }
            }
            if(isAdd)
                res.add(node);
        }
        return res;
    }

    /**
     * 得到DIV的HTML
     * 
     * @param nodeFilter
     * @return String
     * @throws ParserException
     */
    public String getDivHtml(NodeFilter nodeFilter) throws ParserException{

        NodeList nList = getNodeList(nodeFilter);
        StringBuffer str = new StringBuffer();
        for(int i = 0;i < nList.size();i ++ ){
            Div div = (Div) nList.elementAt(i);
            str.append(div.toHtml());
        }
        return str.toString();
    }

    public String getNodeHtml(NodeFilter nodeFilter) throws ParserException{
        NodeList nList = getNodeList(nodeFilter);
        StringBuffer str = new StringBuffer();
        for(int i = 0;i < nList.size();i ++ ){
            Node node = nList.elementAt(i);
            str.append(node.toHtml());
        }
        return str.toString();
    }

    public static void main(String[] args) throws Exception{
        HtmlParse f = new HtmlParse("http://www.wenming.cn","UTF-8");

        NodeFilter nFilter = new AndFilter(new TagNameFilter("div"),
                new HasAttributeFilter("id","main_6th"));

        NodeList nList = f.getNodeList(nFilter);

        for(int i = 0;i < nList.size();i ++ ){
            Div div = (Div) nList.elementAt(i);
            System.out.println(div.toHtml());
            // System.out.println(div.getChildrenHTML());
        }

        System.out.println("======================");

    }
}
