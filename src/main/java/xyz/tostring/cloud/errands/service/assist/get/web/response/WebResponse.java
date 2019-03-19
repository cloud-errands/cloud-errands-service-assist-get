package xyz.tostring.cloud.errands.service.assist.get.web.response;

import xyz.tostring.cloud.errands.service.assist.get.entity.AssistGetOrderDO;

import java.util.List;

public class WebResponse {
    private Integer code;
    private String result;
    private List<AssistGetOrderDO> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<AssistGetOrderDO> getData() {
        return data;
    }

    public void setData(List<AssistGetOrderDO> data) {
        this.data = data;
    }
}
