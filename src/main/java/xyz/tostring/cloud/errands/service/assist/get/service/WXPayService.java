package xyz.tostring.cloud.errands.service.assist.get.service;

import java.util.Map;

public interface WXPayService {
    /**
     * 统一下单
     */
    Map<String, String> unifiedOrder(String orderId, String openId, String ipAddress) throws Exception;

    /**
     * 订单查询
     */
    Map<String, String> orderQuery(String orderId) throws Exception;

    /**
     * 申请退款
     */
    Map<String, String> refund(String orderId, String refundId) throws Exception;

    /**
     * 处理支付通知
     */
    String doWXPayNotify(Map<String, String> notifyMap);

    /**
     * 处理退款通知
     */

    String doWXRefundNotify(Map<String, String> notifyMap) throws Exception;
}
