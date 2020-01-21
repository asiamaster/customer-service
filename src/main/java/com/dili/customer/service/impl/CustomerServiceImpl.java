package com.dili.customer.service.impl;
import java.time.LocalDateTime;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.Contacts;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.FirmInfo;
import com.dili.customer.domain.dto.CustomerBaseInfoDTO;
import com.dili.customer.domain.dto.CustomerCertificateInfoDTO;
import com.dili.customer.service.ContactsService;
import com.dili.customer.service.FirmInfoService;
import com.dili.customer.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-30 11:18:34.
 * @author yuehongbo
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private CustomerMapper getActualMapper() {
        return (CustomerMapper)getDao();
    }

    @Autowired
    private FirmInfoService customerFirmInfoService;
    @Autowired
    private ContactsService contactsService;


    @Override
    public Customer getBaseInfoByCertificateNumber(String certificateNumber) {
        if (StrUtil.isNotBlank(certificateNumber)) {
            Customer customer = new Customer();
            return list(customer).stream().findFirst().orElse(null);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveBaseInfo(CustomerBaseInfoDTO baseInfo) {
        //客户归属市场信息
        FirmInfo firmInfo = null;
        /**
         * ID为空，则认为是新增客户，走新增客户逻辑，否则就按修改客户基本信息逻辑处理
         */
        if (null == baseInfo.getId()){
            //根据证件号判断客户是否已存在
            Customer customer = getBaseInfoByCertificateNumber(baseInfo.getCertificateNumber());
            if (null == customer) {
                //身份证号对应的客户不存在，单手机号对应的客户已存在，则认为手机号已被使用
                Customer queryPhone = new Customer();
                queryPhone.setCellphone(baseInfo.getCellphone());
                List<Customer> phoneExist = list(queryPhone);
                if (CollectionUtil.isNotEmpty(phoneExist)) {
                    return BaseOutput.failure("此手机号已被注册");
                }
                customer = new Customer();
                BeanUtils.copyProperties(baseInfo,customer);
                customer.setCreatorId(baseInfo.getOperatorId());
                customer.setCreateTime(LocalDateTime.now());
                customer.setModifyTime(customer.getCreateTime());
                //TODO 差一个客户状态的枚举定义
                super.insertSelective(customer);
                if ("企业".equalsIgnoreCase(baseInfo.getOrganizationType())){
                    //客户联系人
                    Contacts contacts = new Contacts();
                    contacts.setName(baseInfo.getContactsName());
                    contacts.setPhone(baseInfo.getContactsPhone());
                    contacts.setCustomerId(customer.getId());
                    contactsService.saveContacts(contacts);
                }
            }else{
                //查询客户在当前传入市场的信息
                firmInfo = customerFirmInfoService.queryByFirmAndCustomerId(baseInfo.getFirmId(), customer.getId());
                if (null != firmInfo){
                    return BaseOutput.failure("当前客户已存在，请勿重复添加");
                }
            }
            firmInfo = new FirmInfo();
            firmInfo.setCustomerId(customer.getId());
            firmInfo.setFirmId(baseInfo.getFirmId());
        } else {
            //查询当前客户信息
            Customer customer = this.get(baseInfo.getId());
            //查询客户在当前传入市场的信息
            firmInfo = customerFirmInfoService.queryByFirmAndCustomerId(baseInfo.getFirmId(), customer.getId());
            if (null == firmInfo) {
                firmInfo = new FirmInfo();
                firmInfo.setCustomerId(customer.getId());
                firmInfo.setFirmId(baseInfo.getFirmId());
            }
            //保存客户基本信息
            customer.setBirthdate(baseInfo.getBirthdate());
            customer.setPhoto(baseInfo.getPhoto());
            customer.setProfession(baseInfo.getProfession());
            customer.setModifyTime(LocalDateTime.now());
            super.update(customer);
        }
        firmInfo.setOwnerId(baseInfo.getOwnerId());
        firmInfo.setAlias(baseInfo.getAlias());
        firmInfo.setNotes(baseInfo.getNotes());
        firmInfo.setDepartmentId(baseInfo.getDepartmentId());
        firmInfo.setOwnerId(baseInfo.getOwnerId());
        firmInfo.setModifierId(baseInfo.getOperatorId());
        firmInfo.setModifyTime(LocalDateTime.now());
        customerFirmInfoService.saveOrUpdate(firmInfo);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput saveCertificateInfo(CustomerCertificateInfoDTO certificateInfo) {
        Customer customer = this.get(certificateInfo.getId());
        if (null == customer){
            return BaseOutput.failure("客户信息不存在");
        }
        BeanUtils.copyProperties(certificateInfo,customer);
        update(customer);
        return BaseOutput.success();
    }

}