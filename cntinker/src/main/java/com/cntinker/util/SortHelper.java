/**
 *2013-4-18 下午2:25:51
*/
package com.cntinker.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @autohr: bin_liu
 *
 */
public class SortHelper{

    /**
     * 对Map的key=String进行key排序
     * 
     * @param m
     * @param isDesc
     * @return Map<String, Object>
     */
    public static Map sortMap(Map m,final boolean isDesc){
        List list_Data = new ArrayList(m.keySet());

        Collections.sort(list_Data,new Comparator<String>(){
            public int compare(String o1,String o2){
                int res = 0;
                if(!isDesc)
                    res = o1.compareTo(o2);
                else
                    res = o2.compareTo(o1);
                return res;
            }
        });

        Map m2 = new LinkedHashMap();

        for(Object e : list_Data){
            m2.put(e,m.get(e));
        }
        return m2;
    }
    
    
}
