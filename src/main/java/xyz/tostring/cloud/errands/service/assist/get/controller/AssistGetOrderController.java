package xyz.tostring.cloud.errands.service.assist.get.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.tostring.cloud.errands.common.dto.BaseResult;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.service.AssistGetOrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class AssistGetOrderController {

    @Autowired
    private AssistGetOrderService assistGetOrderService;

    @PostMapping("create")
    public BaseResult create(@RequestBody AssistGetOrderDO assistGetOrderDO) {
        assistGetOrderDO = assistGetOrderService.createOrder(assistGetOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDO);
    }

    @PostMapping("payment")
    public BaseResult payment(@RequestBody AssistGetOrderDO assistGetOrderDO) {
        assistGetOrderDO = assistGetOrderService.paySuccess(assistGetOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDO);
    }

    @PostMapping("finish")
    private BaseResult finish(@RequestBody AssistGetOrderDO assistGetOrderDO) {
        assistGetOrderDO = assistGetOrderService.orderFinish(assistGetOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDO);
    }

    @GetMapping("list")
    public BaseResult listAll() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listAll();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("{id}")
    public BaseResult getById(@PathVariable("id") Long id) {
        AssistGetOrderDO assistGetOrderDO = assistGetOrderService.getById(id);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDO);
    }

    @GetMapping("list/{userOpenId}")
    public BaseResult listByUserOpenId(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenId(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/payment/{userOpenId}")
    public BaseResult listByUserOpenIdAndPayment(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndPayment(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/not-payment/{userOpenId}")
    public BaseResult listByUserOpenIdAndNotPayment(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndNotPayment(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/closed/{userOpenId}")
    public BaseResult listByUserOpenIdAndClosed(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndClosed(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/finished/{userOpenId}")
    public BaseResult listByUserOpenIdAndFinished(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndFinished(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/payment")
    public BaseResult listByPayStatusYes() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByPayment();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/list/not-payment")
    public BaseResult listByPayStatusNot() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByNotPayment();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/closed")
    public BaseResult listByClosed() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByClosed();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("list/finished")
    public BaseResult listByFinished() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByFinished();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

}
