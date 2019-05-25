package xyz.tostring.cloud.errands.service.assist.get.service.impl;

import com.github.wxpay.sdk.WXPayConstants;
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
import xyz.tostring.cloud.errands.service.assist.get.config.DelayQueueConfig;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableRefundDO;
import xyz.tostring.cloud.errands.service.assist.get.entity.query.TableOrderDoQuery;
import xyz.tostring.cloud.errands.service.assist.get.repository.TableOrderRepository;
import xyz.tostring.cloud.errands.service.assist.get.entity.TableOrderDO;
import xyz.tostring.cloud.errands.service.assist.get.repository.TableRefundRepository;
import xyz.tostring.cloud.errands.service.assist.get.service.TableOrderService;
import xyz.tostring.cloud.errands.service.assist.get.service.WXPayService;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
public class TableOrderServiceImpl implements TableOrderService {

    private static final int CLOSE_STATUS = 0;
    private static final int NOT_PAYMENT_STATUS = 1;
    private static final int PAYMENT_STATUS = 2;
    private static final int PRE_REFUND_STATUS = 3;
    private static final int REFUND_STATUS = 4;
    private static final int ACCEPT_STATUS = 5;
    private static final int FINISH_STATUS = 6;
    private static final int END_STATUS = 7;

    private static final String PROPERTY_CREATE_TIME = "createTime";

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private TableRefundRepository tableRefundRepository;

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
    private long cancelTimestamp;

    @Value("${order.overtime.finish}")
    private long finishTimestamp;

    @Value("${order.overtime.end}")
    private long endTimestamp;

//    @Value("${order.overtime.pre-refund}")
//    private long preRefundTimestamp;

    @Override
    @Transactional
    public TableOrderDO createOrder(TableOrderDO tableOrderDO) {
        Date date = new Date();
        Long id = snowflakeIdWorker.nextId();
        tableOrderDO.setId(id);
        int weightLevel = tableOrderDO.getExpressWeightLevel();
        int count = tableOrderDO.getExpressCount();
        tableOrderDO.setOrderPayment(calculateOrderSum(weightLevel, count));
        tableOrderDO.setOrderStatus(NOT_PAYMENT_STATUS);
        tableOrderDO.setCreateTime(date);
        tableOrderDO.setLatestUpdateTime(date);
        tableOrderRepository.save(tableOrderDO);

        //创建订单延时消息
        amqpTemplate.convertAndSend(DelayQueueConfig.CUSTOM_EXCHANGE_NAME, DelayQueueConfig.CANCEL_QUEUE_NAME, String.valueOf(id), (message) -> {
            message.getMessageProperties().setHeader("x-delay", cancelTimestamp);
            return message;
        });

        return tableOrderDO;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (NOT_PAYMENT_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(CLOSE_STATUS);
                tableOrderDO.setCloseTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Transactional
    @RabbitListener(queues = DelayQueueConfig.CANCEL_QUEUE_NAME)
    public void cancelOrder(String msg) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(Long.parseLong(msg));
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (NOT_PAYMENT_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(CLOSE_STATUS);
                tableOrderDO.setCloseTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Override
    @Transactional
    public void paySuccess(Long out_trade_no, Date accountTime) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(out_trade_no);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (NOT_PAYMENT_STATUS == tableOrderDO.getOrderStatus()) {
                tableOrderDO.setOrderStatus(PAYMENT_STATUS);
                tableOrderDO.setPaymentTime(accountTime);
                tableOrderDO.setLatestUpdateTime(accountTime);
                tableOrderRepository.save(tableOrderDO);

                //创建订单延时消息
                amqpTemplate.convertAndSend(DelayQueueConfig.CUSTOM_EXCHANGE_NAME, DelayQueueConfig.FINISH_QUEUE_NAME, String.valueOf(out_trade_no), (message) -> {
                    message.getMessageProperties().setHeader("x-delay", finishTimestamp);
                    return message;
                });
            }
        }
    }

    @Override
    @Transactional
    public String preRefundOrder(Long orderId, String refundContent) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (PAYMENT_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(PRE_REFUND_STATUS);
                tableOrderDO.setPreRefundTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);

                TableRefundDO tableRefundDO = tableRefundRepository.findAllByOrderId(orderId);
                Long refundId;

                if (null == tableRefundDO) {
                    refundId = snowflakeIdWorker.nextId();
                    tableRefundDO = new TableRefundDO();
                    tableRefundDO.setId(refundId);
                    tableRefundDO.setOrderId(orderId);
                    tableRefundDO.setPreRefundTime(date);
                    tableRefundDO.setRefundContent(refundContent);
                    tableRefundRepository.save(tableRefundDO);
                } else {
                    refundId = tableRefundDO.getId();
                    tableRefundDO.setPreRefundTime(date);
                    tableRefundDO.setRefundContent(refundContent);
                    tableRefundRepository.save(tableRefundDO);
                }

                try {
                    Map<String, String> result = wxPayService.refund(String.valueOf(orderId), String.valueOf(refundId));
                    if (null != result && WXPayConstants.SUCCESS.equals(result.get("return_code")) && WXPayConstants.SUCCESS.equals(result.get("result_code"))) {

//                        amqpTemplate.convertAndSend(DelayQueueConfig.CUSTOM_EXCHANGE_NAME, DelayQueueConfig.PRE_REFUND_QUEUE_NAME, String.valueOf(orderId), message -> {
//                            message.getMessageProperties().setHeader("x-delay", preRefundTimestamp);
//                            return message;
//                        });

                        return WXPayConstants.SUCCESS;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return WXPayConstants.FAIL;
    }

//    @Transactional
//    @RabbitListener(queues = DelayQueueConfig.PRE_REFUND_QUEUE_NAME)
//    public void preRefundOrder(String msg) {
//        Optional<TableOrderDO> byId = tableOrderRepository.findById(Long.parseLong(msg));
//        if (byId.isPresent()) {
//            TableOrderDO tableOrderDO = byId.get();
//            if (PRE_REFUND_STATUS == tableOrderDO.getOrderStatus()) {
//
//                try {
//                    wxPayService.refund(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    @Transactional
    public void refundOrder(Long out_trade_no, Date accountTime) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(out_trade_no);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (PRE_REFUND_STATUS == tableOrderDO.getOrderStatus()) {
                tableOrderDO.setOrderStatus(REFUND_STATUS);
                tableOrderDO.setRefundTime(accountTime);
                tableOrderDO.setLatestUpdateTime(accountTime);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (PAYMENT_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(ACCEPT_STATUS);
                tableOrderDO.setRefundTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Transactional
    @RabbitListener(queues = DelayQueueConfig.FINISH_QUEUE_NAME)
    public void finishOrder(String msg) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(Long.parseLong(msg));
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            int orderStatus = tableOrderDO.getOrderStatus();
            if (PAYMENT_STATUS == orderStatus || ACCEPT_STATUS == orderStatus) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(FINISH_STATUS);
                tableOrderDO.setFinishTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);

                amqpTemplate.convertAndSend(DelayQueueConfig.CUSTOM_EXCHANGE_NAME, DelayQueueConfig.END_QUEUE_NAME, msg, (message) -> {
                    message.getMessageProperties().setHeader("x-delay", endTimestamp);
                    return message;
                });
            }
        }
    }

    @Override
    @Transactional
    public void finishOrder(Long orderId) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            int orderStatus = tableOrderDO.getOrderStatus();
            if (PAYMENT_STATUS == orderStatus || ACCEPT_STATUS == orderStatus) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(FINISH_STATUS);
                tableOrderDO.setFinishTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);

                amqpTemplate.convertAndSend(DelayQueueConfig.CUSTOM_EXCHANGE_NAME, DelayQueueConfig.END_QUEUE_NAME, String.valueOf(orderId), (message) -> {
                    message.getMessageProperties().setHeader("x-delay", endTimestamp);
                    return message;
                });
            }
        }
    }

    @Override
    @Transactional
    public void evaluateOrder(Long orderId, String content, Integer stars) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (FINISH_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(END_STATUS);
                tableOrderDO.setEvaluateContent(content);
                tableOrderDO.setEvaluateStars(stars);
                tableOrderDO.setEvaluateTime(date);
                tableOrderDO.setEndTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Override
    @Transactional
    public void endOrder(Long orderId) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(orderId);
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (FINISH_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(END_STATUS);
                tableOrderDO.setEndTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
        }
    }

    @Transactional
    @RabbitListener(queues = DelayQueueConfig.END_QUEUE_NAME)
    public void endOrder(String msg) {
        Optional<TableOrderDO> byId = tableOrderRepository.findById(Long.parseLong(msg));
        if (byId.isPresent()) {
            TableOrderDO tableOrderDO = byId.get();
            if (FINISH_STATUS == tableOrderDO.getOrderStatus()) {
                Date date = new Date();
                tableOrderDO.setOrderStatus(END_STATUS);
                tableOrderDO.setEndTime(date);
                tableOrderDO.setLatestUpdateTime(date);
                tableOrderRepository.save(tableOrderDO);
            }
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
        PageRequest pageRequest = pageRequest(tableOrderDoQuery.getPage(), tableOrderDoQuery.getSize(), direction, PROPERTY_CREATE_TIME);
        Page<TableOrderDO> tableOrderDOPage = tableOrderRepository.findAll((Specification<TableOrderDO>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (null != tableOrderDoQuery.getUserOpenId() && !"".equals(tableOrderDoQuery.getUserOpenId())) {
                predicateList.add(criteriaBuilder.equal(root.get("userOpenId").as(String.class), tableOrderDoQuery.getUserOpenId()));
            }
            if (null != tableOrderDoQuery.getCollegeId() && !"".equals(tableOrderDoQuery.getCollegeId())) {
                predicateList.add(criteriaBuilder.equal(root.get("collegeId").as(String.class), tableOrderDoQuery.getCollegeId()));
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


    private double calculateOrderSum(int expressWeightLevel, int count) {
        switch (expressWeightLevel) {
            case 0:
                return level0 * count;
            case 1:
                return level1 * count;
            case 2:
                return level2 * count;
            default:
                return 0;
        }
    }
}
