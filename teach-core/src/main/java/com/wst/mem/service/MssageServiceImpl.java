/*
 * MssageServiceImpl.java Created on 2017年10月12日 上午11:10:16
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
package com.wst.mem.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dao.Page;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.DateUtils;
import com.wst.constant.MssageConstant.MssageBusiTypeEnum;
import com.wst.constant.MssageConstant.MssageStatusEnmu;
import com.wst.constant.MssageConstant.TutorMssageTypeEnmu;
import com.wst.constant.MssageConstant.UserMssageTypeEnmu;
import com.wst.entity.mem.Mssage;
import com.wst.entity.mem.TutorMssage;
import com.wst.lesson.dao.CustomClassDao;
import com.wst.mem.dao.MssageDao;
import com.wst.mem.dao.TutorMssageDao;
import com.wst.mem.dao.UserDao;
import com.wst.service.mem.dto.MssageDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/**
 * 消息服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class MssageServiceImpl implements MssageService {

	private HiwiLog logger = HiwiLogFactory.getLogger(MssageServiceImpl.class);

	@Resource
	private MssageDao mssageDao;

	@Resource
	private TutorMssageDao tutorMssageDao;

	@Resource
	private CustomClassDao customClassDao;

	@Resource
	private UserDao userDao;

	@Resource
	private WxMpService wxMpService;

	@Resource
	private BusiConfigService busiConfigService;

	@Override
	public void baseSendMssage(long sendUser, long userId, String msgSource, long sendType, long messageType)
			throws Exception {
		// 根据消息类型推送不同的消息
		if (messageType == 1) {
			// 学生支付完成，给涉课导师推送消息
			if (sendType == 2) {
				// 消息内容：你有新的VIP课程，请及时完成课程规划
				List<Long> tutorIdList = customClassDao.findTutorIdList(Long.parseLong(msgSource));
				if (tutorIdList != null && tutorIdList.size() > 0) {
					for (Long tutorId : tutorIdList) {
						tutorCustomMssage(sendUser, tutorId, msgSource, "You have a message",
								"You have a new student. Please set up a course syllabus with the student now.",
								TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
					}
				}
			}
		} else if (messageType == 2) {
			// 导师发起约课，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你的导师约你上课，记得准时上课
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have received a new class invitation from your mentor. Check it out now.",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId,
						"You have received a new class invitation from your mentor. Check it out now.",
						"Course notice");
			}
		} else if (messageType == 3) {
			// 导师上传备课资源，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你的导师已上传了下节课的上课内容，请及时查看
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"The syllabus of next session is avaliable. Check it out now.",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId, "The syllabus of next session is avaliable. Check it out now.",
						"Course notice");
			}
		} else if (messageType == 4) {
			// 导师课程小结，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你的导师已上传了上节课的课程小结，请及时查看
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"The summary of last session is avaliable, Check it out now.",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId, "The summary of last session is avaliable, Check it out now.",
						"Course notice");
			}
		} else if (messageType == 5) {
			// 导师确认上课完成，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你有已上完的课程，请及时做出确认
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have finished this session. Please confirm it now.", UserMssageTypeEnmu.CUSTOM_MSSAGE.type,
						MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId, "You have finished this session. Please confirm it now.",
						"Course notice");
			}
		} else if (messageType == 6) {
			// 学生确认上课完成，给导师推送信息
			if (sendType == 2) {
				// 消息内容：你有已上完的课程，请及时做出确认
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your student has confirmed the compeletion of this session.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 7) {
			// 学生72小时内未做出确认，系统自动确认上课完成，给导师推送信息
			if (sendType == 2) {
				// 消息内容：你的学生未及时确认上课完成，系统已自动确认上课完成
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your student has not confirmed the session on time. The session has been confirmed by the system automatically.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 8) {
			// TODO 学生否认上课完成或上课时间不认同，给导师推送信息
			if (sendType == 2) {
				// 消息内容：你有已上完的课程，请及时做出确认
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your students have confirmed that this course is completed",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 9) {
			// 学生在上课前2小时内取消上课，给导师推送信息
			if (sendType == 2) {
				// 消息内容：你的学生已取消了今天的课程，请及时与学生沟通
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"The session has been cancelled by your student. Please communicate with your student.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 10) {
			// 计划上课当天还未约课推送消息
			if (sendType == 2) {
				// 消息内容：你有VIP课程规划今日上课，你还未约课，请及时与学生沟通约课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have not started your course scheduled for today. Please contact your student ASAP.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 11) {
			// 2周未上课，给导师推送信息
			if (sendType == 2) {
				// 消息内容：你的VIP课程已有两周未上课了，请及时与学生沟通约课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your VIP course for two week class, please timely communication with students about class",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 12) {
			// 约课时间到前2小时提醒上课，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你有VIP课程在2小时后有上课，记得准时上课
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your class will start in 2 hours. Don't forget it!", UserMssageTypeEnmu.CUSTOM_MSSAGE.type,
						MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId, "Your class will start in 2 hours. Don't forget it!", "Course notice");
			} else if (sendType == 2) {
				// 约课时间到前2小时提醒上课，给导师推送信息
				// 消息内容：你有VIP课程在2小时后有上课，记得准时上课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your class will start in 2 hours. Don't forget it!", TutorMssageTypeEnmu.CUSTOM_MSSAGE.type,
						MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 13) {
			// 24小时未确认课时提醒学生，给学生推送信息
			if (sendType == 1) {
				// 消息内容：你有VIP课程规划今日上课，你还未约课，请及时与学生沟通约课
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have a course that hasn't been confirmed yet. Please confirm it in time",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId,
						"You have a course that hasn't been confirmed yet. Please confirm it in time", "Course notice");
			}
		} else if (messageType == 14) {
			// 订单自动取消
			if (sendType == 1) {
				// 消息内容：你有订单已过支付时间，订单已自动取消
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have not paid for your order on time. It has been cancelled by the system automatically.",
						UserMssageTypeEnmu.ORDER_MSSAGE.type, MssageBusiTypeEnum.PUBLIC_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId,
						"You have not paid for your order on time. It has been cancelled by the system automatically.",
						"Order notice");
			}
		} else if (messageType == 15) {
			if (sendType == 1) {
				// 离上课还有2小时的直播课，给学生推送信息
				// 消息内容：你有一节线上微课将在2小时后开播，记得准时收看
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"The Webinar will start in 2 hours. Dont forget it!", UserMssageTypeEnmu.CUSTOM_MSSAGE.type,
						MssageBusiTypeEnum.PUBLIC_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId, "The Webinar will start in 2 hours. Dont forget it!", "Course notice");
			} else if (sendType == 2) {
				// 离上课还有2小时的直播课，给导师推送信息
				// 消息内容：你有一节线上微课将在2小时后开播，请做好准备，准时开讲
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"The Webinar will start in 2 hours. Dont forget it!", TutorMssageTypeEnmu.CUSTOM_MSSAGE.type,
						MssageBusiTypeEnum.PUBLIC_BUSI_TYPE.type);
			}
		} else if (messageType == 16) {
			if (sendType == 2) {
				// 学生确认约课，给导师发送一条消息
				// 消息内容：你的学生已确认课程，记得准时上课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your student has confirmed the time of next session. Please prepare for the class beforehand.",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 17) {
			if (sendType == 1) {
				// 导师取消约课-学生已确认约课
				// 消息内容：你的导师已取消了今天的课程，请及时与导师沟通
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"Your mentor has cancelled the class. Please contact your mentor directly.",
						UserMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId,
						"Your mentor has cancelled the class. Please contact your mentor directly.", "Course notice");
			}
		} else if (messageType == 18) {
			// 计划上课时间快到时，提前2天提醒导师约课，给导师推送信息
			if (sendType == 2) {
				// 消息内容：消息内容：你有VIP课程规划两日后上课，你还未约课，请及时与学生沟通约课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have a pre-scheduled class in 2 days. Please confirm or reschedule the class as needed.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 19) {
			// 过了计划上课日期24小时未约课
			if (sendType == 2) {
				// 消息内容：消息内容：你有VIP课程规划两日后上课，你还未约课，请及时与学生沟通约课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have missed your scheduled class yesterday. Please contact your student directly.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 20) {
			// 过了计划上课日期2周未约课
			if (sendType == 2) {
				// 消息内容：消息内容：你有VIP课程规划两日后上课，你还未约课，请及时与学生沟通约课
				tutorCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have not scheduled any class for the past 2 weeks. Please schedule your class ASAP.",
						TutorMssageTypeEnmu.CUSTOM_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
			}
		} else if (messageType == 21) {
			// 订单自动取消2周
			if (sendType == 1) {
				// 消息内容：你有订单已过支付时间，订单已自动取消
				memCustomMssage(sendUser, userId, msgSource, "You have a message",
						"You have not paid for your order on time. It has been cancelled by the system automatically.",
						UserMssageTypeEnmu.ORDER_MSSAGE.type, MssageBusiTypeEnum.CUSTOM_BUSI_TYPE.type);
				wxSendMessageByTemplate(userId,
						"You have not paid for your order on time. It has been cancelled by the system automatically.",
						"Order notice");
			}
		}
	}

//	/**
//	 * 微信推送信息(文本)
//	 * 
//	 * @param userId
//	 * @param message
//	 * @throws Exception
//	 */
//	private void wxSendMessageByText(long userId, String message) throws Exception {
//		UserDTO user = userDao.getUserById(userId);
//		try {
//			if (StringUtils.isNotEmpty(user.getDeputyLoginAccount())) {
//				WxMpKefuMessage wxMpKefuMessage = WxMpKefuMessage.TEXT().toUser(user.getDeputyLoginAccount())
//						.content(message).build();
//				wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
//			}
//		} catch (Exception e) {
//			logger.error("微信通知学生异常：", e);
//		}
//	}

    /**
     * 微信推送信息(模板)
     * 
     * @param userId
     * @param message
     * @throws Exception
     */
    private void wxSendMessageByTemplate(long userId, String message, String messageType) throws Exception {
        UserDTO user = userDao.getUserById(userId);
        try {
            if (StringUtils.isNotEmpty(user.getDeputyLoginAccount())) {
                String templateId = "";
                String url = "";
                String color = "#FF00FF";
                List<BusiConfigDTO> busiConfigList = busiConfigService.findConfigList("WX_MESSAGE");
                if (busiConfigList != null && busiConfigList.size() > 0) {
                    BusiConfigDTO busiConfigDTO = busiConfigList.get(0);
                    String params = busiConfigDTO.getParams();
                    if (params != null) {
                        JSONObject jsonObject = JSONObject.parseObject(params);
                        templateId = jsonObject.getString("templateId");
                        url = jsonObject.getString("url");
                        color = jsonObject.getString("color");
                    }
                }

                if (StringUtils.isNotEmpty(templateId) && StringUtils.isNotEmpty(url)) {
                    WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                            .toUser(user.getDeputyLoginAccount()).templateId(templateId).url(url).build();
                    // 第一个参数对应模板key，第二个参数对应value，第三个值对应颜色
                    templateMessage.addWxMpTemplateData(new WxMpTemplateData("first", message, color));
                    templateMessage.addWxMpTemplateData(new WxMpTemplateData("keyword1", messageType, color));
                    templateMessage
                            .addWxMpTemplateData(new WxMpTemplateData("keyword2", DateUtils.getNowTime(), color));
                    templateMessage.addWxMpTemplateData(new WxMpTemplateData("remark",
                            "Thank you for paying attention to WST, and we will do better with your support", color));

					wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
				} else {
					logger.error("微信消息模板不存在");
				}
			}
		} catch (Exception e) {
			logger.error("微信通知学生异常：", e);
		}
	}

	@Override
	public void memCustomMssage(long sendUser, long userId, String msgSource, String msgTitle, String msgContent,
			long messageType, long busiType) throws Exception {
		Mssage mssage = new Mssage();
		mssage.setSendUser(sendUser);
		mssage.setMsgType(messageType);
		mssage.setMsgSource(msgSource);
		mssage.setUserId(userId);
		mssage.setMsgTitle(msgTitle);
		mssage.setMsgContent(msgContent);
		mssage.setMsgStatus(MssageStatusEnmu.VALID.status);
		mssage.setSendTime(new Date());
		mssage.setBeginTime(new Date());
		mssage.setBusiType(busiType);
		mssageDao.save(mssage);
	}

	@Override
	public void tutorCustomMssage(long sendUser, long tutorId, String msgSource, String msgTitle, String msgContent,
			long messageType, long busiType) throws Exception {
		TutorMssage tutorMssage = new TutorMssage();
		tutorMssage.setSendUser(sendUser);
		tutorMssage.setMsgType(messageType);
		tutorMssage.setMsgSource(msgSource);
		tutorMssage.setTutorId(tutorId);
		tutorMssage.setMsgTitle(msgTitle);
		tutorMssage.setMsgContent(msgContent);
		tutorMssage.setMsgStatus(MssageStatusEnmu.VALID.status);
		tutorMssage.setSendTime(new Date());
		tutorMssage.setBeginTime(new Date());
		tutorMssage.setBusiType(busiType);
		tutorMssageDao.save(tutorMssage);
	}

	@Override
	public Page<MssageDTO> pageFindMssage(Page<MssageDTO> pageParam, MssageDTO mssageDTO) throws Exception {
		return mssageDao.pageFindMssage(pageParam, mssageDTO);
	}
}
