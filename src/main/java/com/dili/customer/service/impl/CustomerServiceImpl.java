package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerCertificateInput;
import com.dili.customer.domain.dto.CustomerQueryInput;
import com.dili.customer.domain.dto.CustomerUpdateInput;
import com.dili.customer.domain.dto.EnterpriseCustomerInput;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.service.ContactsService;
import com.dili.customer.service.CustomerMarketService;
import com.dili.customer.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.POJOUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-30 11:18:34.
 *
 * @author yuehongbo
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private CustomerMapper getActualMapper() {
        return (CustomerMapper) getDao();
    }

    @Autowired
    private CustomerMarketService customerMarketService;
    @Autowired
    private ContactsService contactsService;


    @Override
    public Customer getBaseInfoByCertificateNumber(String certificateNumber) {
        if (StrUtil.isNotBlank(certificateNumber)) {
            Customer customer = new Customer();
            customer.setCertificateNumber(certificateNumber);
            return list(customer).stream().findFirst().orElse(null);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveBaseInfo(EnterpriseCustomerInput baseInfo) {
        //客户归属市场信息
        CustomerMarket marketInfo = null;
        //客户ID
        Long customerId = baseInfo.getId();
        /**
         * ID为空，则认为是新增客户，走新增客户逻辑，否则就按修改客户基本信息逻辑处理
         */
        if (Objects.isNull(customerId)) {
            //根据证件号判断客户是否已存在
            Customer customer = getBaseInfoByCertificateNumber(baseInfo.getCertificateNumber());
            if (null == customer) {
//                //身份证号对应的客户不存在，手机号对应的客户已存在，则认为手机号已被使用
//                Customer queryPhone = new Customer();
//                queryPhone.setCellphone(baseInfo.getCellphone());
//                List<Customer> phoneExist = list(queryPhone);
//                if (CollectionUtil.isNotEmpty(phoneExist)) {
//                    return BaseOutput.failure("此手机号已被注册");
//                }
                customer = new Customer();
                BeanUtils.copyProperties(baseInfo, customer);
                customer.setCreatorId(baseInfo.getOperatorId());
                customer.setCreateTime(LocalDateTime.now());
                customer.setModifyTime(customer.getCreateTime());
                super.insertSelective(customer);
                customerId = customer.getId();
            } else {
                if (!customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())){
                    return BaseOutput.failure("已存在客户与当次请求的客户类型不一致");
                }
                //查询客户在当前传入市场的信息
                marketInfo = customerMarketService.queryByMarketAndCustomerId(baseInfo.getMarketId(), customer.getId());
                if (null != marketInfo) {
                    return BaseOutput.failure("当前客户已存在，请勿重复添加");
                }
            }
            marketInfo = new CustomerMarket();
            marketInfo.setCustomerId(customer.getId());
            marketInfo.setMarketId(baseInfo.getMarketId());
        } else {
            //查询当前客户信息
            Customer customer = this.get(baseInfo.getId());
            if (!customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())){
                return BaseOutput.failure("已存在客户与当次请求的客户类型不一致");
            }
            //查询客户在当前传入市场的信息
            marketInfo = customerMarketService.queryByMarketAndCustomerId(baseInfo.getMarketId(), customer.getId());
            if (null == marketInfo) {
                marketInfo = new CustomerMarket();
                marketInfo.setCustomerId(customer.getId());
                marketInfo.setMarketId(baseInfo.getMarketId());
                marketInfo.setCreateTime(LocalDateTime.now());
            }
            //保存客户基本信息
            customer.setModifyTime(LocalDateTime.now());
            super.update(customer);
        }
        marketInfo.setOwnerId(baseInfo.getOwnerId());
        marketInfo.setDepartmentId(baseInfo.getDepartmentId());
        marketInfo.setOwnerId(baseInfo.getOwnerId());
        marketInfo.setModifierId(baseInfo.getOperatorId());
        marketInfo.setModifyTime(LocalDateTime.now());
        customerMarketService.saveOrUpdate(marketInfo);
        return BaseOutput.success().setData(customerId);
    }


    @Override
    public PageOutput<List<Customer>> listForPage(CustomerQueryInput input) {
        if (input.getRows() != null && input.getRows() >= 1) {
            PageHelper.startPage(input.getPage(), input.getRows());
        }
        if (StringUtils.isNotBlank(input.getSort())) {
            input.setSort(POJOUtils.humpToLineFast(input.getSort()));
        } else {
            input.setSort("id");
            input.setOrder("desc");
        }
        List<Customer> list = getActualMapper().listForPage(input);
        //总记录
        Long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        //总页数
        int totalPage = list instanceof Page ? ((Page) list).getPages() : 1;
        //当前页数
        int pageNum = list instanceof Page ? ((Page) list).getPageNum() : 1;
        PageOutput output = PageOutput.success();
        output.setData(list).setPageNum(pageNum).setTotal(total.intValue()).setPageSize(input.getPage()).setPages(totalPage);
        return output;
    }

    @Override
    public BaseOutput<Customer> checkExistByNoAndMarket(String certificateNumber, Long marketId) {
        if (StrUtil.isBlank(certificateNumber) || Objects.isNull(marketId)) {
            return BaseOutput.failure("业务关键信息丢失");
        }
        Customer condition = new Customer();
        condition.setCertificateNumber(certificateNumber);
        condition.setIsDelete(0);
        List<Customer> customerList = this.list(condition);
        if (CollectionUtil.isEmpty(customerList)) {
            return BaseOutput.success("客户基本信息不存在");
        }
        if (customerList.size() > 1) {
            return BaseOutput.failure("存在多个客户信息，请联系管理员处理");
        }
        Customer customer = customerList.get(0);
        CustomerMarket customerFirm = customerMarketService.queryByMarketAndCustomerId(marketId, customer.getId());
        if (Objects.nonNull(customerFirm)) {
            return BaseOutput.failure("该证件号对应的客户已存在");
        }
        return BaseOutput.success().setData(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput update(CustomerUpdateInput updateInput) {
        Customer customer = this.get(updateInput.getId());
        if (Objects.isNull(customer)) {
            return BaseOutput.failure("客户信息已不存在");
        }
        customer.setName(updateInput.getName());
        customer.setState(updateInput.getState());
        customer.setProfession(updateInput.getProfession());
        customer.setContactsPhone(updateInput.getContactsPhone());
        if (Objects.nonNull(updateInput.getCustomerCertificate())){
            CustomerCertificateInput customerCertificate = updateInput.getCustomerCertificate();
            customer.setCertificateRange(customerCertificate.getCertificateRange());
            customer.setCertificateAddr(customerCertificate.getCertificateAddr());
            //以下数据为企业客户才有的数据
            customer.setCorporationCertificateType(customerCertificate.getCorporationCertificateType());
            customer.setCorporationCertificateNumber(customerCertificate.getCorporationCertificateNumber());
            customer.setCorporationName(customerCertificate.getCorporationName());
        }
        super.update(customer);
        //更改市场归属信息
        CustomerMarket customerMarket = customerMarketService.queryByMarketAndCustomerId(updateInput.getMarketId(), updateInput.getId());
        if (Objects.isNull(customerMarket)){
            customerMarket = new CustomerMarket();
        }
        customerMarket.setMarketId(updateInput.getMarketId());
        customerMarket.setOwnerId(updateInput.getOwnerId());
        customerMarket.setCustomerId(updateInput.getId());
        customerMarket.setDepartmentId(updateInput.getDepartmentId());
        customerMarket.setModifierId(updateInput.getOperatorId());
        customerMarketService.saveOrUpdate(customerMarket);
        //更新联系人信息
        contactsService.deleteByCustomerId(customer.getId(),updateInput.getMarketId());
        if (CollectionUtil.isNotEmpty(updateInput.getContactsList())){
            List<Contacts> contactsList = updateInput.getContactsList();
            contactsList.forEach(t -> {
                t.setCustomerId(customer.getId());
                t.setMarketId(updateInput.getMarketId());
                t.setCreatorId(updateInput.getOperatorId());
                t.setModifierId(updateInput.getOperatorId());
            });
            contactsService.batchInsert(contactsList);
        }
        return BaseOutput.success();
    }

}