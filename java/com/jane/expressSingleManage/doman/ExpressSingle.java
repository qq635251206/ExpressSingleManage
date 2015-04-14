package com.jane.expressSingleManage.doman;

/** 快递单 模型
 * Created by Jane on 2015/4/4.
 */
public class ExpressSingle {
    /**
     * id
     */
    private int id;
    /**
     * 单号
     */
    private String number;
    /**
     * 状态
     */
    private int status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 最新修改时间
     */
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
