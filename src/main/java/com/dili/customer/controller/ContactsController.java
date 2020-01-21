package com.dili.customer.controller;

import com.dili.customer.domain.Contacts;
import com.dili.customer.service.ContactsService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:18:39.
 * @author yuehongbo
 */
@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    /**
     * 删除CustomerContacts
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput delete(Long id) {
        contactsService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 保存客户联系人信息
     * @param customerContacts 联系人信息
     * @return BaseOutput
     */
    @RequestMapping(value = "/saveContacts.action", method = {RequestMethod.POST})
    public BaseOutput saveContacts(@Validated Contacts customerContacts) {
        return contactsService.saveContacts(customerContacts);
    }

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @return
     */
    @RequestMapping(value = "/listAllContacts.action", method = {RequestMethod.POST})
    public BaseOutput<List<Contacts>> listAllContacts(@RequestParam("customerId") Long customerId) {
        Contacts condition = new Contacts();
        condition.setCustomerId(customerId);
        return BaseOutput.success().setData(contactsService.list(condition));
    }
}