package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.tostring.cloud.errands.common.service.util.DateUtil;
import xyz.tostring.cloud.errands.service.assist.get.config.WXConfig;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableRefundDO;
import xyz.tostring.cloud.errands.service.assist.get.repository.TableRefundRepository;
import xyz.tostring.cloud.errands.service.assist.get.service.TableOrderService;
import xyz.tostring.cloud.errands.service.assist.get.service.WXPayService;
import xyz.tostring.cloud.errands.service.assist.get.util.AESUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {

    @Value("${wx.pay-notify.url}")
    private String payNotifyUrl;

    @Value("${wx.refund-notify.url}")
    private String refundNotifyUrl;

    @Autowired
    private WXConfig wxConfig;

    @Autowired
    private TableOrderService tableOrderService;

    @Autowired
    private TableRefundRepository tableRefundRepository;

    @Override
    public Map<String, String> unifiedOrder(String orderId, String openId, String ipAddress) throws Exception{

        TableOrderDO byId = tableOrderService.getById(Long.parseLong(orderId));
        if (null != byId) {
            WXPay wxPay = new WXPay(wxConfig);
            String total_fee = String.valueOf((int) (byId.getOrderPayment() * 100));

            Map<String, String> data = new HashMap<>();
            data.put("body", "云跑科技-代拿服务");
            data.put("out_trade_no", orderId);
            data.put("fee_type", "CNY");
            data.put("total_fee", total_fee);
            data.put("spbill_create_ip", ipAddress);
            data.put("notify_url", payNotifyUrl);
            data.put("trade_type", "JSAPI");
            data.put("openid", openId);
            data.put("sign_type", WXPayConstants.HMACSHA256);
            Map<String, String> resp = wxPay.unifiedOrder(data);

            Map<String, String> resultMap = new HashMap<>();
            String return_code = (String) resp.get("return_code");
            String result_code = (String) resp.get("result_code");
            String nonceStr = WXPayUtil.generateNonceStr();
            resultMap.put("nonceStr", nonceStr);
            long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepay_id = resp.get("prepay_id");
                resultMap.put("package", "prepay_id=" + prepay_id);
                resultMap.put("signType", WXPayConstants.HMACSHA256);
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                resultMap.put("timeStamp", timeStamp + "");
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                resultMap.put("appId", wxConfig.getAppID());
                String sign = WXPayUtil.generateSignature(resultMap, wxConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
                resultMap.put("paySign", sign);
                resultMap.remove("appId");
                return resultMap;
            }else {
                return resultMap;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> refund(String orderId, String refundId) throws Exception {
        TableOrderDO byId = tableOrderService.getById(Long.parseLong(orderId));
        if (null != byId) {
            WXPay wxPay = new WXPay(wxConfig);
            String fee = String.valueOf((int) (byId.getOrderPayment() * 100));

            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", orderId);
            data.put("out_refund_no", refundId);
            data.put("total_fee", fee);
            data.put("refund_fee", fee);
            data.put("notify_url", refundNotifyUrl);
            data.put("sign_type", WXPayConstants.HMACSHA256);
            Map<String, String> responseMap = wxPay.refund(data);

            return responseMap;
        }
        return null;
    }

    @Override
    public Map<String, String> orderQuery(String orderId) throws Exception {
        TableOrderDO byId = tableOrderService.getById(Long.parseLong(orderId));
        if (null != byId) {
            WXPay wxPay = new WXPay(wxConfig);

            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", orderId);
            return wxPay.unifiedOrder(data);
        }
        return null;
    }

    @Override
    public String doWXPayNotify(Map<String, String> notifyMap) {
        String return_code = notifyMap.get("return_code");
        String result_code = notifyMap.get("result_code");

        if (WXPayConstants.SUCCESS.equals(return_code) && WXPayConstants.SUCCESS.equals(result_code)) {
            double total_fee = Double.valueOf(notifyMap.get("total_fee")) / 100;
            Long out_trade_no = Long.parseLong(notifyMap.get("out_trade_no").split("O")[0]);
            Date accountTime = DateUtil.stringtoDate(notifyMap.get("time_end"), "yyyyMMddHHmmss");

            TableOrderDO tableOrderDO = tableOrderService.getById(out_trade_no);
            if (null != tableOrderDO && total_fee == tableOrderDO.getOrderPayment()) {
                tableOrderService.paySuccess(out_trade_no, accountTime);
            }

            return WXPayConstants.SUCCESS;
        }
        return WXPayConstants.FAIL;
    }

    @Override
    public String doWXRefundNotify(Map<String, String> notifyMap) throws Exception{
        String return_code = notifyMap.get("return_code");
        if (WXPayConstants.SUCCESS.equals(return_code)) {
            String decodeStr = AESUtil.decryptData(notifyMap.get("req_info"), wxConfig.getKey());
            Map<String, String> decodeMap = WXPayUtil.xmlToMap(decodeStr);
            if (WXPayConstants.SUCCESS.equals(decodeMap.get("refund_status"))) {

                Long orderId = Long.parseLong(decodeMap.get("out_trade_no"));
                Integer totalFee = Integer.parseInt(decodeMap.get("total_fee"));
                Integer settlementRefundFee = Integer.parseInt(decodeMap.get("settlement_refund_fee"));
                Integer refundFee = Integer.parseInt(decodeMap.get("refund_fee"));
                Date successTime = DateUtil.stringtoDate(decodeMap.get("success_time"), "yyyy-MM-dd HH:mm:ss");

                if (null != tableRefundRepository.findAllByOrderId(orderId)) {
                    tableOrderService.refundOrder(orderId, successTime);

                    TableRefundDO tableRefundDO = tableRefundRepository.findAllByOrderId(orderId);
                    tableRefundDO.setOrderFee(totalFee / 100.0);
                    tableRefundDO.setSettlementRefundFee(settlementRefundFee / 100.0);
                    tableRefundDO.setRefundFee(refundFee / 100.0);
                    tableRefundDO.setRefundTime(successTime);
                    tableRefundRepository.save(tableRefundDO);
                }
                return WXPayConstants.SUCCESS;
            }
        }
        return WXPayConstants.FAIL;
    }
}
