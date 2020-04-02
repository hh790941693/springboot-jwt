package com.pjb.springbootjwt.sys.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("sys_config")
public class ConfigDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**  */
    private String k;
    /**  */
    private String v;
    /**  */
    private String remark;
    /**  */
    private Date createTime;
    private Integer kvType;


    @Override
    public String toString() {
        return "ConfigDO{" +
                "id=" + id +
                ", k='" + k + '\'' +
                ", v='" + v + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", kvType=" + kvType +
                '}';
    }

    public Integer getKvType() {
        return kvType;
    }

    public void setKvType(Integer kvType) {
        this.kvType = kvType;
    }

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：
     */
    public void setK(String k) {
        this.k = k;
    }

    /**
     * 获取：
     */
    public String getK() {
        return k;
    }

    /**
     * 设置：
     */
    public void setV(String v) {
        this.v = v;
    }

    /**
     * 获取：
     */
    public String getV() {
        return v;
    }

    /**
     * 设置：
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置：
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：
     */
    public Date getCreateTime() {
        return createTime;
    }
}
