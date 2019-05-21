package xyz.tostring.cloud.errands.service.assist.get.service;

import xyz.tostring.cloud.errands.service.assist.get.entity.TableRefundDO;

public interface TableRefundService {
    void createTableRefund(TableRefundDO tableRefundDO);
    void updateTableRefund(TableRefundDO tableRefundDO);
    TableRefundDO getById(Long id);
    TableRefundDO getByOrderId(Long orderId);
}
