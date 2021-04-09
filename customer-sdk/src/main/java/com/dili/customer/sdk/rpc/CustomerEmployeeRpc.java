package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.dto.EmployeeCancelCardInput;
import com.dili.customer.sdk.domain.dto.EmployeeChangeCardInput;
import com.dili.customer.sdk.domain.dto.EmployeeOpenCardInput;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/6 16:55
 */
@FeignClient(name = "customer-service", contextId = "customerEmployeeRpc", url = "${customerService.url:}")
public interface CustomerEmployeeRpc {

    /**
     * 开园区卡时，创建员工信息
     * @param input 员工信息输入项
     * @return
     */
    @PostMapping("/api/customerEmployee/createdByOpenCard")
    BaseOutput createdByOpenCard(EmployeeOpenCardInput input);

    /**
     * 员工销卡时，解除员工关系
     * @param employeeCancelCardInput 员工销卡输入项
     * @return
     */
    @PostMapping("/api/customerEmployee/cancelCard")
    BaseOutput cancelCard(EmployeeCancelCardInput employeeCancelCardInput);

    /**
     * 员工换卡时，更新卡信息
     * @param employeeChangeCardInput 员工换卡输入项
     * @return
     */
    @PostMapping("/api/customerEmployee/changeCard")
    BaseOutput changeCard(EmployeeChangeCardInput employeeChangeCardInput);

}
