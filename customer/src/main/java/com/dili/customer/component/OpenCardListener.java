package com.dili.customer.component;

import cn.hutool.json.JSONUtil;
import com.dili.customer.commons.service.BusinessLogRpcService;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerMarketService;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/2/7 11:39
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OpenCardListener {
    private final CustomerMarketService customerMarketService;
    private final BusinessLogRpcService businessLogRpcService;

    /**
     * 处理客户mq消息数据
     * @param channel
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue_cardWeb_openCard_customer", autoDelete = "false"),
            exchange = @Exchange(value = "exchange_cardWeb_openCard"),
            key = "routing_cardWeb_openCard"
    ))
    public void processOpenCardInfo(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            if (log.isDebugEnabled()) {
                log.debug(String.format("获取到的body数据:%s", data));
            }
            OpenCardDto openCardDto = JSONUtil.toBean(JSONUtil.parseObj(data), OpenCardDto.class);
            Boolean aBoolean = customerMarketService.changeGrade(openCardDto.customerId, openCardDto.firmId, CustomerEnum.Grade.GENERAL);
            if (aBoolean) {
                log.info("卡务开卡数据同步消息处理成功");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                UserTicket userTicket = DTOUtils.newInstance(UserTicket.class);
                userTicket.setFirmId(openCardDto.firmId);
                userTicket.setId(openCardDto.creatorId);
                userTicket.setRealName(openCardDto.creator);
                businessLogRpcService.asyncSave(openCardDto.customerId, openCardDto.customerCode, "客户开通园区卡,等级变更", String.format("卡号%s",openCardDto.cardNo), "edit", userTicket, null);
            } else {
                log.warn(String.format("客户开卡后等级信息更新失败,接收到的对象:{%s}", data));
            }
        } catch (IOException e) {
            //有可能再basicAck时抛出异常
            log.warn(String.format("消息对象: [%s] basicAck出错 {%s}", message, e.getMessage()), e);
        } catch (Exception e) {
            log.error(String.format("处理客户开卡监听对象: [%s] 出错 [%s]", message, e.getMessage()), e);
            // redelivered = true, 表明该消息是重复处理消息
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            try {
                if (redelivered) {
                    /**
                     * 1. 对于重复处理的队列消息做补偿机制处理
                     * 2. 从队列中移除该消息，防止队列阻塞
                     */
                    // 消息已重复处理失败, 扔掉消息
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                    log.error("消息 {} 重新处理失败，扔掉消息", message);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            } catch (IOException ex) {
                log.error(String.format("消息 {} 重放回队列失败 {}", message, ex.getMessage()), ex);
            }
        }
    }


    /**
     * mq消息对象
     */
    @Getter
    @Setter
    class OpenCardDto{
        /** 用户姓名 */
        private String customerName;
        /** CRM系统客户ID */
        private Long customerId;
        /** 客户编号 */
        private String customerCode;
        /** 卡号 */
        private String cardNo;
        /** 商户ID */
        private Long firmId;
        /** 创建人ID **/
        private Long creatorId;
        /** 创建人姓名 **/
        private String creator;
    }

}
