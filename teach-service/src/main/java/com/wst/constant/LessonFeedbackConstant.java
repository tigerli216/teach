/*
 * LessonFeedbackConstant.java Created on 2017年10月31日 下午3:05:36
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.constant;

/**
 * 用户课程反馈枚举
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class LessonFeedbackConstant {
	/**
	 * 课程类别
	 * 
	 * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
	 * @version 1.0
	 */
	public enum LessonTypeEnmu {
		/**
		 * 定制课
		 */
		CUSTOM(1, "定制课"),

		/**
		 * 网课
		 */
		PUBLIC(2, "网课");

		/**
		 * 类型
		 */
		public int type;

		/**
		 * 名称
		 */
		public String name;

		private LessonTypeEnmu(int type, String name) {
			this.type = type;
			this.name = name;
		}
	}
}
