package com.dili.customer.aop;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Uap登录权限拦截
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/20 16:28
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class UapTokenAspect {

    private final UapUserTicket uapUserTicket;

    /**
     * 设置token
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.dili.customer.annotation.UapToken)")
    public Object appletRequestAround(ProceedingJoinPoint point) throws Throwable {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (Objects.nonNull(userTicket)) {
            uapUserTicket.setUserTicket(userTicket);
            //先执行方法
            Object retValue = point.proceed();
            return retValue;
        } else {
            return BaseOutput.failure("未获取到登录信息");
        }
    }
}
