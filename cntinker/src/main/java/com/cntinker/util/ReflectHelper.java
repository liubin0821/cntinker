/**
 * 2012-5-17 上午5:08:04
 */
package com.cntinker.util;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: bin_liu
 */
public class ReflectHelper {

	public static String makeToString(Object obj)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		StringBuffer sb = new StringBuffer();
		sb.append(obj.getClass().getName()).append("[");

		String[] var = getAllvarName(obj.getClass());
		for (String e : var) {
			sb.append("|").append(e).append(":")
					.append(getValue(obj, e) == null ? "" : getValue(obj, e));
		}
		sb.append("]");
		return sb.toString();
	}

	public static String[] getAllvarName(Class clazz) {
		List<String> res = new ArrayList<String>();
		Field[] f = clazz.getDeclaredFields();
		for (Field e : f) {
			res.add(e.getName());
		}
		return (String[]) res.toArray(new String[0]);
	}

	public static Object getValue(Object obj, String paramer)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String methodName = "get" + paramer;

		Method m = getMethod(obj.getClass(), methodName);
		if (m == null) {
			return null;
		}
		return m.invoke(obj, null);
	}

	/**
	 * 获取指定类的第一个参数里的类
	 *
	 * @param serviceImplClass
	 * @return
	 */
	public static Class getActualTypeArgumentClass(Class serviceImplClass) {
		return getActualTypeArgumentClass(serviceImplClass, 0, 0);
	}

	public static Class getActualTypeArgumentClass(Class serviceImplClass, int indexParameter) {
		Type type = serviceImplClass.getGenericInterfaces()[0];
		ParameterizedType p = (ParameterizedType) type;
		Class c = (Class) p.getActualTypeArguments()[indexParameter];
		return c;
	}

	public static Class getActualTypeArgumentClass(Class serviceImplClass, int indexInteface, int indexParameter) {
		Type type = serviceImplClass.getGenericInterfaces()[indexInteface];
		ParameterizedType p = (ParameterizedType) type;
		Class c = (Class) p.getActualTypeArguments()[indexParameter];
		return c;
	}


	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = Maps.newHashMap();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	/**
	 * 获取所有get方法
	 *
	 * @param obj
	 * @return List<Method>
	 */
	public static List<Method> getAllGetMethod(Object obj) {
		Method[] methods = obj.getClass().getMethods();
		List<Method> res = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get") && !methods[i].getName().equals("getClass")) {
				res.add(methods[i]);
			}
		}
		return res;
	}

	/**
	 * 获取所有set方法
	 *
	 * @param obj
	 * @return List<Method>
	 */
	public static List<Method> getAllSetMethod(Object obj) {
		Method[] methods = obj.getClass().getMethods();
		List<Method> res = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("set")) {
				res.add(methods[i]);
			}
		}
		return res;
	}

	/**
	 * 得到一个类的指定名称函数
	 *
	 * @param theClass
	 * @param name
	 * @return Method
	 */
	public static Method getMethod(Class theClass, String name) {
		return cn.hutool.core.util.ReflectUtil.getMethodOfObj(theClass, name);
	}

	/**
	 * 获取一个变量的注解成员的值
	 *
	 * @param clazz
	 * @param annotationClazz
	 * @param var
	 * @param memberName
	 * @param <T>
	 * @return
	 */
	public static <T> T getValueByAnnotationByVar(Class clazz, Class annotationClazz, Field var, String memberName) {
		Annotation t = var.getAnnotation(annotationClazz);
		if (t == null) {
			return null;
		}
		return getValueByAnnotationByClass(t, memberName);
	}

	/**
	 * 获取一个变量的注解成员的值
	 *
	 * @param clazz
	 * @param annotationClazz
	 * @param var
	 * @param memberName
	 * @param <T>
	 * @return
	 */
	public static <T> T getValueByAnnotationByVar(Class clazz, Class annotationClazz, String var, String memberName) {
		Field varField = ReflectUtil.getField(clazz, var);
		if (varField == null) {
			return null;
		}
		Annotation t = varField.getAnnotation(annotationClazz);
		return getValueByAnnotationByClass(t, memberName);
	}

	/**
	 * 获取一个类的注解成员的值
	 *
	 * @param clazz
	 * @param annotationClazz
	 * @param memberName
	 * @param <T>
	 * @return
	 */
	public static <T> T getValueByAnnotationByClass(Class clazz, Class annotationClazz, String memberName) {
		Annotation t = clazz.getAnnotation(annotationClazz);
		return getValueByAnnotationByClass(t, memberName);
	}

	private static <T> T getValueByAnnotationByClass(Annotation t, String memberName) {
		if (t == null || StringUtils.isEmpty(memberName)) {
			return null;
		}
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(t);
		Field value = null;
		try {
			value = invocationHandler.getClass().getDeclaredField("memberValues");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
		if (value == null) {
			return null;
		}
		value.setAccessible(true);
		Map<String, Object> memberValues = null;
		try {
			Object temp = value.get(invocationHandler);
			if (temp == null) {
				return null;
			}
			memberValues = (Map<String, Object>) temp;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		Object obj = memberValues.get(memberName);
		return obj == null ? null : (T) obj;
	}

	/**
	 * 返回k=bean变量名,v=别名<br>
	 * 得到注解，如果没有就返回变量名<br>
	 * 顺序:变量注解>方法注解>变量名<br>
	 *
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getAnnotationVars(Class clazz, boolean hasAnnotation) {
		String[] vars = getAllvarName(clazz, false, false, hasAnnotation);
		Map<String, String> res = new HashMap<>();
		for (String e : vars) {
			String anno = getAnnotationOrVar(clazz, e);
			if (StringUtils.isEmpty(anno)) {
				continue;
			}
			res.put(e, anno);
		}
		return res;
	}

	/**
	 * 得到注解，如果没有就返回变量名,为动态sql用<br>
	 * 顺序:变量注解>方法注解>变量名<br>
	 *
	 * @param clazz
	 * @return String[]
	 */
	private static String getAnnotationOrVar(Class clazz, String varName) {
		List<String> res = new ArrayList<String>();
		Field[] f = clazz.getDeclaredFields();
		for (Field e : f) {
			String var = e.getName();
			if (!var.equals(varName)) {
				continue;
			}
			// 变量注解
			Column c = e.getAnnotation(Column.class);
			if (c != null && !StringHelper.isNull(c.name())) {
				return c.name();
			}
			// 对应的get方法
			String getMenthodName = "get" + var.substring(0, 1).toUpperCase()
					+ var.substring(1);
			Method m = getMethod(clazz, getMenthodName);
			if (m != null) {
				// 对应注解
				Column c2 = m.getAnnotation(Column.class);
				if (c2 != null && !StringHelper.isNull(c2.name())) {
					return c2.name();
				}
			}
			return var;
		}
		return varName;
	}

	public static Class getVarObject(Class clazz, String varName) {
		Field[] lst = clazz.getDeclaredFields();
		for (Field e : lst) {
			if (e.getName().equals(varName)) {
				return e.getType();
			}
		}
		return null;
	}

	public static String[] getAllvarName(Class clazz, boolean hasStatic, boolean hasPublic, boolean hasAnnotation) {
		List<String> res = new ArrayList<String>();
		Field[] f = clazz.getDeclaredFields();
		for (Field e : f) {
			if (!hasStatic) {
				if (Modifier.isStatic(e.getModifiers())) {
					continue;
				}
			}
			if (!hasPublic) {
				if (Modifier.isPublic(e.getModifiers())) {
					continue;
				}
			}
			if (hasAnnotation) {
				Column c = e.getAnnotation(Column.class);
				if (c == null) {
					continue;
				}
			}
			res.add(e.getName());
		}

		return (String[]) res.toArray(new String[0]);
	}

}
