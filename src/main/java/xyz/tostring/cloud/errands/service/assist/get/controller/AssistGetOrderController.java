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

    @PostMapping("/")
    public BaseResult save(AssistGetOrderDO assistGetOrderDO) {
        assistGetOrderService.save(assistGetOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok();
    }

    @GetMapping("/list")
    public BaseResult listAll() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listAll();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/{id}")
    public BaseResult getById(@PathVariable("id") Long id) {
        AssistGetOrderDO assistGetOrderDO = assistGetOrderService.getById(id);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDO);
    }

    @GetMapping("/list/{userOpenId}")
    public BaseResult listByUserOpenId(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenId(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/list/{userOpenId}/yes")
    public BaseResult listByUserOpenIdAndPayStatusYes(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndPayStatusYes(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/list/{userOpenId}/not")
    public BaseResult listByUserOpenIdAndPayStatusNot(@PathVariable("userOpenId") String userOpenId) {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByUserOpenIdAndPayStatusNot(userOpenId);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/list/yes")
    public BaseResult listByPayStatusYes() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByPayStatusYes();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

    @GetMapping("/list/not")
    public BaseResult listByPayStatusNot() {
        List<AssistGetOrderDO> assistGetOrderDOList = assistGetOrderService.listByPayStatusNot();
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(assistGetOrderDOList);
    }

}
