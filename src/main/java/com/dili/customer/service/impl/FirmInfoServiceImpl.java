package com.dili.customer.service.impl;

import com.dili.customer.mapper.FirmInfoMapper;
import com.dili.customer.domain.FirmInfo;
import com.dili.customer.service.FirmInfoService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@Service
public class FirmInfoServiceImpl extends BaseServiceImpl<FirmInfo, Long> implements FirmInfoService {

    public FirmInfoMapper getActualMapper() {
        return (FirmInfoMapper)getDao();
    }

    @Override
    public FirmInfo queryByFirmAndCustomerId(String firmId, Long customerId) {
        FirmInfo info = new FirmInfo();
        info.setFirmId(firmId);
        info.setCustomerId(customerId);
        List<FirmInfo> firmInfos = list(info);
        return firmInfos.stream().findFirst().orElse(null);
    }
}