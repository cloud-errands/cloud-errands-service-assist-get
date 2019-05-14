package xyz.tostring.cloud.errands.service.assist.get.controller;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.tostring.cloud.errands.common.dto.BaseResult;
import xyz.tostring.cloud.errands.service.assist.get.config.WXConfig;
import xyz.tostring.cloud.errands.service.assist.get.service.WXPayService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("wxpay")
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private WXConfig wxConfig;

    @PostMapping("pay")
    public BaseResult pay(String orderId, String openId) throws Exception {
        BaseResult baseResult = new BaseResult();

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        Map resultMap = wxPayService.unifiedOrder(orderId, openId, ipAddress);

        return baseResult.ok(resultMap);
    }

    @RequestMapping(value = "payNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultXml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(resultXml);
        outSteam.close();
        inStream.close();

        WXPay wxPay = new WXPay(wxConfig);

        Map<String, String> returnData = new HashMap<>();
        if (!wxPay.isPayResultNotifySignatureValid(notifyMap)) {
            // 支付失败
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "return_code不正确");
            return WXPayUtil.mapToXml(returnData);
        } else {
            // 处理通知
            wxPayService.doWXNotify(notifyMap);

            returnData.put("return_code", "SUCCESS");
            returnData.put("return_msg", "OK");
            return WXPayUtil.mapToXml(returnData);
        }
    }
}
