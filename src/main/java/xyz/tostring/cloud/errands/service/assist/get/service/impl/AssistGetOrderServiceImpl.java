package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.tostring.cloud.errands.common.service.util.SnowflakeIdWorker;
import xyz.tostring.cloud.errands.service.assist.get.dao.AssistGetOrderDao;
import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.service.AssistGetOrderService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssistGetOrderServiceImpl implements AssistGetOrderService {

    private static final int YES_PAY_STATUS = 1;
    private static final int NOT_PAY_STATUS = 0;

    @Autowired
    private AssistGetOrderDao assistGetOrderDao;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Value("${express.weight.level0}")
    private double level0;

    @Value("${express.weight.level1}")
    private double level1;

    @Value("${express.weight.level2}")
    private double level2;


    @Override
    public AssistGetOrderDO createOrder(AssistGetOrderDO assistGetOrderDO) {
        Date date = new Date();
        assistGetOrderDO.setId(snowflakeIdWorker.nextId());
        int weightLevel = assistGetOrderDO.getExpressWeightLevel();
        assistGetOrderDO.setOrderSum(calculateOrderSum(weightLevel));
        assistGetOrderDO.setPayStatus(0);
        assistGetOrderDO.setCreateTime(date);
        assistGetOrderDO.setLatestUpdateTime(date);
        assistGetOrderDao.save(assistGetOrderDO);
        return assistGetOrderDO;
    }

    @Override
    public AssistGetOrderDO paySuccess(AssistGetOrderDO assistGetOrderDO) {
        Long id = assistGetOrderDO.getId();
        Optional<AssistGetOrderDO> byId = assistGetOrderDao.findById(id);
        if (byId.isPresent()) {
            Date date = new Date();
            assistGetOrderDO = byId.get();
            assistGetOrderDO.setPayStatus(YES_PAY_STATUS);
            assistGetOrderDO.setLatestUpdateTime(date);
            assistGetOrderDao.save(assistGetOrderDO);
            return assistGetOrderDO;
        } else {
            return null;
        }
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

    private double calculateOrderSum(int expressWeightLevel) {
        switch (expressWeightLevel) {
            case 0:
                return level0;
            case 1:
                return level1;
            case 2:
                return level2;
            default:
                return 0;
        }
    }
}
