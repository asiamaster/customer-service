package com.dili.customer.api;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.annotation.UapToken;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.sdk.constants.MqConstant;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.domain.query.CustomerBaseQueryInput;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.*;
import com.dili.customer.service.CustomerManageService;
import com.dili.customer.service.CustomerQueryService;
import com.dili.customer.service.MqService;
import com.dili.customer.service.UserAccountService;
import com.dili.customer.utils.LoginUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 客户基础信息
 * This file was generated on 2019-12-27 14:43:13.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    private final CustomerManageService customerManageService;
    private final CustomerQueryService customerQueryService;
    private final UserAccountService userAccountService;
    private final MqService mqService;

    /**
     * 分页查询客户数据集
     * @param customer
     * @return
     */
    @PostMapping(value="/listPage")
    public PageOutput<List<Customer>> listPage(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("客户listPage查询:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        return customerQueryService.listForPage(customer);
    }

    /**
     * 分页查询客户数据集,此方法需要传入uap登录后的token
     * @param customer 查询条件
     * @return
     */
    @UapToken
    @PostMapping(value="/listPageWithAuth")
    public PageOutput<List<Customer>> listPageWithAuth(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("客户listPageWithAuth查询:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        return customerQueryService.listForPageWithAuth(customer);
    }

    /**
     * 分页查询正常的客户数据集
     * 用户未删除切状态为生效的
     * @param customer
     * @return
     */
    @PostMapping(value="/listNormalPage")
    public PageOutput<List<Customer>> listNormalPage(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("客户listNormalPage查询:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        if (Objects.isNull(customer.getCustomerMarket())){
            customer.setCustomerMarket(new CustomerMarket());
        }
        customer.getCustomerMarket().setState(CustomerEnum.State.NORMAL.getCode());
        customer.setIsDelete(YesOrNoEnum.NO.getCode());
        return customerQueryService.listForPage(customer);
    }

    /**
     * 分页查询简单客户数据集
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * 如不关心客户的理货区、车辆、图片等附加数据，建议用此接口
     * @param customer
     * @return
     */
    @PostMapping(value="/listSimplePage")
    public PageOutput<List<Customer>> listSimplePage(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("客户listSimplePage查询:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        return customerQueryService.listSimpleForPage(customer);
    }

    /**
     * 分页查询简单客户数据集，此方法需要传入uap登录后的token
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * 如不关心客户的理货区、车辆、图片等附加数据，建议用此接口
     * @param customer
     * @return
     */
    @UapToken
    @PostMapping(value="/listSimplePageWithAuth")
    public PageOutput<List<Customer>> listSimplePageWithAuth(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("listSimplePageWithAuth:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        return customerQueryService.listSimpleForPageWithAuth(customer);
    }

    /**
     * 分页查询正常的简单客户数据集
     * 用户未删除切状态为生效的
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * 如不关心客户的理货区、车辆、图片等附加数据，建议用此接口
     * @param customer
     * @return
     */
    @PostMapping(value="/listSimpleNormalPage")
    public PageOutput<List<Customer>> listSimpleNormalPage(@RequestBody CustomerQueryInput customer) {
        log.info(String.format("客户listSimpleNormalPage查询:%s", JSONUtil.toJsonStr(customer)));
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        if (Objects.isNull(customer.getCustomerMarket())){
            customer.setCustomerMarket(new CustomerMarket());
        }
        customer.getCustomerMarket().setState(CustomerEnum.State.NORMAL.getCode());
        customer.setIsDelete(YesOrNoEnum.NO.getCode());
        return customerQueryService.listSimpleForPage(customer);
    }

    /**
     * 只查询并返回客户基础数据，不会带有客户市场属性数据
     * 也不会查询 主表以外的数据
     * @param customer 客户查询条件
     * @return
     */
    @PostMapping(value = "/listBase")
    public BaseOutput<List<Customer>> listBase(@RequestBody CustomerBaseQueryInput customer) {
        log.info(String.format("客户listBase查询:%s", JSONUtil.toJsonStr(customer)));
        PageOutput<List<Customer>> pageOutput = customerQueryService.listBasePage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 根据id及市场，查询客户的信息
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/get")
    public BaseOutput<CustomerExtendDto> get(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId) {
        if (Objects.isNull(id) || Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        return BaseOutput.success().setData(customerQueryService.get(id, marketId));
    }

    /**
     * 根据id及市场，查询客户的信息
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * 如不关心客户的理货区、车辆、图片等附加数据，建议用此接口
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value = "/getSimple")
    public BaseOutput<CustomerSimpleExtendDto> getSimple(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId){
        if (Objects.isNull(id) || Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        CustomerQueryInput condition = new CustomerQueryInput();
        condition.setId(id);
        condition.setMarketId(marketId);
        PageOutput<List<Customer>> pageOutput = customerQueryService.listSimpleForPage(condition);
        Customer customer = pageOutput.getData().stream().findFirst().orElse(null);
        return BaseOutput.success().setData(customer);
    }

    /**
     * 根据id查询客户基本信息，不带有任何市场属性数据
     * @param id 客户ID
     * @return
     */
    @PostMapping(value = "/getById")
    public BaseOutput<Customer> getById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        return BaseOutput.success().setData(customerManageService.get(id));
    }

    /**
     * 根据证件号及市场，查询客户的信息
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/getByCertificateNumber")
    public BaseOutput<Customer> getByCertificateNumber(@RequestParam("certificateNumber") String certificateNumber, @RequestParam("marketId") Long marketId){
        if (StrUtil.isBlank(certificateNumber) || Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        CustomerQueryInput condition = new CustomerQueryInput();
        condition.setCertificateNumber(certificateNumber);
        condition.setMarketId(marketId);
        PageOutput<List<Customer>> pageOutput = customerQueryService.listForPage(condition);
        Customer customer = pageOutput.getData().stream().findFirst().orElse(null);
        return BaseOutput.success().setData(customer);
    }

    /**
     * 查询客户数据集
     * @param customer
     * @return
     */
    @PostMapping(value="/list")
    public BaseOutput<List<Customer>> list(@RequestBody CustomerQueryInput customer) {
        if (Objects.isNull(customer.getMarketId())) {
            return BaseOutput.failure("客户所属市场不能为空");
        }
        PageOutput<List<Customer>> pageOutput = customerQueryService.listForPage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 查询客户数据集
     * @param customer
     * @return
     */
    @PostMapping(value="/listSimple")
    public BaseOutput<List<Customer>> listSimple(@RequestBody CustomerQueryInput customer) {
        if (Objects.isNull(customer.getMarketId())) {
            return BaseOutput.failure("客户所属市场不能为空");
        }
        PageOutput<List<Customer>> pageOutput = customerQueryService.listSimpleForPage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 客户信息更新
     * @param updateInput 更新数据
     * @return BaseOutput
     */
    @UapToken
    @PostMapping(value="/update")
    public BaseOutput<Customer> update(@Validated({UpdateView.class, Default.class}) @RequestBody CustomerUpdateInput updateInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            BaseOutput update = customerManageService.update(updateInput);
            if (update.isSuccess()) {
                mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, updateInput.getId(), updateInput.getCustomerMarket().getMarketId());
            }
            return update;
        } catch (AppException appException) {
            return BaseOutput.failure(appException.getMessage());
        } catch (Exception e) {
            log.error(String.format("客户数据:%s 修改异常:%s", JSONUtil.toJsonStr(updateInput), e.getMessage()), e);
            return BaseOutput.failure("系统异常，请稍后再试");
        }
    }

    /**
     * 企业客户注册
     * @param customer
     * @return BaseOutput
     */
    @UapToken
    @PostMapping(value="/registerEnterprise")
    public BaseOutput<Customer> registerEnterprise(@Validated({AddView.class, EnterpriseView.class}) @RequestBody EnterpriseCustomerInput customer, BindingResult bindingResult) {
        log.info(String.format("企业客户注册:%s", JSONUtil.toJsonStr(customer)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            BaseOutput<Customer> baseOutput = customerManageService.register(customer);
            if (baseOutput.isSuccess()) {
                mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_ADD_MQ_FANOUT_EXCHANGE, baseOutput.getData().getId(), customer.getCustomerMarket().getMarketId());
            }
            return baseOutput;
        } catch (AppException e) {
            log.error(String.format("企业客户注册:%s 异常:%s", JSONUtil.toJsonStr(customer), e.getMessage()), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            log.error(String.format("企业客户注册:%s 异常:%s", JSONUtil.toJsonStr(customer), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 个体户客户注册
     * @param customer
     * @return BaseOutput
     */
    @UapToken
    @PostMapping(value = "/registerIndividual")
    public BaseOutput<Customer> registerIndividual(@Validated({AddView.class}) @RequestBody IndividualCustomerInput customer, BindingResult bindingResult) {
        log.info(String.format("个人客户注册:%s", JSONUtil.toJsonStr(customer)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        BeanUtils.copyProperties(customer, input);
        try {
            BaseOutput<Customer> baseOutput = customerManageService.register(input);
            if (baseOutput.isSuccess()) {
                mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_ADD_MQ_FANOUT_EXCHANGE, baseOutput.getData().getId(), input.getCustomerMarket().getMarketId());
            }
            return baseOutput;
        } catch (AppException e) {
            log.error(String.format("个人客户注册:%s 异常:%s", JSONUtil.toJsonStr(customer), e.getMessage()), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            log.error(String.format("个人客户注册:%s 异常:%s", JSONUtil.toJsonStr(customer), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }


    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @UapToken
    @PostMapping(value="/checkExistByNoAndMarket")
    public BaseOutput<Customer> checkExistByNoAndMarket(@RequestParam(value = "certificateNumber") String certificateNumber,@RequestParam(value = "marketId") Long marketId) {
        return customerQueryService.checkExistByNoAndMarket(certificateNumber,marketId);
    }

    /**
     * 验证客户手机验证码
     * 如果验证通过，则更改是已认证
     * @param customerId 客户ID
     * @param cellphone  手机号
     * @param verificationCode 验证码
     * @return
     */
    @UapToken
    @PostMapping(value = "/verificationCellPhone")
    public BaseOutput<Boolean> verificationCellPhone(@RequestParam("customerId") Long customerId, @RequestParam("cellphone") String cellphone, @RequestParam("verificationCode") String verificationCode) {
        log.info(String.format("客户【%s】手机号【%s】认证 验证码 【%s】", customerId, cellphone, verificationCode));
        try {
            Optional<String> s = customerManageService.verificationCellPhone(cellphone, customerId, verificationCode);
            if (s.isPresent()) {
                return BaseOutput.failure(s.get()).setData(false);
            }
            return BaseOutput.successData(true);
        } catch (Exception e) {
            log.error(String.format("客户【%s】手机号【%s】验证码【%s】认证异常:%s", customerId, cellphone, verificationCode, e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }


    /**
     * 获取被某手机号验证的客户
     * @param cellphone 手机号
     * @return
     */
    @PostMapping(value = "/getValidatedCellphoneCustomer")
    public BaseOutput<Customer> getValidatedCellphoneCustomer(@RequestParam("cellphone") String cellphone) {
        log.info(String.format("获取被手机号【%s】验证的客户", cellphone));
        return customerQueryService.getSingleValidatedCellphoneCustomer(cellphone);
    }

    /**
     * 客户自行注册
     * @param dto
     * @return
     */
    @PostMapping(value = "/autoRegister")
    public BaseOutput<Customer> autoRegister(@Validated @RequestBody CustomerAutoRegisterDto dto, BindingResult bindingResult) {
        log.info(String.format("客户自行注册:%s", JSONUtil.toJsonStr(dto)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerManageService.autoRegister(dto);
    }

    /**
     * 完善企业客户信息
     * 由于可能会有账号合并的情况，所以会自动重新登录并返回登录信息
     * @param input 待完善的信息
     * @return
     */
    @PostMapping(value = "/completeEnterprise")
    public BaseOutput<LoginSuccessData> completeEnterprise(@Validated({CompleteView.class, Default.class, EnterpriseCompleteView.class}) @RequestBody CustomerUpdateInput input, BindingResult bindingResult) {
        log.info(String.format("企业客户信息完善:%s", JSONUtil.toJsonStr(input)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        input.setOrganizationType(CustomerEnum.OrganizationType.ENTERPRISE.getCode());
        return completeInfo(input);
    }

    /**
     * 批量完善企业客户信息
     * 由于可能会有账号合并的情况，所以会自动重新登录并返回登录信息
     * @param inputList 待完善的信息
     * @return
     */
    @PostMapping(value = "/batchCompleteEnterprise")
    public BaseOutput<LoginSuccessData> batchCompleteEnterprise(@RequestBody List<CustomerUpdateInput> inputList) {
        log.info(String.format("企业客户信息批量完善:%s", JSONUtil.toJsonStr(inputList)));
        if (CollectionUtil.isEmpty(inputList)) {
            return BaseOutput.failure("参数丢失");
        }
        try {
            BaseOutput<Long> longBaseOutput = customerManageService.batchCompleteEnterprise(inputList);
            Set<Long> marketIds = (Set<Long>) (longBaseOutput.getMetadata());
            mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE,longBaseOutput.getData(),marketIds);
            return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccountService.getByCellphone(inputList.get(0).getContactsPhone()).get(), null));
        } catch (AppException appException) {
            return BaseOutput.failure(appException.getMessage());
        } catch (Exception e) {
            log.error(String.format("企业客户信息批量完善: %s 发生异常:%s", JSONUtil.toJsonStr(inputList), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 完善个人客户信息
     * 由于可能会有账号合并的情况，所以会自动重新登录并返回登录信息
     * @param input 待完善的信息
     * @return
     */
    @PostMapping(value = "/completeIndividual")
    public BaseOutput<LoginSuccessData> completeIndividual(@Validated({CompleteView.class, Default.class}) @RequestBody CustomerUpdateInput input, BindingResult bindingResult) {
        log.info(String.format("个人客户信息完善:%s", JSONUtil.toJsonStr(input)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (!IdcardUtil.isValidCard(input.getCertificateNumber())) {
            return BaseOutput.failure("个人证件号码错误");
        }
        input.setOrganizationType(CustomerEnum.OrganizationType.INDIVIDUAL.getCode());
        return completeInfo(input);
    }

    /**
     * 批量完善个人客户信息
     * 由于可能会有账号合并的情况，所以会自动重新登录并返回登录信息
     * @param inputList 待完善的信息
     * @return
     */
    @PostMapping(value = "/batchCompleteIndividual")
    public BaseOutput<LoginSuccessData> batchCompleteIndividual(@RequestBody List<CustomerUpdateInput> inputList) {
        log.info(String.format("个人客户信息批量完善:%s", JSONUtil.toJsonStr(inputList)));
        if (CollectionUtil.isEmpty(inputList)) {
            return BaseOutput.failure("参数丢失");
        }
        try {
            BaseOutput<Long> longBaseOutput = customerManageService.batchCompleteIndividual(inputList);
            Set<Long> marketIds = (Set<Long>) (longBaseOutput.getMetadata());
            mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE,longBaseOutput.getData(),marketIds);
            return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccountService.getByCellphone(inputList.get(0).getContactsPhone()).get(), null));
        } catch (AppException appException) {
            return BaseOutput.failure(appException.getMessage());
        } catch (Exception e) {
            log.error(String.format("个人客户信息批量完善: %s 发生异常:%s", JSONUtil.toJsonStr(inputList), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 更改客户基本信息
     * @param input 待完善的信息
     * @return
     */
    @UapToken
    @PostMapping(value = "/updateBaseInfo")
    public BaseOutput updateBaseInfo(@Validated({UpdateView.class, Default.class}) @RequestBody CustomerBaseUpdateInput input, BindingResult bindingResult) {
        log.info(String.format("客户基本信息修改:%s", JSONUtil.toJsonStr(input)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            BaseOutput baseOutput = customerManageService.updateBaseInfo(input, true);
            if (baseOutput.isSuccess()) {
                mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, input.getId(), input.getCustomerMarket().getMarketId());
            }
            return baseOutput;
        } catch (Exception e) {
            log.error(String.format("客户基本信息: %s 修改发生异常:%s", JSONUtil.toJsonStr(input), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 更改客户证件信息
     * @param input 证件信息
     * @return
     */
    @UapToken
    @PostMapping(value = "/updateCertificateInfo")
    public BaseOutput updateCertificateInfo(@RequestBody CustomerCertificateInput input) {
        log.info(String.format("客户证件信息修改:%s", JSONUtil.toJsonStr(input)));
        try {
            Optional<String> s = customerManageService.updateCertificateInfo(input,true);
            if (s.isPresent()) {
                return BaseOutput.failure(s.get());
            }
            return BaseOutput.success();
        } catch (Exception e) {
            log.error(String.format("客户证件信息: %s 修改发生异常:%s", JSONUtil.toJsonStr(input), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 完善客户信息
     * @param input
     * @return
     */
    private BaseOutput<LoginSuccessData> completeInfo(CustomerUpdateInput input) {
        try {
            BaseOutput<Customer> customerBaseOutput = customerManageService.completeInfo(input);
            if (customerBaseOutput.isSuccess()) {
                mqService.asyncSendCustomerToMq(MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, customerBaseOutput.getData().getId(), input.getCustomerMarket().getMarketId());
                return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccountService.getByCellphone(input.getContactsPhone()).get(), null));
            }
            return BaseOutput.failure(customerBaseOutput.getMessage());
        } catch (Exception e) {
            log.error(String.format("完善信息数据：%s 异常:%s", JSONUtil.toJsonStr(input), e.getMessage()), e);
            return BaseOutput.failure("系统异常").setData(false);
        }
    }

}