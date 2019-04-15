package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.tostring.cloud.errands.common.service.util.SnowflakeIdWorker;
import xyz.tostring.cloud.errands.service.assist.get.entity.query.TableOrderDoQuery;
import xyz.tostring.cloud.errands.service.assist.get.repository.TableOrderRepository;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.service.TableOrderService;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TableOrderServiceImpl implements TableOrderService {

    private static final int NOT_PAYMENT_STATUS = 0;
    private static final int PAYMENT_STATUS = 1;
    private static final int CLOSE_STATUS = 2;
    private static final int FINISH_STATUS = 3;

    private static final String PROPERTY_LATEST_UPDATE_TIME = "latestUpdateTime";

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${express.weight.level0}")
    private double level0;

    @Value("${express.weight.level1}")
    private double level1;

    @Value("${express.weight.level2}")
    private double level2;

    @Value("${order.overtime.cancel}")
    private long cancelTime;

    @Value("${order.overtime.finish}")
    private long finishTime;


    @Override
    @Transactional
    public TableOrderDO createOrder(TableOrderDO tableOrderDO) {
        Date date = new Date();
        Long id = snowflakeIdWorker.nextId();
        tableOrderDO.setId(id);
        int weightLevel = tableOrderDO.getExpressWeightLevel();
        tableOrderDO.setOrderPayment(calculateOrderSum(weightLevel));
        tableOrderDO.setOrderStatus(NOT_PAYMENT_STATUS);
        tableOrderDO.setCreateTime(date);
        tableOrderDO.setLatestUpdateTime(date);
        tableOrderRepository.save(tableOrderDO);

        //创建订单延时消息
        amqpTemplate.convertAndSend("assist_get_delay_exchange", "assist_get_cancel_queue", String.valueOf(id), (message) -> {
            message.getMessageProperties().setHeader("x-delay", cancelTime);
            return message;
        });

        return tableOrderDO;
    }

    @Transactional
    @RabbitListener(queues = "assist_get_cancel_queue")
    public void orderCancel(String msg) {
        Date date = new Date();
        long id = Long.parseLong(msg);

        TableOrderDO order = getById(id);
        if (order != null && order.getOrderStatus() == NOT_PAYMENT_STATUS) {
            order.setOrderStatus(CLOSE_STATUS);
            order.setCloseTime(date);
            order.setLatestUpdateTime(date);
            tableOrderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public TableOrderDO paySuccess(TableOrderDO tableOrderDO) {
        Long id = tableOrderDO.getId();
        Optional<TableOrderDO> byId = tableOrderRepository.findById(id);
        if (byId.isPresent()) {
            Date date = new Date();
            tableOrderDO = byId.get();
            tableOrderDO.setOrderStatus(PAYMENT_STATUS);
            tableOrderDO.setLatestUpdateTime(date);
            tableOrderRepository.save(tableOrderDO);

            //创建订单延时消息
            amqpTemplate.convertAndSend("assist_get_delay_exchange", "assist_get_finish_queue", String.valueOf(id), (message) -> {
                message.getMessageProperties().setHeader("x-delay", finishTime);
                return message;
            });

            return tableOrderDO;
        } else {
            return null;
        }
    }

    @Transactional
    @RabbitListener(queues = "assist_get_finish_queue")
    public void orderFinish(String msg) {
        Date date = new Date();
        long id = Long.parseLong(msg);

        TableOrderDO order = getById(id);
        if (order != null && order.getOrderStatus() == PAYMENT_STATUS) {
            order.setOrderStatus(FINISH_STATUS);
            order.setEndTime(date);
            order.setLatestUpdateTime(date);
            tableOrderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public TableOrderDO orderFinish(TableOrderDO tableOrderDO) {
        Long id = tableOrderDO.getId();
        Optional<TableOrderDO> byId = tableOrderRepository.findById(id);
        if (byId.isPresent()) {
            Date date = new Date();
            tableOrderDO = byId.get();
            tableOrderDO.setOrderStatus(FINISH_STATUS);
            tableOrderDO.setEndTime(date);
            tableOrderDO.setLatestUpdateTime(date);
            tableOrderRepository.save(tableOrderDO);
            return tableOrderDO;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TableOrderDO> listAllCriteria(TableOrderDoQuery tableOrderDoQuery) {
        String sort = "";
        if (null != tableOrderDoQuery.getSort())
        sort = tableOrderDoQuery.getSort();
        Sort.Direction direction;
        switch (sort) {
            case "desc":
                direction = Sort.Direction.DESC;
                break;
            case "asc":
                direction = Sort.Direction.ASC;
                break;
            default:
                direction = Sort.Direction.DESC;
                break;
        }
        PageRequest pageRequest = pageRequest(tableOrderDoQuery.getPage(), tableOrderDoQuery.getSize(), direction, PROPERTY_LATEST_UPDATE_TIME);
        Page<TableOrderDO> tableOrderDOPage = tableOrderRepository.findAll((Specification<TableOrderDO>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (null != tableOrderDoQuery.getUserOpenId() && !"".equals(tableOrderDoQuery.getUserOpenId())) {
                predicateList.add(criteriaBuilder.equal(root.get("userOpenId").as(String.class), tableOrderDoQuery.getUserOpenId()));
            }
            if (null != tableOrderDoQuery.getReceiverCollegeName() && !"".equals(tableOrderDoQuery.getReceiverCollegeName())) {
                predicateList.add(criteriaBuilder.equal(root.get("receiverCollegeName").as(String.class), tableOrderDoQuery.getReceiverCollegeName()));
            }
            if (null != tableOrderDoQuery.getOrderStatus() && !"".equals(tableOrderDoQuery.getOrderStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("orderStatus").as(String.class), tableOrderDoQuery.getOrderStatus()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageRequest);
        return tableOrderDOPage;
    }

    @Override
    public TableOrderDO getById(Long id) {
        return tableOrderRepository.findById(id).orElse(null);
    }

    private PageRequest pageRequest(Integer page, Integer size, Sort.Direction direction, String properties) {
        if (null == page || page < 1) {
            page = 0;
        } else {
            page = page - 1;
        }
        if (null == size || size < 0) {
            size = 10;
        }
        return PageRequest.of(page, size, direction, properties);
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
