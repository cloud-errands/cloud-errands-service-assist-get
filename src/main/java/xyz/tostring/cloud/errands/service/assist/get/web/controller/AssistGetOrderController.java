package xyz.tostring.cloud.errands.service.assist.get.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.service.AssistGetOrderService;
import xyz.tostring.cloud.errands.service.assist.get.web.response.WebResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class AssistGetOrderController {

    private static final String SUCCESS_RESULT = "SUCCESS";

    @Autowired
    private AssistGetOrderService assistGetOrderService;

    @PostMapping("/")
    public WebResponse save(AssistGetOrderDO assistGetOrderDO) {
        assistGetOrderService.save(assistGetOrderDO);
        return object2Response(null);
    }

    @GetMapping("/list")
    public WebResponse listAll() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listAll();
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/{id}")
    public WebResponse getById(@PathVariable("id") Long id) {
        AssistGetOrderDO assistGetOrderDO = assistGetOrderService.getById(id);
        List<AssistGetOrderDO> assistGetOrderDOList = new ArrayList<>();
        assistGetOrderDOList.add(assistGetOrderDO);
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/list/{userOpenId}")
    public WebResponse listByUserOpenId(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenId(userOpenId);
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/list/{userOpenId}/yes")
    public WebResponse listByUserOpenIdAndPayStatusYes(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndPayStatusYes(userOpenId);
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/list/{userOpenId}/not")
    public WebResponse listByUserOpenIdAndPayStatusNot(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndPayStatusNot(userOpenId);
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/list/yes")
    public WebResponse listByPayStatusYes() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByPayStatusYes();
        return object2Response(assistGetOrderDOList);
    }

    @GetMapping("/list/not")
    public WebResponse listByPayStatusNot() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByPayStatusNot();
        return object2Response(assistGetOrderDOList);
    }

    private WebResponse object2Response(List<AssistGetOrderDO> assistGetOrderDOList) {
        WebResponse webResponse = new WebResponse();
        webResponse.setCode(HttpStatus.OK.value());
        webResponse.setResult(SUCCESS_RESULT);
        webResponse.setData(assistGetOrderDOList);
        return webResponse;
    }
}
