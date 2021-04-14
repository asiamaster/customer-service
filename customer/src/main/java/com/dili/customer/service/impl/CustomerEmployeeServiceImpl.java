package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.constants.CustomerResultCode;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.domain.*;
import com.dili.customer.mapper.CustomerEmployeeMapper;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.domain.query.CustomerEmployeeDetailQuery;
import com.dili.customer.sdk.domain.query.CustomerEmployeeQuery;
import com.dili.customer.service.CharacterTypeService;
import com.dili.customer.service.CustomerEmployeeService;
import com.dili.customer.service.EmployeeCardService;
import com.dili.customer.service.EmployeeService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.POJOUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:54
 */
@Service
public class CustomerEmployeeServiceImpl extends BaseServiceImpl<CustomerEmployee, Long> implements CustomerEmployeeService {

    @Autowired
    private CustomerEmployeeMapper customerEmployeeMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeCardService employeeCardService;
    @Autowired
    private CharacterTypeService characterTypeService;
    @Autowired
    private CommonDataService commonDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<String> createdByOpenCard(EmployeeOpenCardInput input) {
        Optional<Employee> byCellphone = employeeService.getByCellphone(input.getCellphone());
        Employee employee = byCellphone.orElseGet(() -> {
            Employee saveData = new Employee();
            saveData.setCellphone(input.getCellphone());
            saveData.setName(input.getName());
            employeeService.add(saveData);
            return saveData;
        });
        if (!employee.getName().equalsIgnoreCase(input.getName())) {
            return BaseOutput.failure("已存在相同手机号但名称不一致的数据").setCode(ResultCode.DATA_ERROR);
        }
        Optional<CustomerEmployee> customerEmployeeOptional = queryByCustomerAndEmployee(input.getCustomerId(), employee.getId());
        CustomerEmployee customerEmployee = customerEmployeeOptional.orElseGet(() -> {
            CustomerEmployee saveData = new CustomerEmployee();
            saveData.setCustomerId(input.getCustomerId());
            saveData.setEmployeeId(employee.getId());
            saveData.setCreateTime(LocalDateTime.now());
            saveData.setModifyTime(LocalDateTime.now());
            saveData.setDeleted(YesOrNoEnum.NO.getCode());
            this.insertSelective(saveData);
            return saveData;
        });
        Optional<EmployeeCard> byCustomerEmployeeIdAndCardAccountId = employeeCardService.getByCustomerEmployeeIdAndCardAccountId(customerEmployee.getId(), input.getCardAccountId(), input.getMarketId());
        if (byCustomerEmployeeIdAndCardAccountId.isPresent()) {
            return BaseOutput.failure("数据已存在，请勿重复添加").setCode(CustomerResultCode.DUPLICATE_DATA);
        }
        employeeCardService.add(customerEmployee.getId(), input.getCardAccountId(), input.getMarketId(), input.getCardNo());
        return BaseOutput.success();
    }

    @Override
    public Optional<CustomerEmployee> queryByCustomerAndEmployee(Long customerId, Long employeeId) {
        if (Objects.isNull(customerId) || Objects.isNull(employeeId)) {
            return Optional.empty();
        }
        CustomerEmployee condition = new CustomerEmployee();
        condition.setCustomerId(customerId);
        condition.setEmployeeId(employeeId);
        condition.setDeleted(YesOrNoEnum.NO.getCode());
        List<CustomerEmployee> list = this.list(condition);
        return CollectionUtil.isEmpty(list) ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<String> cancelCard(EmployeeCancelCardInput employeeCancelCardInput) {
        Optional<Employee> byCellphone = employeeService.getByCellphone(employeeCancelCardInput.getCellphone());
        if (byCellphone.isEmpty()) {
            return Optional.of("根据手机号未查询出对应的数据");
        }
        Employee employee = byCellphone.get();
        Optional<CustomerEmployee> customerEmployeeOption = queryByCustomerAndEmployee(employeeCancelCardInput.getCustomerId(), employee.getId());
        if (customerEmployeeOption.isEmpty()) {
            return Optional.of("客户员工关系不存在");
        }
        CustomerEmployee customerEmployee = customerEmployeeOption.get();
        Optional<EmployeeCard> byCustomerEmployeeIdAndCardAccountId = employeeCardService.getByCustomerEmployeeIdAndCardAccountId(customerEmployee.getId(), employeeCancelCardInput.getCardAccountId(), employeeCancelCardInput.getMarketId());
        if (byCustomerEmployeeIdAndCardAccountId.isEmpty()) {
            return Optional.of("客户员工未持有该卡");
        }
        EmployeeCard employeeCard = byCustomerEmployeeIdAndCardAccountId.get();
        employeeCardService.logicDelete(employeeCard.getId());
        List<EmployeeCard> employeeCardList = employeeCardService.listByCustomerEmployeeId(customerEmployee.getId());
        long count = employeeCardList.stream().filter(t -> !t.getId().equals(employeeCard.getId())).count();
        /**
         * 如果除了当前持卡信息，已无其它的持卡数据，则任务该员工已不属于该客户了
         * 要设置客户员工关系为解除
         */
        if (count == 0) {
            customerEmployee.setDeleted(YesOrNoEnum.YES.getCode());
            customerEmployee.setModifyTime(LocalDateTime.now());
            this.update(customerEmployee);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> changeCard(EmployeeChangeCardInput employeeChangeCardInput) {
        Optional<Employee> byCellphone = employeeService.getByCellphone(employeeChangeCardInput.getCellphone());
        if (byCellphone.isEmpty()) {
            return Optional.of("根据手机号未查询出对应的数据");
        }
        Employee employee = byCellphone.get();
        Optional<CustomerEmployee> customerEmployeeOption = queryByCustomerAndEmployee(employeeChangeCardInput.getCustomerId(), employee.getId());
        if (customerEmployeeOption.isEmpty()) {
            return Optional.of("客户员工关系不存在");
        }
        CustomerEmployee customerEmployee = customerEmployeeOption.get();
        Optional<EmployeeCard> byCustomerEmployeeIdAndCardAccountId = employeeCardService.getByCustomerEmployeeIdAndCardAccountId(customerEmployee.getId(), employeeChangeCardInput.getCardAccountId(), employeeChangeCardInput.getMarketId());
        if (byCustomerEmployeeIdAndCardAccountId.isEmpty()) {
            return Optional.of("客户员工未持有该卡");
        }
        EmployeeCard employeeCard = byCustomerEmployeeIdAndCardAccountId.get();
        employeeCard.setCardNo(employeeChangeCardInput.getCardNo());
        employeeCard.setModifyTime(LocalDateTime.now());
        employeeCardService.update(employeeCard);
        return Optional.empty();
    }

    @Override
    public List<Customer> listCustomerByEmployeeId(Long employeeId) {
        if (Objects.isNull(employeeId)) {
            return Collections.emptyList();
        }
        return customerEmployeeMapper.listCustomerByEmployeeId(employeeId);

    }

    @Override
    public List<Customer> listCustomerById(Long id) {
        if (Objects.isNull(id)) {
            return Collections.emptyList();
        }
        return customerEmployeeMapper.listCustomerById(id);
    }

    @Override
    public PageOutput<List<CustomerEmployeeList>> listPage(CustomerEmployeeQuery query) {
        if (StringUtils.isNotBlank(query.getSort())) {
            query.setSort(POJOUtils.humpToLineFast(query.getSort()));
        } else {
            query.setSort("c.id");
            query.setOrder("desc");
        }
        if (query.getRows() != null && query.getRows() >= 1) {
            PageHelper.startPage(query.getPage(), query.getRows());
        }
        List<CustomerEmployeeList> list = customerEmployeeMapper.listForPage(query);
        //总记录
        Long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        //总页数
        int totalPage = list instanceof Page ? ((Page) list).getPages() : 1;
        //当前页数
        int pageNum = list instanceof Page ? ((Page) list).getPageNum() : 1;
        PageOutput output = PageOutput.success();
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> customerIdSet = list.stream().map(CustomerEmployeeList::getId).collect(Collectors.toSet());
            //获取客户角色身份信息
            List<CharacterType> characterTypeList = characterTypeService.listByCustomerAndMarket(customerIdSet, query.getMarketId());
            Map<Long, List<CharacterType>> characterTypeMap = characterTypeList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(CharacterType::getCustomerId));
            list.forEach(t -> {
                if (characterTypeMap.containsKey(t.getId())) {
                    List<CharacterType> characterTypes = characterTypeMap.get(t.getId());
                    t.setCharacterTypeList(JSONArray.parseArray(JSONObject.toJSONString(characterTypes), com.dili.customer.sdk.domain.CharacterType.class));
                    t.setCharacterTypeGroupList(commonDataService.produceCharacterTypeGroup(JSONArray.parseArray(JSONObject.toJSONString(t.getCharacterTypeList()), com.dili.customer.sdk.domain.CharacterType.class), query.getMarketId()));
                }
            });
        }
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(query.getRows()).setPages(totalPage);
        return output;
    }

    @Override
    public PageOutput<List<CustomerEmployeeDetailList>> listEmployeePage(CustomerEmployeeDetailQuery query) {
        if (Objects.nonNull(query.getRows()) && query.getRows() >= 1 && Objects.nonNull(query.getPage()) && query.getPage() > 0) {
            PageHelper.startPage(query.getPage(), query.getRows());
        }
        List<CustomerEmployeeDetailList> list = customerEmployeeMapper.listEmployeePage(query);
        //总记录
        Long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        //总页数
        int totalPage = list instanceof Page ? ((Page) list).getPages() : 1;
        //当前页数
        int pageNum = list instanceof Page ? ((Page) list).getPageNum() : 1;
        PageOutput output = PageOutput.success();
        output.setData(list).setPageNum(pageNum).setTotal(total).setPageSize(query.getRows()).setPages(totalPage);
        return output;
    }
}
