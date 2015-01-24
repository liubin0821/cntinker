/**
 * 2014年2月10日 上午3:19:54
 */
package com.cntinker.util;


import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: bin_liu
 */
public class JsonUtil{

    public static String getValueByArray(JSONArray array,String key)
            throws JSONException{
        JSONObject o = getJsonObjectByArray(array,key);
        if(o == null)
            return null;
        return o.get(key).toString();
    }

    public static JSONObject getJsonObjectByArray(JSONArray array,String key)
            throws JSONException{
                
        for(int i = 0;i < array.length();i ++ ){
            JSONObject o = (JSONObject) array.get(i);  
            Iterator<String> it = o.keys();
            while(it.hasNext()){
                String k = it.next();
                if(k.equals(key)){
                    Object res  = o.get(k);
                    if(res!=null)
                        return o;
                }
            }          
        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("");
    }
}
