package xyz.tostring.cloud.errands.service.assist.get.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tb_assist_get_order")
public class AssistGetOrderDO {
    @Id
    private Long id;
    @Column
    private String userOpenId;
    @Column
    private String addresseeName;
    @Column
    private String addresseeTailNum;
    @Column
    private String addresseePhoneNum;
    @Column
    private String addresseeAddr;
    @Column
    private String expressCompany;
    @Column
    private String expressSerialNum;
    @Column
    private Double expressWeight;
    @Column
    private Double orderSum;
    @Column
    private Integer payStatus;
    @Column
    private Date createTime;
    @Column
    private Date paySuccessTime;
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

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getAddresseeTailNum() {
        return addresseeTailNum;
    }

    public void setAddresseeTailNum(String addresseeTailNum) {
        this.addresseeTailNum = addresseeTailNum;
    }

    public String getAddresseePhoneNum() {
        return addresseePhoneNum;
    }

    public void setAddresseePhoneNum(String addresseePhoneNum) {
        this.addresseePhoneNum = addresseePhoneNum;
    }

    public String getAddresseeAddr() {
        return addresseeAddr;
    }

    public void setAddresseeAddr(String addresseeAddr) {
        this.addresseeAddr = addresseeAddr;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressSerialNum() {
        return expressSerialNum;
    }

    public void setExpressSerialNum(String expressSerialNum) {
        this.expressSerialNum = expressSerialNum;
    }

    public Double getExpressWeight() {
        return expressWeight;
    }

    public void setExpressWeight(Double expressWeight) {
        this.expressWeight = expressWeight;
    }

    public Double getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Double orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(Date paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public Date getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(Date latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }
}
