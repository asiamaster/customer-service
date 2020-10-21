package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:56
 */
@FeignClient(name = "customer-service", contextId = "customerRpc", url = "${customerService.url:}")
public interface CustomerRpc {

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @PostMapping(value = "/api/customer/listPage")
    PageOutput<List<CustomerExtendDto>> listPage(CustomerQueryInput customer);

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @PostMapping(value = "/api/customer/list")
    BaseOutput<List<CustomerExtendDto>> list(CustomerQueryInput customer);

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @PostMapping(value = "/api/customer/listBase")
    BaseOutput<List<Customer>> listBase(CustomerBaseQueryInput customer);

    /**
     * 企业用户注册
     * @param baseInfo
     * @return
     */
    @PostMapping(value = "/api/customer/registerEnterprise")
    BaseOutput<CustomerExtendDto> registerEnterprise(EnterpriseCustomerInput baseInfo);

    /**
     * 个人用户注册
     * @param baseInfo
     * @return
     */
    @PostMapping(value = "/api/customer/registerIndividual")
    BaseOutput<CustomerExtendDto> registerIndividual(IndividualCustomerInput baseInfo);


    /**
     * 更新客户基本信息
     * @param updateInput 客户更新数据
     * @return
     */
    @PostMapping(value = "/api/customer/update")
    BaseOutput<CustomerExtendDto> update(CustomerUpdateInput updateInput);

    /**
     * 更新用户状态
     * @param customerId 客户ID
     * @param state      状态值
     * @return
     */
    @PostMapping(value = "/api/customer/updateState")
    BaseOutput<Customer> updateState(@RequestParam("customerId") Long customerId, @RequestParam("state") Integer state);

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @PostMapping(value = "/api/customer/checkExistByNoAndMarket")
    BaseOutput<Customer> checkExistByNoAndMarket(@RequestParam(value = "certificateNumber") String certificateNumber,@RequestParam(value = "marketId") Long marketId);

    /**
     * 根据id及市场，查询客户的信息
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/api/customer/get")
    BaseOutput<CustomerExtendDto> get(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId);

    /**
     * 根据id查询客户的基本信息，不带有任何的市场属性数据
     * @param id 客户ID
     * @return
     */
    @PostMapping(value = "/api/customer/getById")
    BaseOutput<Customer> getById(@RequestParam("id") Long id);

    /**
     * 根据证件号及市场，查询客户的信息
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/api/customer/getByCertificateNumber")
    BaseOutput<CustomerExtendDto> getByCertificateNumber(@RequestParam("certificateNumber") String certificateNumber, @RequestParam("marketId") Long marketId);

}