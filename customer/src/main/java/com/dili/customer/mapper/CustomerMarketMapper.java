package com.dili.customer.mapper;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

/**
 * @author yuehongbo
 */
public interface CustomerMarketMapper extends MyMapper<CustomerMarket> {

    /**
     * 根据客户手机号查询客户市场归类信息
     * @param phone 手机号
     * @return
     */
    List<CustomerMarketDto> selectByContactsPhone(String phone);
}