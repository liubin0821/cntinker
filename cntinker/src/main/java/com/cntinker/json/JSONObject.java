/*      */package com.cntinker.json;

/*      */
/*      */import java.io.IOException;
/*      */
import java.io.Writer;
/*      */
import java.lang.reflect.Field;
/*      */
import java.lang.reflect.Method;
/*      */
import java.lang.reflect.Modifier;
/*      */
import java.util.Collection;
/*      */
import java.util.Enumeration;
/*      */
import java.util.HashMap;
/*      */
import java.util.Iterator;
import java.util.LinkedHashMap;
/*      */
import java.util.Locale;
/*      */
import java.util.Map;
/*      */
import java.util.Map.Entry;
/*      */
import java.util.ResourceBundle;
/*      */
import java.util.Set;
import java.util.TreeMap;

/*      */
/*      */public class JSONObject
/*      */{
	/*      */private final Map map;
	public static final Object NULL = new Null();

	/*      */
	/*      */public JSONObject()
	/*      */{
		/* 147 */this.map = new LinkedHashMap();
		/*      */}

	/*      */
	/*      */public JSONObject(JSONObject jo, String[] names)
	/*      */{
		/* 161 */this();
		/* 162 */for (int i = 0; i < names.length; i++)
			/*      */try {
				/* 164 */putOnce(names[i], jo.opt(names[i]));
				/*      */}
			/*      */catch (Exception ignore)
			/*      */{
				/*      */}
		/*      */}

	/*      */
	/*      */public JSONObject(JSONTokener x)
	/*      */throws JSONException
	/*      */{
		/* 178 */this();
		/*      */
		/* 182 */if (x.nextClean() != '{')
			/* 183 */throw x
					.syntaxError("A JSONObject text must begin with '{'");
		/*      */while (true)
		/*      */{
			/* 186 */char c = x.nextClean();
			/* 187 */switch (c) {
			/*      */case '\000':
				/* 189 */throw x
						.syntaxError("A JSONObject text must end with '}'");
				/*      */case '}':
				/* 191 */return;
				/*      */}
			/* 193 */x.back();
			/* 194 */String key = x.nextValue().toString();
			/*      */
			/* 199 */c = x.nextClean();
			/* 200 */if (c == '=') {
				/* 201 */if (x.next() != '>')
					/* 202 */x.back();
				/*      */}
			/* 204 */else if (c != ':') {
				/* 205 */throw x.syntaxError("Expected a ':' after a key");
				/*      */}
			/* 207 */putOnce(key, x.nextValue());
			/*      */
			/* 211 */switch (x.nextClean()) {
			/*      */case ',':
				/*      */
			case ';':
				/* 214 */if (x.nextClean() == '}') {
					/* 215 */return;
					/*      */}
				/* 217 */x.back();
				/*      */case '}':
				/*      */}
			/*      */}
		/* 220 */
		/*      */
		/* 222 */
		/*      */}

	/*      */
	/*      */public JSONObject(Map map)
	/*      */{
		/* 236 */this.map = new LinkedHashMap();
		/* 237 */if (map != null) {
			/* 238 */Iterator i = map.entrySet().iterator();
			/* 239 */while (i.hasNext()) {
				/* 240 */Map.Entry e = (Map.Entry) i.next();
				/* 241 */Object value = e.getValue();
				/* 242 */if (value != null)
					/* 243 */this.map.put(e.getKey(), wrap(value));
				/*      */}
			/*      */}
		/*      */}

	/*      */
	/*      */public JSONObject(Object bean)
	/*      */{
		/* 270 */this();
		/* 271 */populateMap(bean);
		/*      */}

	/*      */
	/*      */public JSONObject(Object object, String[] names)
	/*      */{
		/* 287 */this();
		/* 288 */Class c = object.getClass();
		/* 289 */for (int i = 0; i < names.length; i++) {
			/* 290 */String name = names[i];
			/*      */try {
				/* 292 */putOpt(name, c.getField(name).get(object));
				/*      */}
			/*      */catch (Exception ignore)
			/*      */{
				/*      */}
			/*      */}
		/*      */}

	/*      */
	/*      */public JSONObject(String source)
	/*      */throws JSONException
	/*      */{
		/* 309 */this(new JSONTokener(source));
		/*      */}

	/*      */
	/*      */public JSONObject(String baseName, Locale locale)
	/*      */throws JSONException
	/*      */{
		/* 320 */this();
		/* 321 */ResourceBundle bundle = ResourceBundle.getBundle(baseName,
				locale, Thread.currentThread().getContextClassLoader());
		/*      */
		/* 326 */Enumeration keys = bundle.getKeys();
		/* 327 */while (keys.hasMoreElements()) {
			/* 328 */Object key = keys.nextElement();
			/* 329 */if ((key instanceof String))
			/*      */{
				/* 335 */String[] path = ((String) key).split("\\.");
				/* 336 */int last = path.length - 1;
				/* 337 */JSONObject target = this;
				/* 338 */for (int i = 0; i < last; i++) {
					/* 339 */String segment = path[i];
					/* 340 */JSONObject nextTarget = target
							.optJSONObject(segment);
					/* 341 */if (nextTarget == null) {
						/* 342 */nextTarget = new JSONObject();
						/* 343 */target.put(segment, nextTarget);
						/*      */}
					/* 345 */target = nextTarget;
					/*      */}
				/* 347 */target.put(path[last], bundle.getString((String) key));
				/*      */}
			/*      */}
		/*      */}

	/*      */
	/*      */public JSONObject accumulate(String key, Object value)
	/*      */throws JSONException
	/*      */{
		/* 373 */testValidity(value);
		/* 374 */Object object = opt(key);
		/* 375 */if (object == null) {
			/* 376 */put(key,
					(value instanceof JSONArray) ? new JSONArray().put(value)
							: value);
			/*      */}
		/* 379 */else if ((object instanceof JSONArray))
			/* 380 */((JSONArray) object).put(value);
		/*      */else {
			/* 382 */put(key, new JSONArray().put(object).put(value));
			/*      */}
		/* 384 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject append(String key, Object value)
	/*      */throws JSONException
	/*      */{
		/* 400 */testValidity(value);
		/* 401 */Object object = opt(key);
		/* 402 */if (object == null)
			/* 403 */put(key, new JSONArray().put(value));
		/* 404 */else if ((object instanceof JSONArray))
			/* 405 */put(key, ((JSONArray) object).put(value));
		/*      */else {
			/* 407 */throw new JSONException("JSONObject[" + key
					+ "] is not a JSONArray.");
			/*      */}
		/*      */
		/* 410 */return this;
		/*      */}

	/*      */
	/*      */public static String doubleToString(double d)
	/*      */{
		/* 421 */if ((Double.isInfinite(d)) || (Double.isNaN(d))) {
			/* 422 */return "null";
			/*      */}
		/*      */
		/* 427 */String string = Double.toString(d);
		/* 428 */if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0)
				&& (string.indexOf('E') < 0))
		/*      */{
			/* 430 */while (string.endsWith("0")) {
				/* 431 */string = string.substring(0, string.length() - 1);
				/*      */}
			/* 433 */if (string.endsWith(".")) {
				/* 434 */string = string.substring(0, string.length() - 1);
				/*      */}
			/*      */}
		/* 437 */return string;
		/*      */}

	/*      */
	/*      */public Object get(String key)
	/*      */throws JSONException
	/*      */{
		/* 449 */if (key == null) {
			/* 450 */throw new JSONException("Null key.");
			/*      */}
		/* 452 */Object object = opt(key);
		/* 453 */if (object == null) {
			/* 454 */throw new JSONException("JSONObject[" + quote(key)
					+ "] not found.");
			/*      */}
		/*      */
		/* 457 */return object;
		/*      */}

	/*      */
	/*      */public boolean getBoolean(String key)
	/*      */throws JSONException
	/*      */{
		/* 470 */Object object = get(key);
		/* 471 */if ((object.equals(Boolean.FALSE))
				|| (((object instanceof String)) && (((String) object)
						.equalsIgnoreCase("false"))))
		/*      */{
			/* 474 */return false;
			/* 475 */}
		if ((object.equals(Boolean.TRUE))
				|| (((object instanceof String)) && (((String) object)
						.equalsIgnoreCase("true"))))
		/*      */{
			/* 478 */return true;
			/*      */}
		/* 480 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a Boolean.");
		/*      */}

	/*      */
	/*      */public double getDouble(String key)
	/*      */throws JSONException
	/*      */{
		/* 493 */Object object = get(key);
		/*      */try {
			/* 495 */return (object instanceof Number) ? ((Number) object)
					.doubleValue() : Double.parseDouble((String) object);
			/*      */}
		/*      */catch (Exception e) {
			/*      */}
		/* 499 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a number.");
		/*      */}

	/*      */
	/*      */public int getInt(String key)
	/*      */throws JSONException
	/*      */{
		/* 514 */Object object = get(key);
		/*      */try {
			/* 516 */return (object instanceof Number) ? ((Number) object)
					.intValue() : Integer.parseInt((String) object);
			/*      */}
		/*      */catch (Exception e) {
			/*      */}
		/* 520 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not an int.");
		/*      */}

	/*      */
	/*      */public JSONArray getJSONArray(String key)
	/*      */throws JSONException
	/*      */{
		/* 535 */Object object = get(key);
		/* 536 */if ((object instanceof JSONArray)) {
			/* 537 */return (JSONArray) object;
			/*      */}
		/* 539 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a JSONArray.");
		/*      */}

	/*      */
	/*      */public JSONObject getJSONObject(String key)
	/*      */throws JSONException
	/*      */{
		/* 553 */Object object = get(key);
		/* 554 */if ((object instanceof JSONObject)) {
			/* 555 */return (JSONObject) object;
			/*      */}
		/* 557 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a JSONObject.");
		/*      */}

	/*      */
	/*      */public long getLong(String key)
	/*      */throws JSONException
	/*      */{
		/* 571 */Object object = get(key);
		/*      */try {
			/* 573 */return (object instanceof Number) ? ((Number) object)
					.longValue() : Long.parseLong((String) object);
			/*      */}
		/*      */catch (Exception e) {
			/*      */}
		/* 577 */throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a long.");
		/*      */}

	/*      */
	/*      */public static String[] getNames(JSONObject jo)
	/*      */{
		/* 589 */int length = jo.length();
		/* 590 */if (length == 0) {
			/* 591 */return null;
			/*      */}
		/* 593 */Iterator iterator = jo.keys();
		/* 594 */String[] names = new String[length];
		/* 595 */int i = 0;
		/* 596 */while (iterator.hasNext()) {
			/* 597 */names[i] = ((String) iterator.next());
			/* 598 */i++;
			/*      */}
		/* 600 */return names;
		/*      */}

	/*      */
	/*      */public static String[] getNames(Object object)
	/*      */{
		/* 610 */if (object == null) {
			/* 611 */return null;
			/*      */}
		/* 613 */Class klass = object.getClass();
		/* 614 */Field[] fields = klass.getFields();
		/* 615 */int length = fields.length;
		/* 616 */if (length == 0) {
			/* 617 */return null;
			/*      */}
		/* 619 */String[] names = new String[length];
		/* 620 */for (int i = 0; i < length; i++) {
			/* 621 */names[i] = fields[i].getName();
			/*      */}
		/* 623 */return names;
		/*      */}

	/*      */
	/*      */public String getString(String key)
	/*      */throws JSONException
	/*      */{
		/* 635 */Object object = get(key);
		/* 636 */if ((object instanceof String)) {
			/* 637 */return (String) object;
			/*      */}
		/* 639 */throw new JSONException("JSONObject[" + quote(key)
				+ "] not a string.");
		/*      */}

	/*      */
	/*      */public boolean has(String key)
	/*      */{
		/* 650 */return this.map.containsKey(key);
		/*      */}

	/*      */
	/*      */public JSONObject increment(String key)
	/*      */throws JSONException
	/*      */{
		/* 664 */Object value = opt(key);
		/* 665 */if (value == null)
			/* 666 */put(key, 1);
		/* 667 */else if ((value instanceof Integer))
			/* 668 */put(key, ((Integer) value).intValue() + 1);
		/* 669 */else if ((value instanceof Long))
			/* 670 */put(key, ((Long) value).longValue() + 1L);
		/* 671 */else if ((value instanceof Double))
			/* 672 */put(key, ((Double) value).doubleValue() + 1.0D);
		/* 673 */else if ((value instanceof Float))
			/* 674 */put(key, ((Float) value).floatValue() + 1.0F);
		/*      */else {
			/* 676 */throw new JSONException("Unable to increment ["
					+ quote(key) + "].");
			/*      */}
		/* 678 */return this;
		/*      */}

	/*      */
	/*      */public boolean isNull(String key)
	/*      */{
		/* 690 */return NULL.equals(opt(key));
		/*      */}

	/*      */
	/*      */public Iterator keys()
	/*      */{
		/* 700 */return this.map.keySet().iterator();
		/*      */}

	/*      */
	/*      */public int length()
	/*      */{
		/* 710 */return this.map.size();
		/*      */}

	/*      */
	/*      */public JSONArray names()
	/*      */{
		/* 721 */JSONArray ja = new JSONArray();
		/* 722 */Iterator keys = keys();
		/* 723 */while (keys.hasNext()) {
			/* 724 */ja.put(keys.next());
			/*      */}
		/* 726 */return ja.length() == 0 ? null : ja;
		/*      */}

	/*      */
	/*      */public static String numberToString(Number number)
	/*      */throws JSONException
	/*      */{
		/* 737 */if (number == null) {
			/* 738 */throw new JSONException("Null pointer");
			/*      */}
		/* 740 */testValidity(number);
		/*      */
		/* 744 */String string = number.toString();
		/* 745 */if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0)
				&& (string.indexOf('E') < 0))
		/*      */{
			/* 747 */while (string.endsWith("0")) {
				/* 748 */string = string.substring(0, string.length() - 1);
				/*      */}
			/* 750 */if (string.endsWith(".")) {
				/* 751 */string = string.substring(0, string.length() - 1);
				/*      */}
			/*      */}
		/* 754 */return string;
		/*      */}

	/*      */
	/*      */public Object opt(String key)
	/*      */{
		/* 764 */return key == null ? null : this.map.get(key);
		/*      */}

	/*      */
	/*      */public boolean optBoolean(String key)
	/*      */{
		/* 777 */return optBoolean(key, false);
		/*      */}

	/*      */
	/*      */public boolean optBoolean(String key, boolean defaultValue)
	/*      */{
		/*      */try
		/*      */{
			/* 792 */return getBoolean(key);
		} catch (Exception e) {
			/*      */}
		/* 794 */return defaultValue;
		/*      */}

	/*      */
	/*      */public double optDouble(String key)
	/*      */{
		/* 809 */return optDouble(key, (0.0D / 0.0D));
		/*      */}

	/*      */
	/*      */public double optDouble(String key, double defaultValue)
	/*      */{
		/*      */try
		/*      */{
			/* 825 */return getDouble(key);
		} catch (Exception e) {
			/*      */}
		/* 827 */return defaultValue;
		/*      */}

	/*      */
	/*      */public int optInt(String key)
	/*      */{
		/* 842 */return optInt(key, 0);
		/*      */}

	/*      */
	/*      */public int optInt(String key, int defaultValue)
	/*      */{
		/*      */try
		/*      */{
			/* 858 */return getInt(key);
		} catch (Exception e) {
			/*      */}
		/* 860 */return defaultValue;
		/*      */}

	/*      */
	/*      */public JSONArray optJSONArray(String key)
	/*      */{
		/* 874 */Object o = opt(key);
		/* 875 */return (o instanceof JSONArray) ? (JSONArray) o : null;
		/*      */}

	/*      */
	/*      */public JSONObject optJSONObject(String key)
	/*      */{
		/* 888 */Object object = opt(key);
		/* 889 */return (object instanceof JSONObject) ? (JSONObject) object
				: null;
		/*      */}

	/*      */
	/*      */public long optLong(String key)
	/*      */{
		/* 903 */return optLong(key, 0L);
		/*      */}

	/*      */
	/*      */public long optLong(String key, long defaultValue)
	/*      */{
		/*      */try
		/*      */{
			/* 919 */return getLong(key);
		} catch (Exception e) {
			/*      */}
		/* 921 */return defaultValue;
		/*      */}

	/*      */
	/*      */public String optString(String key)
	/*      */{
		/* 935 */return optString(key, "");
		/*      */}

	/*      */
	/*      */public String optString(String key, String defaultValue)
	/*      */{
		/* 948 */Object object = opt(key);
		/* 949 */return NULL.equals(object) ? defaultValue : object.toString();
		/*      */}

	/*      */
	/*      */private void populateMap(Object bean)
	/*      */{
		/* 954 */Class klass = bean.getClass();
		/*      */
		/* 958 */boolean includeSuperClass = klass.getClassLoader() != null;
		/*      */
		/* 960 */Method[] methods = includeSuperClass ? klass.getMethods()
				: klass.getDeclaredMethods();
		/*      */
		/* 963 */for (int i = 0; i < methods.length; i++)
			/*      */try {
				/* 965 */Method method = methods[i];
				/* 966 */if (Modifier.isPublic(method.getModifiers())) {
					/* 967 */String name = method.getName();
					/* 968 */String key = "";
					/* 969 */if (name.startsWith("get")) {
						/* 970 */if (("getClass".equals(name))
								|| ("getDeclaringClass".equals(name)))
						/*      */{
							/* 972 */key = "";
							/*      */}
						/* 974 */else
							key = name.substring(3);
						/*      */}
					/* 976 */else if (name.startsWith("is")) {
						/* 977 */key = name.substring(2);
						/*      */}
					/* 979 */if ((key.length() > 0)
							&& (Character.isUpperCase(key.charAt(0)))
							&& (method.getParameterTypes().length == 0))
					/*      */{
						/* 982 */if (key.length() == 1)
							/* 983 */key = key.toLowerCase();
						/* 984 */else if (!Character.isUpperCase(key.charAt(1))) {
							/* 985 */key = key.substring(0, 1).toLowerCase()
									+ key.substring(1);
							/*      */}
						/*      */
						/* 989 */Object result = method.invoke(bean,
								(Object[]) null);
						/* 990 */if (result != null)
							/* 991 */this.map.put(key, wrap(result));
						/*      */}
					/*      */}
				/*      */}
			/*      */catch (Exception ignore)
			/*      */{
				/*      */}
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, boolean value)
	/*      */throws JSONException
	/*      */{
		/* 1010 */put(key, value ? Boolean.TRUE : Boolean.FALSE);
		/* 1011 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, Collection value)
	/*      */throws JSONException
	/*      */{
		/* 1024 */put(key, new JSONArray(value));
		/* 1025 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, double value)
	/*      */throws JSONException
	/*      */{
		/* 1038 */put(key, new Double(value));
		/* 1039 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, int value)
	/*      */throws JSONException
	/*      */{
		/* 1052 */put(key, new Integer(value));
		/* 1053 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, long value)
	/*      */throws JSONException
	/*      */{
		/* 1066 */put(key, new Long(value));
		/* 1067 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, Map value)
	/*      */throws JSONException
	/*      */{
		/* 1080 */put(key, new JSONObject(value));
		/* 1081 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject put(String key, Object value)
	/*      */throws JSONException
	/*      */{
		/* 1097 */if (key == null) {
			/* 1098 */throw new JSONException("Null key.");
			/*      */}
		/* 1100 */if (value != null) {
			/* 1101 */testValidity(value);
			/* 1102 */this.map.put(key, value);
			/*      */} else {
			/* 1104 */remove(key);
			/*      */}
		/* 1106 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject putOnce(String key, Object value)
	/*      */throws JSONException
	/*      */{
		/* 1120 */if ((key != null) && (value != null)) {
			/* 1121 */if (opt(key) != null) {
				/* 1122 */throw new JSONException("Duplicate key \"" + key
						+ "\"");
				/*      */}
			/* 1124 */put(key, value);
			/*      */}
		/* 1126 */return this;
		/*      */}

	/*      */
	/*      */public JSONObject putOpt(String key, Object value)
	/*      */throws JSONException
	/*      */{
		/* 1141 */if ((key != null) && (value != null)) {
			/* 1142 */put(key, value);
			/*      */}
		/* 1144 */return this;
		/*      */}

	/*      */
	/*      */public static String quote(String string)
	/*      */{
		/* 1157 */if ((string == null) || (string.length() == 0)) {
			/* 1158 */return "\"\"";
			/*      */}
		/*      */
		/* 1162 */char c = '\000';
		/*      */
		/* 1165 */int len = string.length();
		/* 1166 */StringBuffer sb = new StringBuffer(len + 4);
		/*      */
		/* 1168 */sb.append('"');
		/* 1169 */for (int i = 0; i < len; i++) {
			/* 1170 */char b = c;
			/* 1171 */c = string.charAt(i);
			/* 1172 */switch (c) {
			/*      */case '"':
				/*      */
			case '\\':
				/* 1175 */sb.append('\\');
				/* 1176 */sb.append(c);
				/* 1177 */break;
			/*      */case '/':
				/* 1179 */if (b == '<') {
					/* 1180 */sb.append('\\');
					/*      */}
				/* 1182 */sb.append(c);
				/* 1183 */break;
			/*      */case '\b':
				/* 1185 */sb.append("\\b");
				/* 1186 */break;
			/*      */case '\t':
				/* 1188 */sb.append("\\t");
				/* 1189 */break;
			/*      */case '\n':
				/* 1191 */sb.append("\\n");
				/* 1192 */break;
			/*      */case '\f':
				/* 1194 */sb.append("\\f");
				/* 1195 */break;
			/*      */case '\r':
				/* 1197 */sb.append("\\r");
				/* 1198 */break;
			/*      */default:
				/* 1200 */if ((c < ' ') || ((c >= '') && (c < ' '))
						|| ((c >= ' ') && (c < '℀')))
				/*      */{
					/* 1202 */String hhhh = "000" + Integer.toHexString(c);
					/* 1203 */sb.append("\\u"
							+ hhhh.substring(hhhh.length() - 4));
					/*      */} else {
					/* 1205 */sb.append(c);
					/*      */}
				/*      */}
			/*      */}
		/* 1209 */sb.append('"');
		/* 1210 */return sb.toString();
		/*      */}

	/*      */
	/*      */public Object remove(String key)
	/*      */{
		/* 1220 */return this.map.remove(key);
		/*      */}

	/*      */
	/*      */public static Object stringToValue(String string)
	/*      */{
		/* 1231 */if (string.equals("")) {
			/* 1232 */return string;
			/*      */}
		/* 1234 */if (string.equalsIgnoreCase("true")) {
			/* 1235 */return Boolean.TRUE;
			/*      */}
		/* 1237 */if (string.equalsIgnoreCase("false")) {
			/* 1238 */return Boolean.FALSE;
			/*      */}
		/* 1240 */if (string.equalsIgnoreCase("null")) {
			/* 1241 */return NULL;
			/*      */}
		/*      */
		/* 1252 */char b = string.charAt(0);
		/* 1253 */if (((b >= '0') && (b <= '9')) || (b == '.') || (b == '-')
				|| (b == '+')) {
			Double d;
			/*      */try {
				if ((string.indexOf('.') > -1) || (string.indexOf('e') > -1)
						|| (string.indexOf('E') > -1))
				/*      */{
					/* 1257 */d = Double.valueOf(string);
					/* 1258 */if ((!d.isInfinite()) && (!d.isNaN()))
						/* 1259 */return d;
					/*      */}
				/*      */else {
					/* 1262 */Long myLong = new Long(string);
					/* 1263 */if (myLong.longValue() == myLong.intValue()) {
						/* 1264 */return new Integer(myLong.intValue());
						/*      */}
					/* 1266 */return myLong;
					/*      */}
				/*      */} catch (Exception ignore)
			/*      */{
				/*      */}
			/*      */}
		/* 1272 */return string;
		/*      */}

	/*      */
	/*      */public static void testValidity(Object o)
	/*      */throws JSONException
	/*      */{
		/* 1282 */if (o != null)
			/* 1283 */if ((o instanceof Double)) {
				/* 1284 */if ((((Double) o).isInfinite())
						|| (((Double) o).isNaN())) {
					/* 1285 */throw new JSONException(
							"JSON does not allow non-finite numbers.");
					/*      */}
				/*      */}
			/* 1288 */else if (((o instanceof Float)) && (
			/* 1289 */(((Float) o).isInfinite()) || (((Float) o).isNaN())))
				/* 1290 */throw new JSONException(
						"JSON does not allow non-finite numbers.");
		/*      */}

	/*      */
	/*      */public JSONArray toJSONArray(JSONArray names)
	/*      */throws JSONException
	/*      */{
		/* 1307 */if ((names == null) || (names.length() == 0)) {
			/* 1308 */return null;
			/*      */}
		/* 1310 */JSONArray ja = new JSONArray();
		/* 1311 */for (int i = 0; i < names.length(); i++) {
			/* 1312 */ja.put(opt(names.getString(i)));
			/*      */}
		/* 1314 */return ja;
		/*      */}

	/*      */
	/*      */public String toString()
	/*      */{
		/*      */try
		/*      */{
			/* 1331 */Iterator keys = keys();
			/* 1332 */StringBuffer sb = new StringBuffer("{");
			/*      */
			/* 1334 */while (keys.hasNext()) {
				/* 1335 */if (sb.length() > 1) {
					/* 1336 */sb.append(',');
					/*      */}
				/* 1338 */Object o = keys.next();
				/* 1339 */sb.append(quote(o.toString()));
				/* 1340 */sb.append(':');
				/* 1341 */sb.append(valueToString(this.map.get(o)));
				/*      */}
			/* 1343 */sb.append('}');
			/* 1344 */return sb.toString();
		} catch (Exception e) {
			/*      */}
		/* 1346 */return null;
		/*      */}

	/*      */
	/*      */public String toString(int indentFactor)
	/*      */throws JSONException
	/*      */{
		/* 1364 */return toString(indentFactor, 0);
		/*      */}

	/*      */
	/*      */String toString(int indentFactor, int indent)
	/*      */throws JSONException
	/*      */{
		/* 1383 */int length = length();
		/* 1384 */if (length == 0) {
			/* 1385 */return "{}";
			/*      */}
		/* 1387 */Iterator keys = keys();
		/* 1388 */int newindent = indent + indentFactor;
		/*      */
		/* 1390 */StringBuffer sb = new StringBuffer("{");
		/* 1391 */if (length == 1) {
			/* 1392 */Object object = keys.next();
			/* 1393 */sb.append(quote(object.toString()));
			/* 1394 */sb.append(": ");
			/* 1395 */sb.append(valueToString(this.map.get(object),
					indentFactor, indent));
			/*      */}
		/*      */else {
			/* 1398 */while (keys.hasNext()) {
				/* 1399 */Object object = keys.next();
				/* 1400 */if (sb.length() > 1)
					/* 1401 */sb.append(",\n");
				/*      */else {
					/* 1403 */sb.append('\n');
					/*      */}
				/* 1405 */for (int i = 0; i < newindent; i++) {
					/* 1406 */sb.append(' ');
					/*      */}
				/* 1408 */sb.append(quote(object.toString()));
				/* 1409 */sb.append(": ");
				/* 1410 */sb.append(valueToString(this.map.get(object),
						indentFactor, newindent));
				/*      */}
			/*      */
			/* 1413 */if (sb.length() > 1) {
				/* 1414 */sb.append('\n');
				/* 1415 */for (int i = 0; i < indent; i++) {
					/* 1416 */sb.append(' ');
					/*      */}
				/*      */}
			/*      */}
		/* 1420 */sb.append('}');
		/* 1421 */return sb.toString();
		/*      */}

	/*      */
	/*      */public static String valueToString(Object value)
	/*      */throws JSONException
	/*      */{
		/* 1447 */if ((value == null) || (value.equals(null))) {
			/* 1448 */return "null";
			/*      */}
		/* 1450 */if ((value instanceof JSONString)) {
			/*      */Object object;
			/*      */try {
				object = ((JSONString) value).toJSONString();
				/*      */} catch (Exception e) {
				/* 1455 */throw new JSONException(e);
				/*      */}
			/* 1457 */if ((object instanceof String)) {
				/* 1458 */return (String) object;
				/*      */}
			/* 1460 */throw new JSONException("Bad value from toJSONString: "
					+ object);
			/*      */}
		/* 1462 */if ((value instanceof Number)) {
			/* 1463 */return numberToString((Number) value);
			/*      */}
		/* 1465 */if (((value instanceof Boolean))
				|| ((value instanceof JSONObject))
				|| ((value instanceof JSONArray)))
		/*      */{
			/* 1467 */return value.toString();
			/*      */}
		/* 1469 */if ((value instanceof Map)) {
			/* 1470 */return new JSONObject((Map) value).toString();
			/*      */}
		/* 1472 */if ((value instanceof Collection)) {
			/* 1473 */return new JSONArray((Collection) value).toString();
			/*      */}
		/* 1475 */if (value.getClass().isArray()) {
			/* 1476 */return new JSONArray(value).toString();
			/*      */}
		/* 1478 */return quote(value.toString());
		/*      */}

	/*      */
	/*      */static String valueToString(Object value, int indentFactor, int indent)
	/*      */throws JSONException
	/*      */{
		/* 1501 */if ((value == null) || (value.equals(null)))
			/* 1502 */return "null";
		/*      */try
		/*      */{
			/* 1505 */if ((value instanceof JSONString)) {
				/* 1506 */Object o = ((JSONString) value).toJSONString();
				/* 1507 */if ((o instanceof String))
					/* 1508 */return (String) o;
				/*      */}
			/*      */}
		/*      */catch (Exception ignore) {
			/*      */}
		/* 1513 */if ((value instanceof Number)) {
			/* 1514 */return numberToString((Number) value);
			/*      */}
		/* 1516 */if ((value instanceof Boolean)) {
			/* 1517 */return value.toString();
			/*      */}
		/* 1519 */if ((value instanceof JSONObject)) {
			/* 1520 */return ((JSONObject) value)
					.toString(indentFactor, indent);
			/*      */}
		/* 1522 */if ((value instanceof JSONArray)) {
			/* 1523 */return ((JSONArray) value).toString(indentFactor, indent);
			/*      */}
		/* 1525 */if ((value instanceof Map)) {
			/* 1526 */return new JSONObject((Map) value).toString(indentFactor,
					indent);
			/*      */}
		/* 1528 */if ((value instanceof Collection)) {
			/* 1529 */return new JSONArray((Collection) value).toString(
					indentFactor, indent);
			/*      */}
		/* 1531 */if (value.getClass().isArray()) {
			/* 1532 */return new JSONArray(value)
					.toString(indentFactor, indent);
			/*      */}
		/* 1534 */return quote(value.toString());
		/*      */}

	/*      */
	/*      */public static Object wrap(Object object)
	/*      */{
		/*      */try
		/*      */{
			/* 1552 */if (object == null) {
				/* 1553 */return NULL;
				/*      */}
			/* 1555 */if (((object instanceof JSONObject))
					|| ((object instanceof JSONArray)) || (NULL.equals(object))
					|| ((object instanceof JSONString))
					|| ((object instanceof Byte))
					|| ((object instanceof Character))
					|| ((object instanceof Short))
					|| ((object instanceof Integer))
					|| ((object instanceof Long))
					|| ((object instanceof Boolean))
					|| ((object instanceof Float))
					|| ((object instanceof Double))
					|| ((object instanceof String)))
			/*      */{
				/* 1562 */return object;
				/*      */}
			/*      */
			/* 1565 */if ((object instanceof Collection)) {
				/* 1566 */return new JSONArray((Collection) object);
				/*      */}
			/* 1568 */if (object.getClass().isArray()) {
				/* 1569 */return new JSONArray(object);
				/*      */}
			/* 1571 */if ((object instanceof Map)) {
				/* 1572 */return new JSONObject((Map) object);
				/*      */}
			/* 1574 */Package objectPackage = object.getClass().getPackage();
			/* 1575 */String objectPackageName = objectPackage != null ? objectPackage
					.getName() : "";
			/*      */
			/* 1578 */if ((objectPackageName.startsWith("java."))
					|| (objectPackageName.startsWith("javax."))
					|| (object.getClass().getClassLoader() == null))
			/*      */{
				/* 1583 */return object.toString();
				/*      */}
			/* 1585 */return new JSONObject(object);
		} catch (Exception exception) {
			/*      */}
		/* 1587 */return null;
		/*      */}

	/*      */
	/*      */public Writer write(Writer writer)
	/*      */throws JSONException
	/*      */{
		/*      */try
		/*      */{
			/* 1603 */boolean commanate = false;
			/* 1604 */Iterator keys = keys();
			/* 1605 */writer.write(123);
			/*      */
			/* 1607 */while (keys.hasNext()) {
				/* 1608 */if (commanate) {
					/* 1609 */writer.write(44);
					/*      */}
				/* 1611 */Object key = keys.next();
				/* 1612 */writer.write(quote(key.toString()));
				/* 1613 */writer.write(58);
				/* 1614 */Object value = this.map.get(key);
				/* 1615 */if ((value instanceof JSONObject))
					/* 1616 */((JSONObject) value).write(writer);
				/* 1617 */else if ((value instanceof JSONArray))
					/* 1618 */((JSONArray) value).write(writer);
				/*      */else {
					/* 1620 */writer.write(valueToString(value));
					/*      */}
				/* 1622 */commanate = true;
				/*      */}
			/* 1624 */writer.write(125);
			/* 1625 */return writer;
		} catch (IOException exception) {
			throw new JSONException(exception);
			/*      */}
		/* 1627 */
		/*      */}

	/*      */
	private static final class Null {

        /**
         * There is only intended to be a single instance of the NULL object,
         * so the clone method returns itself.
         *
         * @return NULL.
         */
        @Override
        protected final Object clone() {
            return this;
        }

        /**
         * A Null object is equal to the null value and to itself.
         *
         * @param object
         *            An object to test for nullness.
         * @return true if the object parameter is the JSONObject.NULL object or
         *         null.
         */
        @Override
        public boolean equals(Object object) {
            return object == null || object == this;
        }

        /**
         * Get the "null" string value.
         *
         * @return The string "null".
         */
        public String toString() {
            return "null";
        }
    }
	/*      */
}

/*
 * Location: C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar Qualified
 * Name: org.json.JSONObject JD-Core Version: 0.6.0
 */