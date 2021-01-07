package com.cntinker.test;

import com.cntinker.uuid.UUID;
import com.cntinker.uuid.UUIDGener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classname TestUUID
 * @Date 2021/1/7 下午6:13
 * @Created by geddon
 */
public class TestUUID {

    @Test
    public void test(){
        System.out.println("----> start");
        System.out.println("UUIDGener.getUUID(): "+UUIDGener.getUUID());
        System.out.println("UUID.getDefault().getNextUID(): "+ UUID.getDefault().getNextUID());
        System.out.println("UUID.getDefault().getUID(): "+ UUID.getDefault().getUID());
        System.out.println("----> end");
    }
}
