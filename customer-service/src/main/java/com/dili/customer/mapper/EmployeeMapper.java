package com.dili.customer.mapper;

import com.dili.customer.domain.Employee;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:36
 */
public interface EmployeeMapper extends MyMapper<Employee> {

    /**
     * 根据园区卡号及客户ID查询员工信息
     * @param params
     * @return
     */
    List<Employee> queryByCardNoAndCustomerId(Map<String,Object> params);
}
