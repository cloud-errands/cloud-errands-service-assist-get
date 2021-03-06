package xyz.tostring.cloud.errands.service.assist.get.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tb_order")
public class TableOrderDO {
    @Id
    private Long id;
    @Column
    private String userOpenId;
    @Column
    private Long receiveInfoId;
    @Column
    private Integer collegeId;
    @Column
    private String expressCompany;
    @Column
    private String expressTailNum;
    @Column
    private String expressReceiveName;
    @Column
    private String expressSerialNum;
    @Column
    private Integer expressWeightLevel;
    @Column
    private Date expressArriveDate;
    @Column
    private String expressType;
    @Column
    private Integer expressCount;
    @Column
    private Integer orderStatus;
    @Column
    private Double orderPayment;
    @Column
    private String leaveMessage;
    @Column
    private String evaluateContent;
    @Column
    private Integer evaluateStars;
    @Column
    private Date createTime;
    @Column
    private Date closeTime;
    @Column
    private Date paymentTime;
    @Column
    private Date refundTime;
    @Column
    private Date preRefundTime;
    @Column
    private Date acceptTime;
    @Column
    private Date finishTime;
    @Column
    private Date evaluateTime;
    @Column
    private Date endTime;
    @Column
    private Date latestUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public Long getReceiveInfoId() {
        return receiveInfoId;
    }

    public void setReceiveInfoId(Long receiveInfoId) {
        this.receiveInfoId = receiveInfoId;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressTailNum() {
        return expressTailNum;
    }

    public void setExpressTailNum(String expressTailNum) {
        this.expressTailNum = expressTailNum;
    }

    public String getExpressReceiveName() {
        return expressReceiveName;
    }

    public void setExpressReceiveName(String expressReceiveName) {
        this.expressReceiveName = expressReceiveName;
    }

    public String getExpressSerialNum() {
        return expressSerialNum;
    }

    public void setExpressSerialNum(String expressSerialNum) {
        this.expressSerialNum = expressSerialNum;
    }

    public Integer getExpressWeightLevel() {
        return expressWeightLevel;
    }

    public void setExpressWeightLevel(Integer expressWeightLevel) {
        this.expressWeightLevel = expressWeightLevel;
    }

    public Date getExpressArriveDate() {
        return expressArriveDate;
    }

    public void setExpressArriveDate(Date expressArriveDate) {
        this.expressArriveDate = expressArriveDate;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public Integer getExpressCount() {
        return expressCount;
    }

    public void setExpressCount(Integer expressCount) {
        this.expressCount = expressCount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(Double orderPayment) {
        this.orderPayment = orderPayment;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public Integer getEvaluateStars() {
        return evaluateStars;
    }

    public void setEvaluateStars(Integer evaluateStars) {
        this.evaluateStars = evaluateStars;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPreRefundTime() {
        return preRefundTime;
    }

    public void setPreRefundTime(Date preRefundTime) {
        this.preRefundTime = preRefundTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(Date latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }
}
