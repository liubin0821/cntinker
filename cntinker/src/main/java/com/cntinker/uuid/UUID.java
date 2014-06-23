package com.cntinker.uuid;


public class UUID extends UIDFactory{
    protected static final int BITS8 = 8;

    protected static final int BYTELEN = 16;

    protected static final int HIMASK = 240;

    protected static final int LO8BITMASK = 255;

    protected static final int LOMASK = 15;

    protected static final long MAX_INT = 32767;

    protected static final long MAX_LONG = 2147483647;

    protected String m_uuid = null;

    /** Epoch has millisecond */
    protected long m_hiTag;

    protected long m_loTag;

    /**
     * @param highTag
     *            High order tag
     *@param loTag
     *            Low order tag
     */
    protected UUID(long highTag,long loTag){
        m_hiTag = highTag;
        m_loTag = loTag;
        m_uuid = toString(this.toByteArray());
    }

    protected UUID(){
        next();
        m_uuid = toString(this.toByteArray());
    }

    /**
     * @return java.lang.String
     */
    public String getNextUID(){
        next();
        return m_uuid;
    }

    /**
     * @return java.lang.String
     */
    public String getNextUID(int len){
        next();
        return m_uuid;
    }

    /**
     * @param uidStr
     *            The new uID value
     *@exception Exception
     *                Bad string format
     */
    public void setUID(String uidStr) throws Exception{
        long loTag = 0L;
        long hiTag = 0L;
        int len = uidStr.length();
        if(32 != len){
            throw new Exception("bad string format");
        }
        int i = 0;
        int idx = 0;
        for(;i < 2;i ++ ){
            loTag = 0L;
            for(int j = 0;j < ( len / 2 );j ++ ){
                String s = uidStr.substring(idx ++ ,idx);
                int val = Integer.parseInt(s,16);
                loTag <<= 4;
                loTag |= val;
            }
            if(i == 0){
                hiTag = loTag;
            }
        }
        m_hiTag = hiTag;
        m_loTag = loTag;
        m_uuid = toString(this.toByteArray());
    }

    /**
     * @return java.lang.String
     */
    public String getUID(){
        return m_uuid;
    }

    /**
     * @param obj
     *            Object UUID
     *@return Ture if equal
     */
    public boolean equals(Object obj){
        try{
            UUID uuid = (UUID) obj;
            boolean flag = ( uuid.m_hiTag == m_hiTag )
                    && ( uuid.m_loTag == m_loTag );
            return flag;
        }catch(ClassCastException cce){
            return false;
        }
    }

    /**
     * @return java.lang.String
     */
    public String toPrintableString(){
        byte[] bytes = toByteArray();
        if(16 != bytes.length){
            return "** Bad UUID Format/Value **";
        }
        StringBuffer buf = new StringBuffer();
        int i;
        for(i = 0;i < 4;i ++ ){
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }
        while(i < 10){
            buf.append('-');
            int j = 0;
            while(j < 2){
                buf.append(Integer.toHexString(hiNibble(bytes[i])));
                buf.append(Integer.toHexString(loNibble(bytes[i ++ ])));
                j ++ ;
            }
        }
        buf.append('-');
        for(;i < 16;i ++ ){
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }
        return buf.toString();
    }

    /**
     * @return UID String
     */
    public String toString(){
        return m_uuid;
    }

    /**
     * @return UUID
     */
    protected static UIDFactory getInstance(){
        return new UUID();
    }

    /**
     * Overpass a bytes array generator UID String.
     * 
     *@param bytes
     *            Object bytes array
     *@return UID String
     */
    protected static String toString(byte[] bytes){
        if(16 != bytes.length){
            return "** Bad UUID Format/Value **";
        }
        StringBuffer buf = new StringBuffer();
        for(int i = 0;i < 16;i ++ ){
            buf.append(Integer.toHexString(hiNibble(bytes[i])));
            buf.append(Integer.toHexString(loNibble(bytes[i])));
        }
        return buf.toString();
    }

    protected void next(){
        m_hiTag = ( System.currentTimeMillis() + ( JVMHASH * 4294967296L ) )
                ^ MACHINEID;
        m_loTag = EPOCH + Math.abs(RANDOM.nextLong());
        m_uuid = toString(this.toByteArray());
    }

    /**
     * Overpass high order tag & low order tag convert to array bytes.
     * 
     *@return Array bytes
     */
    protected byte[] toByteArray(){
        byte[] bytes = new byte[16];
        int idx = 15;
        long val = m_loTag;
        for(int i = 0;i < 8;i ++ ){
            bytes[idx -- ] = (byte) (int) ( val & (long) 255 );
            val >>= 8;
        }
        val = m_hiTag;
        for(int i = 0;i < 8;i ++ ){
            bytes[idx -- ] = (byte) (int) ( val & (long) 255 );
            val >>= 8;
        }
        if(!this.isMD5()){
            return bytes;
        }else{
            return toMD5(bytes);
        }
    }

    /**
     * @param dest
     *            Object byte
     *@param b
     *            Source byte
     *@return Result byte
     */
    private static final byte setHiNibble(byte dest,int b){
        dest &= 0xf;
        dest |= ( (byte) b << 4 );
        return dest;
    }

    /**
     * @param dest
     *            Object byte
     *@param b
     *            Source byte
     *@return Result byte
     */
    private static final byte setLoNibble(byte dest,int b){
        dest &= 0xf0;
        dest |= ( (byte) b & 0xf );
        return dest;
    }

    /**
     * @param b
     *            Object byte
     *@return Result byte
     */
    private static final byte hiNibble(byte b){
        return (byte) ( ( b >> 4 ) & 0xf );
    }

    /**
     * @param b
     *            Object byte
     *@return Result byte
     */
    private static final byte loNibble(byte b){
        return (byte) ( b & 0xf );
    }
}
