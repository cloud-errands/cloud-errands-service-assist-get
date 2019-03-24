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

    private static final int NOT_PAYMENT_STATUS = 0;
    private static final int PAYMENT_STATUS = 1;
    private static final int CLOSE_STATUS = 2;
    private static final int FINISH_STATUS = 3;

    private static final int TIME_OUT = 30 * 60 * 1000;
    private static final int FINISH_TIME = 3 * 24 * 60 * 60 * 1000;

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
        assistGetOrderDO.setOrderPayment(calculateOrderSum(weightLevel));
        assistGetOrderDO.setOrderStatus(NOT_PAYMENT_STATUS);
        assistGetOrderDO.setCreateTime(date);
        assistGetOrderDO.setCloseTime(new Date(date.getTime() + TIME_OUT));
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
            assistGetOrderDO.setOrderStatus(PAYMENT_STATUS);
            assistGetOrderDO.setLatestUpdateTime(date);
            assistGetOrderDO.setEndTime(new Date(date.getTime() + FINISH_TIME));
            assistGetOrderDao.save(assistGetOrderDO);
            return assistGetOrderDO;
        } else {
            return null;
        }
    }

    @Override
    public AssistGetOrderDO orderFinish(AssistGetOrderDO assistGetOrderDO) {
        Long id = assistGetOrderDO.getId();
        Optional<AssistGetOrderDO> byId = assistGetOrderDao.findById(id);
        if (byId.isPresent()) {
            Date date = new Date();
            assistGetOrderDO = byId.get();
            assistGetOrderDO.setOrderStatus(FINISH_STATUS);
            assistGetOrderDO.setEndTime(date);
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
    public List<AssistGetOrderDO> listByUserOpenIdAndPayment(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeDesc(userOpenId, PAYMENT_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenIdAndNotPayment(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeDesc(userOpenId, NOT_PAYMENT_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenIdAndClosed(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeDesc(userOpenId, CLOSE_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByUserOpenIdAndFinished(String userOpenId) {
        return assistGetOrderDao.findAllByUserOpenIdAndOrderStatusOrderByLatestUpdateTimeDesc(userOpenId, FINISH_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByNotPayment() {
        return assistGetOrderDao.findAllByOrderStatusOrderByLatestUpdateTimeAsc(NOT_PAYMENT_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByPayment() {
        return assistGetOrderDao.findAllByOrderStatusOrderByLatestUpdateTimeDesc(PAYMENT_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByClosed() {
        return assistGetOrderDao.findAllByOrderStatusOrderByLatestUpdateTimeDesc(CLOSE_STATUS);
    }

    @Override
    public List<AssistGetOrderDO> listByFinished() {
        return assistGetOrderDao.findAllByOrderStatusOrderByLatestUpdateTimeDesc(FINISH_STATUS);
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
