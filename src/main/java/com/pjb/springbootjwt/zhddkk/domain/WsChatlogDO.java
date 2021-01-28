package com.pjb.springbootjwt.zhddkk.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 聊天记录表.
 */
@TableName("ws_chatlog")
@Data
@ToString
public class WsChatlogDO implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",name = "")
    private Long id;

    @ApiModelProperty(value = "time",name = "")
    private String time;

    // 房间号
    private String roomName;

    //
    @ApiModelProperty(value = "user",name = "")
    private String user;
    //
    @ApiModelProperty(value = "to_user",name = "")
    private String toUser;
    //
    @ApiModelProperty(value = "msg",name = "")
    private String msg;
    //
    @ApiModelProperty(value = "remark",name = "")
    private String remark;

    @TableField(exist = false)
    private String beginTime;
    @TableField(exist = false)
    private String endTime;

    public WsChatlogDO() {}

    public WsChatlogDO(String time, String roomName, String user, String toUser, String msg, String remark) {
        this.time = time;
        this.roomName = roomName;
        this.user = user;
        this.toUser = toUser;
        this.msg = msg;
        this.remark = remark;
    }
}
