package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.tostring.cloud.errands.service.assist.get.dao.AssistGetOrderDao;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.service.AssistGetOrderService;
import xyz.tostring.cloud.errands.service.assist.get.util.SnowflakeIdWorker;

import java.util.Date;
import java.util.List;

@Service
public class AssistGetOrderServiceImpl implements AssistGetOrderService {

    private static final int YES_PAY_STATUS = 1;
    private static final int NOT_PAY_STATUS = 0;

    @Autowired
    private AssistGetOrderDao assistGetOrderDao;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public void save(AssistGetOrderDO assistGetOrderDO) {
        Date date = new Date();

        assistGetOrderDO.setId(snowflakeIdWorker.nextId());
        if (assistGetOrderDO.getCreateTime() == null) {
            assistGetOrderDO.setCreateTime(date);
            assistGetOrderDO.setLatestUpdateTime(date);
        } else {
            assistGetOrderDO.setLatestUpdateTime(date);
        }
        assistGetOrderDao.save(assistGetOrderDO);
    }

    @Override
    public List<AssistGetOrderDO> listAll() {
        return assistGetOrderDao.findAllByOrderByLatestUpdateTimeDesc();
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenId(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdOrderByLatestUpdateTimeDesc(userOpenId);
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenIdAndPayStatusYes(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndPayStatusOrderByLatestUpdateTimeDesc(userOpenId, YES_PAY_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenIdAndPayStatusNot(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndPayStatusOrderByLatestUpdateTimeDesc(userOpenId, NOT_PAY_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByPayStatusNot() {
        return assistGetOrderDao.findAllByPayStatusOrderByLatestUpdateTimeAsc(NOT_PAY_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByPayStatusYes() {
        return assistGetOrderDao.findAllByPayStatusOrderByLatestUpdateTimeDesc(YES_PAY_STATUS);
    }

    @Override
    public AssistGetOrderDO getById(Long id) {
        return assistGetOrderDao.findById(id).orElse(null);
    }
}
