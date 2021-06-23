package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.vo.FirmCharcterTypeVo;
import com.dili.customer.sdk.domain.query.CustomerBaseQueryInput;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/3/24 15:19
 */
public interface CustomerQueryService extends BaseService<Customer, Long> {

    /**
     * 根据证件号获取客户的基本信息
     * 此方法只会单表查询Customer表
     * @param certificateNumber 证件号
     * @return
     */
    Customer getBaseInfoByCertificateNumber(String certificateNumber);

    /**
     * 分页查询客户信息
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listSimpleForPage(CustomerQueryInput input);

    /**
     * 分页查询客户信息，此方法会按照客户市场权限隔离判断
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * @param input 查询条件
     * @return
     */
    PageOutput<List<Customer>> listSimpleForPageWithAuth(CustomerQueryInput input);

    /**
     * 分页查询客户信息
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listForPage(CustomerQueryInput input);

    /**
     * 分页查询客户信息
     * 此方法会判断是否有设置部门及归属人等相关的数据权限
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listForPageWithAuth(CustomerQueryInput input);

    /**
     * 分页查询客户基本信息，不带有任何市场属性数据
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listBasePage(CustomerBaseQueryInput input);

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    BaseOutput<Customer> checkExistByNoAndMarket(String certificateNumber,Long marketId);


    /**
     * 获取被某手机号验证的客户
     * @param cellphone 手机号
     * @return
     */
    List<Customer> getValidatedCellphoneCustomer(String cellphone);

    /**
     * 获取客户及对应所在市场的详细数据信息
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    Customer get(Long id, Long marketId);

    /**
     * 根据手机获取单个已验证该手机号的客户
     * @param cellphone 手机号
     * @return
     */
    BaseOutput<Customer> getSingleValidatedCellphoneCustomer(String cellphone);

    /**
     * 根据手机号获取客户信息
     *
     * @param contactsPhone    客户手机号
     * @param organizationType 客户类型
     * @return
     */
    List<Customer> getByContactsPhone(String contactsPhone, String organizationType);

    /**
     * 根据单个客户id及多个市场id，查询市场信息详情及客户在多个市场的角色信息
     *
     * @param customerId 客户ID
     * @param marketIds  市场ID集合
     * @return
     */
    BaseOutput<List<FirmCharcterTypeVo>> getMarketInfoAndCharacterTypes(Long customerId, Set<Long> marketIds);
}
