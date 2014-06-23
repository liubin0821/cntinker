package com.cntinker.uuid;


import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Random;

/**
 * <pre>
 * private static UIDFactory uuid = null;
 * 
 * try{
 *     uuid = UIDFactory.getInstance(&quot;UUID&quot;);
 * }catch(UIDNotSupportException unsex){
 * }
 * ;
 * 
 * String id = uuid.getNextUID();
 * </pre>
 */
public abstract class UIDFactory{
    // ~ Static fields/initializers
    // /////////////////////////////////////////////
    /** Global Unified Identifier */
    public static final String UID_GUID = "GUID";

    /** United Unified Identifier */
    public static final String UID_UUID = "UUID";

    /** Current Epoch millis SEED */
    protected static final long EPOCH = System.currentTimeMillis();

    /** JVM Hashcode */
    protected static final long JVMHASH = Math
            .abs( ( new Object() ).hashCode());

    /** Epoch has millisecond */
    protected static final long MACHINEID = getMachineID();

    /** Random by seed */
    protected static final Random RANDOM = new Random(EPOCH);

    /** MD5 Instance */
    private static MessageDigest md5;

    /* Initialize MD5 factory */
    static{
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch(java.security.NoSuchAlgorithmException ex){
            System.out.println("->" + ex);
        }
    }

    private boolean isMd5 = false;

    /**
     * @return UIDFactory UID manager object
     */
    public static UIDFactory getDefault(){
        return UUID.getInstance();
    }

    /**
     * @param uidfactory
     *            Description of the Parameter
     *@return UIDFactory
     *@exception UIDNotSupportException
     *                Description of the Exception
     *@throws java.lang.ClassNotFoundException
     */
    public static UIDFactory getInstance(String uidfactory)
            throws UIDNotSupportException{
        if(uidfactory.equalsIgnoreCase(UID_UUID)){
            return UUID.getInstance();
        }
        if(uidfactory.equalsIgnoreCase(UID_GUID)){
            return GUID.getInstance();
        }
        throw new UIDNotSupportException(uidfactory + " Not Found!");
    }

    /**
     * @return String Storagable UID
     */
    public abstract String getNextUID();

    /**
     * @return String Storagable UID
     */
    public abstract String getUID();

    /**
     * @param flag
     *            MD5 switch
     */
    public void setMD5(boolean flag){
        isMd5 = flag;
    }

    /**
     * @return true
     */
    public boolean isMD5(){
        return isMd5;
    }

    /**
     * @param uid
     *            Object uid
     *@exception Exception
     *                Description of the Exception
     */
    public abstract void setUID(String uid) throws Exception;

    /**
     * @return String
     */
    public abstract String toPrintableString();

    /**
     *@param bytes
     *            Description of the Parameter
     *@return byte[]
     */
    protected static byte[] toMD5(byte[] bytes){
        return md5.digest(bytes);
    }

    /**
     * @return The machineID value
     */
    private static long getMachineID(){
        long i = 0;
        try{
            InetAddress inetaddress = InetAddress.getLocalHost();
            byte[] abyte0 = inetaddress.getAddress();
            i = toInt(abyte0);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return i;
    }

    /**
     * @param abyte0
     *            Object bytes array
     *@return Result int
     */
    private static int toInt(byte[] abyte0){
        int i = ( ( abyte0[0] << 24 ) & 0xff000000 )
                | ( ( abyte0[1] << 16 ) & 0xff0000 )
                | ( ( abyte0[2] << 8 ) & 0xff00 ) | ( abyte0[3] & 0xff );
        return i;
    }

    /**
     * @param len
     *            int
     * @return String
     */
    public abstract String getNextUID(int len);
}
