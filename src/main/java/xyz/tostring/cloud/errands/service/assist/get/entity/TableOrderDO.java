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
    private String receiverName;
    @Column
    private String receiverMobile;
    @Column
    private String receiverProvince;
    @Column
    private String receiverCity;
    @Column
    private String receiverDistrict;
    @Column
    private String receiverCollegeName;
    @Column
    private String receiverAddress;
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
    private Date createTime;
    @Column
    private Date paymentTime;
    @Column
    private Date endTime;
    @Column
    private Date closeTime;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverCollegeName() {
        return receiverCollegeName;
    }

    public void setReceiverCollegeName(String receiverCollegeName) {
        this.receiverCollegeName = receiverCollegeName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(Date latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }

    public Integer getExpressCount() {
        return expressCount;
    }

    public void setExpressCount(Integer expressCount) {
        this.expressCount = expressCount;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
