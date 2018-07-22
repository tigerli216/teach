/*
 * TxVideoModule.java Created on 2017年11月1日 下午7:56:10
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
package com.wst.tencent.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.Globals;
import com.hiwi.common.util.HttpUtils;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.SpringUtils;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.util.security.Base64;
import com.hiwi.common.util.security.MD5Util;
import com.tls.sigcheck.tls_sigcheck;
import com.wst.constant.RedisConstant;
import com.wst.constant.SysConfigConstant;
import com.wst.constant.SysConfigConstant.BusiTypeEnum;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.sys.dao.BusiConfigDao;

/**
 * 腾讯视频点播，上传组件
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class TxVideoModule {

	private static HiwiLog log = HiwiLogFactory.getLogger(TxVideoModule.class);

	public static void main(String[] args) {
		try {
			String[] result = TxVideoModule.describeRecordPlayInfo("210018127_39a3a1555d18487cac144a0a8feb8db8");
			System.out.println("msg:" + result[0] + ",url:" + result[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 依照 VID 查询视频信息
	 * 
	 * @param vid
	 * @return
	 */
	public static String[] describeRecordPlayInfo(String vid) throws Exception {
		Map<String, Object> reqParams = new HashMap<>();
		reqParams.put("vid", vid);

		Map<String, Object> resultParams = TxVideoModule.initParams("DescribeRecordPlayInfo", reqParams);
		String result = HttpUtils.doGet("https://vod.api.qcloud.com/v2/index.php", null, resultParams);
		if (StringUtils.isEmpty(result)) {
			return null;
		}

		JSONObject resultObj = JSONObject.parseObject(result);
		if (resultObj.getInteger("code") != 0) {
			log.error("依照 VID 查询视频信息，请求错误。" + resultObj.getString("message"));

			return new String[] { resultObj.getString("message") };
		}
		JSONArray fileSet = resultObj.getJSONArray("fileSet");
		JSONArray playSet = fileSet.getJSONObject(0).getJSONArray("playSet");
		log.info("上传地址获取：====" + playSet.getJSONObject(0).getString("url") + "====");

		String[] resultInfo = new String[2];
		// 获取结果信息
		resultInfo[0] = resultObj.getString("message");
		// 获取URL
		resultInfo[1] = playSet.getJSONObject(0).getString("url");

		return resultInfo;
	}

	/**
	 * 生成签名信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getSignature() throws Exception {
		JSONObject configObject = TxVideoModule.getConfig();

		Long currentTime = System.currentTimeMillis() / 1000;
		StringBuilder contextStr = new StringBuilder();
		contextStr.append("secretId=" + configObject.getString("secretId"));
		contextStr.append("&currentTimeStamp=" + currentTime);
		contextStr.append("&expireTime=" + (currentTime + 60 * 60)); // 1小时后过期
		contextStr.append("&random=" + new Random().nextInt(java.lang.Integer.MAX_VALUE));

		byte[] hash = TxVideoModule.HmacSHA1Encrypt(contextStr.toString(), configObject.getString("secretKey"));
		byte[] contentHash = contextStr.toString().getBytes("utf8");

		byte[] mergerBytes = new byte[hash.length + contentHash.length];
		System.arraycopy(hash, 0, mergerBytes, 0, hash.length);
		System.arraycopy(contentHash, 0, mergerBytes, hash.length, contentHash.length);

		String strSign = new String(Base64.encode(mergerBytes).getBytes());
		strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");

		return strSign;
	}

	/**
	 * 视频转码
	 * 
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static boolean convertVodFile(String fileId) throws Exception {
		Map<String, Object> reqParams = new HashMap<>();
		reqParams.put("fileId", fileId);
		reqParams.put("isScreenshot", 0);
		reqParams.put("isWatermark", 1);

		Map<String, Object> resultParams = TxVideoModule.initParams("ConvertVodFile", reqParams);

		String result = HttpUtils.doGet("https://vod.api.qcloud.com/v2/index.php", null, resultParams);
		if (StringUtils.isEmpty(result)) {
			return false;
		}
		JSONObject resultObj = JSONObject.parseObject(result);
		if (resultObj.getInteger("code") != 0) {
			log.error("视频转码，请求错误。" + result);
			return false;
		}

		return true;
	}

	/**
	 * 创建群组AVChatRoom（聊天室）
	 * 
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public static String createGroup(String groupName, String tutorAccount) throws Exception {
		JSONObject configObject = TxVideoModule.getConfig();

		StringBuilder url = new StringBuilder();
		String identifier = configObject.getString("identifier");
		String userSig = createUserSig(identifier);

		url.append("https://console.tim.qq.com/v4/group_open_http_svc/create_group");
		url.append("?usersig=" + userSig);
		url.append("&identifier=" + identifier);
		url.append("&sdkappid=" + configObject.getString("sdkappid"));
		url.append("&random=" + Globals.getRandomCode(8));
		url.append("&contenttype=json");

		Map<String, Object> params = new HashMap<>();
		params.put("Owner_Account", tutorAccount);
		params.put("Type", "ChatRoom");
		params.put("Name", groupName);

		String result = HttpUtils.doPost(url.toString(), null, JsonUtils.getJson(params));
		if (StringUtils.isEmpty(result)) {
			return null;
		}
		JSONObject resultObj = JSONObject.parseObject(result);
		if (resultObj.getInteger("ErrorCode") != 0) {
			log.error("创建群组Public（公开群），请求错误。" + result);
			return null;
		}

		return resultObj.getString("GroupId");
	}

	/**
	 * 创建用户签名
	 * 
	 * @param account
	 *            用户账号
	 * @return
	 * @throws Exception
	 */
	public static String createUserSig(String account) throws Exception {
		JSONObject configObject = TxVideoModule.getConfig();

		String userSig = null;
		try {
			String userSigBak = RedisClient.get(RedisConstant.LIVE_USER_SIG + account);
			if (StringUtils.isNotEmpty(userSigBak)) {
				return userSigBak;
			}

			tls_sigcheck demo = new tls_sigcheck();

			// 使用前请修改动态库的加载路径
			demo.loadJniLib(configObject.getString("jniAddr"));

			File priKeyFile = new File(configObject.getString("prikeyAddr"));
			StringBuilder strBuilder = new StringBuilder();
			String s = "";

			BufferedReader br = new BufferedReader(new FileReader(priKeyFile));
			while ((s = br.readLine()) != null) {
				strBuilder.append(s + '\n');
			}
			br.close();
			String priKey = strBuilder.toString();
			int ret = demo.tls_gen_signature_ex2(configObject.getString("sdkappid"), account, priKey);

			if (0 != ret) {
				return null;
			} else {
				userSig = demo.getSig();
			}

			File pubKeyFile = new File(configObject.getString("publickeyAddr"));
			br = new BufferedReader(new FileReader(pubKeyFile));
			strBuilder.setLength(0);
			while ((s = br.readLine()) != null) {
				strBuilder.append(s + '\n');
			}
			br.close();
			String pubKey = strBuilder.toString();
			ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, configObject.getString("sdkappid"), account);
			if (0 == ret) {
				RedisClient.set(RedisConstant.LIVE_USER_SIG + account, userSig);
				RedisClient.expire(RedisConstant.LIVE_USER_SIG + account, demo.getExpireTime() - 1000);
			}
		} catch (Exception e) {
			log.error("===腾讯云，创建用户签名", e);
		}

		return userSig;
	}

	/**
	 * 云端混流
	 * 
	 * @param mainStreamId
	 *            主播流ID
	 * @param streamId
	 *            观众流ID
	 * @return
	 * @throws Exception
	 */
	public static String mixStream(String mainStreamId, String streamId, String groupId) throws Exception {
		JSONObject configObject = TxVideoModule.getConfig();

		// JSONObject configObject = new JSONObject();
		// configObject.put("appid", "1255321877");
		// configObject.put("bizid", "13862");
		// configObject.put("liveKey", "a25288f8fb7546688531b4656ca34145");

		long t = System.currentTimeMillis() / 1000 + 600;

		// 输出流ID
		String outputStreamId = configObject.getString("bizid") + "_MIX_" + groupId + "_" + mainStreamId + "_2_" + t;

		StringBuilder url = new StringBuilder();
		url.append("http://fcgi.video.qcloud.com/common_access");
		url.append("?appid=" + configObject.getString("appid"));
		url.append("&interface=mix_streamv2");
		url.append("&t=" + t);
		url.append("&sign=" + MD5Util.MD5(configObject.getString("liveKey") + t));

		// 输入流列表
		List<Map<String, Object>> inputStreamList = new ArrayList<>();
		Map<String, Object> inputStreamMap1 = new HashMap<>();
		inputStreamMap1.put("input_stream_id", mainStreamId);
		Map<String, Object> inputStreamLayout1 = new HashMap<>();
		inputStreamLayout1.put("image_layer", 1);
		inputStreamLayout1.put("input_type", 0);
		inputStreamMap1.put("layout_params", inputStreamLayout1);
		inputStreamList.add(inputStreamMap1);

		Map<String, Object> inputStreamMap2 = new HashMap<>();
		inputStreamMap2.put("input_stream_id", streamId);
		Map<String, Object> inputStreamLayout2 = new HashMap<>();
		inputStreamLayout2.put("image_layer", 2);
		inputStreamLayout2.put("input_type", 0);
		inputStreamMap2.put("layout_params", inputStreamLayout2);
		inputStreamList.add(inputStreamMap2);

		// 参数对象
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("interface", "mix_streamv2.start_mix_stream_advanced");
		paraMap.put("app_id", configObject.getString("appid"));
		paraMap.put("mix_stream_template_id", 40);
		paraMap.put("mix_stream_session_id", mainStreamId);
		paraMap.put("output_stream_id", outputStreamId);
		paraMap.put("output_stream_type", 1);
		paraMap.put("input_stream_list", inputStreamList);

		// interfaceMap
		Map<String, Object> interfaceMap = new HashMap<>();
		interfaceMap.put("interfaceName", "Mix_StreamV2");
		interfaceMap.put("para", paraMap);

		// 最外层map
		Map<String, Object> params = new HashMap<>();
		params.put("timestamp", t);
		params.put("eventId", Globals.getRandomCode(10));
		params.put("interface", interfaceMap);

		String result = HttpUtils.doPost(url.toString(), null, JsonUtils.getJson(params));
		log.debug("======腾讯云云端混流，请求参数：" + JsonUtils.getJson(params) + "，响应结果：" + result);
		if (StringUtils.isEmpty(result)) {
			return null;
		}

		JSONObject resultObj = JSONObject.parseObject(result);
		if (resultObj.getInteger("code") != 0) {
			log.error("======腾讯云云端混流，请求错误。" + result);
			return null;
		}

		return outputStreamId;
	}

	/**
	 * 拼接播放地址
	 * 
	 * @param streamId
	 * @return
	 * @throws Exception
	 */
	public static String getLiveUrl(String streamId) throws Exception {
		JSONObject configObject = TxVideoModule.getConfig();

		String bizid = configObject.getString("bizid");

		return "http://" + bizid + ".liveplay.myqcloud.com/live/" + streamId + ".m3u8";
	}

	/**
	 * 生成请求参数MAP
	 * 
	 * @param action
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> initParams(String action, Map<String, Object> params) throws Exception {
		if (StringUtils.isEmpty(action)) {
			log.error("action is null");
			return null;
		}

		// JSONObject configObject = TxVideoModule.getConfig();

		JSONObject configObject = new JSONObject();
		configObject.put("secretId", "AKIDVr12sXsJPfswrZLZr35xhIhvxnTz8WPc");
		configObject.put("secretKey", "VxGQRGwQiq9KJH4fMxMYYM3YyDrofNwL");
		// configObject.put("sdkappid", "1400052752");
		// configObject.put("secretId", "AKIDVr12sXsJPfswrZLZr35xhIhvxnTz8WPc");
		// configObject.put("secretId", "AKIDVr12sXsJPfswrZLZr35xhIhvxnTz8WPc");
		// configObject.put("secretId", "AKIDVr12sXsJPfswrZLZr35xhIhvxnTz8WPc");

		String secretId = configObject.getString("secretId");
		String secretKey = configObject.getString("secretKey");

		Map<String, Object> allParams = new TreeMap<>();
		allParams.put("Action", action);
		allParams.put("Timestamp", System.currentTimeMillis() / 1000);
		allParams.put("Nonce", Globals.getRandomCode(5));
		allParams.put("SecretId", secretId);
		allParams.putAll(params);

		String cryptParams = ""; // 加密字符串
		Iterator<Entry<String, Object>> iter = allParams.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			cryptParams += entry.getKey() + "=" + entry.getValue() + "&";
		}
		cryptParams = cryptParams.substring(0, cryptParams.length() - 1);
		cryptParams = "GET" + "vod.api.qcloud.com" + "/v2/index.php" + "?" + cryptParams;

		String signature = Base64.encode(TxVideoModule.HmacSHA1Encrypt(cryptParams, secretKey));
		allParams.put("Signature", signature);

		return allParams;
	}

	/**
	 * 获取腾讯云配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	private static JSONObject getConfig() throws Exception {
		BusiConfigDTO busiQuery = new BusiConfigDTO();
		busiQuery.setBusiCode(SysConfigConstant.TENCENT_VIDEO_ACC);
		busiQuery.setBusiType(BusiTypeEnum.SYS_CONFIG.type);
		BusiConfigDao busiConfigDao = SpringUtils.getBean(BusiConfigDao.class);
		List<BusiConfigDTO> busiConfigList = busiConfigDao.findBusiConfigList(busiQuery);
		if (busiConfigList == null || busiConfigList.size() <= 0) {
			throw new IllegalArgumentException("tengxun config is null");
		}
		JSONObject configObject = JSONObject.parseObject(busiConfigList.get(0).getParams());

		return configObject;
	}

	/**
	 * HmacSHA1加密
	 * 
	 * @param encryptText
	 *            加密字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	private static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		byte[] data = encryptKey.getBytes("UTF-8");
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance("HmacSHA1");
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes("UTF-8");
		// 完成 Mac 操作
		return mac.doFinal(text);
	}

}
