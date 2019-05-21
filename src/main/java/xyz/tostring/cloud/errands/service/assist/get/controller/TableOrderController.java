package xyz.tostring.cloud.errands.service.assist.get.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import xyz.tostring.cloud.errands.common.dto.BaseResult;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.entity.query.TableOrderDoQuery;
import xyz.tostring.cloud.errands.service.assist.get.service.TableOrderService;

@RestController
@RequestMapping("/order")
public class TableOrderController {

    @Autowired
    private TableOrderService tableOrderService;

    @CrossOrigin
    @PostMapping("create")
    public BaseResult create(@RequestBody TableOrderDO tableOrderDO) {
        tableOrderDO = tableOrderService.createOrder(tableOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(tableOrderDO);
    }

    @CrossOrigin
    @PostMapping("cancel")
    public BaseResult cancel(Long orderId) {
        BaseResult baseResult = new BaseResult();
        tableOrderService.cancelOrder(orderId);
        return baseResult.ok();
    }

    @CrossOrigin
    @PostMapping("refund")
    public BaseResult refund(Long orderId, String refundContent) {
        BaseResult baseResult = new BaseResult();
        String result = tableOrderService.preRefundOrder(orderId, refundContent);
        return baseResult.ok(result);
    }

    @CrossOrigin
    @PostMapping("accept")
    public BaseResult accept(Long orderId) {
        BaseResult baseResult = new BaseResult();
        tableOrderService.acceptOrder(orderId);
        return baseResult.ok();
    }

    @CrossOrigin
    @PostMapping("finish")
    public BaseResult finish(Long orderId) {
        BaseResult baseResult = new BaseResult();
        tableOrderService.finishOrder(orderId);
        return baseResult.ok();
    }

    @CrossOrigin
    @PostMapping("evaluate")
    public BaseResult evaluate(Long orderId, String content) {
        BaseResult baseResult = new BaseResult();
        tableOrderService.evaluateOrder(orderId, content);
        return baseResult.ok();
    }

    @CrossOrigin
    @PostMapping("list")
    public BaseResult listAll(@RequestBody TableOrderDoQuery tableOrderDoQuery) {
        Page<TableOrderDO> tableOrderDOPage = tableOrderService.listAllCriteria(tableOrderDoQuery);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(tableOrderDOPage);
    }

    @CrossOrigin
    @GetMapping("{id}")
    public BaseResult getById(@PathVariable("id") Long id) {
        TableOrderDO tableOrderDO = tableOrderService.getById(id);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(tableOrderDO);
    }
}
