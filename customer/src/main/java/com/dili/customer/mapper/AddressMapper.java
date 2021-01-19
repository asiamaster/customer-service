package com.dili.customer.mapper;

import com.dili.customer.domain.Address;
import com.dili.ss.base.MyMapper;

import java.util.Map;

public interface AddressMapper extends MyMapper<Address> {

    /**
     * 更改默认地址标记
     * @param params
     */
    void updateDefaultFlag(Map<String, Object> params);

}