package com.dili.customer.service;

import com.dili.customer.domain.AccountTerminal;
import com.dili.customer.enums.UserAccountEnum;
import com.dili.ss.base.BaseService;

import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 18:33
 */
public interface AccountTerminalService extends BaseService<AccountTerminal, Long> {

    /**
     * 根据应用ID及终端号，查询客户终端信息
     * @param appId 应用ID
     * @param terminalType 终端类型
     * @param terminalCode 终端号
     * @return
     */
    Optional<AccountTerminal> getByAppAndTerminalCode(String appId, UserAccountEnum.AccountTerminalType terminalType, String terminalCode);

    /**
     * 更新绑定的终端对应的新旧账号
     * @param oldAccountId 旧账号ID
     * @param newAccountId 新账号ID
     * @return
     */
    Integer updateAccountId(Long oldAccountId, Long newAccountId);
}
