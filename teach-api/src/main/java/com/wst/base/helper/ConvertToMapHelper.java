/*
 * ConvertToMapUtil.java Created on 2016年9月27日 下午1:20:06
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.base.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;

/**
 * 对象转换map工具类
 * 
 * @author <a href="mailto:chenl@hiwitech.com">chenliang</a>
 * @version 1.0
 */
public class ConvertToMapHelper {
	private static HiwiLog logger = HiwiLogFactory.getLogger(ConvertToMapHelper.class);

	/***
	 * 转换对象为map 指定转换参数 和定义参数名称
	 * 
	 * @param o
	 * @param keys
	 *            结果map的key
	 * @param attrNames
	 *            DTO对象属性名
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> convertToMap(Object obj, String[] keys, String[] attrNames) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		if (CommonFuntions.isEmptyObject(obj)) {
			logger.warn("convertToMap方法 传入obj对象为空！");
			return null;
		}
		if (CommonFuntions.isEmptyObject(keys)) {
			logger.warn("convertToMap方法  传入keys对象为空！");
			return null;
		}
		if (CommonFuntions.isEmptyObject(attrNames)) {
			logger.warn("convertToMap方法  传入attrNames对象为空！");
			return null;
		}
		if (keys.length != attrNames.length) {
			logger.warn("convertToMap方法  传入keys attrNames 对象长度不一致！");

			return null;
		}
		for (int i = 0; i < keys.length; i++) {
			String attr = attrNames[i];
			// 根据 对象的属性名称 获取属性的值 放入结果map key 为attr 对应的key值
			result.put(keys[i], getFieldValueByName(attr, obj));
		}
		return result;
	}

	/**
	 * 根据属性名获取属性值
	 */
	private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		Method method = o.getClass().getMethod(getter, new Class[] {});
		Object value = method.invoke(o, new Object[] {});
		return value;
	}
}
