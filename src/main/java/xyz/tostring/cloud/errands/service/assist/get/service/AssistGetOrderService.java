package xyz.tostring.cloud.errands.service.assist.get.service;

import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;

import java.util.List;

public interface AssistGetOrderService {
    AssistGetOrderDO createOrder(AssistGetOrderDO assistGetOrderDO);
    AssistGetOrderDO paySuccess(AssistGetOrderDO assistGetOrderDO);
    AssistGetOrderDO orderFinish(AssistGetOrderDO assistGetOrderDO);
    List<AssistGetOrderDO> listAll();
    List<AssistGetOrderDO> listByUserOpenId(String UserOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndPayment(String userOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndNotPayment(String userOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndClosed(String userOpenId);
    List<AssistGetOrderDO> listByUserOpenIdAndFinished(String userOpenId);
    List<AssistGetOrderDO> listByPayment();
    List<AssistGetOrderDO> listByNotPayment();
    List<AssistGetOrderDO> listByClosed();
    List<AssistGetOrderDO> listByFinished();
    AssistGetOrderDO getById(Long id);
}
