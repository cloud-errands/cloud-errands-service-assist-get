package xyz.tostring.cloud.errands.service.assist.get.service;

import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;

import java.util.List;

public interface AssistGetOrderService {
    AssistGetOrderDO createOrder(AssistGetOrderDO assistGetOrderDO);
    AssistGetOrderDO paySuccess(AssistGetOrderDO assistGetOrderDO);
    List<AssistGetOrderDO> listAll();
    List<AssistGetOrderDO> listByUserOpenId(String UserOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndPayStatusYes(String userOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndPayStatusNot(String userOpenId);
    List<AssistGetOrderDO> listByPayStatusNot();
    List<AssistGetOrderDO> listByPayStatusYes();
    AssistGetOrderDO getById(Long id);
}
