package xyz.tostring.cloud.errands.service.assist.get.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tb_refund")
public class TableRefundDO {
    @Id
    private Long id;
    @Column
    private Long orderId;
    @Column
    private Double orderFee;
    @Column
    private Double refundFee;
    @Column
    private Double settlementRefundFee;
    @Column
    private String refundContent;
    @Column
    private Date preRefundTime;
    @Column
    private Date refundTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(Double orderFee) {
        this.orderFee = orderFee;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public Double getSettlementRefundFee() {
        return settlementRefundFee;
    }

    public void setSettlementRefundFee(Double settlementRefundFee) {
        this.settlementRefundFee = settlementRefundFee;
    }

    public String getRefundContent() {
        return refundContent;
    }

    public void setRefundContent(String refundContent) {
        this.refundContent = refundContent;
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
}
