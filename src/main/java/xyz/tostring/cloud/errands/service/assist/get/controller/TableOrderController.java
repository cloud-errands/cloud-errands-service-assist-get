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
    @PostMapping("payment")
    public BaseResult payment(@RequestBody TableOrderDO tableOrderDO) {
        tableOrderDO = tableOrderService.paySuccess(tableOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(tableOrderDO);
    }

    @CrossOrigin
    @PostMapping("finish")
    private BaseResult finish(@RequestBody TableOrderDO tableOrderDO) {
        tableOrderDO = tableOrderService.orderFinish(tableOrderDO);
        BaseResult baseResult = new BaseResult();
        return baseResult.ok(tableOrderDO);
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
