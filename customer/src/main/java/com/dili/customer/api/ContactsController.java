package com.dili.customer.api;

import com.dili.customer.annotation.UapToken;
import com.dili.customer.domain.Contacts;
import com.dili.customer.service.ContactsService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户联系人信息
 * This file was generated on 2020-01-02 16:18:39.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contacts")
public class ContactsController {
    
    private final ContactsService contactsService;

    /**
     * 删除客户联系人
     * @param id 数据ID
     * @return BaseOutput
     */
    @UapToken
    @PostMapping(value = "/delete")
    public BaseOutput delete(@RequestParam("id") Long id) {
        contactsService.deleteWithLogger(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 保存客户联系人信息
     * @param customerContacts 联系人信息
     * @return BaseOutput
     */
    @UapToken
    @PostMapping(value = "/saveContacts")
    public BaseOutput saveContacts(@Validated @RequestBody Contacts customerContacts, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return contactsService.saveContacts(customerContacts);
    }

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @return
     */
    @PostMapping(value = "/listAllContacts")
    public BaseOutput<List<Contacts>> listAllContacts(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        Contacts condition = new Contacts();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(contactsService.list(condition));
    }
}