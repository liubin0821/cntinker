/*     */ package com.cntinker.json;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class JSONML
/*     */ {
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja)
/*     */     throws JSONException
/*     */   {
/*  55 */     String closeTag = null;
/*     */ 
/*  57 */     JSONArray newja = null;
/*  58 */     JSONObject newjo = null;
/*     */ 
/*  60 */     String tagName = null;
/*     */     while (true)
/*     */     {
/*  69 */       if (!x.more()) {
/*  70 */         throw x.syntaxError("Bad XML");
/*     */       }
/*  72 */       Object token = x.nextContent();
/*  73 */       if (token == XML.LT) {
/*  74 */         token = x.nextToken();
/*  75 */         if ((token instanceof Character)) {
/*  76 */           if (token == XML.SLASH)
/*     */           {
/*  80 */             token = x.nextToken();
/*  81 */             if (!(token instanceof String)) {
/*  82 */               throw new JSONException("Expected a closing name instead of '" + token + "'.");
/*     */             }
/*     */ 
/*  86 */             if (x.nextToken() != XML.GT) {
/*  87 */               throw x.syntaxError("Misshaped close tag");
/*     */             }
/*  89 */             return token;
/*  90 */           }if (token == XML.BANG)
/*     */           {
/*  94 */             char c = x.next();
/*  95 */             if (c == '-') {
/*  96 */               if (x.next() == '-') {
/*  97 */                 x.skipPast("-->");
/*     */               }
/*  99 */               x.back(); continue;
/* 100 */             }if (c == '[') {
/* 101 */               token = x.nextToken();
/* 102 */               if ((token.equals("CDATA")) && (x.next() == '[')) {
/* 103 */                 if (ja != null) {
/* 104 */                   ja.put(x.nextCDATA()); continue;
/*     */                 }
/*     */               }
/* 107 */               throw x.syntaxError("Expected 'CDATA['");
/*     */             }
/*     */ 
/* 110 */             int i = 1;
/*     */             do {
/* 112 */               token = x.nextMeta();
/* 113 */               if (token == null)
/* 114 */                 throw x.syntaxError("Missing '>' after '<!'.");
/* 115 */               if (token == XML.LT)
/* 116 */                 i++;
/* 117 */               else if (token == XML.GT)
/* 118 */                 i--;
/*     */             }
/* 120 */             while (i > 0); continue;
/*     */           }
/* 122 */           if (token == XML.QUEST)
/*     */           {
/* 126 */             x.skipPast("?>"); continue;
/*     */           }
/* 128 */           throw x.syntaxError("Misshaped tag");
/*     */         }
/*     */ 
/* 134 */         if (!(token instanceof String)) {
/* 135 */           throw x.syntaxError("Bad tagName '" + token + "'.");
/*     */         }
/* 137 */         tagName = (String)token;
/* 138 */         newja = new JSONArray();
/* 139 */         newjo = new JSONObject();
/* 140 */         if (arrayForm) {
/* 141 */           newja.put(tagName);
/* 142 */           if (ja != null)
/* 143 */             ja.put(newja);
/*     */         }
/*     */         else {
/* 146 */           newjo.put("tagName", tagName);
/* 147 */           if (ja != null) {
/* 148 */             ja.put(newjo);
/*     */           }
/*     */         }
/* 151 */         token = null;
/*     */         while (true) {
/* 153 */           if (token == null) {
/* 154 */             token = x.nextToken();
/*     */           }
/* 156 */           if (token == null) {
/* 157 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 159 */           if (!(token instanceof String))
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 165 */           String attribute = (String)token;
/* 166 */           if ((!arrayForm) && (("tagName".equals(attribute)) || ("childNode".equals(attribute)))) {
/* 167 */             throw x.syntaxError("Reserved attribute.");
/*     */           }
/* 169 */           token = x.nextToken();
/* 170 */           if (token == XML.EQ) {
/* 171 */             token = x.nextToken();
/* 172 */             if (!(token instanceof String)) {
/* 173 */               throw x.syntaxError("Missing value");
/*     */             }
/* 175 */             newjo.accumulate(attribute, XML.stringToValue((String)token));
/* 176 */             token = null; continue;
/*     */           }
/* 178 */           newjo.accumulate(attribute, "");
/*     */         }
/*     */ 
/* 181 */         if ((arrayForm) && (newjo.length() > 0)) {
/* 182 */           newja.put(newjo);
/*     */         }
/*     */ 
/* 187 */         if (token == XML.SLASH) {
/* 188 */           if (x.nextToken() != XML.GT) {
/* 189 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 191 */           if (ja == null) {
/* 192 */             if (arrayForm) {
/* 193 */               return newja;
/*     */             }
/* 195 */             return newjo;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 202 */         if (token != XML.GT) {
/* 203 */           throw x.syntaxError("Misshaped tag");
/*     */         }
/* 205 */         closeTag = (String)parse(x, arrayForm, newja);
/* 206 */         if (closeTag != null) {
/* 207 */           if (!closeTag.equals(tagName)) {
/* 208 */             throw x.syntaxError("Mismatched '" + tagName + "' and '" + closeTag + "'");
/*     */           }
/*     */ 
/* 211 */           tagName = null;
/* 212 */           if ((!arrayForm) && (newja.length() > 0)) {
/* 213 */             newjo.put("childNodes", newja);
/*     */           }
/* 215 */           if (ja == null) {
/* 216 */             if (arrayForm) {
/* 217 */               return newja;
/*     */             }
/* 219 */             return newjo;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 226 */       if (ja != null)
/* 227 */         ja.put((token instanceof String) ? XML.stringToValue((String)token) : token);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static JSONArray toJSONArray(String string)
/*     */     throws JSONException
/*     */   {
/* 249 */     return toJSONArray(new XMLTokener(string));
/*     */   }
/*     */ 
/*     */   public static JSONArray toJSONArray(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 266 */     return (JSONArray)parse(x, true, null);
/*     */   }
/*     */ 
/*     */   public static JSONObject toJSONObject(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 284 */     return (JSONObject)parse(x, false, null);
/*     */   }
/*     */ 
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/* 302 */     return toJSONObject(new XMLTokener(string));
/*     */   }
/*     */ 
/*     */   public static String toString(JSONArray ja)
/*     */     throws JSONException
/*     */   {
/* 319 */     StringBuffer sb = new StringBuffer();
/*     */ 
/* 325 */     String tagName = ja.getString(0);
/* 326 */     XML.noSpace(tagName);
/* 327 */     tagName = XML.escape(tagName);
/* 328 */     sb.append('<');
/* 329 */     sb.append(tagName);
/*     */ 
/* 331 */     Object object = ja.opt(1);
/* 332 */     if ((object instanceof JSONObject)) {
/* 333 */       int i = 2;
/* 334 */       JSONObject jo = (JSONObject)object;
/*     */ 
/* 338 */       Iterator keys = jo.keys();
/* 339 */       while (keys.hasNext()) {
/* 340 */         String key = keys.next().toString();
/* 341 */         XML.noSpace(key);
/* 342 */         String value = jo.optString(key);
/* 343 */         if (value != null) {
/* 344 */           sb.append(' ');
/* 345 */           sb.append(XML.escape(key));
/* 346 */           sb.append('=');
/* 347 */           sb.append('"');
/* 348 */           sb.append(XML.escape(value));
/* 349 */           sb.append('"');
/*     */         }
/*     */       }
/*     */     }
/* 353 */     int i = 1;
/*     */ 
/* 358 */     int length = ja.length();
/* 359 */     if (i >= length) {
/* 360 */       sb.append('/');
/* 361 */       sb.append('>');
/*     */     } else {
/* 363 */       sb.append('>');
/*     */       do {
/* 365 */         object = ja.get(i);
/* 366 */         i++;
/* 367 */         if (object != null) {
/* 368 */           if ((object instanceof String))
/* 369 */             sb.append(XML.escape(object.toString()));
/* 370 */           else if ((object instanceof JSONObject))
/* 371 */             sb.append(toString((JSONObject)object));
/* 372 */           else if ((object instanceof JSONArray))
/* 373 */             sb.append(toString((JSONArray)object));
/*     */         }
/*     */       }
/* 376 */       while (i < length);
/* 377 */       sb.append('<');
/* 378 */       sb.append('/');
/* 379 */       sb.append(tagName);
/* 380 */       sb.append('>');
/*     */     }
/* 382 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String toString(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 395 */     StringBuffer sb = new StringBuffer();
/*     */ 
/* 407 */     String tagName = jo.optString("tagName");
/* 408 */     if (tagName == null) {
/* 409 */       return XML.escape(jo.toString());
/*     */     }
/* 411 */     XML.noSpace(tagName);
/* 412 */     tagName = XML.escape(tagName);
/* 413 */     sb.append('<');
/* 414 */     sb.append(tagName);
/*     */ 
/* 418 */     Iterator keys = jo.keys();
/* 419 */     while (keys.hasNext()) {
/* 420 */       String key = keys.next().toString();
/* 421 */       if ((!"tagName".equals(key)) && (!"childNodes".equals(key))) {
/* 422 */         XML.noSpace(key);
/* 423 */         String value = jo.optString(key);
/* 424 */         if (value != null) {
/* 425 */           sb.append(' ');
/* 426 */           sb.append(XML.escape(key));
/* 427 */           sb.append('=');
/* 428 */           sb.append('"');
/* 429 */           sb.append(XML.escape(value));
/* 430 */           sb.append('"');
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 437 */     JSONArray ja = jo.optJSONArray("childNodes");
/* 438 */     if (ja == null) {
/* 439 */       sb.append('/');
/* 440 */       sb.append('>');
/*     */     } else {
/* 442 */       sb.append('>');
/* 443 */       int length = ja.length();
/* 444 */       for (int i = 0; i < length; i++) {
/* 445 */         Object object = ja.get(i);
/* 446 */         if (object != null) {
/* 447 */           if ((object instanceof String))
/* 448 */             sb.append(XML.escape(object.toString()));
/* 449 */           else if ((object instanceof JSONObject))
/* 450 */             sb.append(toString((JSONObject)object));
/* 451 */           else if ((object instanceof JSONArray))
/* 452 */             sb.append(toString((JSONArray)object));
/*     */           else {
/* 454 */             sb.append(object.toString());
/*     */           }
/*     */         }
/*     */       }
/* 458 */       sb.append('<');
/* 459 */       sb.append('/');
/* 460 */       sb.append(tagName);
/* 461 */       sb.append('>');
/*     */     }
/* 463 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar
 * Qualified Name:     org.json.JSONML
 * JD-Core Version:    0.6.0
 */