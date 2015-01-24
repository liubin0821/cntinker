/*    */ package com.cntinker.json;
/*    */ 
/*    */ import java.io.StringWriter;
/*    */ 
/*    */ public class JSONStringer extends JSONWriter
/*    */ {
/*    */   public JSONStringer()
/*    */   {
/* 64 */     super(new StringWriter());
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return this.mode == 'd' ? this.writer.toString() : null;
/*    */   }
/*    */ }

/* Location:           C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar
 * Qualified Name:     org.json.JSONStringer
 * JD-Core Version:    0.6.0
 */