package com.dili.customer.mapper;

import com.dili.customer.domain.UserAccount;
import com.dili.ss.base.MyMapper;
import org.springframework.data.repository.query.Param;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 11:57
 */
public interface UserAccountMapper extends MyMapper<UserAccount> {

    /**
     * 根据id批量更新状态
     * @param ids 需要更新状态的数据ID
     * @param isEnable 目标状态
     * @return
     */
    Integer updateState(@Param("ids") Long[] ids, @Param("isEnable") Integer isEnable);
}
