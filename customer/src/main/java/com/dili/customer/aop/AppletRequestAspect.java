package com.dili.customer.aop;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.config.AppletSecretConfig;
import com.dili.customer.domain.wechat.AppletSystemInfo;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 14:26
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class AppletRequestAspect {


    private final AppletSystemInfo appletSystemInfo;
    private final AppletSecretConfig appletSecretConfig;

    /**
     * 设置token
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.dili.customer.annotation.AppletRequest)")
    public Object appletRequestAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String systemCode = request.getHeader("systemCode");
        String appletCode = request.getHeader("appletCode");
        if (StrUtil.isNotBlank(systemCode) && StrUtil.isNotBlank(appletCode)) {
            appletSystemInfo.setAppletSystem(systemCode);
            appletSystemInfo.setAppletCode(appletCode);
            if (appletSecretConfig.getInitAppIdSecretMaps().containsKey(appletSystemInfo.getJoint())) {
                //先执行方法
                Object retValue = point.proceed();
                return retValue;
            }
            return BaseOutput.failure("小程序appId未配置");
        } else {
            return BaseOutput.failure("来源系统或程序识别码为空");
        }
    }
}
