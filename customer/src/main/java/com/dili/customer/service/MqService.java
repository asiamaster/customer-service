package com.dili.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * MQ消息发送辅助类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/21 11:28
 */
@RequiredArgsConstructor
@Service
public class MqService {

    @Autowired
    private CustomerQueryService customerQueryService;
    private final RabbitMQMessageService rabbitMQMessageService;

    /**
     * 异步发送客户信息到对应的mq中
     * @param exchange
     * @param customerId
     * @param marketId
     */
    @Async
    public void asyncSendCustomerToMq(String exchange, Long customerId, Long marketId) {
        Customer customer = customerQueryService.get(customerId, marketId);
        if (Objects.nonNull(customer)) {
            rabbitMQMessageService.send(exchange, null, JSONObject.toJSONString(customer));
        }
    }

    /**
     * 批量异步数据发送MQ
     * @param exchange
     * @param customerId
     * @param marketIds
     */
    @Async
    public void asyncSendCustomerToMq(String exchange, Long customerId, Set<Long> marketIds) {
        marketIds.forEach(t -> {
            this.asyncSendCustomerToMq(exchange, customerId, t);
        });
    }

}
