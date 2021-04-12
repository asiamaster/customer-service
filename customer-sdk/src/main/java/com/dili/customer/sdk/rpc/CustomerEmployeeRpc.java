package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.domain.query.CustomerEmployeeDetailQuery;
import com.dili.customer.sdk.domain.query.CustomerEmployeeQuery;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    /**
     * 查询客户员工信息数据集
     * @param query 查询条件输入项
     * @return
     */
    @PostMapping("/api/customerEmployee/listPage")
    PageOutput<List<CustomerEmployeeList>> listPage(CustomerEmployeeQuery query);

    /**
     * 客户员工列表详细信息查询
     * @param query 查询条件输入项
     * @return
     */
    @PostMapping("/api/customerEmployee/listEmployeePage")
    PageOutput<List<CustomerEmployeeDetailList>> listEmployeePage(CustomerEmployeeDetailQuery query);

}
