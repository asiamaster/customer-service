package com.dili.customer.mapper;

import com.dili.customer.domain.Contacts;
import com.dili.ss.base.MyMapper;

import java.util.Map;

public interface ContactsMapper extends MyMapper<Contacts> {

    /**
     * 更改默认联系人标记
     * @param params
     */
    void updateDefaultFlag(Map<String, Object> params);
}