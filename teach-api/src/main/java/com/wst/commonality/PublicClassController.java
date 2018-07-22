package com.wst.commonality;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.Globals;
import com.hiwi.common.util.RandomUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.RedisConstant;
import com.wst.constant.SysConfigConstant;
import com.wst.constant.SysConfigConstant.BusiTypeEnum;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 首页
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/live/")
public class PublicClassController {

	private Lock classLock = new ReentrantLock(); // 课时锁

	@Reference(version = "0.0.1")
	private PublicClassService publicClassService;

	@Reference(version = "0.0.1")
	private TxVideoService txVideoService;

	@Reference(version = "0.0.1")
	private BusiConfigService busiConfigService;

	/**
	 * 获取群组ID
	 * 
	 * @param dto
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getGroupId")
	public ResultDTO<Map<String, String>> getGroupId(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long classId, String theOneCode) throws Exception {
		ResultDTO<Map<String, String>> result = new ResultDTO<>();
		String classLimitKet = null;
		try {
			if (classId <= 0) {
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Please select class hour");
			}

			// 课时一次只能一个用户操作(课时已被锁定，正在操作)
			classLimitKet = RedisConstant.CLASS_LIMIT_ + classId + "_PUBLIC";
			RedisClient.del(classLimitKet);
			try {
				classLock.lock();
				if (RedisClient.exists(classLimitKet)) {
					return result.set(ResultCodeEnum.ERROR_REQ_OFTEN, "The class has been locked and is operating");
				}
				RedisClient.set(classLimitKet, classId);
			} finally {
				classLock.unlock();
			}

			// 获取直播课群组ID
			result = publicClassService.getGroupId(classId);
			if (!result.isSuccess()) {
				return result;
			}

			Map<String, String> resultMap = result.getResult();
			String account = UserHelper.getUserAccount(dto);
			// 游客
			if (StringUtils.isEmpty(account)) {
				if (StringUtils.isEmpty(theOneCode)) {
					theOneCode = Globals.getRealIP(dto.getRequest());
				}
				String theOneLimtKey = RedisConstant.LIVE_THEONE_ACCOUNT_ + theOneCode;

				if (RedisClient.exists(theOneLimtKey)) {
					account = RedisClient.get(theOneLimtKey);
				} else {
					account = RandomUtils.generateString(12) + "_S";
					RedisClient.set(theOneLimtKey, account);
				}
			}

			resultMap.put("userAccount", account);
			resultMap.put("userSig", txVideoService.createUserSig(account));

			BusiConfigDTO busiQuery = new BusiConfigDTO();
			busiQuery.setBusiCode(SysConfigConstant.TENCENT_VIDEO_ACC);
			busiQuery.setBusiType(BusiTypeEnum.SYS_CONFIG.type);

			List<BusiConfigDTO> busiConfigList = busiConfigService.getConfigList(busiQuery);
			if (busiConfigList == null || busiConfigList.size() == 0) {
				return result.set(ResultCodeEnum.ERROR_HANDLE, "busi handle error");
			}
			String adminAccount = JSONObject.parseObject(busiConfigList.get(0).getParams()).getString("identifier");
			resultMap.put("adminAccount", adminAccount);
			resultMap.put("adminSig", txVideoService.createUserSig(adminAccount));

			// 禁言状态
			String silenceRedisKey = RedisConstant.LIVE_GROUP_SILENCE_ + resultMap.get("groupId");
			String silenceStatus = "1";
			if (RedisClient.exists(silenceRedisKey)) {
			    silenceStatus = RedisClient.get(silenceRedisKey);
			}
			resultMap.put("silenceStatus", silenceStatus);
		} finally {
			// 删掉redis中记录
			RedisClient.del(classLimitKet);
		}

		return result;
	}

	/**
	 * 获取直播课课件图片
	 * 
	 * @param dto
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findPic")
	public ResultDTO<String> getCoursewarePic(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (classId <= 0) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Please select class hour");
		}

		return publicClassService.getCoursewarePic(classId);
	}

	/**
	 * 获取直播课视频信息
	 * 
	 * @param dto
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getMoiveUrl")
	public ResultDTO<String> getMovieUrl(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (classId <= 0) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Please select class hour");
		}

		return publicClassService.getMovieUrl(classId);
	}

	/**
	 * 获取用户头像信息
	 * 
	 * @param dto
	 * @param loginAccount
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getHeadImg")
	public ResultDTO<String> getHeadImg(CommonParamsDTO dto, String loginAccount, String userType) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (StringUtils.isEmpty(loginAccount)) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "User accounts cannot be empty");
		}

		if (StringUtils.isEmpty(userType)) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "User types cannot be empty");
		}

		return publicClassService.getHeadImg(loginAccount, userType);
	}

	/**
	 * 获取课时信息
	 * 
	 * @param dto
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getClassInfo")
	public ResultDTO<Map<String, Object>> getClassInfo(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long classId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

		if (classId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Class ID cannot be empty");
		}

		return publicClassService.getClassInfo(classId);
	}

}
