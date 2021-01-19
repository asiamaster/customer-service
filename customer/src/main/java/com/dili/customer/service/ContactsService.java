package com.dili.customer.service;

import com.dili.customer.domain.Contacts;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:18:39.
 */
public interface ContactsService extends BaseService<Contacts, Long> {

    /**
     * 保存客户联系人信息
     * @param customerContacts
     * @return 保存结果
     */
    BaseOutput saveContacts(Contacts customerContacts);

    /**
     * 根据客户及市场，删除对应的联系人信息
     * @param customerId 客户ID
     * @param marketId  市场ID
     * @return 记录数
     */
    Integer deleteByCustomerId(Long customerId,Long marketId);

    /**
     * 批量更新或者删除联系人
     * @param contactsList 联系人信息
     * @return 记录数
     */
    Integer batchSaveOrUpdate(List<Contacts> contactsList);

    /**
     * 更改数据为默认标记
     * @param customerId 客户ID
     * @param marketId   所属市场
     * @param id         需要更更改为默认联系人的数据ID
     */
    void updateDefaultFlag(Long customerId, Long marketId, Long id);
}