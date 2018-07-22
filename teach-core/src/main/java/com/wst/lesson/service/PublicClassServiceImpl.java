package com.wst.lesson.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.RandomUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.constant.RedisConstant;
import com.wst.lesson.dao.PublicClassDao;
import com.wst.lesson.dao.PublicDao;
import com.wst.mem.dao.TutorDao;
import com.wst.mem.dao.UserDao;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.tencent.service.TxVideoService;
import com.wst.tencent.module.TxVideoModule;

/**
 * 直播课课时接口实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class PublicClassServiceImpl implements PublicClassService {

	@Resource
	private PublicClassDao publicClassDao;

	@Resource
	private TxVideoService TxVideoService;

	@Resource
	private UserDao userDao;

	@Resource
	private TutorDao tutorDao;

	@Resource
	private PublicDao publicDao;

	@Override
	public ResultDTO<Map<String, String>> getGroupId(long classId) throws Exception {
		ResultDTO<Map<String, String>> result = new ResultDTO<>();
		Map<String, String> mapResult = new HashMap<>();

		PublicClassDTO publicClass = publicClassDao.getById(classId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The course ID is not valid");
		}
		// 已存在
		if (StringUtils.isNotEmpty(publicClass.getGroupId()) && publicClass.getGroupId() != "") {
			mapResult.put("groupId", publicClass.getGroupId());
			mapResult.put("roomId", publicClass.getRoomId());

			return result.set(ResultCodeEnum.SUCCESS, "", mapResult);
		}

		PublicDTO publicDTO = publicDao.getLessonById(publicClass.getLessonId());
		if (publicDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Lesson doesn't exist");
		}
		TutorDTO tutor = tutorDao.getTutorById(publicDTO.getTutorId());
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Tutor doesn't exist");
		}

		// 自动生成一个群组ID
		String tutorAccount = tutor.getLoginAccount() + "_T";
		String groupName = RandomUtils.generateNumString(9) + classId;
		String groupId = TxVideoService.createGroup(groupName, tutorAccount);
		String roomId = String.valueOf(classId);
		int idLength = roomId.length();
		if (idLength < 6) {
			int[] randArry = RandomUtils.randomCommon(0, 9, 6 - idLength);
			for (int i : randArry) {
				roomId += i;
			}
		}
		roomId = "20" + roomId; // 直播课固定以20开头

		publicClassDao.addGroupId(classId, groupId, roomId);

		mapResult.put("groupId", groupId);
		mapResult.put("roomId", roomId);
		return result.set(ResultCodeEnum.SUCCESS, "", mapResult);
	}

	@Override
	public ResultDTO<String> getCoursewarePic(long classId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		PublicClassDTO publicClass = publicClassDao.getById(classId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The course ID is not valid");
		}

		return result.set(ResultCodeEnum.SUCCESS, "", publicClass.getCoursewarePic());
	}

	@Override
	public ResultDTO<String> getMovieUrl(long classId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		PublicClassDTO publicClass = publicClassDao.getById(classId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The course ID is not valid");
		}

		return result.set(ResultCodeEnum.SUCCESS, "", publicClass.getMovieUrl());
	}

	@Override
	public ResultDTO<String> getHeadImg(String loginAccount, String userType) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		String headImg = "";

		// 学生
		if (userType.equals("S")) {
			UserDTO user = userDao.getUserByAccount(loginAccount);
			if (user == null) {
				return result.set(ResultCodeEnum.SUCCESS, "", headImg);
			}
			headImg = user.getPortraitImgUrl();
		} else { // 导师
			TutorDTO tutor = tutorDao.getTutorByAccount(loginAccount);
			if (tutor == null) {
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Tutor login account incorrect");
			}
			headImg = tutor.getPortraitImgUrl();
		}

		return result.set(ResultCodeEnum.SUCCESS, "", headImg);
	}

	@Override
	public int readyFileUpload(long classId, String readyFile) throws Exception {
		return publicClassDao.readyFileUpload(classId, readyFile);
	}

	@Override
	public String getLiveUrl(long classId) throws Exception {
		PublicClassDTO publicClassDTO = publicClassDao.getById(classId);
		if (publicClassDTO != null) {
			String roomId = publicClassDTO.getRoomId();

			String streamId = RedisClient.get(RedisConstant.LIVE_GROUP + roomId);

			return TxVideoModule.getLiveUrl(streamId);
		}
		return null;
	}

	@Override
	public PublicClassDTO getByRoomId(long roomId) throws Exception {
		return publicClassDao.getByRoomId(roomId);
	}

	@Override
	public long upMoveUrlById(String moveUrl, String coverUrl, long classId) throws Exception {
		return publicClassDao.upMoveUrlById(moveUrl, coverUrl, classId);
	}

	@Override
	public ResultDTO<Map<String, Object>> getClassInfo(long classId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

		PublicClassDTO publicClass = publicClassDao.getById(classId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Class ID is not legal");
		}

		Map<String, Object> resultMap = new HashMap<String, Object>(5);
		resultMap.put("className", publicClass.getClassName());
		resultMap.put("classStatus", publicClass.getClassStatus());
		resultMap.put("beginTime", publicClass.getBeginTime());
		resultMap.put("duration", publicClass.getDuration());

		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}

	@Override
	public ResultDTO<String> classFinish(long classId, long tutorId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		PublicClassDTO publicClass = publicClassDao.getById(classId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist");
		}
		if (publicClass.getClassStatus() == 1) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Class status is not legal");
		}

		PublicDTO lessonPublic = publicDao.getLessonById(publicClass.getLessonId());
		if (lessonPublic.getTutorId() != tutorId) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "User illegal");
		}

		publicClassDao.classFinish(classId);

		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public Map<String, Object> findImgList(long lessonId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> resultList = new ArrayList<String>();

		PublicClassDTO publicClass = publicClassDao.getLiveClassByLessonId(lessonId);
		resultMap.put("classStatus", publicClass.getClassStatus());

		String coursewarePic = publicClass.getCoursewarePic();
		if (coursewarePic == null || coursewarePic.equals("")) {
			return resultMap;
		}

		String[] imgs = coursewarePic.split(",");
		for (String img : imgs) {
			resultList.add(img);
		}
		resultMap.put("imgList", resultList);
		return resultMap;
	}

	@Override
	public ResultDTO<String> imgDelete(long lessonId, String picName) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		PublicClassDTO publicClass = publicClassDao.getLiveClassByLessonId(lessonId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");
		}

		String courseware = publicClass.getCoursewarePic();
		if (courseware == null || courseware.equals("")) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "图片不存在");
		}

		// 通过集合删除
		List<String> imgList = new ArrayList<String>();
		String[] imgs = courseware.split(",");
		for (String img : imgs) {
			imgList.add(img);
		}

		Iterator<String> it = imgList.iterator();
		while (it.hasNext()) {
			if (it.next().equals(picName)) {
				it.remove();
			}
		}

		if (imgList.size() == 0) {
			publicClassDao.updateCoursewarePic(publicClass.getClassId(), "");
			return result.set(ResultCodeEnum.SUCCESS, "");
		}

		courseware = imgList.get(0);
		for (int i = 0; i < imgList.size(); i++) {
			if (i > 0) {
				courseware += "," + imgList.get(i);
			}
		}
		publicClassDao.updateCoursewarePic(publicClass.getClassId(), courseware);

		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public ResultDTO<String> updateLivePic(long lessonId, String img) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		PublicClassDTO publicClass = publicClassDao.getLiveClassByLessonId(lessonId);
		if (publicClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");
		}

		if (publicClass.getCoursewarePic() != null && !publicClass.getCoursewarePic().equals("")) {
			img = publicClass.getCoursewarePic() + "," + img;
		}

		publicClassDao.updateCoursewarePic(publicClass.getClassId(), img);
		return result.set(ResultCodeEnum.SUCCESS, "上传成功");
	}

}
