package com.cntinker.uuid;


public final class GUID extends UUID{
    private long m_count = 0;

    private GUID(){
        next();
    }

    protected void next(){
        m_hiTag = ( EPOCH + ( JVMHASH * 4294967296L ) ) ^ MACHINEID;
        m_loTag = ( EPOCH * MAX_LONG ) + ( ++ m_count );
        m_uuid = toString(toByteArray());
    }
}
