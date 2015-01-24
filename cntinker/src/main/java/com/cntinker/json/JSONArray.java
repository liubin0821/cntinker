/*     */package com.cntinker.json;

/*     */
/*     */import java.io.IOException;
/*     */
import java.io.Writer;
/*     */
import java.lang.reflect.Array;
/*     */
import java.util.ArrayList;
/*     */
import java.util.Collection;
/*     */
import java.util.Iterator;
/*     */
import java.util.Map;

/*     */
/*     */public class JSONArray
/*     */{
	/*     */private final ArrayList myArrayList;

	/*     */
	/*     */public JSONArray()
	/*     */{
		/* 94 */this.myArrayList = new ArrayList();
		/*     */}

	/*     */
	/*     */public JSONArray(JSONTokener x)
	/*     */throws JSONException
	/*     */{
		/* 103 */this();
		/* 104 */if (x.nextClean() != '[') {
			/* 105 */throw x
					.syntaxError("A JSONArray text must start with '['");
			/*     */}
		/* 107 */if (x.nextClean() != ']') {
			/* 108 */x.back();
			/*     */while (true) {
				/* 110 */if (x.nextClean() == ',') {
					/* 111 */x.back();
					/* 112 */this.myArrayList.add(JSONObject.NULL);
					/*     */} else {
					/* 114 */x.back();
					/* 115 */this.myArrayList.add(x.nextValue());
					/*     */}
				/* 117 */switch (x.nextClean()) {
				/*     */case ',':
					/*     */
				case ';':
					/* 120 */if (x.nextClean() == ']') {
						/* 121 */return;
						/*     */}
					/* 123 */x.back();
					/*     */case ']':
					/*     */}
				/*     */}}
		/*     */}

	/*     */
	/*     */public JSONArray(String source)
	/*     */throws JSONException
	/*     */{
		/* 143 */this(new JSONTokener(source));
		/*     */}

	/*     */
	/*     */public JSONArray(Collection collection)
	/*     */{
		/* 152 */this.myArrayList = new ArrayList();
		/* 153 */if (collection != null) {
			/* 154 */Iterator iter = collection.iterator();
			/* 155 */while (iter.hasNext())
				/* 156 */this.myArrayList.add(JSONObject.wrap(iter.next()));
			/*     */}
		/*     */}

	/*     */
	/*     */public JSONArray(Object array)
	/*     */throws JSONException
	/*     */{
		/* 167 */this();
		/* 168 */if (array.getClass().isArray()) {
			/* 169 */int length = Array.getLength(array);
			/* 170 */for (int i = 0; i < length; i++)
				/* 171 */put(JSONObject.wrap(Array.get(array, i)));
			/*     */}
		/*     */else {
			/* 174 */throw new JSONException(
					"JSONArray initial value should be a string or collection or array.");
			/*     */}
		/*     */}

	/*     */
	/*     */public Object get(int index)
	/*     */throws JSONException
	/*     */{
		/* 188 */Object object = opt(index);
		/* 189 */if (object == null) {
			/* 190 */throw new JSONException("JSONArray[" + index
					+ "] not found.");
			/*     */}
		/* 192 */return object;
		/*     */}

	/*     */
	/*     */public boolean getBoolean(int index)
	/*     */throws JSONException
	/*     */{
		/* 206 */Object object = get(index);
		/* 207 */if ((object.equals(Boolean.FALSE))
				|| (((object instanceof String)) && (((String) object)
						.equalsIgnoreCase("false"))))
		/*     */{
			/* 210 */return false;
			/* 211 */}
		if ((object.equals(Boolean.TRUE))
				|| (((object instanceof String)) && (((String) object)
						.equalsIgnoreCase("true"))))
		/*     */{
			/* 214 */return true;
			/*     */}
		/* 216 */throw new JSONException("JSONArray[" + index
				+ "] is not a boolean.");
		/*     */}

	/*     */
	/*     */public double getDouble(int index)
	/*     */throws JSONException
	/*     */{
		/* 229 */Object object = get(index);
		/*     */try {
			/* 231 */return (object instanceof Number) ? ((Number) object)
					.doubleValue() : Double.parseDouble((String) object);
			/*     */}
		/*     */catch (Exception e) {
			/*     */}
		/* 235 */throw new JSONException("JSONArray[" + index
				+ "] is not a number.");
		/*     */}

	/*     */
	/*     */public int getInt(int index)
	/*     */throws JSONException
	/*     */{
		/* 249 */Object object = get(index);
		/*     */try {
			/* 251 */return (object instanceof Number) ? ((Number) object)
					.intValue() : Integer.parseInt((String) object);
			/*     */}
		/*     */catch (Exception e) {
			/*     */}
		/* 255 */throw new JSONException("JSONArray[" + index
				+ "] is not a number.");
		/*     */}

	/*     */
	/*     */public JSONArray getJSONArray(int index)
	/*     */throws JSONException
	/*     */{
		/* 269 */Object object = get(index);
		/* 270 */if ((object instanceof JSONArray)) {
			/* 271 */return (JSONArray) object;
			/*     */}
		/* 273 */throw new JSONException("JSONArray[" + index
				+ "] is not a JSONArray.");
		/*     */}

	/*     */
	/*     */public JSONObject getJSONObject(int index)
	/*     */throws JSONException
	/*     */{
		/* 286 */Object object = get(index);
		/* 287 */if ((object instanceof JSONObject)) {
			/* 288 */return (JSONObject) object;
			/*     */}
		/* 290 */throw new JSONException("JSONArray[" + index
				+ "] is not a JSONObject.");
		/*     */}

	/*     */
	/*     */public long getLong(int index)
	/*     */throws JSONException
	/*     */{
		/* 304 */Object object = get(index);
		/*     */try {
			/* 306 */return (object instanceof Number) ? ((Number) object)
					.longValue() : Long.parseLong((String) object);
			/*     */}
		/*     */catch (Exception e) {
			/*     */}
		/* 310 */throw new JSONException("JSONArray[" + index
				+ "] is not a number.");
		/*     */}

	/*     */
	/*     */public String getString(int index)
	/*     */throws JSONException
	/*     */{
		/* 323 */Object object = get(index);
		/* 324 */if ((object instanceof String)) {
			/* 325 */return (String) object;
			/*     */}
		/* 327 */throw new JSONException("JSONArray[" + index
				+ "] not a string.");
		/*     */}

	/*     */
	/*     */public boolean isNull(int index)
	/*     */{
		/* 337 */return JSONObject.NULL.equals(opt(index));
		/*     */}

	/*     */
	/*     */public String join(String separator)
	/*     */throws JSONException
	/*     */{
		/* 350 */int len = length();
		/* 351 */StringBuffer sb = new StringBuffer();
		/*     */
		/* 353 */for (int i = 0; i < len; i++) {
			/* 354 */if (i > 0) {
				/* 355 */sb.append(separator);
				/*     */}
			/* 357 */sb
					.append(JSONObject.valueToString(this.myArrayList.get(i)));
			/*     */}
		/* 359 */return sb.toString();
		/*     */}

	/*     */
	/*     */public int length()
	/*     */{
		/* 369 */return this.myArrayList.size();
		/*     */}

	/*     */
	/*     */public Object opt(int index)
	/*     */{
		/* 380 */return (index < 0) || (index >= length()) ? null
				: this.myArrayList.get(index);
		/*     */}

	/*     */
	/*     */public boolean optBoolean(int index)
	/*     */{
		/* 395 */return optBoolean(index, false);
		/*     */}

	/*     */
	/*     */public boolean optBoolean(int index, boolean defaultValue)
	/*     */{
		/*     */try
		/*     */{
			/* 410 */return getBoolean(index);
		} catch (Exception e) {
			/*     */}
		/* 412 */return defaultValue;
		/*     */}

	/*     */
	/*     */public double optDouble(int index)
	/*     */{
		/* 426 */return optDouble(index, (0.0D / 0.0D));
		/*     */}

	/*     */
	/*     */public double optDouble(int index, double defaultValue)
	/*     */{
		/*     */try
		/*     */{
			/* 441 */return getDouble(index);
		} catch (Exception e) {
			/*     */}
		/* 443 */return defaultValue;
		/*     */}

	/*     */
	/*     */public int optInt(int index)
	/*     */{
		/* 457 */return optInt(index, 0);
		/*     */}

	/*     */
	/*     */public int optInt(int index, int defaultValue)
	/*     */{
		/*     */try
		/*     */{
			/* 471 */return getInt(index);
		} catch (Exception e) {
			/*     */}
		/* 473 */return defaultValue;
		/*     */}

	/*     */
	/*     */public JSONArray optJSONArray(int index)
	/*     */{
		/* 485 */Object o = opt(index);
		/* 486 */return (o instanceof JSONArray) ? (JSONArray) o : null;
		/*     */}

	/*     */
	/*     */public JSONObject optJSONObject(int index)
	/*     */{
		/* 499 */Object o = opt(index);
		/* 500 */return (o instanceof JSONObject) ? (JSONObject) o : null;
		/*     */}

	/*     */
	/*     */public long optLong(int index)
	/*     */{
		/* 513 */return optLong(index, 0L);
		/*     */}

	/*     */
	/*     */public long optLong(int index, long defaultValue)
	/*     */{
		/*     */try
		/*     */{
			/* 527 */return getLong(index);
		} catch (Exception e) {
			/*     */}
		/* 529 */return defaultValue;
		/*     */}

	/*     */
	/*     */public String optString(int index)
	/*     */{
		/* 543 */return optString(index, "");
		/*     */}

	/*     */
	/*     */public String optString(int index, String defaultValue)
	/*     */{
		/* 556 */Object object = opt(index);
		/* 557 */return JSONObject.NULL.equals(object) ? defaultValue : object
				.toString();
		/*     */}

	/*     */
	/*     */public JSONArray put(boolean value)
	/*     */{
		/* 570 */put(value ? Boolean.TRUE : Boolean.FALSE);
		/* 571 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(Collection value)
	/*     */{
		/* 582 */put(new JSONArray(value));
		/* 583 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(double value)
	/*     */throws JSONException
	/*     */{
		/* 595 */Double d = new Double(value);
		/* 596 */JSONObject.testValidity(d);
		/* 597 */put(d);
		/* 598 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int value)
	/*     */{
		/* 609 */put(new Integer(value));
		/* 610 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(long value)
	/*     */{
		/* 621 */put(new Long(value));
		/* 622 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(Map value)
	/*     */{
		/* 633 */put(new JSONObject(value));
		/* 634 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(Object value)
	/*     */{
		/* 646 */this.myArrayList.add(value);
		/* 647 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, boolean value)
	/*     */throws JSONException
	/*     */{
		/* 661 */put(index, value ? Boolean.TRUE : Boolean.FALSE);
		/* 662 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, Collection value)
	/*     */throws JSONException
	/*     */{
		/* 676 */put(index, new JSONArray(value));
		/* 677 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, double value)
	/*     */throws JSONException
	/*     */{
		/* 692 */put(index, new Double(value));
		/* 693 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, int value)
	/*     */throws JSONException
	/*     */{
		/* 707 */put(index, new Integer(value));
		/* 708 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, long value)
	/*     */throws JSONException
	/*     */{
		/* 722 */put(index, new Long(value));
		/* 723 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, Map value)
	/*     */throws JSONException
	/*     */{
		/* 737 */put(index, new JSONObject(value));
		/* 738 */return this;
		/*     */}

	/*     */
	/*     */public JSONArray put(int index, Object value)
	/*     */throws JSONException
	/*     */{
		/* 755 */JSONObject.testValidity(value);
		/* 756 */if (index < 0) {
			/* 757 */throw new JSONException("JSONArray[" + index
					+ "] not found.");
			/*     */}
		/* 759 */if (index < length()) {
			/* 760 */this.myArrayList.set(index, value);
			/*     */} else {
			/* 762 */while (index != length()) {
				/* 763 */put(JSONObject.NULL);
				/*     */}
			/* 765 */put(value);
			/*     */}
		/* 767 */return this;
		/*     */}

	/*     */
	/*     */public Object remove(int index)
	/*     */{
		/* 778 */Object o = opt(index);
		/* 779 */this.myArrayList.remove(index);
		/* 780 */return o;
		/*     */}

	/*     */
	/*     */public JSONObject toJSONObject(JSONArray names)
	/*     */throws JSONException
	/*     */{
		/* 794 */if ((names == null) || (names.length() == 0)
				|| (length() == 0)) {
			/* 795 */return null;
			/*     */}
		/* 797 */JSONObject jo = new JSONObject();
		/* 798 */for (int i = 0; i < names.length(); i++) {
			/* 799 */jo.put(names.getString(i), opt(i));
			/*     */}
		/* 801 */return jo;
		/*     */}

	/*     */
	/*     */public String toString()
	/*     */{
		/*     */try
		/*     */{
			/* 818 */return '[' + join(",") + ']';
		} catch (Exception e) {
			/*     */}
		/* 820 */return null;
		/*     */}

	/*     */
	/*     */public String toString(int indentFactor)
	/*     */throws JSONException
	/*     */{
		/* 837 */return toString(indentFactor, 0);
		/*     */}

	/*     */
	/*     */String toString(int indentFactor, int indent)
	/*     */throws JSONException
	/*     */{
		/* 852 */int len = length();
		/* 853 */if (len == 0) {
			/* 854 */return "[]";
			/*     */}
		/*     */
		/* 857 */StringBuffer sb = new StringBuffer("[");
		/* 858 */if (len == 1) {
			/* 859 */sb.append(JSONObject.valueToString(
					this.myArrayList.get(0), indentFactor, indent));
			/*     */}
		/*     */else {
			/* 862 */int newindent = indent + indentFactor;
			/* 863 */sb.append('\n');
			/* 864 */for (int i = 0; i < len; i++) {
				/* 865 */if (i > 0) {
					/* 866 */sb.append(",\n");
					/*     */}
				/* 868 */for (int j = 0; j < newindent; j++) {
					/* 869 */sb.append(' ');
					/*     */}
				/* 871 */sb.append(JSONObject.valueToString(
						this.myArrayList.get(i), indentFactor, newindent));
				/*     */}
			/*     */
			/* 874 */sb.append('\n');
			/* 875 */for (int i = 0; i < indent; i++) {
				/* 876 */sb.append(' ');
				/*     */}
			/*     */}
		/* 879 */sb.append(']');
		/* 880 */return sb.toString();
		/*     */}

	/*     */
	/*     */public Writer write(Writer writer)
	/*     */throws JSONException
	/*     */{
		/*     */try
		/*     */{
			/* 895 */boolean b = false;
			/* 896 */int len = length();
			/*     */
			/* 898 */writer.write(91);
			/*     */
			/* 900 */for (int i = 0; i < len; i++) {
				/* 901 */if (b) {
					/* 902 */writer.write(44);
					/*     */}
				/* 904 */Object v = this.myArrayList.get(i);
				/* 905 */if ((v instanceof JSONObject))
					/* 906 */((JSONObject) v).write(writer);
				/* 907 */else if ((v instanceof JSONArray))
					/* 908 */((JSONArray) v).write(writer);
				/*     */else {
					/* 910 */writer.write(JSONObject.valueToString(v));
					/*     */}
				/* 912 */b = true;
				/*     */}
			/* 914 */writer.write(93);
			/* 915 */return writer;
		} catch (IOException e) {
			throw new JSONException(e);
			/*     */}
		/* 917 */
		/*     */}
	/*     */
}

/*
 * Location: C:\Users\liuxiang\Desktop\org.json-chargebee-1.0.jar Qualified
 * Name: org.json.JSONArray JD-Core Version: 0.6.0
 */