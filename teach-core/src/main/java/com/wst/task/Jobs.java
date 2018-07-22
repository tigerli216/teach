/*
 * Jobs.java Created on 2017年10月18日 下午6:20:06
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
package com.wst.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.constant.RedisConstant;
import com.wst.constant.UserConstant.UserLevelTypeEnmu;
import com.wst.entity.lesson.CustomClassVideo;
import com.wst.lesson.dao.PublicClassDao;
import com.wst.mem.dao.UserDao;
import com.wst.order.dao.OrderDao;
import com.wst.order.dao.PaymentDao;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.mem.service.MssageService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.pay.service.PayResultService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 定时作业管理
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Component
public class Jobs {

	private static HiwiLog log = HiwiLogFactory.getLogger(Jobs.class);

	@Resource
	private OrderDao orderDao;

	@Resource
	private PaymentDao paymentDao;

	@Resource
	private PayResultService payResultService;

	@Resource
	private CustomClassService customClassService;

	@Resource
	private MssageService mssageService;

	@Resource
	private PublicClassDao publicClassDao;

	@Resource
	private TxVideoService txVideoService;

	@Resource
	private UserDao userDao;

	/**
	 * 2小时未支付订单关闭任务（网课）
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class ClosePayJob implements SimpleJob {

		@Override
		public void execute(ShardingContext context) {
			long begin = System.currentTimeMillis();
			log.debug("------------2小时未支付订单关闭任务执行------------");

			try {
				Calendar beginCalendar = Calendar.getInstance();
				beginCalendar.add(Calendar.HOUR_OF_DAY, -5);// 5小时前

				Calendar endCalendar = Calendar.getInstance();
				endCalendar.add(Calendar.HOUR_OF_DAY, -2);// 2小时前

				OrderDTO orderQuery = new OrderDTO();
				orderQuery.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);
				orderQuery.setPayStatus(PayStatusEnum.NO_PAY.status);
				orderQuery.setPayType(OrderPayTypeEnum.ON_PAY.type);
				orderQuery.setBeginTime(beginCalendar.getTime());
				orderQuery.setEndTime(endCalendar.getTime());
				orderQuery.setOrderType(OrderTypeEnmu.PUBLIC_CLASS_ORDER.type);
				List<OrderDTO> orderList = orderDao.findOrderPaging(null, orderQuery).getList();
				if (orderList != null && orderList.size() > 0) {
					for (OrderDTO order : orderList) {
						try {
							ResultDTO<String> resultStr = payResultService.closeOrder(order.getOrderCode());
							if (resultStr.isSuccess()) {
								mssageService.baseSendMssage(order.getUserId(), order.getUserId(), order.getOrderCode(),
										1, 14);
							}
						} catch (Exception e) {
							log.error("------------2小时未支付订单关闭任务异常，orderCode=" + order.getOrderCode());
						}
					}
				}
			} catch (Exception e) {
				log.error("------------2小时未支付订单关闭任务,异常：" + e);
			}
			log.debug("------------2小时未支付订单关闭任务执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}

	}

	/**
	 * 72小时未确认课时任务
	 * 
	 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
	 * @version 1.0
	 */
	class CustomClass72HourNotConfirm implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------72小时未确认课时任务执行开始------------");

			try {
				// 查询72小时未确认课时
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassByNHourNotConfirm(72);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							ResultDTO<String> result = customClassService
									.confirmAttendClass(customClassDTO.getClassId(), customClassDTO.getUserId(), 2);
							if (!result.isSuccess()) {
								log.error("72小时未确认课时,确认失败，失败原因：" + result.getErrDesc() + ",classID="
										+ customClassDTO.getClassId());
							}
							// 消息已经在确认课时发送过,这里不需要再次发送,发件人和收件人参数还写反了
							// mssageService.baseSendMssage(customClassDTO.getTutorId(),
							// customClassDTO.getUserId(),
							// customClassDTO.getLessonId() + "", 2, 7);
						} catch (Exception e) {
							log.error("------------72小时未确认课时任务异常，classID=" + customClassDTO.getClassId(), e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------72小时未确认课时任务,异常：", e);
			}

			log.debug("------------72小时未确认课时任务执行结束，耗时：" + (System.currentTimeMillis() - begin));

			/*
			 * begin = System.currentTimeMillis();
			 * log.debug("------------24小时未确认课时提醒学生任务执行开始------------");
			 * 
			 * try { // 24小时未确认课时 List<CustomClassDTO> custoClassList =
			 * customClassService.findCustomClassByNHourNotConfirm(24); if
			 * (custoClassList != null && custoClassList.size() > 0) { for
			 * (CustomClassDTO customClassDTO : custoClassList) { long lessonId
			 * = customClassDTO.getLessonId(); long classId =
			 * customClassDTO.getClassId(); try { String key = "Message_13_1_" +
			 * lessonId + "_" + classId; if (!RedisClient.exists(key)) {
			 * mssageService.baseSendMssage(customClassDTO.getTutorId(),
			 * customClassDTO.getUserId(), customClassDTO.getLessonId() + "", 1,
			 * 13); log.info("24小时未确认课时提醒学生推送消息,课时ID=" + classId); // 一天发送一次
			 * RedisClient.set(key, key); RedisClient.expire(key, 86400); } }
			 * catch (Exception e) {
			 * log.error("------------24小时未确认课时提醒学生任务异常，classID=" +
			 * customClassDTO.getClassId(), e); } } } } catch (Exception e) {
			 * log.error("------------24小时未确认课时提醒学生任务,异常：", e); }
			 * 
			 * log.debug("------------24小时未确认课时提醒学生任务执行结束，耗时：" +
			 * (System.currentTimeMillis() - begin));
			 */
		}
	}

	/**
	 * 离计划上课还有2天未约课推送消息
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class CustomClassPlanTime2Day implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------离计划上课还有2天未约课推送消息任务执行开始------------");

			try {
				// 离计划上课时长还有2天的课时推送消息
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByPlanTimeNDay(2);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 18);
							log.info("离计划上课还有2天未约课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------离计划上课还有2天未约课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------离计划上课还有2天未约课推送消息,异常：", e);
			}
			log.debug("------------离计划上课还有2天未约课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));

			begin = System.currentTimeMillis();
			log.debug("------------计划上课当天还未约课推送消息任务执行开始------------");

			try {
				// 计划上课当天还未约课推送消息
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByPlanTimeNDay(0);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 10);
							log.info("计划上课当天还未约课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------计划上课当天还未约课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------计划上课当天还未约课推送消息,异常：", e);
			}
			log.debug("------------计划上课当天还未约课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}

	}

	/**
	 * 2周未上课提醒导师推送消息
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class CustomClass2WeekNotClass implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------2周未上课提醒导师任务执行开始------------");

			try {
				// 查询上节课结束后，14天未再次上课
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByNDayNotClass(14);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 11);
							log.info("上节课结束后，14天未再次上课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------上节课结束后，14天未再次上课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：",
									e);
						}
					}
				}
				// 课程购买后，开始时间已过14天未上课
				custoClassList = customClassService.findCustomClassListByPayNDayNotClass(14);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 11);
							log.info("课程购买后，开始时间已过14天未上课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------课程购买后，开始时间已过14天未上课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：",
									e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------2周未上课提醒导师推送消息,异常：", e);
			}
			log.debug("------------2周未上课提醒导师推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 约课时间到前2小时提醒上课
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class CustomClassDataTime2Hour implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------约课时间到前2小时提醒上课任务执行开始------------");

			try {
				// 约课时间到前2小时提醒上课
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByDataTimeNHour(2);
				if (custoClassList == null || custoClassList.size() == 0) {
					log.info("------------没有约课时间到前2小时提醒上课数据------------");
					return;
				}

				for (CustomClassDTO customClassDTO : custoClassList) {
					long lessonId = customClassDTO.getLessonId();
					long classId = customClassDTO.getClassId();
					try {
						String key = "Message_12_2_" + lessonId + "_" + classId;
						if (!RedisClient.exists(key)) {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									lessonId + "", 2, 12);

							mssageService.baseSendMssage(customClassDTO.getTutorId(), customClassDTO.getUserId(),
									lessonId + "", 1, 12);
							log.info("约课时间到前2小时提醒上课推送消息,课时ID=" + classId);

							// 一天发送一次
							RedisClient.set(key, key);
							RedisClient.expire(key, 86400);
						}
					} catch (Exception e) {
						log.error("------------约课时间到前2小时提醒上课推送消息,课时ID=" + classId + ",异常：", e);
					}
				}
			} catch (Exception e) {
				log.error("------------约课时间到前2小时提醒上课推送消息,异常：", e);
			}

			log.debug("------------约课时间到前2小时提醒上课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 离上课还有2小时的直播课推送消息
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class PublicClassBeginTime2Hour implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------离上课还有2小时的直播课推送消息任务执行开始------------");

			try {
				// 离上课还有2小时的直播课推送消息
				List<PublicClassDTO> publicClassList = publicClassDao.findPublicClassListByNHourClass(2);
				if (publicClassList != null && publicClassList.size() > 0) {
					for (PublicClassDTO publicClassDTO : publicClassList) {
						long lessonId = publicClassDTO.getLessonId();
						long classId = publicClassDTO.getClassId();
						try {
							String key = "Message_15_0_" + lessonId + "_" + classId;
							if (!RedisClient.exists(key)) {
								mssageService.baseSendMssage(publicClassDTO.getUserId(), publicClassDTO.getTutorId(),
										publicClassDTO.getLessonId() + "", 2, 15);
								mssageService.baseSendMssage(publicClassDTO.getTutorId(), publicClassDTO.getUserId(),
										publicClassDTO.getLessonId() + "", 1, 15);
								log.info("离上课还有2小时的直播课推送消息,课时ID=" + classId);
								// 一天发送一次
								RedisClient.set(key, key);
								RedisClient.expire(key, 86400);
							}
						} catch (Exception e) {
							log.error("------------离上课还有2小时的直播课推送消息,课时ID=" + publicClassDTO.getClassId() + ",异常：", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------离上课还有2小时的直播课推送消息,异常：", e);
			}
			log.debug("------------离上课还有2小时的直播课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 异步从腾讯云拉取视频地址
	 * 
	 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
	 * @version 1.0
	 */
	class CustomClassVideoJob implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			try {
				List<CustomClassVideo> classVideos = customClassService.getPendingCustomClassVideo();
				if (classVideos == null || classVideos.size() == 0) {
					log.info("当前没有待拉取地址视频信息");
					return;
				}

				for (CustomClassVideo classVideo : classVideos) {
					ResultDTO<String[]> resultInfo = txVideoService.describeRecordPlayInfo(classVideo.getVid());
					if (!resultInfo.isSuccess() || resultInfo.getResult() == null) {
						continue;
					}

					String[] result = resultInfo.getResult();
					if (result.length == 1) { // 拉取地址失败
						classVideo.setDownloadTime(new Date());
						classVideo.setDownloadRemark(result[0]);

						customClassService.updateCustomClassVideoFail(classVideo);
					} else if (result.length == 2) { // 拉取地址成功
						classVideo.setVideoUrl(result[1]);
						classVideo.setDownloadTime(new Date());
						classVideo.setDownloadRemark(result[0]);
						classVideo.setDownloadNum(classVideo.getDownloadNum() + 1);

						customClassService.updateCustomClassVideoSuccess(classVideo);
					}
				}
			} catch (Exception e) {
				log.error("获取视频地址异常", e);
			}
		}
	}

	/**
	 * 超过计划上课2天未约课推送消息
	 * 
	 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
	 * @version 1.0
	 */
	class CustomClassAfterPlanTime2Day implements SimpleJob {

		@Override
		public void execute(ShardingContext shardingContext) {
			long begin = System.currentTimeMillis();
			log.debug("------------超过计划上课1天未约课推送消息任务执行开始------------");

			try {
				// 超过计划上课1天未约课推送消息
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByPlanTimeNDay(-1);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 19);
							log.info("超过计划上课2周未约课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------超过计划上课2天未约课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------超过计划上课1天未约课推送消息,异常：", e);
			}
			log.debug("------------超过计划上课1天未约课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));

			begin = System.currentTimeMillis();
			log.debug("------------超过计划上课2周未约课推送消息任务执行开始------------");

			try {
				// 离计划上课时长还有14天的课时推送消息
				List<CustomClassDTO> custoClassList = customClassService.findCustomClassListByPlanTimeNDay(-14);
				if (custoClassList != null && custoClassList.size() > 0) {
					for (CustomClassDTO customClassDTO : custoClassList) {
						try {
							mssageService.baseSendMssage(customClassDTO.getUserId(), customClassDTO.getTutorId(),
									customClassDTO.getLessonId() + "", 2, 20);
							log.info("超过计划上课2周未约课推送消息,课时ID=" + customClassDTO.getClassId());
						} catch (Exception e) {
							log.error("------------超过计划上课2周未约课推送消息,课时ID=" + customClassDTO.getClassId() + ",异常：", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("------------超过计划上课2周未约课推送消息,异常：", e);
			}
			log.debug("------------超过计划上课2周未约课推送消息执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 2周未支付订单关闭任务（定制课）
	 * 
	 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
	 * @version 1.0
	 */
	class CloseCustomPayJob implements SimpleJob {

		@Override
		public void execute(ShardingContext context) {
			long begin = System.currentTimeMillis();
			log.debug("------------2周未支付订单关闭任务执行------------");

			try {
				Calendar beginCalendar = Calendar.getInstance();
				beginCalendar.add(Calendar.DAY_OF_MONTH, -17);// 17天前

				Calendar endCalendar = Calendar.getInstance();
				endCalendar.add(Calendar.DAY_OF_MONTH, -14);// 14天前

				OrderDTO orderQuery = new OrderDTO();
				orderQuery.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);
				orderQuery.setPayStatus(PayStatusEnum.NO_PAY.status);
				orderQuery.setBeginTime(beginCalendar.getTime());
				orderQuery.setEndTime(endCalendar.getTime());
				orderQuery.setOrderType(OrderTypeEnmu.CUSTOM_CLASS_ORDER.type);
				List<OrderDTO> orderList = orderDao.findOrderPaging(null, orderQuery).getList();
				if (orderList != null && orderList.size() > 0) {
					for (OrderDTO order : orderList) {
						try {
							ResultDTO<String> resultStr = payResultService.closeOrder(order.getOrderCode());
							if (resultStr.isSuccess()) {
								mssageService.baseSendMssage(order.getUserId(), order.getUserId(), order.getOrderCode(),
										1, 21);
							}
						} catch (Exception e) {
							log.error("------------2周未支付订单关闭任务异常，orderCode=" + order.getOrderCode());
						}
					}
				}
			} catch (Exception e) {
				log.error("------------2周未支付订单关闭任务,异常：" + e);
			}
			log.debug("------------2周未支付订单关闭任务执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 更新直播课课时状态
	 * 
	 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
	 * @version 1.0
	 */
	class UpdatePublicStatusJob implements SimpleJob {

		@Override
		public void execute(ShardingContext context) {
			long begin = System.currentTimeMillis();
			log.debug("------------更新直播课课时状态任务执行------------");

			try {
				List<PublicClassDTO> publicClassList = publicClassDao.LiveClassList();
				if (publicClassList == null || publicClassList.size() == 0) {
					return;
				}

				for (PublicClassDTO publicClass : publicClassList) {
					publicClassDao.updateClassStatus(publicClass.getClassId(), publicClass.getClassStatus());
				}
			} catch (Exception e) {
				log.error("------------更新直播课课时状态任务,异常：" + e);
			}
			log.debug("------------更新直播课课时状态任务执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

	/**
	 * 更新用户等级
	 * 
	 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
	 * @version 1.0
	 */
	class UpdateUSerLevel implements SimpleJob {

		@Override
		public void execute(ShardingContext context) {
			long begin = System.currentTimeMillis();
			log.debug("------------更新用户等级任务执行------------");

			try {

				List<OrderDTO> orderList = orderDao.findLessons();
				if (orderList == null || orderList.size() == 0) {
					return;
				}

				for (OrderDTO order : orderList) {
					if (order.getEndTime().before(new Date())) {
						// 更新学生级别
						userDao.updateUserLevel(order.getUserId(), UserLevelTypeEnmu.REG.type);
						// 更新缓存
						String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + order.getUserId();
						if (!RedisClient.exists(userLimitKey)) {
							RedisClient.set(userLimitKey, order.getUserId());
						}
					}
				}

			} catch (Exception e) {
				log.error("------------更新用户等级任务,异常：" + e);
			}
			log.debug("------------更新用户等级任务执行结束，耗时：" + (System.currentTimeMillis() - begin));
		}
	}

}
