package com.yang.common.to.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 发送到mq消息队列的to
 **/

@Data
public class StockLockedTo implements Serializable {

    /** 库存工作单的id **/
    private Long id;

    /** 工作单详情的所有信息 **/
    private StockDetailTo detailTo;
}
