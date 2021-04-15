//package com.dili.customer.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dili.commons.rabbitmq.RabbitMQMessageService;
//import com.dili.customer.domain.Customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//import java.util.Set;
//
///**
// * MQ消息发送辅助类
// * @author yuehongbo
// * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
// * @date 2021/1/21 11:28
// */
//@Service
//@Lazy
//public class MqService {
//
//    @Autowired
//    private RabbitMQMessageService rabbitMQMessageService;
//
//    /**
//     * 异步发送客户信息到对应的mq中
//     * @param exchange
//     * @param customer
//     */
//    @Async
//    public void asyncSendCustomerToMq(String exchange, Customer customer) {
//        if (Objects.nonNull(customer)) {
//            rabbitMQMessageService.send(exchange, null, JSONObject.toJSONString(customer));
//        }
//    }
//
//}
