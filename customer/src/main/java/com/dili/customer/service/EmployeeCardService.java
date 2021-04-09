package com.dili.customer.service;

import com.dili.customer.domain.EmployeeCard;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 17:03
 */
public interface EmployeeCardService extends BaseService<EmployeeCard, Long> {

    /**
     * 根据客户员工ID及卡账户ID及市场ID查询正常的持卡信息
     * @param customerEmployeeId 客户员工ID
     * @param cardAccountId      卡账户ID
     * @param marketId           所属市场ID
     * @return
     */
    Optional<EmployeeCard> getByCustomerEmployeeIdAndCardAccountId(Long customerEmployeeId, Long cardAccountId, Long marketId);

    /**
     * 根据条件保存员工持卡信息
     * @param customerEmployeeId 员工关系ID
     * @param cardAccountId      卡账户ID
     * @param marketId           所属市场ID
     * @param cardNo             卡号
     * @return
     */
    int add(Long customerEmployeeId, Long cardAccountId, Long marketId, String cardNo);

    /**
     * 标记性逻辑删除数据
     * @param id 数据ID
     * @return
     */
    int logicDelete(Long id);

    /**
     * 根据客户员工ID查询有效的持卡信息
     * @param customerEmployeeId 客户员工关联ID
     * @return
     */
    List<EmployeeCard> listByCustomerEmployeeId(Long customerEmployeeId);

    /**
     * 根据卡号查询有效的持卡信息
     * @param cardNo 客户员工卡号
     * @return
     */
    List<EmployeeCard> listByCardNo(String cardNo);
}
