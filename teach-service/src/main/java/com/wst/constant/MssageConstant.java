/*
 * MssageConstant.java Created on 2017年10月12日 上午10:39:40
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
 * 消息枚举
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class MssageConstant {

	/**
	 * 消息类型
	 * 
	 * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
	 * @version 1.0
	 */
	public enum UserMssageTypeEnmu {
		/**
		 * 系统消息
		 */
		SYSTEM_MSSAGE(1, "系统消息"),

		/**
		 * 课程消息
		 */
		CUSTOM_MSSAGE(2, "课程消息"),

		/**
		 * 订单消息
		 */
		ORDER_MSSAGE(3, "订单消息");

		/**
		 * 类型
		 */
		public int type;

		/**
		 * 名称
		 */
		public String name;

		private UserMssageTypeEnmu(int type, String name) {
			this.type = type;
			this.name = name;
		}
	}

	/**
	 * 消息类型
	 * 
	 * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
	 * @version 1.0
	 */
	public enum TutorMssageTypeEnmu {
		/**
		 * 系统消息
		 */
		SYSTEM_MSSAGE(1, "系统消息"),

		/**
		 * 课程消息
		 */
		CUSTOM_MSSAGE(2, "课程消息"),

		/**
		 * 结算消息
		 */
		SETTLE_MSSAGE(3, "结算消息");

		/**
		 * 类型
		 */
		public int type;

		/**
		 * 名称
		 */
		public String name;

		private TutorMssageTypeEnmu(int type, String name) {
			this.type = type;
			this.name = name;
		}
	}

	/**
	 * 消息类状态
	 * 
	 * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
	 * @version 1.0
	 */
	public enum MssageStatusEnmu {
		/**
		 * 有效
		 */
		VALID(1, "有效"),

		/**
		 * 无效
		 */
		INVALID(2, "无效");

		/**
		 * 类型
		 */
		public int status;

		/**
		 * 名称
		 */
		public String name;

		private MssageStatusEnmu(int status, String name) {
			this.status = status;
			this.name = name;
		}
	}

	/**
	 * 消息业务类型
	 * 
	 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
	 * @version 1.0
	 */
	public enum MssageBusiTypeEnum {
		/**
		 * 21.网课
		 */
		PUBLIC_BUSI_TYPE(21, "网课"),

		/**
		 * 22.定制课
		 */
		CUSTOM_BUSI_TYPE(22, "定制课");

		/**
		 * 类型
		 */
		public int type;

		/**
		 * 名称
		 */
		public String name;

		private MssageBusiTypeEnum(int type, String name) {
			this.type = type;
			this.name = name;
		}
	}

}
