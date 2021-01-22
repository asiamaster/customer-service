package com.dili.customer.commons.service;

import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONUtil;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.util.WebContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 业务日志rpc服务类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/21 14:52
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BusinessLogRpcService {

    private final BusinessLogRpc businessLogRpc;

    /**
     * 日志请求域
     */
    private static final String referer = "http://customer-service.diligrp.com";

    /**
     * 异步保存业务日志
     * @param condition
     */
    @Async
    public void asyncSave(BusinessLog condition) {
        try {
            businessLogRpc.save(condition, referer);
        } catch (Exception e) {
            log.error(String.format("业务日志内容:%s  记录失败:%s", JSONUtil.toJsonStr(condition), e.getMessage()), e);
        }
    }


    /**
     * 异步保存客户操作日志
     * @param businessId 业务ID
     * @param businessCode 业务编号
     * @param content 日志内容
     * @param notes 备注
     * @param operationType 操作类型
     * @param userTicket 操作人信息
     * @param remoteIp 远程IP
     */
    @Async
    public void asyncSave(Long businessId, String businessCode, String content, String notes, String operationType, UserTicket userTicket, String remoteIp) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(businessId);
        businessLog.setBusinessCode(businessCode);
        businessLog.setMarketId(userTicket.getFirmId());
        businessLog.setOperationType(operationType);
        businessLog.setSystemCode("customer");
        businessLog.setBusinessType("customer");
        businessLog.setOperatorId(userTicket.getId());
        businessLog.setOperatorName(userTicket.getRealName());
        businessLog.setRemoteIp(remoteIp);
        businessLog.setServerIp(NetUtil.getLocalhostStr());
        businessLog.setContent(content);
        businessLog.setNotes(notes);
        try {
            businessLogRpc.save(businessLog, referer);
        } catch (Exception e) {
            log.error(String.format("业务日志内容:%s  记录失败:%s", JSONUtil.toJsonStr(businessLog), e.getMessage()), e);
        }
    }

}
