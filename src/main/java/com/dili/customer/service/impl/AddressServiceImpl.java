package com.dili.customer.service.impl;

import com.dili.customer.mapper.AddressMapper;
import com.dili.customer.domain.Address;
import com.dili.customer.service.AddressService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:19:35.
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long> implements AddressService {

    public AddressMapper getActualMapper() {
        return (AddressMapper)getDao();
    }
}