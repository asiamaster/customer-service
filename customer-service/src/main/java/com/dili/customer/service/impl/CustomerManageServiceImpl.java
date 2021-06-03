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
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.customer.commons.config.CustomerCommonConfig;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.customer.commons.service.BusinessLogRpcService;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.commons.service.DepartmentRpcService;
import com.dili.customer.config.CustomerConfig;
import com.dili.customer.domain.*;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.sdk.validator.EnterpriseView;
import com.dili.customer.service.*;
import com.dili.customer.service.remote.UidRpcService;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.util.LoggerUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
@Slf4j
@Service
public class CustomerManageServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerManageService {

    /**
     * 客户编号生成策略类型
     */
    private static final String UID_TYPE = "customerCode";


    private static final String DRIVER_CLIENT_BUSINESSNATURE_TEMPORARY_ABSENT = "driver_client_businessNature_absent";

    private CustomerMapper getActualMapper() {
        return (CustomerMapper) getDao();
    }

    @Autowired
    private TallyingAreaService tallyingAreaService;
    @Autowired
    private BusinessCategoryService businessCategoryService;
    @Autowired
    private CustomerConfig customerConfig;
    @Autowired
    private CharacterTypeService characterTypeService;
    @Autowired
    private UidRpcService uidRpcService;
    @Autowired
    private CommonDataService commonDataService;
    @Autowired
    private CustomerCommonConfig customerCommonConfig;
    @Autowired
    private AccountTerminalService accountTerminalService;
    @Autowired
    private DepartmentRpcService departmentRpcService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private BusinessLogRpcService businessLogRpcService;
    @Autowired
    private UapUserTicket uapUserTicket;
    @Autowired
    private CustomerQueryService customerQueryService;
    @Autowired
    @Lazy
    private CustomerMarketService customerMarketService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLogger(businessType = "customer", systemCode = "CUSTOMER", operationType = "add")
    public BaseOutput<Customer> register(EnterpriseCustomerInput baseInfo) {
        UserTicket userTicket = getOperatorUserTicket();
        if (Objects.isNull(baseInfo.getCustomerMarket())) {
            return BaseOutput.failure("客户所属市场信息丢失");
        }
        if (Objects.isNull(baseInfo.getOperatorId())) {
            baseInfo.setOperatorId(getOperatorUserTicket().getId());
        }
        Optional<String> checkCertificateType = checkCertificateType(baseInfo.getOrganizationType(), baseInfo.getCertificateType());
        if (checkCertificateType.isPresent()) {
            return BaseOutput.failure(checkCertificateType.get());
        }
        //客户归属市场信息
        CustomerMarket marketInfo = new CustomerMarket();
        BeanUtils.copyProperties(baseInfo.getCustomerMarket(), marketInfo);
        marketInfo.setState(CustomerEnum.State.NORMAL.getCode());
        //客户基本信息对象
        Customer customer = null;
        /**
         * ID为空，则认为是新增客户，走新增客户逻辑，否则就按修改客户基本信息逻辑处理
         */
        if (Objects.isNull(baseInfo.getId())) {
            //根据证件号判断客户是否已存在
            customer = customerQueryService.getBaseInfoByCertificateNumber(baseInfo.getCertificateNumber());
            if (null == customer) {
                String invalidPhoneNumberInfo = validatePhoneNumberBeforeAddingCustomer(baseInfo);
                if (StringUtils.isNotBlank(invalidPhoneNumberInfo)) {
                    return BaseOutput.failure(invalidPhoneNumberInfo);
                }
                customer = addCustomer(baseInfo);
            } else {
                if (customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())) {
                    return BaseOutput.failure("已存在相同证件号的客户").setCode(ResultCode.DATA_ERROR);
                }
                /**
                 *  查询客户与市场的关联信息，本平台已启用市场隔离。
                 *  如果未启用部门或归属人权限隔离，则查询客户与市场的关联信息存在，则为全局存在，可直接判定客户存在
                 *  如果启用部门或归属人权限隔离，
                 */
                CustomerMarket customerMarket = customerMarketService.queryByMarketAndCustomerId(marketInfo.getMarketId(), customer.getId());
                if (Objects.nonNull(customerMarket)) {
                    Boolean departmentAuth = commonDataService.checkCustomerDepartmentAuth(marketInfo.getMarketId());
                    Boolean ownerAuth = commonDataService.checkCustomerOwnerAuth(marketInfo.getMarketId());
                    // 当部门或归属人权限启用至少一种时：
                    if (departmentAuth || ownerAuth) {
                        if (Objects.isNull(userTicket.getDepartmentId())) {
                            return BaseOutput.failure("当前市场客户数据已启用部门、归属人权限，您归属部门为空").setCode(ResultCode.NOT_AUTH_ERROR);
                        }
                        if (StrUtil.isBlank(customerMarket.getDepartmentIds()) && StrUtil.isBlank(customerMarket.getOwnerIds())) {
                            return BaseOutput.failure("当前客户已存在，请勿重复添加").setCode(ResultCode.DATA_ERROR);
                        }
                        // 更新标志，true表示将有更新操作。
                        Boolean updateFlag = false;
                        // 获取客户所在的部门id，并去重
                        Set<String> customerDepartmentIds = Arrays.stream(customerMarket.getDepartmentIds().split(",")).collect(Collectors.toSet());
                        // 查询用户在该市场中有权限的部门id，并去重
                        List<Department> userHasAuthorityDepartments = departmentRpcService.listUserAuthDepartmentByFirmId(userTicket.getId(), userTicket.getFirmId());
                        Set<String> userHasAuthorityDepartmentIds = userHasAuthorityDepartments.stream().map(t -> String.valueOf(t.getId())).collect(Collectors.toSet());
                        if (ownerAuth) {
                            if (StrUtil.isNotBlank(customerMarket.getOwnerIds())) {
                                // 如果用户登录凭证中的归属部门信息不包含在用户市场权限部门中，则其无权操作此部门的数据，直接返回失败
                                if (!userHasAuthorityDepartmentIds.contains(String.valueOf(userTicket.getDepartmentId()))) {
                                    return BaseOutput.failure("您暂无归属部门的数据权限，不能新增客户");
                                }
                                // 如果客户所在部门信息不包含当前登录用户凭证中归属部门信息，则将用户归属部门信息添加到客户所属部门
                                if (!customerDepartmentIds.contains(String.valueOf(userTicket.getDepartmentId()))) {
                                    customerDepartmentIds.add(String.valueOf(userTicket.getDepartmentId()));
                                }
                                Set<String> ownerIdSet = Arrays.stream(customerMarket.getOwnerIds().split(",")).collect(Collectors.toSet());
                                ownerIdSet.add(String.valueOf(userTicket.getId()));
                                customerMarket.setOwnerIds(String.join(",", ownerIdSet));
                                updateFlag = true;
                            }
                        }
                        if (departmentAuth) {
                            if (StrUtil.isNotBlank(customerMarket.getDepartmentIds())) {
                                Set<String> departmentIdTemp = new HashSet<>();
                                departmentIdTemp.addAll(customerDepartmentIds);
                                departmentIdTemp.retainAll(userHasAuthorityDepartmentIds);
                                //已有归属部门与部门权限不存在
                                if (CollectionUtil.isEmpty(departmentIdTemp) && userHasAuthorityDepartmentIds.contains(String.valueOf(userTicket.getDepartmentId()))) {
                                    customerDepartmentIds.add(String.valueOf(userTicket.getDepartmentId()));
                                    updateFlag = true;
                                } else if (CollectionUtil.isNotEmpty(departmentIdTemp) && departmentIdTemp.contains(String.valueOf(userTicket.getDepartmentId()))) {
                                    updateFlag = true;
                                } else {
                                    return BaseOutput.failure("您暂无归属部门的数据权限，不能新增客户");
                                }
                            }
                        }
                        if (updateFlag) {
                            customerMarket.setDepartmentIds(customerDepartmentIds.stream().collect(Collectors.joining(",")));
                            customerMarketService.update(customerMarket);
                            StringBuffer str = new StringBuffer();
                            str.append(getOperatorUserTicket().getRealName()).append("创建").append(CustomerEnum.OrganizationType.getInstance(customer.getOrganizationType()).getValue())
                                    .append("客户[").append(customer.getName()).append("]成功");
                            LoggerUtil.buildBusinessLoggerContext(customer.getId(), customer.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), str.toString());
                            return BaseOutput.success("客户资料维护成功").setData(customer);
                        } else {
                            return BaseOutput.failure("当前客户已存在，请勿重复添加").setCode(ResultCode.DATA_ERROR);
                        }
                    }
                    return BaseOutput.failure("当前客户已存在，请勿重复添加").setCode(ResultCode.DATA_ERROR);
                } else {
                    marketInfo.setCreateTime(LocalDateTime.now());
                    marketInfo.setCreatorId(baseInfo.getOperatorId());
                }
            }
        } else {
            //查询当前客户信息
            customer = this.get(baseInfo.getId());
            //查询客户在当前传入市场的信息是否已经存在
            CustomerMarket customerMarkeExisted = customerMarketService.queryByMarketAndCustomerId(marketInfo.getMarketId(), customer.getId());
            /**
             * 判断客户重复，需以下条件同时满足：
             *  1. 根据客户id和市场id查询的CustomerMarket对象不为空；
             *  2. 客户类型与当前传入的类型相同；
             *  3. 若查询出的CustomerMarket对象部门id集合为空，可直接认定为重复。
             *  4. 若查询出的CustomerMarket对象部门id集合不为空，则此集合包含当前传入的客户部门id，方可认定为重复。
             */
            if (Objects.nonNull(customerMarkeExisted)
                    && customer.getOrganizationType().equalsIgnoreCase(baseInfo.getOrganizationType())
                    && Arrays.asList(customerMarkeExisted.getDepartmentIds().split(",")).contains(marketInfo.getDepartmentIds())) {
                return BaseOutput.failure("已存在相同证件号的客户").setCode(ResultCode.DATA_ERROR);
            }

            //如果数据库中已存在的客户未实名，而本次传入的数据为已实名，则需要更新实名认证标识
//            if (YesOrNoEnum.NO.getCode().equals(customer.getIsCertification()) && YesOrNoEnum.YES.getCode().equals(baseInfo.getIsCertification())){
//                customer.setIsCertification(baseInfo.getIsCertification());
//            }
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
        marketInfo.setAlias(baseInfo.getName());
        marketInfo.setCustomerId(customer.getId());
        marketInfo.setModifierId(baseInfo.getOperatorId());
        if (Objects.isNull(marketInfo.getId())) {
            if (Objects.isNull(marketInfo.getGrade())) {
                marketInfo.setGrade(customerCommonConfig.getDefaultGrade(marketInfo.getMarketId()).getCode());
            }
            //后台创建的，默认审核通过，且审核人为当前人
            marketInfo.setApprovalStatus(CustomerEnum.ApprovalStatus.PASSED.getCode());
            marketInfo.setApprovalTime(LocalDateTime.now());
            marketInfo.setApprovalUserId(baseInfo.getOperatorId());
            marketInfo.setCreatorId(baseInfo.getOperatorId());
            marketInfo.setModifyTime(LocalDateTime.now());
            marketInfo.setCreateTime(marketInfo.getModifyTime());
        }
        customerMarketService.saveOrUpdate(marketInfo);
        //组装并保存客户联系人信息
        List<Contacts> contactsList = generateContacts(baseInfo, customer);
        contactsService.batchInsert(contactsList);
        //如果联系地址不为空，则保存默认联系地址
        if (Objects.nonNull(baseInfo.getCurrentCityPath()) && StrUtil.isNotBlank(baseInfo.getCurrentAddress())) {
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
        if (CollectionUtil.isNotEmpty(baseInfo.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketInfo.getMarketId());
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(baseInfo.getCharacterTypeList())) {
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(baseInfo.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList, customer.getId(), marketInfo.getMarketId());
            customer.setCharacterTypeList(characterTypeList);
        }
        customer.setCustomerMarket(marketInfo);
        StringBuffer str = new StringBuffer();
        str.append(getOperatorUserTicket().getRealName()).append("创建").append(CustomerEnum.OrganizationType.getInstance(customer.getOrganizationType()).getValue())
                .append("客户[").append(customer.getName()).append("]成功");
        LoggerUtil.buildBusinessLoggerContext(customer.getId(), customer.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), str.toString());
        return BaseOutput.success().setData(customer);
    }

    private String validatePhoneNumberBeforeAddingCustomer(EnterpriseCustomerInput baseInfo) {
        Integer countByContactsPhone = this.countByContactsPhone(baseInfo.getContactsPhone());
        if (countByContactsPhone > 0) {
            return "该手机号已被其他客户验证";
        }
        /**
         * 身份证号对应的客户不存在，手机号对应的客户已存在，则认为手机号已被使用
         * 个人客户中，手机号不允许重复，企业客户中，手机号允许重复
         */
        List<Customer> phoneExist = customerQueryService.getByContactsPhone(baseInfo.getContactsPhone(), baseInfo.getOrganizationType());
        //如果为个人用户
        if (CustomerEnum.OrganizationType.INDIVIDUAL.equals(CustomerEnum.OrganizationType.getInstance(baseInfo.getOrganizationType()))) {
            if (CollectionUtil.isNotEmpty(phoneExist)) {
                return "此手机号对应的客户已存在";

            }
        } else {
            if (CollectionUtil.isNotEmpty(phoneExist) && phoneExist.size() >= customerConfig.getPhoneLimit()) {
                return "此手机号注册的客户数量已达上限";
            }
        }
        return null;
    }

    private Customer addCustomer(EnterpriseCustomerInput baseInfo) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(baseInfo, customer);
        String customerCode = getCustomerCode();
        if (StrUtil.isBlank(customerCode)) {
            throw new AppException(ResultCode.APP_ERROR, "未获取到编号");
        }

        customer.setCode(customerCode);
        customer.setCreatorId(baseInfo.getOperatorId());
        customer.setCreateTime(LocalDateTime.now());
        customer.setModifyTime(customer.getCreateTime());
        customer.setIsDelete(0);
        customer.setIsCertification(YesOrNoEnum.NO.getCode());
        customer.setCurrentCityName(baseInfo.getCurrentCityName());
        customer.setCurrentCityPath(baseInfo.getCurrentCityPath());
        super.insertSelective(customer);
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput update(CustomerUpdateInput updateInput) {
        BaseOutput baseOutput = this.updateBaseInfo(updateInput);
        if (!baseOutput.isSuccess()) {
            return BaseOutput.failure(baseOutput.getMessage());
        }
        if (Objects.nonNull(updateInput.getCustomerCertificate())) {
            Optional<String> s = this.updateCertificateInfo(updateInput.getCustomerCertificate(), false);
            if (s.isPresent()) {
                throw new AppException(s.get());
            }
        }
        //声明市场ID变量，以便使用
        Long marketId = updateInput.getCustomerMarket().getMarketId();
        /**
         * 更改客户联系人信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getContactsList())) {
            List<Contacts> contactsList = Lists.newArrayList();
            updateInput.getContactsList().forEach(t -> {
                if (Objects.nonNull(t)) {
                    Contacts temp = new Contacts();
                    BeanUtils.copyProperties(t, temp);
                    temp.setCustomerId(updateInput.getId());
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
            contactsService.deleteByCustomerId(updateInput.getId(), updateInput.getCustomerMarket().getMarketId());
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
                    temp.setCustomerId(updateInput.getId());
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
                        Customer customer = this.get(updateInput.getId());
                        customer.setCurrentCityPath(t.getCityPath());
                        customer.setCurrentCityName(t.getCityName());
                        customer.setCurrentAddress(t.getAddress());

                    }
                }
            });
            addressService.batchSaveOrUpdate(addressList);
        } else {
            addressService.deleteByCustomerAndMarket(updateInput.getId(), marketId);
        }
        /**
         * 更新客户车辆资源信息
         */
        if (CollectionUtil.isNotEmpty(updateInput.getVehicleInfoList())) {
            List<VehicleInfo> vehicleInfoList = Lists.newArrayList();
            Boolean exist = false;
            Set<String> registrationNumberSet = Sets.newHashSetWithExpectedSize(updateInput.getVehicleInfoList().size());
            for (com.dili.customer.sdk.domain.VehicleInfo vehicleInfo : updateInput.getVehicleInfoList()) {
                if (Objects.nonNull(vehicleInfo)) {
                    if (registrationNumberSet.contains(vehicleInfo.getRegistrationNumber())) {
                        exist = true;
                        break;
                    }
                    registrationNumberSet.add(vehicleInfo.getRegistrationNumber());
                    VehicleInfo temp = new VehicleInfo();
                    BeanUtils.copyProperties(vehicleInfo, temp);
                    temp.setCustomerId(updateInput.getId());
                    temp.setMarketId(marketId);
                    temp.setModifyTime(LocalDateTime.now());
                    temp.setModifierId(updateInput.getOperatorId());
                    if (Objects.isNull(temp.getId())) {
                        temp.setCreatorId(updateInput.getOperatorId());
                        temp.setCreateTime(temp.getModifyTime());
                    }
                    vehicleInfoList.add(temp);
                }
            }
            if (exist) {
                throw new AppException("车辆信息车牌号存在重复");
            }
            vehicleInfoService.batchSaveOrUpdate(vehicleInfoList);
        } else {
            vehicleInfoService.deleteByCustomerAndMarket(updateInput.getId(), marketId);
        }
        return BaseOutput.success();
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
        BaseOutput<Customer> customerBaseOutput = this.insertByContactsPhone(dto.getContactsPhone(), dto.getSourceSystem(), dto.getName());
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
        /**
         * 验证手机号对应的客户是否已存在
         */
        List<Customer> validatedCellphoneCustomer = customerQueryService.getValidatedCellphoneCustomer(input.getContactsPhone());
        if (CollectionUtil.isNotEmpty(validatedCellphoneCustomer)) {
            if (validatedCellphoneCustomer.size() > 1) {
                return BaseOutput.failure("数据有误,手机号已被多个客户实名");
            }
            Customer customer = validatedCellphoneCustomer.get(0);
            if (StrUtil.isNotBlank(customer.getCertificateNumber()) && !Objects.equals(customer.getCertificateNumber(), input.getCertificateNumber())) {
                return BaseOutput.failure("该手机号已认证绑定的其它证件客户");
            }
        }
        Long oldDelId = null;
        Customer customer = this.get(input.getId());
        if (Objects.isNull(customer) || YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())) {
            return BaseOutput.failure("客户信息不存在");
        }
        Optional<String> checkCertificateType = checkCertificateType(input.getOrganizationType(), input.getCertificateType());
        if (checkCertificateType.isPresent()) {
            return BaseOutput.failure(checkCertificateType.get());
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
        Customer baseInfoByCertificateNumber = customerQueryService.getBaseInfoByCertificateNumber(input.getCertificateNumber());
        if (Objects.nonNull(baseInfoByCertificateNumber)) {
            if (!StrUtil.equalsIgnoreCase(baseInfoByCertificateNumber.getOrganizationType(), input.getOrganizationType())) {
                return BaseOutput.failure("已存在证件号相同，组织类型不同的客户信息");
            }
            /**
             * 验证客户证件号是否已存在实名验证手机号的客户
             */
            if (baseInfoByCertificateNumber.getIsCellphoneValid().equals(YesOrNoEnum.YES.getCode()) && !Objects.equals(baseInfoByCertificateNumber.getContactsPhone(), input.getContactsPhone())) {
                return BaseOutput.failure("该[企业/个人]证件号已认证绑定的其它手机号");
            }
            if (!Objects.equals(customer.getId(), baseInfoByCertificateNumber.getId())) {
                oldDelId = customer.getId();
            }
            //复制数据库已有值到客户对象中
            BeanUtil.copyProperties(baseInfoByCertificateNumber, customer);
            /**
             * 获取数据库已有证件号对应的账号信息
             * 如果已经存在，则需要把当前账号信息合并到已有账号中
             * 如果不存在，则可直接用当前账号信息
             */
            Optional<UserAccount> byCustomerId = userAccountService.getByCustomerId(baseInfoByCertificateNumber.getId());
            if (byCustomerId.isPresent()) {
                customer.setContactsPhone(input.getContactsPhone());
                customer.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
                UserAccount userAccount = byCustomerId.get();
                userAccount.setCertificateNumber(input.getCertificateNumber());
                if (Objects.nonNull(accountData)) {
                    if (!Objects.equals(accountData.getId(), userAccount.getId())) {
                        Long oldAccountId = accountData.getId();
                        String oldPwd = accountData.getPassword();
                        BeanUtil.copyProperties(userAccount, accountData);
                        accountData.setPassword(oldPwd);
                        userAccountService.delete(oldAccountId);
                        accountTerminalService.updateAccountId(oldAccountId, userAccount.getId());
                    }
                }
            }
        } else {
            if (CustomerEnum.OrganizationType.INDIVIDUAL.equalsToCode(input.getOrganizationType())) {
                if (!IdcardUtil.isValidCard(input.getCertificateNumber())) {
                    return BaseOutput.failure("个人证件号码错误");
                }
            }
            customer.setOrganizationType(input.getOrganizationType());
            customer.setCertificateNumber(input.getCertificateNumber());
            customer.setCertificateType(input.getCertificateType());
        }
        customer.setName(input.getName());
        if (Objects.isNull(accountData)) {
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
        if (Objects.nonNull(oldDelId)) {
            this.logicDelete(oldDelId);
        }
        customer.setIsCellphoneValid(accountData.getCellphoneValid());
        this.update(customer);
        CustomerMarket oldCustomerMarket = customerMarketService.queryByMarketAndCustomerId(input.getCustomerMarket().getMarketId(), customer.getId());
        Boolean notApproval = commonDataService.checkCustomerNotApproval(input.getCustomerMarket().getMarketId());
        if (Objects.nonNull(oldCustomerMarket)) {
            oldCustomerMarket.setAlias(input.getName());
            if (notApproval) {
                oldCustomerMarket.setApprovalStatus(CustomerEnum.ApprovalStatus.PASSED.getCode());
                oldCustomerMarket.setState(CustomerEnum.State.NORMAL.getCode());
            } else {
                oldCustomerMarket.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
                oldCustomerMarket.setState(CustomerEnum.State.USELESS.getCode());
            }
            // 如果请求来自"农溯安-司机端"，且设置这个值为"driver_client_businessNature_absent"（客户经营性质暂时空缺）时，则暂时不设置客户经营性质的值
            if (!input.getCustomerMarket().getBusinessNature().equals(DRIVER_CLIENT_BUSINESSNATURE_TEMPORARY_ABSENT)) {
                oldCustomerMarket.setBusinessNature(input.getCustomerMarket().getBusinessNature());
            }
            oldCustomerMarket.setApprovalUserId(null);
            oldCustomerMarket.setApprovalTime(null);
            customerMarketService.update(oldCustomerMarket);
        } else {
            Boolean saveMarketInfo = true;
            if (Objects.nonNull(input.getCustomerMarket().getId())) {
                CustomerMarket old = customerMarketService.get(input.getCustomerMarket().getId());
                if (Objects.nonNull(old) && old.getCustomerId().equals(customer.getId())) {
                    old.setAlias(input.getName());
                    if (notApproval) {
                        old.setApprovalStatus(CustomerEnum.ApprovalStatus.PASSED.getCode());
                        old.setState(CustomerEnum.State.NORMAL.getCode());
                    } else {
                        old.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
                        old.setState(CustomerEnum.State.USELESS.getCode());
                    }
                    old.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
                    // 如果请求来自"农溯安-司机端"，且设置这个值为"driver_client_businessNature_absent"（客户经营性质暂时空缺）时，则暂时不设置客户经营性质的值
                    if (!input.getCustomerMarket().getBusinessNature().equals(DRIVER_CLIENT_BUSINESSNATURE_TEMPORARY_ABSENT)) {
                        old.setBusinessNature(input.getCustomerMarket().getBusinessNature());
                    }
                    old.setApprovalUserId(null);
                    old.setApprovalTime(null);
                    customerMarketService.update(old);
                    saveMarketInfo = false;
                }
            }
            if (saveMarketInfo) {
                CustomerMarket customerMarket = BeanUtil.copyProperties(input.getCustomerMarket(), CustomerMarket.class, "id", "grade");
                customerMarket.setGrade(customerCommonConfig.getDefaultGrade(customerMarket.getMarketId()).getCode());
                customerMarket.setCustomerId(customer.getId());
                if (notApproval) {
                    customerMarket.setApprovalStatus(CustomerEnum.ApprovalStatus.PASSED.getCode());
                    customerMarket.setState(CustomerEnum.State.NORMAL.getCode());
                } else {
                    customerMarket.setApprovalStatus(CustomerEnum.ApprovalStatus.WAIT_CONFIRM.getCode());
                    customerMarket.setState(CustomerEnum.State.USELESS.getCode());
                }
                customerMarket.setCreateTime(LocalDateTime.now());
                customerMarket.setModifyTime(LocalDateTime.now());
                customerMarket.setAlias(input.getName());
                customerMarketService.insert(customerMarket);
            }
        }
        Long marketId = input.getCustomerMarket().getMarketId();
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(input.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(input.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketId);
        }
        /**
         * 如果客户理货区不为空，则保存对应的理货区信息
         */
        if (CollectionUtil.isNotEmpty(input.getTallyingAreaList())) {
            List<TallyingArea> tallyingAreaList = JSONArray.parseArray(JSONObject.toJSONString(input.getTallyingAreaList()), TallyingArea.class);
            tallyingAreaService.saveInfo(tallyingAreaList, customer.getId(), marketId);
        } else {
            //如果传入的客户理货区为空，则表示该客户在该市场没有租赁理货区(手动关联的，可编辑)，所有可以直接删除
            tallyingAreaService.deleteByCustomerId(customer.getId(), marketId);
        }
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(input.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(input.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketId);
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(input.getCharacterTypeList())) {
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(input.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList, customer.getId(), marketId);
            customer.setCharacterTypeList(characterTypeList);
        }
        //组装保存客户附件信息
        if (CollectionUtil.isNotEmpty(input.getAttachmentList())) {
            List<Attachment> attachmentList = JSONArray.parseArray(JSONObject.toJSONString(input.getAttachmentList()), Attachment.class);
            attachmentService.batchSave(attachmentList, customer.getId(), marketId);
        }
        if (CollectionUtils.isNotEmpty(input.getVehicleInfoList())) {
            List<com.dili.customer.domain.VehicleInfo> vehicleInfoList = JSONArray.parseArray(JSONObject.toJSONString(input.getVehicleInfoList()), com.dili.customer.domain.VehicleInfo.class);
            vehicleInfoService.batchSaveOrUpdate(vehicleInfoList);
        }
        return BaseOutput.successData(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Long> batchCompleteIndividual(List<CustomerUpdateInput> inputList) {
        String result = null;
        Long customerId = null;
        Set<Long> marketIds = new HashSet<>();
        for (CustomerUpdateInput input : inputList) {
            if (!IdcardUtil.isValidCard(input.getCertificateNumber())) {
                result = "个人证件号码错误";
                break;
            }
            if (Objects.nonNull(customerId)) {
                input.setId(customerId);
            }
            input.setOrganizationType(CustomerEnum.OrganizationType.INDIVIDUAL.getCode());
            BaseOutput<Customer> customerBaseOutput = this.completeInfo(input);
            if (!customerBaseOutput.isSuccess()) {
                result = customerBaseOutput.getMessage();
                break;
            }
            customerId = customerBaseOutput.getData().getId();
            marketIds.add(input.getCustomerMarket().getMarketId());
        }
        if (StrUtil.isBlank(result)) {
            return BaseOutput.successData(customerId).setMetadata(marketIds);
        }
        throw new AppException(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Long> batchCompleteEnterprise(List<CustomerUpdateInput> inputList) {
        String result = null;
        Long customerId = null;
        Set<Long> marketIds = new HashSet<>();
        for (CustomerUpdateInput input : inputList) {
            input.setOrganizationType(CustomerEnum.OrganizationType.ENTERPRISE.getCode());
            if (Objects.nonNull(customerId)) {
                input.setId(customerId);
            }
            BaseOutput<Customer> customerBaseOutput = this.completeInfo(input);
            if (!customerBaseOutput.isSuccess()) {
                result = customerBaseOutput.getMessage();
                break;
            }
            marketIds.add(input.getCustomerMarket().getMarketId());
            customerId = customerBaseOutput.getData().getId();
        }
        if (StrUtil.isBlank(result)) {
            return BaseOutput.successData(customerId).setMetadata(marketIds);
        }
        throw new AppException(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<Customer> insertByContactsPhone(String contactsPhone, String sourceSystem, String name) {
        List<Customer> validatedCellphoneCustomerList = customerQueryService.getValidatedCellphoneCustomer(contactsPhone);
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
        saveBusinessLogger(customerId, data.getCode(), String.format("手机号%s验证通过", cellphone), "手机号验证通过", "edit");
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void defaultRegister(Customer customer) {
        if (Objects.nonNull(customer)) {
            customer.setIsDelete(YesOrNoEnum.NO.getCode());
            String customerCode = getCustomerCode();
            if (StrUtil.isBlank(customerCode)) {
                throw new AppException(ResultCode.APP_ERROR, "未获取到编号");
            }
            customer.setCode(customerCode);
            customer.setCreateTime(LocalDateTime.now());
            customer.setModifyTime(customer.getCreateTime());
            customer.setSourceChannel("auto_register");
            this.insertSelective(customer);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput updateBaseInfo(CustomerBaseUpdateInput input) {
        return updateBaseInfo(input, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput updateBaseInfo(CustomerBaseUpdateInput updateInput, Boolean isLogger) {
        Customer customer = this.get(updateInput.getId());
        if (Objects.isNull(customer)) {
            return BaseOutput.failure("客户信息已不存在").setCode(ResultCode.DATA_ERROR);
        }
        //如果更新了客户电话，且电话未实名，则需要验证电话是否已存在
        if (!updateInput.getContactsPhone().equals(customer.getContactsPhone()) && YesOrNoEnum.NO.getCode().equals(customer.getIsCellphoneValid())) {
            List<Customer> phoneExist = customerQueryService.getByContactsPhone(updateInput.getContactsPhone(), customer.getOrganizationType());
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
        //更改市场归属信息
        CustomerMarket customerMarket = customerMarketService.queryByMarketAndCustomerId(updateInput.getCustomerMarket().getMarketId(), updateInput.getId());
        if (Objects.isNull(customerMarket)) {
            customerMarket = new CustomerMarket();
        }
        BeanUtils.copyProperties(updateInput.getCustomerMarket(), customerMarket, "grade", "modifyTime", "approvalStatus", "approvalUserId", "approvalTime", "approvalNotes");
        customerMarket.setCustomerId(updateInput.getId());
        customerMarket.setModifierId(updateInput.getOperatorId());
        customerMarket.setAlias(updateInput.getName());
        if (Objects.isNull(customerMarket.getId())) {
            customerMarket.setCreatorId(updateInput.getOperatorId());
            customerMarket.setCreateTime(LocalDateTime.now());
            customerMarket.setModifyTime(LocalDateTime.now());
        }
        customerMarketService.saveOrUpdate(customerMarket);
        //声明市场ID变量，以便使用
        Long marketId = customerMarket.getMarketId();
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
        //更新客户经营品类信息
        if (CollectionUtil.isNotEmpty(updateInput.getBusinessCategoryList())) {
            List<BusinessCategory> businessCategoryList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getBusinessCategoryList()), BusinessCategory.class);
            businessCategoryService.saveInfo(businessCategoryList, customer.getId(), marketId);
        }
        //组装保存客户角色身份信息
        if (CollectionUtil.isNotEmpty(updateInput.getCharacterTypeList())) {
            List<CharacterType> characterTypeList = JSONArray.parseArray(JSONObject.toJSONString(updateInput.getCharacterTypeList()), CharacterType.class);
            characterTypeService.saveInfo(characterTypeList, customer.getId(), marketId);
            customer.setCharacterTypeList(characterTypeList);
        }
        customer.setName(updateInput.getName());
        int update = super.update(customer);
        if (update == 0) {
            return BaseOutput.failure("数据已变更，更新失败");
        }
        if (isLogger) {
            StringBuffer content = new StringBuffer("修改后的数据为：");
            List<CharacterTypeGroupDto> characterTypeGroupDtoList = commonDataService.produceCharacterTypeGroup(updateInput.getCharacterTypeList(), getOperatorUserTicket().getFirmId());
            StringBuffer characterType = new StringBuffer("角色身份：");
            characterTypeGroupDtoList.stream().forEach(t -> {
                if (t.getSelected()) {
                    characterType.append(" &").append(t.getValue());
                    if (CollectionUtil.isNotEmpty(t.getSubTypeList())) {
                        t.getSubTypeList().forEach(s -> {
                            if (s.getSelected()) {
                                characterType.append(" ").append(s.getName());
                            }
                        });
                    }
                }
            });
            content.append("客户名称").append(customer.getName()).append(characterType)
                    .append("手机号:").append(customer.getContactsPhone());
            saveBusinessLogger(customer.getId(), customer.getCode(), content.toString(), "修改渠道:APP", "edit");
        }
        return BaseOutput.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<String> updateCertificateInfo(CustomerCertificateInput customerCertificate, Boolean isLogger) {
        if (Objects.isNull(customerCertificate.getId())) {
            return Optional.of("数据ID不能为空");
        }
        //如果法人证件号不为空，且证件号不是有效的人身份证，则认为证件号是错误的
        if (StrUtil.isNotBlank(customerCertificate.getCorporationCertificateNumber()) && IdcardUtil.isValidCard(customerCertificate.getCorporationCertificateNumber())) {
            return Optional.of("法人证件号码不正确");
        }
        Customer customer = this.get(customerCertificate.getId());
        BeanValidationResult beanValidationResult = null;
        if (CustomerEnum.OrganizationType.ENTERPRISE.equalsToCode(customer.getOrganizationType())) {
            beanValidationResult = ValidationUtil.warpValidate(customerCertificate, Default.class, EnterpriseView.class);
        } else {
            beanValidationResult = ValidationUtil.warpValidate(customerCertificate, Default.class);
        }
        if (!beanValidationResult.isSuccess()) {
            return Optional.of(beanValidationResult.getErrorMessages().get(0).getMessage());
        }
        customer.setCertificateRange(customerCertificate.getCertificateRange());
        customer.setCertificateAddr(customerCertificate.getCertificateAddr());
        if (Objects.isNull(customerCertificate.getCertificateLongTerm())) {
            customerCertificate.setCertificateLongTerm(YesOrNoEnum.NO.getCode());
        }
        customer.setCertificateLongTerm(customerCertificate.getCertificateLongTerm());
        //以下数据为企业客户才有的数据
        customer.setCorporationCertificateType(customerCertificate.getCorporationCertificateType());
        customer.setCorporationCertificateNumber(customerCertificate.getCorporationCertificateNumber());
        customer.setCorporationName(customerCertificate.getCorporationName());
        int update = this.update(customer);
        if (update == 0) {
            return Optional.of("数据已变更，更新失败");
        }
        if (isLogger) {
            StringBuffer content = new StringBuffer("修改后的数据为：");
            content.append("证件有效期:").append(customer.getCertificateRange())
                    .append("证件地址:").append(customer.getCertificateAddr())
                    .append("证件是否长期有效:").append(YesOrNoEnum.getYesOrNoEnum(customer.getCertificateLongTerm()).getName())
                    .append("法人姓名:").append(customer.getCorporationName())
                    .append("法人证件号:").append(customer.getCorporationCertificateNumber());
            saveBusinessLogger(customer.getId(), customer.getCode(), content.toString(), "修改渠道:APP", "edit");
        }
        return Optional.empty();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long customerId) {
        Customer customer = get(customerId);
        customer.setIsDelete(YesOrNoEnum.YES.getCode());
        this.update(customer);
    }

    @Override
    public Optional<String> verificationCellPhone(String cellphone, Long customerId, String verificationCode) {
        BaseOutput<Customer> validatedCellphoneCustomer = customerQueryService.getSingleValidatedCellphoneCustomer(cellphone);
        if (validatedCellphoneCustomer.isSuccess()) {
            if (Objects.nonNull(validatedCellphoneCustomer.getData())) {
                if (validatedCellphoneCustomer.getData().getId().equals(customerId)) {
                    return Optional.of("此手机号已被该客户认证");
                } else {
                    return Optional.of("您的联系电话系统已存在，请更换其他号码，谢谢！");
                }
            }
            Optional<String> result = commonDataService.checkVerificationCode(cellphone, CustomerConstant.COMMON_SCENE_CODE, verificationCode);
            if (result.isEmpty()) {
                return Optional.ofNullable(updateCellphoneValid(customerId, cellphone));
            } else {
                return Optional.of(result.get());
            }
        } else {
            return Optional.of(validatedCellphoneCustomer.getMessage());
        }
    }

    @Override
    public void asyncSendCustomerToMq(String exchange, Long customerId, Long marketId) {
        send(exchange, customerQueryService.get(customerId, marketId));
    }

    /**
     * 批量异步数据发送MQ
     *
     * @param exchange
     * @param customerId
     * @param marketIds
     */
    @Override
    public void asyncSendCustomerToMq(String exchange, Long customerId, Set<Long> marketIds) {
        marketIds.forEach(t -> {
            send(exchange, customerQueryService.get(customerId, t));
        });
    }

    /**
     * 组装客户联系人信息
     *
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
        if (StringUtils.isNotBlank(customer.getCurrentCityName())) {
            contacts.setAddress(customer.getCurrentCityName().replaceAll(",", "") + customer.getCurrentAddress());
        }
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
     * 根据手机号，获取此手机号已认证的数量
     *
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
     *
     * @return 客户编码
     */
    private String getCustomerCode() {
        return uidRpcService.getBizNumber(UID_TYPE);
    }

    /**
     * 验证完善资料时的数据
     *
     * @param input
     * @return
     */
    private Optional<String> validationCompleteData(CustomerUpdateInput input) {
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

    /**
     * 获取当前操作人的信息
     *
     * @return
     */
    private UserTicket getOperatorUserTicket() {
        return SessionContext.getSessionContext().getUserTicket();
    }

    /**
     * 保存客户操作日志
     *
     * @param businessId    业务ID
     * @param businessCode  业务编号
     * @param content       日志内容
     * @param notes         备注
     * @param operationType 操作类型
     */
    private void saveBusinessLogger(Long businessId, String businessCode, String content, String notes, String operationType) {
        businessLogRpcService.asyncSave(businessId, businessCode, content, notes, operationType, getOperatorUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
    }

    /**
     * 根据组织类型验证证件类型是否合法
     *
     * @param organizationType 组织类型
     * @param certificateType  证件类型
     * @return
     */
    private Optional<String> checkCertificateType(String organizationType, String certificateType) {
        if (StrUtil.isBlank(organizationType) || StrUtil.isBlank(certificateType)) {
            return Optional.of("证件类型校验参数丢失");
        }
        List<DataDictionaryValue> dataDictionaryValueList = null;
        if (CustomerEnum.OrganizationType.ENTERPRISE.equalsToCode(organizationType)) {
            dataDictionaryValueList = commonDataService.queryEnterpriseCertificate(null);
        } else if (CustomerEnum.OrganizationType.INDIVIDUAL.equalsToCode(organizationType)) {
            dataDictionaryValueList = commonDataService.queryIndividualCertificate(null);
        }
        if (CollectionUtil.isEmpty(dataDictionaryValueList)) {
            return Optional.of("证件类型数据为空");
        }
        boolean b = dataDictionaryValueList.stream().anyMatch(t -> Objects.equals(t.getCode().toLowerCase(), certificateType.toLowerCase()));
        return b ? Optional.empty() : Optional.of("证件类型错误");
    }

    /**
     * 发送消息
     *
     * @param exchange
     * @param customer
     */
    private void send(String exchange, Customer customer) {
        rabbitMQMessageService.send(exchange, null, JSONObject.toJSONString(customer));
    }
}