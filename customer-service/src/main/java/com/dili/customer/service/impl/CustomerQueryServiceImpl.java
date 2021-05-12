package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.commons.service.DepartmentRpcService;
import com.dili.customer.commons.service.UapUserRpcService;
import com.dili.customer.domain.*;
import com.dili.customer.mapper.CustomerMapper;
import com.dili.customer.sdk.domain.query.CustomerBaseQueryInput;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.*;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.POJOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/3/24 15:32
 */
@Slf4j
@Service
public class CustomerQueryServiceImpl  extends BaseServiceImpl<Customer, Long> implements CustomerQueryService {

    private CustomerMapper getActualMapper() {
        return (CustomerMapper) getDao();
    }

    @Autowired
    private TallyingAreaService tallyingAreaService;
    @Autowired
    private BusinessCategoryService businessCategoryService;
    @Autowired
    private CharacterTypeService characterTypeService;
    @Autowired
    private CommonDataService commonDataService;
    @Autowired
    private UapUserRpcService uapUserRpcService;
    @Autowired
    private DepartmentRpcService departmentRpcService;
    @Autowired
    private CustomerMarketService customerMarketService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private VehicleInfoService vehicleInfoService;


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
    public PageOutput<List<Customer>> listSimpleForPageWithAuth(CustomerQueryInput input) {
        Optional<CustomerQueryInput> customerQueryInput = produceQueryCondition(input);
        if (customerQueryInput.isPresent()) {
            return listSimpleForPage(input);
        }
        return PageOutput.success();
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
                produceCustomerMarketOutputData(t.getCustomerMarket());
            });
        }
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(input.getRows()).setPages(totalPage);
        return output;
    }

    @Override
    public PageOutput<List<Customer>> listForPageWithAuth(CustomerQueryInput input) {
        Optional<CustomerQueryInput> customerQueryInput = produceQueryCondition(input);
        if (customerQueryInput.isPresent()) {
            return listForPage(input);
        }
        return PageOutput.success();
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
            customer.setCustomerMarket(customerFirm);
            return BaseOutput.failure("该证件号对应的客户已存在").setCode(ResultCode.DATA_ERROR).setData(customer);
        }
        return BaseOutput.success().setData(customer);
    }

    @Override
    public List<Customer> getValidatedCellphoneCustomer(String cellphone) {
        Customer condition = new Customer();
        condition.setContactsPhone(cellphone);
        condition.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
        condition.setIsDelete(YesOrNoEnum.NO.getCode());
        return list(condition);
    }

    @Override
    public Customer get(Long id, Long marketId) {
        CustomerQueryInput condition = new CustomerQueryInput();
        condition.setId(id);
        condition.setMarketId(marketId);
        PageOutput<List<Customer>> pageOutput = this.listForPage(condition);
        return pageOutput.getData().stream().findFirst().orElse(null);
    }

    @Override
    public BaseOutput<Customer> getSingleValidatedCellphoneCustomer(String cellphone) {
        List<Customer> customerList = this.getValidatedCellphoneCustomer(cellphone);
        if (CollectionUtil.isNotEmpty(customerList)) {
            if (customerList.size() > 1) {
                return BaseOutput.failure("数据有误,手机号已被多个客户实名");
            }
            return BaseOutput.successData(customerList.get(0));
        }
        return BaseOutput.success();
    }

    @Override
    public List<Customer> getByContactsPhone(String contactsPhone, String organizationType) {
        Customer queryPhone = new Customer();
        if (StrUtil.isNotBlank(organizationType)) {
            queryPhone.setOrganizationType(organizationType);
        }
        queryPhone.setContactsPhone(contactsPhone);
        return list(queryPhone);
    }

    /**
     * 构造按权限过滤的查询条件
     * @param input
     * @return
     */
    private Optional<CustomerQueryInput> produceQueryCondition(CustomerQueryInput input){
        UserTicket userTicket = getOperatorUserTicket();
        com.dili.customer.sdk.domain.CustomerMarket customerMarket = input.getCustomerMarket();
        if (Objects.isNull(customerMarket)) {
            customerMarket = new com.dili.customer.sdk.domain.CustomerMarket();
        }
        Boolean ownerAuth = commonDataService.checkCustomerOwnerAuth(userTicket.getFirmId());
        Boolean departmentAuth = commonDataService.checkCustomerDepartmentAuth(userTicket.getFirmId());
        /**
         * 归属人权限，需依赖归属部门，人属于部门下
         * 当要求按归属人隔离时，则需要验证归属部门
         */
        if (ownerAuth || departmentAuth) {
            List<Department> departmentList = departmentRpcService.listUserAuthDepartmentByFirmId(userTicket.getId(), userTicket.getFirmId());
            if (CollectionUtil.isEmpty(departmentList)) {
                log.warn(String.format("市场【%s】设置了按部门权限隔离客户数据，用户【%s】没有任何部门权限"), userTicket.getFirmCode(), userTicket.getRealName());
                return Optional.empty();
            }
            Set<Long> collect = departmentList.stream().filter(t -> Objects.nonNull(t.getId())).map(t -> t.getId()).collect(Collectors.toSet());
            customerMarket.setDepartmentIdSet(collect);
            if (ownerAuth) {
                customerMarket.setOwnerIds(String.valueOf(userTicket.getId()));
            }
        }
        input.setCustomerMarket(customerMarket);
        return Optional.ofNullable(input);
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
     * 生成客户市场数据输出对象
     * @param customerMarket 客户信息对象
     */
    private void produceCustomerMarketOutputData(CustomerMarket customerMarket) {
        if (Objects.isNull(customerMarket)){
            return;
        }
        String businessNature = customerMarket.getBusinessNature();
        if (StrUtil.isNotBlank(businessNature)) {
            List<DataDictionaryValue> dataDictionaryValues = commonDataService.queryBusinessNature(null);
            Optional<DataDictionaryValue> first = dataDictionaryValues.stream().filter(d -> d.getCode().equals(businessNature)).findFirst();
            if (first.isPresent()) {
                customerMarket.setMetadata("businessNatureValue", first.get().getName());
            }
        }
        if (StrUtil.isNotBlank(customerMarket.getOwnerIds())) {
            List<String> collect = Arrays.stream(customerMarket.getOwnerIds().split(",")).map(String::toString).collect(Collectors.toList());
            List<User> userList = uapUserRpcService.listUserByIds(collect);
            if (CollectionUtil.isNotEmpty(userList)) {
                customerMarket.setMetadata("userRealName", userList.stream().map(User::getRealName).collect(Collectors.joining(",")));
            }
        }
        if (StrUtil.isNotBlank(customerMarket.getDepartmentIds())) {
            Set<Long> collect = Arrays.stream(customerMarket.getDepartmentIds().split(",")).map(Long::parseLong).collect(Collectors.toSet());
            List<Department> departmentList = departmentRpcService.getByIds(collect);
            if (CollectionUtil.isNotEmpty(departmentList)) {
                customerMarket.setMetadata("departmentName", departmentList.stream().map(Department::getName).collect(Collectors.joining(",")));
            }
        }
        if (Objects.nonNull(customerMarket.getBusinessRegionTag())) {
            customerMarket.setMetadata("businessRegionTagValue", CustomerEnum.BusinessRegionTag.getValueByCode(customerMarket.getBusinessRegionTag()));
        }
    }

    /**
     * 获取当前操作人的信息
     * @return
     */
    private UserTicket getOperatorUserTicket(){
        return  SessionContext.getSessionContext().getUserTicket();
    }

}
