package com.dili.customer.aop;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.annotation.AppletRequest;
import com.dili.customer.config.AppletSecretConfig;
import com.dili.customer.domain.AppletInfo;
import com.dili.customer.domain.wechat.AppletRequestInfo;
import com.dili.customer.enums.AppletTerminalType;
import com.dili.customer.service.AppletInfoService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

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


    private final AppletRequestInfo appletRequestInfo;
    private final AppletSecretConfig appletSecretConfig;
    private final AppletInfoService appletInfoService;

    /**
     * 设置token
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@within(com.dili.customer.annotation.AppletRequest)&&@within(appletRequest)")
    public Object appletRequestAround(ProceedingJoinPoint point, AppletRequest appletRequest) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        AppletTerminalType appletTerminalType = appletRequest.appletType();
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String systemCode = request.getHeader("systemCode");
        String appletCode = request.getHeader("appletCode");
        String appId = request.getHeader("appId");
        if (StrUtil.isBlank(appId)) {
            if (StrUtil.isNotBlank(systemCode) && StrUtil.isNotBlank(appletCode)) {
                appId = appletSecretConfig.getWechatAppletSecret().get(String.format("%s#%s", systemCode, appletCode).toUpperCase());
            }
        }
        if (StrUtil.isNotBlank(appId)) {
            Optional<AppletInfo> byAppletTypeAndAppId = appletInfoService.getByAppletTypeAndAppId(appletTerminalType, appId);
            if (byAppletTypeAndAppId.isEmpty()) {
                log.warn(String.format("%s 小程序 appId:%s,系统:%s,小程序编码:%s 无法识别", appletTerminalType.getValue(), appId, systemCode, appletCode));
                return BaseOutput.failure("无法识别的请求程序");
            }
            AppletInfo appletInfo = byAppletTypeAndAppId.get();
            appletRequestInfo.setAppletInfo(byAppletTypeAndAppId.get());
            log.info(String.format("接收到【%s】小程序【%s】 方法【%s】 的调用", appletTerminalType.getValue(), appletInfo.getAppletName(), method.getName()));
            //执行方法
            Object retValue = point.proceed();
            return retValue;
        } else {
            log.warn(String.format("未知的【%s】请求程序,appId:%s,系统:%s,小程序编码:%s ", appletTerminalType.getValue(), appId, systemCode, appletCode));
            return BaseOutput.failure("小程序识别码为空");
        }
    }
}
