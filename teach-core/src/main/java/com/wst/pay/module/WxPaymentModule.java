/*
 * WxPaymentServiceImpl.java Created on 2016年9月27日 下午5:54:48
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
package com.wst.pay.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxEntPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxEntPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.SpringUtils;
import com.wst.constant.PayConstant.TradeTypeEnum;
import com.wst.order.dao.PayChnConfigDao;
import com.wst.pay.dao.WxEntPayReqDTO;
import com.wst.pay.dao.WxOrderReqDTO;
import com.wst.pay.dao.WxOrderRespDTO;
import com.wst.pay.dao.WxPrePayReqDTO;
import com.wst.pay.dao.WxPrePayRespDTO;
import com.wst.pay.dao.WxRefundReqDTO;
import com.wst.pay.dao.WxRefundRespDTO;
import com.wst.service.order.dto.PayChnConfigDTO;


/**
 * 微信支付组件
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Component
public class WxPaymentModule {

    private static HiwiLog logger = HiwiLogFactory.getLogger(WxPaymentModule.class);

    private static Map<String, WxPayService> wxPayServiceMap = new HashMap<>();

    /**
     * 微信支付接口
     * 
     * @param wxPrePayReq
     *            微信预支付订单请求信息
     * @return
     * @throws Exception
     */
    public static ResultDTO<WxPrePayRespDTO> payment(WxPrePayReqDTO wxPrePayReq) throws Exception {
        ResultDTO<WxPrePayRespDTO> result = new ResultDTO<>();
        if (null == wxPrePayReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
        }
        if (StringUtils.isEmpty(wxPrePayReq.getBody())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少商品描述");
        }
        if (StringUtils.isEmpty(wxPrePayReq.getOrderNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少订单编号");
        }
        if (StringUtils.isEmpty(wxPrePayReq.getReqIp())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少请求IP");
        }
        if (wxPrePayReq.getTotalFee() <= 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少订单总额");
        }
        if (StringUtils.isEmpty(wxPrePayReq.getPayType())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少支付类型");
        }
        ResultDTO<String> resultCheck = checkConfigParams(wxPrePayReq, 1);
        if (!resultCheck.isSuccess()) {
            return result.set(resultCheck.getErrCode(), resultCheck.getErrDesc());
        }
        logger.debug("微信预支付请求信息：" + wxPrePayReq.toString());
        try {
            if (StringUtils.equals(wxPrePayReq.getPayType(), "JSAPI")) {
                if (StringUtils.isEmpty(wxPrePayReq.getOpenid())) {
                    return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少用户标识");
                }
            } else if (StringUtils.equals(wxPrePayReq.getPayType(), "NATIVE")) {
                if (StringUtils.isEmpty(wxPrePayReq.getProductId())) {
                    return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少商户产品ID");
                }
            }
            WxPayConfig wxPayConfig = JsonUtils.parseJson(JsonUtils.getJson(wxPrePayReq), WxPayConfig.class);
            wxPayConfig.setTradeType(wxPrePayReq.getPayType());
            WxPayService wxPayService = getWxPayService(wxPayConfig);
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody(wxPrePayReq.getBody());
            orderRequest.setOutTradeNo(wxPrePayReq.getOrderNo());
            orderRequest.setTotalFee((int) wxPrePayReq.getTotalFee());
            orderRequest.setSpbillCreateIp(wxPrePayReq.getReqIp());
            orderRequest.setOpenid(wxPrePayReq.getOpenid());

            // 预支付返回信息
            WxPrePayRespDTO wxPrePayResp = new WxPrePayRespDTO();
            if (StringUtils.equals(wxPrePayReq.getPayType(), "JSAPI")) {
                orderRequest.setOpenid(wxPrePayReq.getOpenid());
                WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(orderRequest);
                wxPrePayResp = JsonUtils.parseJson(JsonUtils.getJson(wxPayMpOrderResult), WxPrePayRespDTO.class);
                wxPrePayResp.setSign(wxPayMpOrderResult.getPaySign());	// 签名
            } else if (StringUtils.equals(wxPrePayReq.getPayType(), "APP")) {
                WxPayAppOrderResult wxPayAppOrderResult = wxPayService.createOrder(orderRequest);
                wxPrePayResp = JsonUtils.parseJson(JsonUtils.getJson(wxPayAppOrderResult), WxPrePayRespDTO.class);
            } else if (StringUtils.equals(wxPrePayReq.getPayType(), "NATIVE")) {
                orderRequest.setProductId(wxPrePayReq.getProductId());
                WxPayNativeOrderResult wxPayNativeOrderResult = wxPayService.createOrder(orderRequest);
                wxPrePayResp.setCodeUrl(wxPayNativeOrderResult.getCodeUrl());
            }

            logger.debug("微信预支付订单返回信息：" + wxPrePayResp.toString());
            return result.set(ResultCodeEnum.SUCCESS, "", wxPrePayResp);
        } catch (Exception e) {
            logger.error("微信预支付失败，订单号：" + wxPrePayReq.getOrderNo(), e);
            return result.set(ResultCodeEnum.ERROR_HANDLE, "微信预支付异常");
        }
    }

    /**
     * 支付回调
     * 
     * @param payNotifyInfo
     *            微信支付回调信息
     * @return
     * @throws Exception
     */
    public static ResultDTO<WxOrderRespDTO> payNotify(String payNotifyInfo) throws Exception {
        ResultDTO<WxOrderRespDTO> resultDTO = new ResultDTO<>();
        try {
            logger.info("微信支付回调返回数据：" + payNotifyInfo);
            if (StringUtils.isEmpty(payNotifyInfo)) {
                return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付回调数据为空");
            }
            WxPayOrderNotifyResult wxPayOrderNotifyResult = WxPayOrderNotifyResult.fromXML(payNotifyInfo);
            if (CommonFuntions.isEmptyObject(wxPayOrderNotifyResult)) {
                return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付回调参数异常");
            }
            if (StringUtils.equals("FAIL", wxPayOrderNotifyResult.getReturnCode())) {
                return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付回调失败");
            }
            String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
            // 获取商户支付配置信息
            PayChnConfigDao payChnConfigDao = SpringUtils.getBean(PayChnConfigDao.class);
            PayChnConfigDTO payChnConfig = payChnConfigDao.getPayChnConfigByPayNo(outTradeNo,
                    TradeTypeEnum.CONSUME.type);
            if (CommonFuntions.isEmptyObject(payChnConfig)) {
                return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付配置异常");
            }
            JSONObject wxParams = JSONObject.parseObject(payChnConfig.getPayChnParams());
            // jar包坑之一，转换多出参数，签名错误
            Map<String, String> map=wxPayOrderNotifyResult.toMap();
            map.remove("xmlString");
            map.remove("couponList");
            if (!SignUtils.checkSign(map, null, wxParams.getString("paternerKey"))) {
                logger.debug("----微信返回参数：" + map);
                logger.debug("签名校验：" + SignUtils.createSign(map, null,
                        wxParams.getString("paternerKey"), false) + "===="
                        + wxPayOrderNotifyResult.toMap().get("sign"));
                return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付回调签名错误");
            }

            String resultCode = wxPayOrderNotifyResult.getResultCode();
            long orderStatus = 2;
            if (StringUtils.equals(resultCode, "SUCCESS")) {
                orderStatus = 1;
            }
            WxOrderRespDTO wxOrderResp = new WxOrderRespDTO();
            wxOrderResp.setOrderNo(wxPayOrderNotifyResult.getOutTradeNo());
            wxOrderResp.setOutOrderNo(wxPayOrderNotifyResult.getTransactionId());
            wxOrderResp.setOrderStatus(orderStatus);
            wxOrderResp.setTime(DateUtils.getDateParse(wxPayOrderNotifyResult.getTimeEnd(), DateUtils.YYYYMMDDHHMMSS));
            logger.debug("微信支付回调返回信息：" + wxOrderResp.toString());
            return resultDTO.set(ResultCodeEnum.SUCCESS, "", wxOrderResp);
        } catch (Exception e) {
            logger.error("微信支付回调失败", e);
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "微信支付回调异常");
        }
    }

    /**
     * 微信订单查询
     * 
     * @param wxOrderReq
     * @return
     * @throws Exception
     */
    public static ResultDTO<WxOrderRespDTO> wxPayQuery(WxOrderReqDTO wxOrderReq) throws Exception {
        ResultDTO<WxOrderRespDTO> result = new ResultDTO<>();

        if (null == wxOrderReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
        }
        if (StringUtils.isEmpty(wxOrderReq.getOrderNo()) && StringUtils.isEmpty(wxOrderReq.getOutOrderNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少平台订单号或微信订单号");
        }
        ResultDTO<String> resultCheck = checkConfigParams(wxOrderReq, 2);
        if (!resultCheck.isSuccess()) {
            return result.set(resultCheck.getErrCode(), resultCheck.getErrDesc());
        }
        logger.debug("微信支付订单查询请求信息：" + wxOrderReq.toString());
        try {
            WxPayConfig wxPayConfig = JsonUtils.parseJson(JsonUtils.getJson(wxOrderReq), WxPayConfig.class);
            WxPayService wxPayService = getWxPayService(wxPayConfig);

            WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(wxOrderReq.getOutOrderNo(),
                    wxOrderReq.getOrderNo());
            WxOrderRespDTO wxOrderResp = new WxOrderRespDTO();
            wxOrderResp.setOrderNo(wxPayOrderQueryResult.getOutTradeNo());
            wxOrderResp.setOutOrderNo(wxPayOrderQueryResult.getTransactionId());
            String resultCode = wxPayOrderQueryResult.getResultCode();
            long orderStatus = 2;
            if (StringUtils.equals(resultCode, "SUCCESS")) {
                orderStatus = 1;
            }
            wxOrderResp.setOrderStatus(orderStatus);
            wxOrderResp.setTime(DateUtils.getDateParse(wxPayOrderQueryResult.getTimeEnd(), DateUtils.YYYYMMDDHHMMSS));
            logger.debug("微信支付查询返回信息：" + wxOrderResp.toString());
            return result.set(ResultCodeEnum.SUCCESS, "", wxOrderResp);
        } catch (Exception e) {
            logger.error("=========微信支付查询异常===========", e);
            return result.set(ResultCodeEnum.ERROR_HANDLE, "微信支付查询失败");
        }
    }

    /**
     * 退款
     * 
     * @param wxRefundReq
     *            微信退款请求信息
     * @throws Exception
     */
    public static ResultDTO<WxRefundRespDTO> refund(WxRefundReqDTO wxRefundReq) throws Exception {
        ResultDTO<WxRefundRespDTO> result = new ResultDTO<>();
        if (null == wxRefundReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
        }
        if (StringUtils.isEmpty(wxRefundReq.getOrderNo()) && StringUtils.isEmpty(wxRefundReq.getOutOrderNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少平台订单号或微信订单号");
        }
        if (StringUtils.isEmpty(wxRefundReq.getRefundNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少退款订单号");
        }
        if (wxRefundReq.getTotalFee() <= 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少订单总额");
        }
        if (wxRefundReq.getRefundFee() <= 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少退款总额");
        }
        ResultDTO<String> resultCheck = checkConfigParams(wxRefundReq, 3);
        if (!resultCheck.isSuccess()) {
            return result.set(resultCheck.getErrCode(), resultCheck.getErrDesc());
        }
        logger.debug("微信退款请求信息：" + wxRefundReq.toString());
        try {
            WxPayConfig wxPayConfig = JsonUtils.parseJson(JsonUtils.getJson(wxRefundReq), WxPayConfig.class);
            WxPayService wxPayService = getWxPayService(wxPayConfig);
            WxPayRefundRequest wxPayRefundRequest = WxPayRefundRequest.newBuilder().build();
            if (StringUtils.isNotEmpty(wxRefundReq.getOrderNo())) {
                wxPayRefundRequest.setOutTradeNo(wxRefundReq.getOrderNo());
            }
            if (StringUtils.isNotEmpty(wxRefundReq.getOutOrderNo())) {
                wxPayRefundRequest.setTransactionId(wxRefundReq.getOutOrderNo());
            }
            wxPayRefundRequest.setOutRefundNo(wxRefundReq.getRefundNo());
            wxPayRefundRequest.setTotalFee((int) wxRefundReq.getTotalFee());
            wxPayRefundRequest.setRefundFee((int) wxRefundReq.getRefundFee());
            // 微信退款信息
            WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
            // 封装微信退款信息
            WxRefundRespDTO wxRefundResp = new WxRefundRespDTO();
            wxRefundResp.setOrderNo(wxPayRefundResult.getOutTradeNo());
            wxRefundResp.setOutOrderNo(wxPayRefundResult.getTransactionId());
            wxRefundResp.setRefundNo(wxPayRefundResult.getOutRefundNo());
            wxRefundResp.setOutRefundNo(wxPayRefundResult.getRefundId());
            String resultCode = wxPayRefundResult.getResultCode();
            long orderStatus = 2;
            if (StringUtils.equals(resultCode, "SUCCESS")) {
                orderStatus = 1;
            }
            wxRefundResp.setOrderStatus(orderStatus);
            logger.debug("微信退款返回信息：" + wxRefundResp.toString());
            return result.set(ResultCodeEnum.SUCCESS, "微信退款成功", wxRefundResp);
        } catch (Exception e) {
            logger.error("=========微信退款异常===========", e);
            return result.set(ResultCodeEnum.ERROR_HANDLE, "微信退款异常");
        }
    }

    /**
     * 微信企业打款
     * 
     * @param wxEntPayReq
     * @return
     * @throws Exception
     */
    public static ResultDTO<WxOrderRespDTO> entPay(WxEntPayReqDTO wxEntPayReq) throws Exception {
        ResultDTO<WxOrderRespDTO> result = new ResultDTO<>();
        if (null == wxEntPayReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
        }
        if (StringUtils.isEmpty(wxEntPayReq.getOrderNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少平台订单号");
        }
        if (StringUtils.isEmpty(wxEntPayReq.getTransferDesc())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少打款描述");
        }
        if (wxEntPayReq.getTransferFee() <= 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少打款金额");
        }
        if (StringUtils.isEmpty(wxEntPayReq.getReqIp())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少请求IP");
        }
        ResultDTO<String> resultCheck = checkConfigParams(wxEntPayReq, 4);
        if (!resultCheck.isSuccess()) {
            return result.set(resultCheck.getErrCode(), resultCheck.getErrDesc());
        }
        logger.debug("微信打款请求信息：" + wxEntPayReq.toString());
        try {
            WxPayConfig wxPayConfig = JsonUtils.parseJson(JsonUtils.getJson(wxEntPayReq), WxPayConfig.class);
            WxPayService wxPayService = getWxPayService(wxPayConfig);
            WxEntPayRequest wxEntPayRequest = new WxEntPayRequest();
            wxEntPayRequest.setPartnerTradeNo(wxEntPayReq.getOrderNo());
            wxEntPayRequest.setOpenid(wxEntPayReq.getOpenid());
            wxEntPayRequest.setCheckName("NO_CHECK");
            wxEntPayRequest.setAmount((int) wxEntPayReq.getTransferFee());
            wxEntPayRequest.setDescription(wxEntPayReq.getTransferDesc());
            wxEntPayRequest.setSpbillCreateIp(wxEntPayReq.getReqIp());
            // 微信打款返回信息
            WxEntPayResult wxEntPayResult = wxPayService.entPay(wxEntPayRequest);
            WxOrderRespDTO wxOrderResp = new WxOrderRespDTO();
            wxOrderResp.setOrderNo(wxEntPayResult.getPartnerTradeNo());
            wxOrderResp.setOutOrderNo(wxEntPayResult.getPaymentNo());
            wxOrderResp.setTime(DateUtils.getDateParse(wxEntPayResult.getPaymentTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
            String resultCode = wxEntPayResult.getResultCode();
            long orderStatus = 2;
            if (StringUtils.equals(resultCode, "SUCCESS")) {
                orderStatus = 1;
            }
            wxOrderResp.setOrderStatus(orderStatus);
            logger.debug("微信打款返回信息：" + wxOrderResp.toString());
            return result.set(ResultCodeEnum.SUCCESS, "微信打款成功", wxOrderResp);
        } catch (Exception e) {
            logger.error("=========微信提现（打款）异常===========", e);
            return result.set(ResultCodeEnum.SUCCESS, "微信打款异常");
        }
    }

    /**
     * 微信关闭订单
     * 
     * @param wxOrderReq
     *            微信订单信息
     * @return
     */
    public static ResultDTO<WxOrderRespDTO> closeOrder(WxOrderReqDTO wxOrderReq) {
        ResultDTO<WxOrderRespDTO> result = new ResultDTO<>();
        if (null == wxOrderReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
        }
        if (StringUtils.isEmpty(wxOrderReq.getOrderNo())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少平台订单号");
        }
        ResultDTO<String> resultCheck = checkConfigParams(wxOrderReq, 5);
        if (!resultCheck.isSuccess()) {
            return result.set(resultCheck.getErrCode(), resultCheck.getErrDesc());
        }
        logger.debug("微信关闭订单请求信息：" + wxOrderReq.toString());
        try {
            WxPayConfig wxPayConfig = JsonUtils.parseJson(JsonUtils.getJson(wxOrderReq), WxPayConfig.class);
            WxPayService wxPayService = getWxPayService(wxPayConfig);
            wxPayService.setConfig(wxPayConfig);

            WxPayOrderCloseResult wxPayOrderCloseResult = wxPayService.closeOrder(wxOrderReq.getOrderNo());
            WxOrderRespDTO wxOrderResp = new WxOrderRespDTO();
            wxOrderResp.setOrderNo(wxOrderReq.getOrderNo());
            String resultCode = wxPayOrderCloseResult.getResultCode();
            long orderStatus = 2;
            if (StringUtils.equals(resultCode, "SUCCESS")) {
                orderStatus = 1;
            }
            wxOrderResp.setOrderStatus(orderStatus);
            logger.debug("微信关闭订单返回信息：" + wxOrderResp.toString());

            return result.set(ResultCodeEnum.SUCCESS, "微信关闭订单成功", wxOrderResp);
        } catch (Exception e) {
            logger.error("=========微信关闭订单异常===========", e);
            return result.set(ResultCodeEnum.ERROR_HANDLE, "微信关闭订单异常");
        }
    }

    /**
     * 获取微信支付信息
     * 
     * @param wxPayConfig
     * @return
     */
    private static WxPayService getWxPayService(WxPayConfig wxPayConfig) {
        String mapKey = DigestUtils.md5Hex(JsonUtils.getJson(wxPayConfig));
        WxPayService wxPayService = wxPayServiceMap.get(mapKey);
        if (null == wxPayService) {
            wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            wxPayServiceMap.put(mapKey, wxPayService);
        }

        return wxPayService;
    }

    /**
     * 微信配置参数校验
     * 
     * @param wxOrderReq
     *            微信配置信息
     * @param type
     *            类型，1：支付，2：查询，3：退款，4：打款，5：关闭订单
     * @return
     */
    private static ResultDTO<String> checkConfigParams(WxOrderReqDTO wxOrderReq, long type) {
        ResultDTO<String> result = new ResultDTO<>();
        if (null == wxOrderReq) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少微信参数配置");
        }
        if (StringUtils.isEmpty(wxOrderReq.getAppId())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少微信appId");
        }
        if (StringUtils.isEmpty(wxOrderReq.getMchId())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少微信商户号");
        }
        if (StringUtils.isEmpty(wxOrderReq.getMchKey())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少微信商户秘钥");
        }
        if (type == 1) {
            if (StringUtils.isEmpty(wxOrderReq.getNotifyUrl())) {
                return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少支付回调地址");
            }
        } else if (type == 3 || type == 4) {
            if (StringUtils.isEmpty(wxOrderReq.getKeyPath())) {
                return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少证书");
            }
        }
        return result.set(ResultCodeEnum.SUCCESS, "成功");
    }
    
    /**
     * XML转MAP
     * 
     * @param xmlString
     * @return
     * @throws DocumentException
     */
    public static Map<String, Object> xml2map(String xmlString)
        throws DocumentException {
      Document doc = DocumentHelper.parseText(xmlString);
      Element rootElement = doc.getRootElement();
      Map<String, Object> map = new HashMap<String, Object>();
      ele2map(map, rootElement);
      return map;
    }

    /***
     * 核心方法，里面有递归调用
     * 
     * @param map
     * @param ele
     */
    @SuppressWarnings("unchecked")
    static void ele2map(Map<String, Object> map, Element ele) {
      // 获得当前节点的子节点
      List<Element> elements = ele.elements();
      if (elements.size() == 0) {
        // 没有子节点说明当前节点是叶子节点，直接取值即可
        map.put(ele.getName(), ele.getText());
      } else if (elements.size() == 1) {
        // 只有一个子节点说明不用考虑list的情况，直接继续递归即可
        Map<String, Object> tempMap = new HashMap<String, Object>();
        ele2map(tempMap, elements.get(0));
        map.put(ele.getName(), tempMap);
      } else {
        // 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
        // 构造一个map用来去重
        Map<String, Object> tempMap = new HashMap<String, Object>();
        for (Element element : elements) {
          tempMap.put(element.getName(), null);
        }
        Set<String> keySet = tempMap.keySet();
        for (String string : keySet) {
          Namespace namespace = elements.get(0).getNamespace();
          List<Element> elements2 = ele.elements(new QName(string, namespace));
          // 如果同名的数目大于1则表示要构建list
          if (elements2.size() > 1) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Element element : elements2) {
              Map<String, Object> tempMap1 = new HashMap<String, Object>();
              ele2map(tempMap1, element);
              list.add(tempMap1);
            }
            map.put(string, list);
          } else {
            // 同名的数量不大于1则直接递归去
            ele2map(map, elements2.get(0));
          }
        }
      }
    }

    public static void main(String[] args) {
        try {

            String xml = "<xml><appid><![CDATA[wx06da328151afb124]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1491101472]]></mch_id><nonce_str><![CDATA[1509691699900]]></nonce_str><openid><![CDATA[oa93M00HG19_VrNdja22PTU2huu4]]></openid><out_trade_no><![CDATA[PP17113117]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[49E52E9A2375A4BC579D7B8A080AECF1]]></sign><time_end><![CDATA[20171103145059]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4200000026201711032174568822]]></transaction_id></xml>";
            Map<String, Object> map=xml2map(xml);
            Map<String, String> map2=new TreeMap<>();
            for(Entry<String, Object> entry : map.entrySet()){
                System.out.println(entry.getKey()+":"+entry.getValue());
                map2.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            String sign=SignUtils.createSign(map2, null, "3dba48fff334c8272c0bc557d6f239de", false);
            System.out.println(sign);
            System.out.println("49E52E9A2375A4BC579D7B8A080AECF1");
            System.out.println(SignUtils.checkSign(map2, null, "3dba48fff334c8272c0bc557d6f239de"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
