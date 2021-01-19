package com.dili.customer.service;

import com.dili.customer.domain.Address;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Optional;

/**
 * 客户联系地址
 * This file was generated on 2020-01-02 16:19:35.
 */
public interface AddressService extends BaseService<Address, Long> {

    /**
     * 删除客户在某市场的对应联系地址信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    Integer deleteByCustomerAndMarket(Long customerId,Long marketId);

    /**
     * 批量更新或者删除联系地址
     * @param addressList 联系地址信息
     * @return 记录数
     */
    Integer batchSaveOrUpdate(List<Address> addressList);

    /**
     * 保存客户地址信息
     * @param address
     * @return 保存结果
     */
    Optional<String> saveAddress(Address address);

    /**
     * 更改数据为当前住址标记
     * @param customerId 客户ID
     * @param marketId   所属市场
     * @param id         需要更改为当前住址的数据ID
     */
    void updateDefaultFlag(Long customerId, Long marketId, Long id);

}