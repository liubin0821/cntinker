/**
 * 2012-5-10 下午3:34:07
 */
package com.cntinker.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: bin_liu
 */
public class HttpHelper{

    public static void main(String[] args) throws IOException{
        String url = "http://itunes.apple.com/lookup?id=284910350";
        System.out.println(postHttpRquest(url,"","UTF-8"));
        System.out.println("----------- sending -------------");

    }

    public static String postXml(String url,String xml) throws IOException{
        return postXml(url,xml,"utf-8",false);
    }

    public static String postXml(String url,String xml,String character)
            throws IOException{
        return postXml(url,xml,character,false);
    }

    public static String postXml(String url,String xml,String character,
            boolean displayHeadInfo) throws IOException{
        StringBuffer tempStr = new StringBuffer();
        BufferedReader br = null;
        URL u = new URL(url);
        URLConnection con = u.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type","text/xml");
        con.setRequestProperty("Pragma:","no-cache");
        con.setRequestProperty("Cache-Control","no-cache");
        con.setRequestProperty("Content-length",String.valueOf(xml.length()));

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        // OutputStreamWriter out = new
        // OutputStreamWriter(con.getOutputStream());

        out.write(new String(xml.getBytes(character)).getBytes());

        InputStream urlStream = null;
        urlStream = con.getInputStream();
        Map m = con.getHeaderFields();

        Iterator it = m.keySet().iterator();
        String currentLine = "";
        if(displayHeadInfo){
            while(it.hasNext()){
                String k = (String) it.next();
                tempStr.append(k).append(":").append(con.getHeaderField(k))
                        .append("\n");
            }
        }
        br = new BufferedReader(new InputStreamReader(urlStream));
        while( ( currentLine = br.readLine() ) != null){
            tempStr.append(currentLine);
        }

        return tempStr.toString();
    }

    public static String sendToGet(String url,
            Map<String, String> requestProperty,String chacter)
            throws MalformedURLException{
        url = conChar(url);
        String result = "";
        BufferedReader in = null;
        try{
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            if(requestProperty != null && requestProperty.keySet() != null
                    && requestProperty.keySet().size() > 0){
                Iterator<String> it = requestProperty.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    connection.setRequestProperty(key,requestProperty.get(key));
                }
            }else{
                connection.setRequestProperty("accept","*/*");
                connection.setRequestProperty("connection","Keep-Alive");
                connection
                        .setRequestProperty("user-agent",
                                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /**
             * for (String key : map.keySet()) { System.out.println(key + "--->"
             * + map.get(key)); }
             */
            // 定义 BufferedReader输入流来读取URL的响应
            if(!StringHelper.isNull(chacter))
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),chacter));
            else
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
            String line;
            while( ( line = in.readLine() ) != null){
                result += line;
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new MalformedURLException("url error:"
                    + StringHelper.getStackInfo(e));
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 请求指定地址
     * 
     * @param destURL
     *            (目标地址,http://xxx.xx.com)
     * @param parameters
     *            (POST请求体参数,parame_a=a&param_b=b)
     * @return String(请求对方回写的内容)
     * @throws IOException
     */
    public static String postHttpRquest(String destURL,String parameters)
            throws IOException{
        URL url = null;
        HttpURLConnection uc = null;
        OutputStream out = null;
        InputStream urlStream = null;
        BufferedReader br = null;
        String currentLine = "";
        StringBuffer tempStr = new StringBuffer();

        url = new URL(destURL);
        uc = (HttpURLConnection) url.openConnection();

        uc.setRequestMethod("POST");
        uc.setDoOutput(true);
        out = uc.getOutputStream();
        if(!StringHelper.isNull(parameters))
            out.write(parameters.getBytes());
        urlStream = uc.getInputStream();
        Map m = uc.getHeaderFields();

        Iterator it = m.keySet().iterator();

        while(it.hasNext()){
            String k = (String) it.next();
            tempStr.append(k).append(":").append(uc.getHeaderField(k))
                    .append("\n");
        }
        tempStr.append("\n\n");
        br = new BufferedReader(new InputStreamReader(urlStream));
        while( ( currentLine = br.readLine() ) != null){
            tempStr.append(currentLine);
        }

        return tempStr.toString();
    }

    /**
     * 指定字符读回流
     * 
     * @param destURL
     * @param parameters
     * @param character
     * @return String
     * @throws IOException
     */
    public static String postHttpRquest(String destURL,String parameters,
            String character) throws IOException{
        URL url = null;
        HttpURLConnection uc = null;
        OutputStream out = null;
        InputStream urlStream = null;
        BufferedReader br = null;
        String currentLine = "";
        StringBuffer tempStr = new StringBuffer();

        url = new URL(destURL);
        uc = (HttpURLConnection) url.openConnection();

        uc.setRequestMethod("POST");
        uc.setDoOutput(true);
        out = uc.getOutputStream();
        if(!StringHelper.isNull(parameters))
            out.write(parameters.getBytes());
        urlStream = uc.getInputStream();

        // Map m = uc.getHeaderFields();
        // Iterator it = m.keySet().iterator();
        // while(it.hasNext()){
        // String k = (String) it.next();
        // tempStr.append(k).append(":").append(uc.getHeaderField(k))
        // .append("\n");
        // }

        tempStr.append("\n\n");
        br = new BufferedReader(new InputStreamReader(urlStream,character));
        while( ( currentLine = br.readLine() ) != null){
            tempStr.append(currentLine);
            tempStr.append("\n");
        }

        return tempStr.toString();
    }

    /**
     * 读回流的时候用指定字符集
     * 
     * @param destURL
     * @param chacter
     * @return String
     * @throws MalformedURLException
     * @throws IOException
     */
    public static String sendGet(String destURL,String chacter)
            throws MalformedURLException,IOException{
        return sendToGet(destURL,null,chacter);
    }

    /**
     * @param destURL
     * @param parameters
     * @return
     */
    public static String sendGet(String destURL) throws MalformedURLException,
            IOException{
        return sendToGet(destURL,null,null);
    }

    public static String getHeader(String destURL){
        URL url = null;
        HttpURLConnection uc = null;
        OutputStream out = null;
        InputStream urlStream = null;
        BufferedReader br = null;
        String currentLine = "";
        StringBuffer tempStr = new StringBuffer();
        try{
            url = new URL(destURL);
            uc = (HttpURLConnection) url.openConnection();
            Map m = uc.getHeaderFields();
            Iterator it = m.entrySet().iterator();

            while(it.hasNext()){
                // System.out.println(it.next());
                tempStr.append(it.next()).append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return tempStr.toString();
    }

    private static String conChar(String url){
        return StringHelper.isNull(url) ? null : url.replaceAll("#","%23")
                .replaceAll("\\+","%2B").replaceAll(" ","%20")
                .replaceAll("/","%2F");
    }
}
