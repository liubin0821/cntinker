package com.cntinker.uuid;


public class UIDNotSupportException extends ClassNotFoundException{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Description of the Parameter
     */
    public UIDNotSupportException(String s){
        super(s,null);
        // Disallow initCause
    }
}
