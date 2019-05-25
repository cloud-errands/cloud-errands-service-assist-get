package xyz.tostring.cloud.errands.service.assist.get.service;

import org.springframework.data.domain.Page;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.entity.query.TableOrderDoQuery;

import javax.persistence.Table;
import java.util.Date;

public interface TableOrderService {
    TableOrderDO createOrder(TableOrderDO assistGetOrderDO);
    void cancelOrder(Long orderId);
    void paySuccess(Long out_trade_no, Date accountTime);
    String preRefundOrder(Long orderId, String refundContent);
    void refundOrder(Long out_trade_no, Date accountTime);
    void acceptOrder(Long orderId);
    void finishOrder(Long orderId);
    void evaluateOrder(Long orderId, String content, Integer stars);
    void endOrder(Long orderId);
    Page<TableOrderDO> listAllCriteria(TableOrderDoQuery tableOrderDoQuery);
    TableOrderDO getById(Long id);
}
