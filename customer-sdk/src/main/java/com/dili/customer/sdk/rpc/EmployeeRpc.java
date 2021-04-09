package com.dili.customer.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 员工基础信息RPC接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/9 11:11
 */
@FeignClient(name = "customer-service", contextId = "employeeRpc", url = "${customerService.url:}")
public interface EmployeeRpc {

    /**
     * 根据id重置员工登录密码
     * @param id 员工ID
     * @return
     */
    @PostMapping(value = "/api/employee/resetPassword")
    BaseOutput resetPassword(@RequestParam("id") Long id);
}
