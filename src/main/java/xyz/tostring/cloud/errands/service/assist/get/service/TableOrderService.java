package xyz.tostring.cloud.errands.service.assist.get.service;

import org.springframework.data.domain.Page;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.entity.query.TableOrderDoQuery;

public interface TableOrderService {
    TableOrderDO createOrder(TableOrderDO assistGetOrderDO);
    TableOrderDO paySuccess(TableOrderDO assistGetOrderDO);
    TableOrderDO orderFinish(TableOrderDO assistGetOrderDO);
    Page<TableOrderDO> listAllCriteria(TableOrderDoQuery tableOrderDoQuery);
    TableOrderDO getById(Long id);
}