/*
 * PayResultServiceImpl.java Created on 2017年10月13日 上午11:33:03
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
package com.wst.pay.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.constant.PayConstant.PayChnEnum;
import com.wst.constant.PayConstant.PaySourceChnEnum;
import com.wst.constant.PayConstant.PayTypeEnum;
import com.wst.constant.PayConstant.TradeSourceEnum;
import com.wst.constant.PayConstant.TradeStatusEnum;
import com.wst.constant.PayConstant.TradeTypeEnum;
import com.wst.constant.RedisConstant;
import com.wst.constant.UserConstant.UserLevelTypeEnmu;
import com.wst.lesson.dao.CustomClassDao;
import com.wst.lesson.dao.CustomDao;
import com.wst.mem.dao.UserDao;
import com.wst.order.dao.OrderDao;
import com.wst.order.dao.PayChnConfigDao;
import com.wst.order.dao.PaymentDao;
import com.wst.order.helper.OrderHelper;
import com.wst.pay.dao.WxOrderReqDTO;
import com.wst.pay.dao.WxOrderRespDTO;
import com.wst.pay.dao.WxPrePayReqDTO;
import com.wst.pay.dao.WxPrePayRespDTO;
import com.wst.pay.module.WxPaymentModule;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.dto.PayChnConfigDTO;
import com.wst.service.order.dto.PaymentDTO;
import com.wst.service.pay.dto.AppPaymentResp;
import com.wst.service.pay.service.PayResultService;

/**
 * 支付回调结果处理
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class PayResultServiceImpl implements PayResultService {

    private static HiwiLog loger = HiwiLogFactory.getLogger(PayResultServiceImpl.class);

    @Resource
    private WxPaymentModule wxPaymentModule;

    @Resource
    private OrderDao orderDao;

    @Resource
    private PaymentDao paymentDao;

    @Resource
    private PayChnConfigDao payChnConfigDao;

    @Resource
    private UserDao userDao;

    @Resource
    private CustomDao customDao;

    @Resource
    private CustomClassDao customClassDao;

    @Resource
    private MssageService mssageService;

    @Override
    public ResultDTO<String> payCallbackResult(String wxRespInfo) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (StringUtils.isEmpty(wxRespInfo)) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "回调数据为空");
        }
        ResultDTO<WxOrderRespDTO> callbackResult = WxPaymentModule.payNotify(wxRespInfo);
        if (!callbackResult.isSuccess()) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, callbackResult.getErrDesc());
        }
        WxOrderRespDTO wxOrderRespDTO = callbackResult.getResult();
        PaymentDTO paymentDTO = paymentDao.getPaymentByPayNo(wxOrderRespDTO.getOrderNo());
        if (paymentDTO == null) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "支付记录为空");
        }
        String orderCode = paymentDTO.getOrderCode();// 平台订单号
        String outTradeNo = wxOrderRespDTO.getOutOrderNo();
        long orderStatus = wxOrderRespDTO.getOrderStatus();

        boolean isPayStatus = false;
        if (orderStatus == 1) {
            isPayStatus = true;
        }

        // 为了防止并发重复回调
        if (RedisClient.exists(RedisConstant.PAY_CALLBACK_ + outTradeNo))
            return result.set(ResultCodeEnum.ERROR_REQ_OFTEN, "在线支付回调过于频繁");

        RedisClient.set(RedisConstant.PAY_CALLBACK_ + outTradeNo, orderCode);
        RedisClient.expire(RedisConstant.PAY_CALLBACK_ + outTradeNo, RedisConstant.PAY_CALLBACK_EXPIRE); // 60秒过期

        ResultDTO<String> handleResult = this.payResultHandle(orderCode, outTradeNo, isPayStatus);

        if (!handleResult.isSuccess()) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, handleResult.getErrDesc());
        }

        RedisClient.del(RedisConstant.PAY_CALLBACK_ + outTradeNo);
        return result.set(ResultCodeEnum.SUCCESS, "回调处理成功");
    }

    /**
     * 支付结果的处理
     * 
     * @param orderCode
     *            订单编号
     * @param tradeNo
     *            第三方交易号
     * @param success
     *            成功标识
     * @return
     * @throws Exception
     */
    private ResultDTO<String> payResultHandle(String orderCode, String tradeNo, boolean success) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();

        OrderDTO order = orderDao.getOrderByCode(orderCode);
        if (order == null) {
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "订单不存在");
        }
        long payStatus = order.getPayStatus();
        if (payStatus != PayStatusEnum.NO_PAY.status) {
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "订单状态异常，orderCode:" + order.getOrderCode());
        }

        // 获取待更新支付记录
        PaymentDTO payment = paymentDao.getPaymentByOrderCode(order.getOrderCode());
        if (success) {
            orderDao.paySuccess(orderCode); // 更新订单表

            paymentDao.upPayStatusByPayId(payment.getPayId(), tradeNo, TradeStatusEnum.PAY_SUCCESS.status); // 更新支付记录表

            // 购买定制课成功，用户级别变更为vip
            if (order.getOrderType() == OrderTypeEnmu.CUSTOM_CLASS_ORDER.type) {
                userDao.updateUserLevel(order.getUserId(), UserLevelTypeEnmu.VIP.type);

                customDao.buyCustom(order.getBusiId());

                // 给涉课导师推送消息
                mssageService.baseSendMssage(order.getUserId(), 0, order.getBusiId() + "", 2, 1);

                String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + order.getUserId();
                if (!RedisClient.exists(userLimitKey)) {
                    RedisClient.set(userLimitKey, order.getUserId());
                }
            }
        } else {
            orderDao.payFail(orderCode);

            paymentDao.upPayStatusByPayId(payment.getPayId(), tradeNo, TradeStatusEnum.PAY_FAIL.status); // 更新支付记录表
        }

        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    @Override
    public ResultDTO<AppPaymentResp> prePayment(String orderCode, UserDTO user, long payAmount, long payType,
            String reqIp) throws Exception {
        ResultDTO<AppPaymentResp> result = new ResultDTO<>();
        OrderDTO orderDTO = orderDao.getOrderByCode(orderCode);
        if (orderDTO == null) {
            // 订单不存在
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Order does not exist");
        }
        if (orderDTO.getUserId() != user.getUserId()) {
            // 用户不合法
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal");
        }
        if (orderDTO.getOrderStatus() != OrderStatusEnum.HAS_CONFIRM.status
                || orderDTO.getPayStatus() != PayStatusEnum.NO_PAY.status) {
            // 订单不合法
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Order illegal");
        }
        if (payAmount != orderDTO.getDiscountPrice()) {
            // 支付金额不正确
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The amount of payment is incorrect");
        }
        PayChnConfigDTO payChnConfig = null;
        if (payType == PayChnEnum.WX_APP.chn) { // 微信APP_Pay
            payChnConfig = payChnConfigDao.getPayConfig(PaySourceChnEnum.S_APP.chn, PayChnEnum.WX_APP.chn);
        } else if (payType == PayChnEnum.WX_JS_PAY.chn) { // 微信JS_Pay
            payChnConfig = payChnConfigDao.getPayConfig(PaySourceChnEnum.WEIXIN.chn, PayChnEnum.WX_JS_PAY.chn);
        }
        if (payChnConfig == null) {
            // 支付通道配置不存在
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Payment channel configuration does not exist");
        }
        String tradeNo = OrderHelper.getTradeNo(payType);
        JSONObject wxParams = JSONObject.parseObject(payChnConfig.getPayChnParams());
        // 发起微信预支付
        WxPrePayReqDTO wxPrePayReq = new WxPrePayReqDTO();
        wxPrePayReq.setAppId(wxParams.getString("appId"));// 微信应用id
        wxPrePayReq.setMchId(wxParams.getString("mchId"));// 商户号
        wxPrePayReq.setMchKey(wxParams.getString("paternerKey"));// 密钥key
        wxPrePayReq.setKeyPath(wxParams.getString("certPath"));// 证书路径
        wxPrePayReq.setNotifyUrl(wxParams.getString("notifyUrl"));// 回调地址
        wxPrePayReq.setBody(orderDTO.getOrderName());// 商品描述
        wxPrePayReq.setOrderNo(tradeNo);// 平台交易流水号
        wxPrePayReq.setReqIp(reqIp);// 请求ip
        wxPrePayReq.setTotalFee(payAmount);// 订单金额
        wxPrePayReq.setPayType(TradeType.APP);
        if (payChnConfig.getPayChn() == PayChnEnum.WX_JS_PAY.chn) {
            wxPrePayReq.setPayType(TradeType.JSAPI);
            wxPrePayReq.setOpenid(user.getDeputyLoginAccount());
        }

        ResultDTO<WxPrePayRespDTO> PayResult = WxPaymentModule.payment(wxPrePayReq);
        if (PayResult.isSuccess()) {
            PaymentDTO payment = new PaymentDTO();
            payment.setPayNo(tradeNo);
            payment.setOrderCode(orderCode);
            payment.setTradeSource(TradeSourceEnum.ON_PAY.type);// 线上
            payment.setPayAmount(payAmount);// 支付金额
            payment.setPayType(PayTypeEnum.WEIXIN.type);// 微信
            payment.setTradeType(TradeTypeEnum.CONSUME.type);// 消费
            payment.setTradeStatus(TradeStatusEnum.PAY_IN.status);// 支付中
            payment.setPayChnId(payChnConfig.getPayChnId());
            paymentDao.save(payment);
        }
        WxPrePayRespDTO wxPrePayRespDTO = PayResult.getResult();
        AppPaymentResp appPaymentResp = new AppPaymentResp();
        BeanUtils.copyProperties(wxPrePayRespDTO, appPaymentResp);
        loger.debug("-------预支付返回参数=" + JSONObject.toJSONString(appPaymentResp));
        return result.set(ResultCodeEnum.SUCCESS, "", appPaymentResp);
    }

    @Override
    public ResultDTO<String> closeOrder(String orderCode) throws Exception {
        ResultDTO<String> result = new ResultDTO<String>();

        OrderDTO order = orderDao.getOrderByCode(orderCode);
        if (order == null) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "订单不存在");
        }

        // 关闭订单
        orderDao.cancelOrder(order.getOrderCode());

        /*
         * 关闭支付记录
         */
        this.closePayment(orderCode);

        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    /**
     * 关闭支付记录
     * 
     * @param orderCode
     *            订单编码
     * @return
     * @throws Exception
     */
    public ResultDTO<String> closePayment(String orderCode) throws Exception {
        ResultDTO<String> result = new ResultDTO<String>();

        OrderDTO order = orderDao.getOrderByCode(orderCode);
        if (order == null) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "订单不存在");
        }

        // 线下支付不处理
        if (order.getPayType() == OrderPayTypeEnum.DOWN_PAY.type) {
            return result.set(ResultCodeEnum.SUCCESS, "");
        }

        /*
         * 关闭支付记录
         */
        PaymentDTO payment = paymentDao.getPaymentByOrderCode(order.getOrderCode());
        if (payment == null) {
            return result.set(ResultCodeEnum.SUCCESS, "");
        }

        if (payment.getTradeStatus() > 2) {
            return result.set(ResultCodeEnum.SUCCESS, "");
        }

        if (payment.getTradeStatus() == TradeStatusEnum.PAY_IN.status) {
            PayChnConfigDTO payChnConfigDTO = payChnConfigDao.getPayConfigById(payment.getPayChnId());
            if (payChnConfigDTO == null) {
                // 支付通道配置不存在
                return result.set(ResultCodeEnum.ERROR_HANDLE, "支付通道配置不存在");
            }

            if (payment.getPayType() == PayTypeEnum.WEIXIN.type) {
                // 支付时间已经超过5分钟,不在需要调用微信支付关闭订单
                if (payment.getPayTime().getTime() + 300000 > System.currentTimeMillis()) {
                    // 微信关闭订单
                    JSONObject wxParams = JSONObject.parseObject(payChnConfigDTO.getPayChnParams());
                    WxOrderReqDTO wxOrderReqDTO = new WxOrderReqDTO();
                    wxOrderReqDTO.setOrderNo(payment.getPayNo());
                    wxOrderReqDTO.setAppId(wxParams.getString("appId"));// 微信应用id
                    wxOrderReqDTO.setMchId(wxParams.getString("mchId"));// 商户号
                    wxOrderReqDTO.setMchKey(wxParams.getString("paternerKey"));// 密钥key
                    wxOrderReqDTO.setKeyPath(wxParams.getString("certPath"));// 证书路径
                    wxOrderReqDTO.setNotifyUrl(wxParams.getString("notifyUrl"));// 回调地址

                    ResultDTO<WxOrderRespDTO> CloseOrderResult = WxPaymentModule.closeOrder(wxOrderReqDTO);
                    if (!CloseOrderResult.isSuccess()) {
                        return result.set(CloseOrderResult.getErrCode(), CloseOrderResult.getErrDesc());
                    }
                }
            }
        }

        // 将支付记录置为无效
        paymentDao.upPayStatusByPayId(payment.getPayId(), payment.getThridTradeNo(), TradeStatusEnum.PAY_CLOSE.status);

        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    @Override
    public ResultDTO<String> offlinePayHandle(String orderCode, boolean success) throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        PaymentDTO payment = paymentDao.getPaymentByOrderCode(orderCode);
        if (payment == null) {
            // 支付记录不存在
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Payment record does not exist");
        }
        if (payment.getTradeSource() != TradeSourceEnum.DOWN_PAY.type
                || payment.getTradeStatus() != TradeStatusEnum.PAY_IN.status) {
            // 支付记录不合法
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The payment record is illegal");
        }

        OrderDTO order = orderDao.getOrderByCode(orderCode);
        if (success) {
            orderDao.paySuccess(orderCode);
            // 购买定制课成功，用户级别变更为vip
            userDao.updateUserLevel(order.getUserId(), UserLevelTypeEnmu.VIP.type);
            // 记录定制课购买时间
            customDao.buyCustom(order.getBusiId());
        }

        paymentDao.upPayStatusByPayId(payment.getPayId(), "",
                success ? TradeStatusEnum.PAY_SUCCESS.status : TradeStatusEnum.PAY_FAIL.status);

        return resultDTO.set(ResultCodeEnum.SUCCESS, "");
    }
}
