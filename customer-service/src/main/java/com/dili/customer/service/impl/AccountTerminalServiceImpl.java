package com.dili.customer.service.impl;

import com.dili.customer.domain.AccountTerminal;
import com.dili.customer.enums.AppletTerminalType;
import com.dili.customer.service.AccountTerminalService;
import com.dili.ss.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 18:34
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class AccountTerminalServiceImpl extends BaseServiceImpl<AccountTerminal, Long> implements AccountTerminalService {

    @Override
    public Optional<AccountTerminal> getByAppAndTerminalCode(String appId, AppletTerminalType terminalType, String terminalCode) {
        AccountTerminal condition = new AccountTerminal();
        condition.setAppId(appId).setTerminalCode(terminalCode).setTerminalType(terminalType.getCode());
        return list(condition).stream().findFirst();
    }

    @Override
    public Integer updateAccountId(Long oldAccountId, Long newAccountId) {
        if (Objects.equals(oldAccountId, newAccountId)) {
            return 0;
        }
        AccountTerminal domain = new AccountTerminal();
        domain.setAccountId(newAccountId);
        AccountTerminal condition = new AccountTerminal();
        condition.setAccountId(oldAccountId);
        return this.updateSelectiveByExample(domain, condition);
    }
}
