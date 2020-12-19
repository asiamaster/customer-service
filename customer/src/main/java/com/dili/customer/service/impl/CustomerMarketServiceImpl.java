package com.dili.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.commons.service.MarketRpcService;
import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.mapper.CustomerMarketMapper;
import com.dili.customer.sdk.domain.dto.MarketApprovalResultInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerMarketService;
import com.dili.ss.base.BaseServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@Service
public class CustomerMarketServiceImpl extends BaseServiceImpl<CustomerMarket, Long> implements CustomerMarketService {

    public CustomerMarketMapper getActualMapper() {
        return (CustomerMarketMapper)getDao();
    }

    private final MarketRpcService marketRpcService;

    @Override
    public CustomerMarket queryByMarketAndCustomerId(Long marketId, Long customerId) {
        CustomerMarket info = new CustomerMarket();
        info.setMarketId(marketId);
        info.setCustomerId(customerId);
        List<CustomerMarket> firmInfos = list(info);
        return firmInfos.stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeGrade(Long customerId, Long marketId, CustomerEnum.Grade nextGrade) {
        CustomerMarket customerMarket = this.queryByMarketAndCustomerId(marketId, customerId);
        if (Objects.nonNull(customerMarket)) {
            Integer gradeCode = customerMarket.getGrade();
            CustomerEnum.Grade grade = CustomerEnum.Grade.getInstance(gradeCode);
            if (Objects.isNull(grade) || grade.next().contains(nextGrade)) {
                customerMarket.setGrade(nextGrade.getCode());
                this.update(customerMarket);
            }
        }
    }

    @Override
    public List<CustomerMarketDto> selectByCustomerId(Long customerId) {
        if (Objects.isNull(customerId)) {
            return Collections.EMPTY_LIST;
        }
        CustomerMarket condition = new CustomerMarket();
        condition.setCustomerId(customerId);
        List<CustomerMarket> customerMarketList = this.list(condition);
        if (CollectionUtil.isNotEmpty(customerMarketList)) {
            Map<String, String> marketMap = marketRpcService.listForMap();
            List<CustomerMarketDto> resultData = Lists.newArrayList();
            StreamEx.of(customerMarketList).forEach(t -> {
                CustomerMarketDto customerMarketDto = BeanUtil.copyProperties(t, CustomerMarketDto.class);
                customerMarketDto.setMarketName(marketMap.get(String.valueOf(t.getMarketId())));
                resultData.add(customerMarketDto);
            });
            return resultData;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Map<String, Object>> statisticsApproval(Long marketId) {
        List<Map<String, Object>> mapList = getActualMapper().statisticsApproval(marketId);
        mapList.forEach(t -> {
            t.put("statusName", CustomerEnum.ApprovalStatus.getValueByCode(Integer.valueOf(Objects.toString(t.get("approvalStatus"), "0"))));
        });
        return mapList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<String> approval(MarketApprovalResultInput input) {
        CustomerMarket customerMarket = this.queryByMarketAndCustomerId(input.getMarketId(), input.getCustomerId());
        if (Objects.isNull(customerMarket)) {
            return Optional.of("客户市场资料信息不存在");
        }
        if (!CustomerEnum.ApprovalStatus.WAIT_CONFIRM.equalsToCode(customerMarket.getApprovalStatus())) {
            return Optional.of("状态已变更，不支持此操作");
        }
        customerMarket.setApprovalTime(LocalDateTime.now());
        customerMarket.setApprovalStatus(input.getPassed() ? CustomerEnum.ApprovalStatus.PASSED.getCode() : CustomerEnum.ApprovalStatus.UN_PASS.getCode());
        customerMarket.setApprovalUserId(input.getOperatorId());
        customerMarket.setApprovalNotes(input.getApprovalNotes());
        this.update(customerMarket);
        return Optional.empty();
    }
}