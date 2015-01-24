/*    */ package com.cntinker.json;
/*    */ 
/*    */ public class JSONException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private Throwable cause;
/*    */ 
/*    */   public JSONException(String message)
/*    */   {
/* 17 */     super(message);
/*    */   }
/*    */ 
/*    */   public JSONException(Throwable cause) {
/* 21 */     super(cause.getMessage());
/* 22 */     this.cause = cause;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 26 */     return this.cause;
/*    */   }
/*    */ }

/* Location:           C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar
 * Qualified Name:     org.json.JSONException
 * JD-Core Version:    0.6.0
 */