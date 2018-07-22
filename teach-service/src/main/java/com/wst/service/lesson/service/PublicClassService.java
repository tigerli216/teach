package com.wst.service.lesson.service;

import java.util.Map;

import com.hiwi.common.dto.ResultDTO;
import com.wst.service.lesson.dto.PublicClassDTO;

/**
 * 直播课课时接口
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface PublicClassService {

	/**
	 * 获取群组ID
	 * 
	 * @param classId
	 * @param userType
	 * @param userId
	 * @return
	 */
	public ResultDTO<Map<String, String>> getGroupId(long classId) throws Exception;

	/**
	 * 获取直播课课件图片
	 * 
	 * @param claddId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> getCoursewarePic(long classId) throws Exception;

	/**
	 * 获取直播课视频信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> getMovieUrl(long classId) throws Exception;

	/**
	 * 获取用户头像信息
	 * 
	 * @param loginAccount
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> getHeadImg(String loginAccount, String userType) throws Exception;

	/**
	 * 备课文件上传
	 * 
	 * @param classId
	 * @param readyFile
	 * @return
	 * @throws Exception
	 */
	public int readyFileUpload(long classId, String readyFile) throws Exception;

	/**
	 * 获取直播课视频地址
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public String getLiveUrl(long classId) throws Exception;

	/**
	 * 根据房间ID获取课时信息
	 * 
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO getByRoomId(long roomId) throws Exception;

	/**
	 * 修改视频地址和封面地址
	 * 
	 * @param moveUrl
	 * @param coverUrl
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public long upMoveUrlById(String moveUrl, String coverUrl, long classId) throws Exception;

	/**
	 * 获取课时信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<Map<String, Object>> getClassInfo(long classId) throws Exception;

	/**
	 * 网课课时结束
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classFinish(long classId, long tutorId) throws Exception;

	/**
	 * 获取直播课课程图片
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findImgList(long lessonId) throws Exception;

	/**
	 * 图片删除
	 * 
	 * @param lessonId
	 * @param picName
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> imgDelete(long lessonId, String picName) throws Exception;

	/**
	 * 更新课件图片
	 * 
	 * @param lessonId
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> updateLivePic(long lessonId, String img) throws Exception;
}
