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
     * 处理支付通知
     */
    void doWXNotify(Map<String, String> notifyMap);
}
