/*    */ package com.cntinker.json;
/*    */ 
/*    */ public class HTTPTokener extends JSONTokener
/*    */ {
/*    */   public HTTPTokener(String string)
/*    */   {
/* 40 */     super(string);
/*    */   }
/*    */ 
/*    */   public String nextToken() throws JSONException {
/* 52 */     StringBuffer sb = new StringBuffer();
/*    */     char c;
/*    */     do c = next();
/* 55 */     while (Character.isWhitespace(c));
/* 56 */     if ((c == '"') || (c == '\'')) {
/* 57 */       char q = c;
/*    */       while (true) {
/* 59 */         c = next();
/* 60 */         if (c < ' ') {
/* 61 */           throw syntaxError("Unterminated string.");
/*    */         }
/* 63 */         if (c == q) {
/* 64 */           return sb.toString();
/*    */         }
/* 66 */         sb.append(c);
/*    */       }
/*    */     }
/*    */     while (true) {
/* 70 */       if ((c == 0) || (Character.isWhitespace(c))) {
/* 71 */         return sb.toString();
/*    */       }
/* 73 */       sb.append(c);
/* 74 */       c = next();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar
 * Qualified Name:     org.json.HTTPTokener
 * JD-Core Version:    0.6.0
 */