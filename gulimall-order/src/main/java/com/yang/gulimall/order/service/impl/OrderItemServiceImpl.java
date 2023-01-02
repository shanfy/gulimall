package com.yang.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.Query;
import com.yang.gulimall.order.dao.OrderItemDao;
import com.yang.gulimall.order.entity.OrderItemEntity;
import com.yang.gulimall.order.entity.OrderReturnReasonEntity;
import com.yang.gulimall.order.service.OrderItemService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * queues：声明需要监听的队列
     * channel：当前传输数据的通道
     * Queue 可以很多人都来监听。只要收到消息，队列删除消息，而且只能有一个收到此消息
     * *Oueue:场景:
     * 1)订单服务启动多个;同一个消息，只能有一个客户端收到
     * 2)、只有一个消息完全处理完，方法运行结束，我们就可以接收到下一个消息
     */
    //@RabbitListener(queues = {"hello-java-queue"})
    public void revieveMessage(Message message,
                               OrderReturnReasonEntity content) {
        //拿到主体内容
        byte[] body = message.getBody();
        //拿到的消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println("接受到的消息...内容" + message + "===内容：" + content);

    }
}