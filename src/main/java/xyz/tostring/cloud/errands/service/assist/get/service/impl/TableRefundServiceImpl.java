package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.tostring.cloud.errands.common.service.util.SnowflakeIdWorker;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableRefundDO;
import xyz.tostring.cloud.errands.service.assist.get.repository.TableRefundRepository;
import xyz.tostring.cloud.errands.service.assist.get.service.TableRefundService;

@Service
public class TableRefundServiceImpl implements TableRefundService {
    @Autowired
    private TableRefundRepository tableRefundRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public void createTableRefund(TableRefundDO tableRefundDO) {
        if (null == tableRefundRepository.findAllByOrderId(tableRefundDO.getOrderId())) {
            tableRefundDO.setId(snowflakeIdWorker.nextId());
            tableRefundRepository.save(tableRefundDO);
        }

    }

    @Override
    public void updateTableRefund(TableRefundDO tableRefundDO) {
        tableRefundRepository.save(tableRefundDO);
    }

    @Override
    public TableRefundDO getById(Long id) {
        return tableRefundRepository.findById(id).orElse(null);
    }

    @Override
    public TableRefundDO getByOrderId(Long orderId) {
        return tableRefundRepository.findAllByOrderId(orderId);
    }
}
