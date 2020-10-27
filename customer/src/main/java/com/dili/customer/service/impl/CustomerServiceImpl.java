package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.config.CustomerConfig;
import com.dili.customer.domain.*;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.*;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.POJOUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-30 11:18:34.
 *
 * @author yuehongbo
 */
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private CustomerMapper getActualMapper() {
        return (CustomerMapper) getDao();
    }

    private final CustomerMarketService customerMarketService;
    private final ContactsService contactsService;
    private final TallyingAreaService tallyingAreaService;
    private final AddressService addressService;
    private final BusinessCategoryService businessCategoryService;
    private final CustomerConfig customerConfig;

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
    public BaseOutput<Customer> register(EnterpriseCustomerInput baseInfo) {
        if (Objects.isNull(baseInfo.getCustomerMarket())){
            return BaseOutput.failure("客户所属市场信息丢失");
        }
        //客户归属市场信息
        CustomerMarket marketInfo = new CustomerMarket();
        BeanUtils.copyProperties(baseInfo.getCustomerMarket(),marketInfo);
        //客户基本信息对象
        Customer customer = null;
        /**
         * ID为空，则认为是新增客户，走新增客户逻辑，否则就按修改客户基本信息逻辑处理
         */
        if (Objects.isNull(baseInfo.getId())) {
            //根据证件号判断客户是否已存在
            customer = getBaseInfoByCertificateNumber(baseInfo.getCertificateNumber());
            if (null == customer) {
                /**
                 * 身份证号对应的客户不存在，手机号对应的客户已存在，则认为手机号已被使用
                 * 个人客户中，手机号不允许重复，企业客户中，手机号允许重复
                 */
                List<Customer> phoneExist = getByContactsPhone(baseInfo.getContactsPhone(), baseInfo.getOrganizationType());
                //如果为个人用户
                if (CustomerEnum.OrganizationType.INDIVIDUAL.equals(CustomerEnum.OrganizationType.getInstance(baseInfo.getOrganizationType()))) {
                    if (CollectionUtil.isNotEmpty(phoneExist)) {
                        return BaseOutput.failure("此手机号对应的客户已存在");
                    }
                } else {
                    if (CollectionUtil.isNotEmpty(phoneExist) && phoneExist.size() >= customerConfig.getPhoneLimit()) {
                        return BaseOutput.failure("此手机号注册的客户数量已达上限");
                    }
                }
                customer = new Customer();
                BeanUtils.copyProperties(baseInfo, customer);
                customer.setCreatorId(baseInfo.getOperatorId());
                customer.setCreateTime(LocalDateTime.now());
                customer.setModifyTime(customer.getCreateTime());
                customer.setState(CustomerEnum.State.NORMAL.getCode());
                customer.setIsDelete(0);
                super.insertSelective(customer);
            } else {
                if (!customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())) {
                    return BaseOutput.failure("已存在相同证件号的客户").setCode(ResultCode.DATA_ERROR);
                }
                //查询客户在当前传入市场的信息
                CustomerMarket temp = customerMarketService.queryByMarketAndCustomerId(marketInfo.getMarketId(), customer.getId());
                if (Objects.nonNull(temp)) {
                    return BaseOutput.failure("当前客户已存在，请勿重复添加").setCode(ResultCode.DATA_ERROR);
                } else {
                    marketInfo.setCreateTime(LocalDateTime.now());
                    marketInfo.setCreatorId(baseInfo.getOperatorId());
                }
            }
        } else {
            //查询当前客户信息
            customer = this.get(baseInfo.getId());
            if (!customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())) {
                return BaseOutput.failure("已存在相同证件号的客户").setCode(ResultCode.DATA_ERROR);
            }
            //查询客户在当前传入市场的信息
            CustomerMarket temp = customerMarketService.queryByMarketAndCustomerId(marketInfo.getMarketId(), customer.getId());
            if (Objects.nonNull(temp)) {
                BeanUtils.copyProperties(temp, marketInfo);
            }
            /**
             * 如果在此次创建中，增加了现地址信息
             */
            if (StrUtil.isNotBlank(baseInfo.getCurrentCityPath())) {
                customer.setCurrentCityPath(baseInfo.getCurrentCityPath());
                customer.setCurrentCityName(baseInfo.getCurrentCityName());
                customer.setCurrentAddress(baseInfo.getCurrentAddress());
                this.update(customer);
            }
        }
        marketInfo.setCustomerId(customer.getId());
        marketInfo.setModifierId(baseInfo.getOperatorId());
        marketInfo.setModifyTime(LocalDateTime.now());
        if (Objects.isNull(marketInfo.getId())) {
            marketInfo.setCreatorId(baseInfo.getOperatorId());
            marketInfo.setCreateTime(marketInfo.getModifyTime());
        }
        customerMarketService.saveOrUpdate(marketInfo);
        //组装并保存客户联系人信息
        List<Contacts> contactsList = generateContacts(baseInfo, customer);
        contactsService.batchInsert(contactsList);
        //如果联系地址不为空，则保存默认联系地址
        if(Objects.nonNull(baseInfo.getCurrentCityPath()) && StrUtil.isNotBlank(baseInfo.getCurrentAddress())){
            Address address = new Address();
            address.setCityPath(baseInfo.getCurrentCityPath());
            address.setCityName(baseInfo.getCurrentCityName());
            address.setAddress(baseInfo.getCurrentAddress());
            address.setCustomerId(customer.getId());
            address.setMarketId(marketInfo.getMarketId());
            address.setCreatorId(baseInfo.getOperatorId());
            address.setModifierId(baseInfo.getOperatorId());
            address.setCreateTime(LocalDateTime.now());
            address.setModifyTime(address.getCreateTime());
            address.setIsCurrent(1);
            addressService.insert(address);
        }
        /**
         * 处理理货区数据
         */
        if (CollectionUtil.isNotEmpty(customer.getTallyingAreaList())) {
            List<TallyingArea> tallyingAreaList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getTallyingAreaList()), TallyingArea.class);
            tallyingAreaService.saveInfo(tallyingAreaList, customer.getId(), marketInfo.getMarketId());
        }
        if (CollectionUtil.isNotEmpty(baseInfo.getBusinessCategoryList())){
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketInfo.getMarketId());
        }
        customer.setCustomerMarket(marketInfo);
        return BaseOutput.success().setData(customer);
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
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> collect = list.stream().map(Customer::getId).collect(Collectors.toSet());
            TallyingArea tallyingArea = new TallyingArea();
            tallyingArea.setMarketId(input.getMarketId());
            tallyingArea.setCustomerIdSet(collect);
            List<TallyingArea> tallyingAreaList = tallyingAreaService.listByExample(tallyingArea);
            if (CollectionUtil.isNotEmpty(tallyingAreaList)) {
                Map<Long, List<TallyingArea>> listMap = tallyingAreaList.stream().collect(Collectors.groupingBy(TallyingArea::getCustomerId));
                list.forEach(t -> {
                    if (listMap.containsKey(t.getId())) {
                        t.setTallyingAreaList(listMap.get(t.getId()));
                    }
                });
            }
        }
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getPage()).setPages(totalPage);
        return output;
    }

    @Override
    public PageOutput<List<Customer>> listBasePage(CustomerBaseQueryInput input) {
        if (input.getRows() != null && input.getRows() >= 1) {
            PageHelper.startPage(input.getPage(), input.getRows());
        }
        if (StringUtils.isNotBlank(input.getSort())) {
            input.setSort(POJOUtils.humpToLineFast(input.getSort()));
        } else {
            input.setSort("id");
            input.setOrder("desc");
        }
        List<Customer> list = getActualMapper().listBasePage(input);
        //总记录
        Long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        //总页数
        int totalPage = list instanceof Page ? ((Page) list).getPages() : 1;
        //当前页数
        int pageNum = list instanceof Page ? ((Page) list).getPageNum() : 1;
        PageOutput output = PageOutput.success();
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getPage()).setPages(totalPage);
        return output;
    }

    @Override
    public BaseOutput<Customer> checkExistByNoAndMarket(String certificateNumber, Long marketId) {
        if (StrUtil.isBlank(certificateNumber) || Objects.isNull(marketId)) {
            return BaseOutput.failure("业务关键信息丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        Customer condition = new Customer();
        condition.setCertificateNumber(certificateNumber);
        condition.setIsDelete(0);
        List<Customer> customerList = this.list(condition);
        if (CollectionUtil.isEmpty(customerList)) {
            return BaseOutput.success("客户基本信息不存在");
        }
        if (customerList.size() > 1) {
            return BaseOutput.failure("存在多个客户信息，请联系管理员处理").setCode(ResultCode.DATA_ERROR);
        }
        Customer customer = customerList.get(0);
        CustomerMarket customerFirm = customerMarketService.queryByMarketAndCustomerId(marketId, customer.getId());
        if (Objects.nonNull(customerFirm)) {
            return BaseOutput.failure("该证件号对应的客户已存在").setCode(ResultCode.DATA_ERROR);
        }
        return BaseOutput.success().setData(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Customer> update(CustomerUpdateInput updateInput) {
        Customer customer = this.get(updateInput.getId());
        if (Objects.isNull(customer)) {
            return BaseOutput.failure("客户信息已不存在").setCode(ResultCode.DATA_ERROR);
        }
        //如果更新了客户电话，则需要验证电话是否已存在
        if (!updateInput.getContactsPhone().equals(customer.getContactsPhone())) {
            List<Customer> phoneExist = getByContactsPhone(updateInput.getContactsPhone(), customer.getOrganizationType());
            //如果为个人用户
            if (CustomerEnum.OrganizationType.INDIVIDUAL.equals(CustomerEnum.OrganizationType.getInstance(updateInput.getOrganizationType()))) {
                if (CollectionUtil.isNotEmpty(phoneExist)) {
                    return BaseOutput.failure("此手机号对应的客户已存在");
                }
            } else {
                if (CollectionUtil.isNotEmpty(phoneExist) && phoneExist.size() >= customerConfig.getPhoneLimit()) {
                    return BaseOutput.failure("此手机号注册的客户数量已达上限");
                }
            }
        }
        customer.setName(updateInput.getName());
        customer.setState(updateInput.getState());
        customer.setContactsPhone(updateInput.getContactsPhone());
        if (Objects.nonNull(updateInput.getCustomerCertificate())){
            CustomerCertificateInput customerCertificate = updateInput.getCustomerCertificate();
            customer.setCertificateRange(customerCertificate.getCertificateRange());
            customer.setCertificateAddr(customerCertificate.getCertificateAddr());
            customer.setCertificateLongTerm(customerCertificate.getCertificateLongTerm());
            //以下数据为企业客户才有的数据
            customer.setCorporationCertificateType(customerCertificate.getCorporationCertificateType());
            customer.setCorporationCertificateNumber(customerCertificate.getCorporationCertificateNumber());
            customer.setCorporationName(customerCertificate.getCorporationName());
        }
        //更改市场归属信息
        CustomerMarket customerMarket = customerMarketService.queryByMarketAndCustomerId(updateInput.getCustomerMarket().getMarketId(), updateInput.getId());
        if (Objects.isNull(customerMarket)){
            customerMarket = new CustomerMarket();
        }
        BeanUtils.copyProperties(updateInput.getCustomerMarket(), customerMarket,"grade");
        customerMarket.setCustomerId(updateInput.getId());
        customerMarket.setModifierId(updateInput.getOperatorId());
        customerMarket.setModifyTime(LocalDateTime.now());
        if (Objects.isNull(customerMarket.getId())) {
            customerMarket.setCreatorId(updateInput.getOperatorId());
            customerMarket.setCreateTime(customerMarket.getModifyTime());
        }
        customerMarketService.saveOrUpdate(customerMarket);
        //声明市场ID变量，以便使用
        Long marketId = customerMarket.getMarketId();
        /**
         * 更改客户联系人信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getContactsList())) {
            List<Contacts> contactsList = Lists.newArrayList();
            updateInput.getContactsList().forEach(t -> {
                if (Objects.nonNull(t)) {
                    Contacts temp = new Contacts();
                    BeanUtils.copyProperties(t, temp);
                    temp.setCustomerId(customer.getId());
                    temp.setMarketId(marketId);
                    temp.setModifyTime(LocalDateTime.now());
                    temp.setModifierId(updateInput.getOperatorId());
                    if (Objects.isNull(temp.getId())) {
                        temp.setCreatorId(updateInput.getOperatorId());
                        temp.setCreateTime(t.getModifyTime());
                    }
                    contactsList.add(temp);
                }
            });
            contactsService.batchSaveOrUpdate(contactsList);
        } else {
            //如果传入的联系人信息为空，则删除对应的联系人信息
            contactsService.deleteByCustomerId(customer.getId(), updateInput.getCustomerMarket().getMarketId());
        }
        /**
         * 如果客户理货区不为空，则保存对应的理货区信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getTallyingAreaList())) {
            List<TallyingArea> tallyingAreaList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getTallyingAreaList()), TallyingArea.class);
            tallyingAreaService.saveInfo(tallyingAreaList, customer.getId(), marketId);
        } else {
            //如果传入的客户理货区为空，则表示该客户在该市场没有租赁理货区(手动关联的，可编辑)，所有可以直接删除
            tallyingAreaService.deleteByCustomerId(customer.getId(), marketId);
        }
        /**
         * 更新客户地址信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getAddressList())) {
            List<Address> addressList = Lists.newArrayList();
            updateInput.getAddressList().forEach(t -> {
                if (Objects.nonNull(t)) {
                    Address temp = new Address();
                    BeanUtils.copyProperties(t, temp);
                    temp.setCustomerId(customer.getId());
                    temp.setMarketId(marketId);
                    temp.setModifyTime(LocalDateTime.now());
                    temp.setModifierId(updateInput.getOperatorId());
                    if (Objects.isNull(temp.getId())) {
                        temp.setCreatorId(updateInput.getOperatorId());
                        temp.setCreateTime(temp.getModifyTime());
                    }
                    addressList.add(temp);
                    //如果此地址为当前居住地，则需更新客户主表上的现住址
                    if (YesOrNoEnum.YES.getCode().equals(t.getIsCurrent())) {
                        customer.setCurrentCityPath(t.getCityPath());
                        customer.setCurrentCityName(t.getCityName());
                        customer.setCurrentAddress(t.getAddress());
                    }
                }
            });
            addressService.batchSaveOrUpdate(addressList);
        } else {
            addressService.deleteByCustomerAndMarket(customer.getId(), marketId);
        }
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(updateInput.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketId);
        }
        super.update(customer);
        customer.setCustomerMarket(customerMarket);
        return BaseOutput.success().setData(customer);
    }


    /**
     *  组装客户联系人信息
     * @param baseInfo
     * @param customer
     * @return
     */
    private List<Contacts> generateContacts(EnterpriseCustomerInput baseInfo, Customer customer) {
        Contacts contacts = new Contacts();
        contacts.setCustomerId(customer.getId());
        if (StrUtil.isNotBlank(customer.getContactsName())) {
            contacts.setName(customer.getContactsName());
        } else {
            contacts.setName(customer.getName());
        }
        contacts.setPhone(customer.getContactsPhone());
        contacts.setMarketId(baseInfo.getCustomerMarket().getMarketId());
        contacts.setCreatorId(baseInfo.getOperatorId());
        contacts.setModifierId(baseInfo.getOperatorId());
        contacts.setCreateTime(LocalDateTime.now());
        contacts.setModifyTime(contacts.getCreateTime());
        contacts.setIsDefault(1);
        List<Contacts> contactsList = Lists.newArrayList();
        contactsList.add(contacts);
        /**
         * 如果紧急联系人不为空，则构建紧急联系人保存信息
         */
        if (StrUtil.isNotBlank(baseInfo.getEmergencyContactsName()) && StrUtil.isNotBlank(baseInfo.getEmergencyContactsPhone())) {
            Contacts emergencyContacts = new Contacts();
            BeanUtils.copyProperties(contacts, emergencyContacts);
            emergencyContacts.setName(baseInfo.getEmergencyContactsName());
            emergencyContacts.setPhone(baseInfo.getEmergencyContactsPhone());
            emergencyContacts.setIsDefault(0);
            contactsList.add(emergencyContacts);
        }
        return contactsList;
    }

    /**
     * 根据手机号获取客户信息
     * @param contactsPhone 客户手机号
     * @param organizationType 客户类型
     * @return
     */
    private List<Customer> getByContactsPhone(String contactsPhone,String organizationType) {
        Customer queryPhone = new Customer();
        queryPhone.setOrganizationType(organizationType);
        queryPhone.setContactsPhone(contactsPhone);
        return list(queryPhone);
    }

}