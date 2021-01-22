package com.dili.customer.api;

import com.dili.customer.domain.Address;
import com.dili.customer.service.AddressService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 客户地址信息
 * This file was generated on 2020-01-02 16:19:35.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    /**
     * 根据客户ID查询该客户的联系地址信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return 客户地址信息数据
     */
    @PostMapping(value = "/listAllAddress")
    public BaseOutput<List<Address>> listAllAddress(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        Address condition = new Address();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(addressService.list(condition));
    }

    /**
     * 删除客户地址信息
     * @param id
     * @return BaseOutput
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public BaseOutput delete(@RequestParam("id") Long id) {
        addressService.deleteWithLogger(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 保存客户地址信息
     * @param address 地址信息
     * @return BaseOutput
     */
    @PostMapping(value = "/saveAddress")
    public BaseOutput saveAddress(@Validated @RequestBody Address address, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Optional<String> s = addressService.saveAddress(address);
        if (s.isPresent()) {
            return BaseOutput.failure(s.get());
        }
        return BaseOutput.success();
    }
}