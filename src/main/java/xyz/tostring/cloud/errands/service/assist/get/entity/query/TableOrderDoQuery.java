package xyz.tostring.cloud.errands.service.assist.get.entity.query;

public class TableOrderDoQuery {
    private String userOpenId;
    private String receiverCollegeName;
    private Integer orderStatus;
    private Integer page;
    private Integer size;
    private String sort;

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getReceiverCollegeName() {
        return receiverCollegeName;
    }

    public void setReceiverCollegeName(String receiverCollegeName) {
        this.receiverCollegeName = receiverCollegeName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
