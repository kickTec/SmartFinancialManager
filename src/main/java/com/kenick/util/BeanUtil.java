package com.kenick.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class BeanUtil extends BeanUtilsBean
{
	 private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	 /**
	  * <一句话功能简述> bean 复制方法
	  * <功能详细描述>
	  * author: user
	  * 创建时间:  2018年4月6日
	  * @param dest
	  * @param orig
	  * @param isCopyNull 是否复制空字段
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @see [类、类#方法、类#成员]
	  */
	 public void copyProperties(final Object dest, final Object orig ,boolean isCopyNull)
		        throws IllegalAccessException, InvocationTargetException {

        // Validate existence of the specified beans
        if (dest == null) {
            throw new IllegalArgumentException
                    ("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }
        if (logger.isDebugEnabled()) {
        	logger.debug("BeanUtils.copyProperties(" + dest + ", " +
                      orig + ")");
        }

        // Copy the properties, converting as necessary
        if (orig instanceof DynaBean) {
            final DynaProperty[] origDescriptors =
                ((DynaBean) orig).getDynaClass().getDynaProperties();
            for (DynaProperty origDescriptor : origDescriptors) {
                final String name = origDescriptor.getName();
                // Need to check isReadable() for WrapDynaBean
                // (see Jira issue# BEANUTILS-61)
                if (getPropertyUtils().isReadable(orig, name) &&
                    getPropertyUtils().isWriteable(dest, name)) {
                    final Object value = ((DynaBean) orig).get(name);
                    
                    // 定制空是否保存
                    if (isCopyNull)
                    {
                    	copyProperty(dest, name, value);
                    }else if (null != value)
                	{
                    	copyProperty(dest, name, value);
                	}
                }
            }
        } else if (orig instanceof Map) {
            @SuppressWarnings("unchecked")
            final
            // Map properties are always of type <String, Object>
            Map<String, Object> propMap = (Map<String, Object>) orig;
            for (final Map.Entry<String, Object> entry : propMap.entrySet()) {
                final String name = entry.getKey();
                if (getPropertyUtils().isWriteable(dest, name)) {
                	 Object value = entry.getValue();
                	 // 定制空是否保存
                     if (isCopyNull)
                     {
                     	copyProperty(dest, name, value);
                     }else if (null != value)
                 	{
                     	copyProperty(dest, name, value);
                 	}
                }
            }
        } else /* if (orig is a standard JavaBean) */ {
            final PropertyDescriptor[] origDescriptors =
                getPropertyUtils().getPropertyDescriptors(orig);
            for (PropertyDescriptor origDescriptor : origDescriptors) {
                final String name = origDescriptor.getName();
                if ("class".equals(name)) {
                    continue; // No point in trying to set an object's class
                }
                if (getPropertyUtils().isReadable(orig, name) &&
                    getPropertyUtils().isWriteable(dest, name)) {
                    try {
                        final Object value =
                            getPropertyUtils().getSimpleProperty(orig, name);
                        
                        // 定制空是否保存
                        if (isCopyNull)
                        {
                        	copyProperty(dest, name, value);
                        }else if (null != value)
                    	{
                        	copyProperty(dest, name, value);
                    	}
                    } catch (final NoSuchMethodException e) {
                        // Should not happen
                    }
                }
            }
        }

    }
	 

	/**
	 * <一句话功能简述>比较两个对象的属性，返回属性不一样的属性字段map
	 * <功能详细描述>
	 * @param oldObj
	 * @param newObj
	 * @return
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 * @author  liuml
	 * 修改时间:2018年11月27日 下午3:56:03
	 * @param <T>
	 */
	public static <T> ArrayList<String> beanCompare(T oldObj, T newObj) throws Exception {

		ArrayList<String> array = new ArrayList<>();
		if (oldObj == null || newObj == null) {
			return array;
		}
		
		// 获取某个类的所有声明的字段 包括public、private和proteced，但是不包括父类的申明字段
		Field[] fs = oldObj.getClass().getDeclaredFields();for (Field f : fs) {
			f.setAccessible(true);// 设置用反射时可以访问私有变量
			Object v1 = f.get(oldObj);
			Object v2 = f.get(newObj);
			if (!equals(v1, v2)) {
				array.add(f.getName());
			}
		}

		return array;
	}

	public void copyPropertiesNew(Object dest, Object orig) throws Exception{
		ConvertUtils.register(new org.apache.commons.beanutils.converters.DateConverter(null), java.util.Date.class);
		ConvertUtils.register(new org.apache.commons.beanutils.converters.BigDecimalConverter(null), BigDecimal.class);
		BeanUtils.copyProperties(dest, orig);
	}

	public static boolean equals(Object obj1, Object obj2) {

		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}
	
}
