package com.dili.customer.sdk.boot;

import com.dili.customer.sdk.constants.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/22 10:25
 */
@Configuration
@ConditionalOnExpression("'${customer_mq.enable}'=='true'")
public class CustomerRabbitConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "messageConverter")
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "customerFanoutExchange")
    public FanoutExchange customerFanoutExchange() {
        return new FanoutExchange(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, true, false);
    }

    /**
     * 修改客户员工手机号和姓名时发消息
     * @return
     */

    public static final String CUSTOMER_UPDATE_EMPLOYEE_NAME_PHONENUMBER_QUEUE = "customer_update_employee_name_phonenumber_queue";
    public static final String CUSTOMER_UPDATE_EMPLOYEE_NAME_PHONENUMBER_KEY = "customer_update_employee_name_phonenumber_key";
    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        return new TopicExchange(MqConstant.CUSTOMER_EMPLOYEE_MQ_UPDATE_EXCHANGE, true, false);
    }

    @Bean("updaterEmployeeQueueNamePhoneNumberQueue")
    public Queue updaterEmployeeQueueNamePhoneNumberQueue() {
        return new Queue(CUSTOMER_UPDATE_EMPLOYEE_NAME_PHONENUMBER_QUEUE, true, false, false);
    }

    @Bean("updaterEmployeeQueueNamePhoneNumberBinding")
    public Binding updaterEmployeeQueueNamePhoneNumberBinding() {
        return BindingBuilder.bind(updaterEmployeeQueueNamePhoneNumberQueue()).to(topicExchange())
                .with(CUSTOMER_UPDATE_EMPLOYEE_NAME_PHONENUMBER_KEY);
    }

}
