package com.dili.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.BeanValidationResult;
import cn.hutool.extra.validation.ValidationUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.config.CustomerCommonConfig;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.config.CustomerConfig;
import com.dili.customer.domain.*;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.service.*;
import com.dili.customer.service.remote.UidRpcService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.ss.util.POJOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.*;
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

    /**
     * 客户编号生成策略类型
     */
    private static final String UID_TYPE = "customerCode";

    private CustomerMapper getActualMapper() {
        return (CustomerMapper) getDao();
    }

    private final ContactsService contactsService;
    private final TallyingAreaService tallyingAreaService;
    private final AddressService addressService;
    private final BusinessCategoryService businessCategoryService;
    private final CustomerConfig customerConfig;
    private final CharacterTypeService characterTypeService;
    private final VehicleInfoService vehicleInfoService;
    private final UidRpcService uidRpcService;
    private final RedisUtil redisUtil;
    private final AttachmentService attachmentService;
    private final CommonDataService commonDataService;
    private final CustomerCommonConfig customerCommonConfig;
    private final AccountTerminalService accountTerminalService;
    @Autowired
    private CustomerMarketService customerMarketService;
    @Autowired
    private UserAccountService userAccountService;

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
                Integer countByContactsPhone = this.countByContactsPhone(baseInfo.getContactsPhone());
                if (countByContactsPhone > 0) {
                    return BaseOutput.failure("该手机号已被其他客户验证");
                }
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
                customer.setCode(getCustomerCode());
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
            //如果数据库中已存在的客户未实名，而本次传入的数据为已实名，则需要更新实名认证标识
            if (YesOrNoEnum.NO.getCode().equals(customer.getIsCertification()) && YesOrNoEnum.YES.getCode().equals(baseInfo.getIsCertification())){
                customer.setIsCertification(baseInfo.getIsCertification());
            }
            /**
             * 如果在此次创建中，增加了现地址信息
             */
            if (StrUtil.isNotBlank(baseInfo.getCurrentCityPath())) {
                customer.setCurrentCityPath(baseInfo.getCurrentCityPath());
                customer.setCurrentCityName(baseInfo.getCurrentCityName());
                customer.setCurrentAddress(baseInfo.getCurrentAddress());
            }
            this.update(customer);
        }
        marketInfo.setCustomerId(customer.getId());
        marketInfo.setModifierId(baseInfo.getOperatorId());
        if (Objects.isNull(marketInfo.getId())) {
            marketInfo.setCreatorId(baseInfo.getOperatorId());
            marketInfo.setModifyTime(LocalDateTime.now());
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
        //处理客户经营品类信息
        if (CollectionUtil.isNotEmpty(baseInfo.getBusinessCategoryList())){
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketInfo.getMarketId());
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(baseInfo.getCharacterTypeList())){
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList,customer.getId(),marketInfo.getMarketId());
            customer.setCharacterTypeList(characterTypeList);
        }
        customer.setCustomerMarket(marketInfo);
        return BaseOutput.success().setData(customer);
    }

    @Override
    public PageOutput<List<Customer>> listSimpleForPage(CustomerQueryInput input) {
        if (StringUtils.isNotBlank(input.getSort())) {
            input.setSort(POJOUtils.humpToLineFast(input.getSort()));
        } else {
            input.setSort("id");
            input.setOrder("desc");
        }
        if (input.getRows() != null && input.getRows() >= 1) {
            PageHelper.startPage(input.getPage(), input.getRows());
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
            Set<Long> customerIdSet = list.stream().map(Customer::getId).collect(Collectors.toSet());
            //获取客户角色身份信息
            List<CharacterType> characterTypeList = characterTypeService.listByCustomerAndMarket(customerIdSet, input.getMarketId());
            Map<Long, List<CharacterType>> characterTypeMap = characterTypeList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(CharacterType::getCustomerId));
            list.forEach(t -> {
                if (characterTypeMap.containsKey(t.getId())) {
                    t.setCharacterTypeList(characterTypeMap.get(t.getId()));
                    t.setCharacterTypeGroupList(commonDataService.produceCharacterTypeGroup(JSONArray.parseArray(JSONObject.toJSONString(t.getCharacterTypeList()), com.dili.customer.sdk.domain.CharacterType.class), input.getMarketId()));
                }
            });
        }
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getRows()).setPages(totalPage);
        return output;
    }


    @Override
    public PageOutput<List<Customer>> listForPage(CustomerQueryInput input) {
        if (StringUtils.isNotBlank(input.getSort())) {
            input.setSort(POJOUtils.humpToLineFast(input.getSort()));
        } else {
            input.setSort("id");
            input.setOrder("desc");
        }
        if (input.getRows() != null && input.getRows() >= 1) {
            PageHelper.startPage(input.getPage(), input.getRows());
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
            Set<Long> customerIdSet = list.stream().map(Customer::getId).collect(Collectors.toSet());
            //获取客户理货区信息
            List<TallyingArea> tallyingAreaList = Lists.newArrayList();
            //获取客户角色身份信息
            List<CharacterType> characterTypeList = characterTypeService.listByCustomerAndMarket(customerIdSet, input.getMarketId());
            //获取客户车辆信息
            List<VehicleInfo> vehicleInfoList = Lists.newArrayList();
            //客户附件信息
            List<Attachment> attachmentList = Lists.newArrayList();
            //市场经营品类信息
            List<BusinessCategory> businessCategoryList = Lists.newArrayList();

            TallyingArea tallyingArea = new TallyingArea();
            tallyingArea.setMarketId(input.getMarketId());
            tallyingArea.setCustomerIdSet(customerIdSet);
            tallyingAreaList.addAll(tallyingAreaService.listByExample(tallyingArea));
            vehicleInfoList.addAll(vehicleInfoService.listByCustomerAndMarket(customerIdSet, input.getMarketId()));
            attachmentList.addAll(attachmentService.listByCustomerAndMarket(customerIdSet,input.getMarketId()));
            businessCategoryList.addAll(businessCategoryService.listByCustomerAndMarket(customerIdSet,input.getMarketId()));

            Map<Long, List<TallyingArea>> tallyingAreaMap = tallyingAreaList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(TallyingArea::getCustomerId));
            Map<Long, List<CharacterType>> characterTypeMap = characterTypeList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(CharacterType::getCustomerId));
            Map<Long, List<VehicleInfo>> vehicleInfoMap = vehicleInfoList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(VehicleInfo::getCustomerId));
            Map<Long, List<Attachment>> attachmentMap = attachmentList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(Attachment::getCustomerId));
            Map<Long, List<BusinessCategory>> businessCategoryMap = businessCategoryList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(BusinessCategory::getCustomerId));

            list.forEach(t -> {
                if (tallyingAreaMap.containsKey(t.getId())) {
                    t.setTallyingAreaList(tallyingAreaMap.get(t.getId()));
                }
                if (characterTypeMap.containsKey(t.getId())) {
                    t.setCharacterTypeList(characterTypeMap.get(t.getId()));
                    t.setCharacterTypeGroupList(commonDataService.produceCharacterTypeGroup(JSONArray.parseArray(JSONObject.toJSONString(t.getCharacterTypeList()), com.dili.customer.sdk.domain.CharacterType.class), input.getMarketId()));
                }
                if (vehicleInfoMap.containsKey(t.getId())) {
                    t.setVehicleInfoList(vehicleInfoMap.get(t.getId()));
                }
                if (attachmentMap.containsKey(t.getId())) {
                    List<Attachment> attachmentDataList = attachmentMap.get(t.getId());
                    t.setAttachmentList(attachmentDataList);
                    t.setAttachmentGroupInfoList(attachmentService.convertToGroup(attachmentDataList, t.getOrganizationType()));
                }
                if (businessCategoryMap.containsKey(t.getId())) {
                    produceBusinessCategoryListData(t, businessCategoryMap.get(t.getId()));
                }
                String businessNature = t.getCustomerMarket().getBusinessNature();
                if (StrUtil.isNotBlank(businessNature)) {
                    List<DataDictionaryValue> dataDictionaryValues = commonDataService.queryBusinessNature(null);
                    Optional<DataDictionaryValue> first = dataDictionaryValues.stream().filter(d -> d.getCode().equals(businessNature)).findFirst();
                    if (first.isPresent()) {
                        t.getCustomerMarket().setMetadata("businessNatureValue", first.get().getName());
                    }
                }
            });
        }
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getRows()).setPages(totalPage);
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
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getRows()).setPages(totalPage);
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
        //如果更新了客户电话，且电话未实名，则需要验证电话是否已存在
        if (!updateInput.getContactsPhone().equals(customer.getContactsPhone()) && YesOrNoEnum.NO.getCode().equals(customer.getIsCellphoneValid())) {
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
            customer.setContactsPhone(updateInput.getContactsPhone());
        }
        customer.setName(updateInput.getName());
        customer.setState(updateInput.getState());
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
        BeanUtils.copyProperties(updateInput.getCustomerMarket(), customerMarket, "grade", "modifyTime", "approvalStatus","approvalUserId","approvalTime","approvalNotes");
        customerMarket.setCustomerId(updateInput.getId());
        customerMarket.setModifierId(updateInput.getOperatorId());
        if (Objects.isNull(customerMarket.getId())) {
            customerMarket.setCreatorId(updateInput.getOperatorId());
            customerMarket.setCreateTime(LocalDateTime.now());
            customerMarket.setModifyTime(LocalDateTime.now());
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
        /**
         * 更新客户车辆资源信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getVehicleInfoList())) {
            List<VehicleInfo> vehicleInfoList = Lists.newArrayList();
            updateInput.getVehicleInfoList().forEach(t -> {
                if (Objects.nonNull(t)) {
                    VehicleInfo temp = new VehicleInfo();
                    BeanUtils.copyProperties(t, temp);
                    temp.setCustomerId(customer.getId());
                    temp.setMarketId(marketId);
                    temp.setModifyTime(LocalDateTime.now());
                    temp.setModifierId(updateInput.getOperatorId());
                    if (Objects.isNull(temp.getId())) {
                        temp.setCreatorId(updateInput.getOperatorId());
                        temp.setCreateTime(temp.getModifyTime());
                    }
                    vehicleInfoList.add(temp);
                }
            });
            vehicleInfoService.batchSaveOrUpdate(vehicleInfoList);
        } else {
            vehicleInfoService.deleteByCustomerAndMarket(customer.getId(), marketId);
        }
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(updateInput.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketId);
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(updateInput.getCharacterTypeList())){
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList,customer.getId(),marketId);
            customer.setCharacterTypeList(characterTypeList);
        }
        super.update(customer);
        customer.setCustomerMarket(customerMarket);
        return BaseOutput.success().setData(customer);
    }

    @Override
    public List<Customer> getValidatedCellphoneCustomer(String cellphone) {
        Customer condition = new Customer();
        condition.setContactsPhone(cellphone);
        condition.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
        return list(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Customer> autoRegister(CustomerAutoRegisterDto dto) {
        //验证短信验证码是否正确
        Optional<String> s = commonDataService.checkVerificationCode(dto.getContactsPhone(), CustomerConstant.REGISTER_SCENE_CODE, dto.getVerificationCode());
        if (s.isPresent()) {
            return BaseOutput.failure(s.get());
        }
        Optional<UserAccount> byCellphone = userAccountService.getByCellphone(dto.getContactsPhone());
        if (byCellphone.isPresent()) {
            return BaseOutput.failure("您的联系电话系统已存在对应账号，请更换其他号码，谢谢！");
        }
        BaseOutput<Customer> customerBaseOutput = this.insertByContactsPhone(dto.getContactsPhone(), dto.getSourceSystem(),dto.getName());
        if (customerBaseOutput.isSuccess()) {
            Customer customer = customerBaseOutput.getData();
            UserAccount userAccount = new UserAccount();
            userAccount.setCellphone(customer.getContactsPhone()).setCustomerId(customer.getId())
                    .setCustomerCode(customer.getCode()).setCellphoneValid(customer.getIsCellphoneValid())
                    .setAccountName(customer.getName()).setAccountCode(customer.getContactsPhone()).setPassword(dto.getPassword());
            userAccountService.add(userAccount);
            return BaseOutput.successData(customer);
        } else {
            return BaseOutput.failure(customerBaseOutput.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Customer> completeInfo(CustomerUpdateInput input) {
        BeanValidationResult beanValidationResult = ValidationUtil.warpValidate(input, CompleteView.class, Default.class);
        if (!beanValidationResult.isSuccess()) {
            return BaseOutput.failure(beanValidationResult.getErrorMessages().get(0).getMessage());
        }
        Optional<String> s = validationCompleteData(input);
        if (s.isPresent()) {
            return BaseOutput.failure(s.get());
        }
        List<Customer> validatedCellphoneCustomer = this.getValidatedCellphoneCustomer(input.getContactsPhone());
        if (CollectionUtil.isNotEmpty(validatedCellphoneCustomer)) {
            if (validatedCellphoneCustomer.size() > 1) {
                return BaseOutput.failure("数据有误,手机号已被多个客户实名");
            }
            Customer customer = validatedCellphoneCustomer.get(0);
            if (!Objects.equals(customer.getCertificateNumber(), input.getCertificateNumber())) {
                return BaseOutput.failure("该手机号已认证绑定的其它证件客户");
            }
        }
        Customer customer = this.get(input.getId());
        if (Objects.isNull(customer)) {
            return BaseOutput.failure("客户信息不存在");
        }
        //获取当前登录可以对应的账号信息
        Optional<UserAccount> accountCustomer = userAccountService.getByCustomerId(input.getId());
        UserAccount accountData = null;
        if (accountCustomer.isPresent()) {
            accountData = accountCustomer.get();
        }
        /**
         * 检查是否已存在对应证件号的客户，如果已存在，则需要合并客户信息
         */
        Customer baseInfoByCertificateNumber = getBaseInfoByCertificateNumber(input.getCertificateNumber());
        if (Objects.nonNull(baseInfoByCertificateNumber)) {
            if (!StrUtil.equalsIgnoreCase(baseInfoByCertificateNumber.getOrganizationType(),input.getOrganizationType())){
                return BaseOutput.failure("已存在证件号相同，组织类型不同的客户信息");
            }
            //复制数据库已有值到客户对象中
            BeanUtil.copyProperties(baseInfoByCertificateNumber, customer);
            /**
             * 获取数据库已有证件号对应的账号信息
             * 如果已经存在，则需要把当前账号信息合并到已有账号中
             * 如果不存在，则可直接用当前账号信息
             */
            Optional<UserAccount> byCustomerId = userAccountService.getByCustomerId(baseInfoByCertificateNumber.getId());
            if (byCustomerId.isPresent()){
                customer.setContactsPhone(input.getContactsPhone());
                customer.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
                UserAccount userAccount = byCustomerId.get();
                userAccount.setCertificateNumber(input.getCertificateNumber());
                if (Objects.nonNull(accountData)){
                    if (!Objects.equals(accountData.getId(),userAccount.getId())){
                        userAccount.setChangedPwdTime(accountData.getChangedPwdTime());
                        accountData.setNewAccountId(userAccount.getId());
                        accountData.setDeleted(YesOrNoEnum.YES.getCode());
                        userAccount.setPassword(accountData.getPassword());
                        userAccount.setCellphone(input.getContactsPhone()).setCellphoneValid(YesOrNoEnum.YES.getCode());
                        userAccountService.update(userAccount);
                        accountTerminalService.updateAccountId(accountData.getId(), userAccount.getId());
                    }
                }
            }
        } else {
            if (CustomerEnum.OrganizationType.INDIVIDUAL.equalsToCode(input.getOrganizationType())) {
                if (!IdcardUtil.isValidCard(input.getCertificateNumber())) {
                    return BaseOutput.failure("个人证件号码错误");
                }
            }
            customer.setName(input.getName());
            customer.setOrganizationType(input.getOrganizationType());
            customer.setCertificateNumber(input.getCertificateNumber());
            customer.setCertificateType(input.getCertificateType());
        }
        if (Objects.isNull(accountData)){
            accountData = new UserAccount();
        }
        accountData.setCustomerId(customer.getId()).setCustomerCode(customer.getCode()).setCertificateNumber(customer.getCertificateNumber())
                .setCellphone(input.getContactsPhone()).setCellphoneValid(YesOrNoEnum.YES.getCode());
        if (StrUtil.isBlank(accountData.getAccountName())) {
            accountData.setAccountName(customer.getName());
        }

        userAccountService.insertOrUpdate(accountData);
        CustomerCertificateInput customerCertificate = input.getCustomerCertificate();
        if (Objects.nonNull(customerCertificate)) {
            customer.setCorporationName(customerCertificate.getCorporationName());
            if (StrUtil.isNotBlank(customerCertificate.getCorporationCertificateNumber())) {
                customer.setCorporationCertificateNumber(customerCertificate.getCorporationCertificateNumber());
                if (StrUtil.isBlank(customerCertificate.getCorporationCertificateType())) {
                    customer.setCorporationCertificateType("ID");
                } else {
                    customer.setCorporationCertificateType(customerCertificate.getCorporationCertificateType());
                }
            }
        }
        this.update(customer);
        CustomerMarket customerMarket = BeanUtil.copyProperties(input.getCustomerMarket(), CustomerMarket.class, "id","grade");
        CustomerMarket old = customerMarketService.queryByMarketAndCustomerId(customerMarket.getMarketId(), customer.getId());
        if (Objects.nonNull(old)) {
            old.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
            old.setBusinessNature(customerMarket.getBusinessNature());
            old.setApprovalUserId(null);
            old.setApprovalTime(null);
            customerMarketService.update(old);
        } else {
            customerMarket.setGrade(customerCommonConfig.getDefaultGrade(customerMarket.getMarketId()).getCode());
            customerMarket.setCustomerId(customer.getId());
            customerMarket.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
            customerMarket.setCreateTime(LocalDateTime.now());
            customerMarket.setModifyTime(LocalDateTime.now());
            customerMarketService.insert(customerMarket);
        }
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(input.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(input.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), customerMarket.getMarketId());
        }
        /**
         * 如果客户理货区不为空，则保存对应的理货区信息
         */
        if (CollectionUtil.isNotEmpty(input.getTallyingAreaList())) {
            List<TallyingArea> tallyingAreaList = JSONArray.parseArray(JSONObject.toJSONString(input.getTallyingAreaList()), TallyingArea.class);
            tallyingAreaService.saveInfo(tallyingAreaList, customer.getId(), customerMarket.getMarketId());
        } else {
            //如果传入的客户理货区为空，则表示该客户在该市场没有租赁理货区(手动关联的，可编辑)，所有可以直接删除
            tallyingAreaService.deleteByCustomerId(customer.getId(), customerMarket.getMarketId());
        }
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(input.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(input.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), customerMarket.getMarketId());
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(input.getCharacterTypeList())){
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(input.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList,customer.getId(),customerMarket.getMarketId());
            customer.setCharacterTypeList(characterTypeList);
        }
        //组装保存客户附件信息
        if (CollectionUtil.isNotEmpty(input.getAttachmentList())) {
            List<Attachment> attachmentList = JSONArray.parseArray(JSONObject.toJSONString(input.getAttachmentList()), Attachment.class);
            attachmentService.batchSave(attachmentList, customer.getId(), customerMarket.getMarketId());
        }
        return BaseOutput.successData(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Customer> insertByContactsPhone(String contactsPhone, String sourceSystem, String name) {
        List<Customer> validatedCellphoneCustomerList = getValidatedCellphoneCustomer(contactsPhone);
        if (CollectionUtil.isNotEmpty(validatedCellphoneCustomerList)) {
            return BaseOutput.failure("您的联系电话系统已存在，请更换其他号码，谢谢！");
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
        customer.setContactsPhone(contactsPhone);
        customer.setSourceSystem(sourceSystem);
        defaultRegister(customer);
        return BaseOutput.successData(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateCellphoneValid(Long customerId, String cellphone) {
        if (Objects.isNull(customerId) || StrUtil.isBlank(cellphone)) {
            return "必要参数丢失";
        }
        //先检查此手机号是否已被其它客户认证
        Integer countByContactsPhone = this.countByContactsPhone(cellphone);
        if (countByContactsPhone > 0) {
            return "该手机号已被认证";
        }
        Customer data = get(customerId);
        //查询此验证手机号对应的验证客户
        Optional<UserAccount> byCellphone = userAccountService.getByCellphone(cellphone);
        Optional<UserAccount> byCustomerId = userAccountService.getByCustomerId(data.getId());
        UserAccount userAccount = null;
        /**
         * 如果已存在验证的手机号对应的账号
         * 则需要判断此账号有无对应的客户证件信息(微信一键注册等可能会还没有对应的客户账号信息)
         */
        if (byCellphone.isPresent()) {
            UserAccount byCellphoneData = byCellphone.get();
            if (StrUtil.isNotBlank(byCellphoneData.getCertificateNumber())) {
                return "该手机号已认证";
            }
        }
        if (byCustomerId.isPresent()) {
            userAccount = byCustomerId.get();
        }
        if (Objects.isNull(userAccount)) {
            userAccount = new UserAccount();

        }
        userAccount.setCertificateNumber(data.getCertificateNumber()).setCustomerId(data.getId()).setCustomerCode(data.getCode())
                .setAccountName(data.getName()).setCellphone(cellphone).setCellphoneValid(YesOrNoEnum.YES.getCode());
        userAccountService.insertOrUpdate(userAccount);
        data.setContactsPhone(cellphone);
        data.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
        this.update(data);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void defaultRegister(Customer customer) {
        if (Objects.nonNull(customer)) {
            customer.setIsDelete(YesOrNoEnum.NO.getCode());
            customer.setCode(getCustomerCode());
            customer.setState(CustomerEnum.State.USELESS.getCode());
            customer.setCreateTime(LocalDateTime.now());
            customer.setModifyTime(customer.getCreateTime());
            customer.setSourceChannel("auto_register");
            this.insertSelective(customer);
        }
    }


    /**
     * 生成客户经营品类数据
     * @param customer 客户信息对象
     */
    private void produceBusinessCategoryListData(Customer customer, List<BusinessCategory> businessCategoryList) {
        if (CollectionUtil.isNotEmpty(businessCategoryList)) {
            customer.setBusinessCategoryList(businessCategoryList);
            String categoryIdStr = businessCategoryList.stream().map(o -> o.getCategoryId()).filter(StrUtil::isNotBlank).collect(Collectors.joining(","));
            String categoryNameStr = businessCategoryList.stream().map(o -> o.getCategoryName()).filter(StrUtil::isNotBlank).collect(Collectors.joining(","));
            customer.setMetadata("businessCategoryName", categoryNameStr);
            customer.setMetadata("businessCategorySelectedList", categoryIdStr);
        }
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
        if (StrUtil.isNotBlank(organizationType)) {
            queryPhone.setOrganizationType(organizationType);
        }
        queryPhone.setContactsPhone(contactsPhone);
        return list(queryPhone);
    }

    /**
     * 根据手机号，获取此手机号已认证的数量
     * @param contactsPhone
     * @return
     */
    private Integer countByContactsPhone(String contactsPhone) {
        Customer queryPhone = new Customer();
        queryPhone.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
        queryPhone.setContactsPhone(contactsPhone);
        return getActualMapper().selectCount(queryPhone);
    }

    /**
     * 获取客户编码
     * @return 客户编码
     */
    private String getCustomerCode() {
        return uidRpcService.getBizNumber(UID_TYPE);
    }

    /**
     * 验证完善资料时的数据
     * @param input
     * @return
     */
    private Optional<String> validationCompleteData(CustomerUpdateInput input){
        //客户行业ID是否为空
        boolean profession = StrUtil.isNotBlank(input.getCustomerMarket().getProfession());
        //客户行业名称是否为空
        boolean professionName = StrUtil.isNotBlank(input.getCustomerMarket().getProfessionName());
        if (profession && !professionName) {
            return Optional.of("客户行业名称不能为空");
        }
        if (!profession && professionName) {
            return Optional.of("客户行业ID不能为空");
        }
        return Optional.empty();
    }
}