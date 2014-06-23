package com.cntinker.uuid;


import java.util.Date;

import com.cntinker.util.MD5Encoder;

/**
 * @author bin_liu
 */
public class GUID16{
    public static String getGUID16(){
        return MD5Encoder.encode16(new Date().getTime() + "");
    }
}
