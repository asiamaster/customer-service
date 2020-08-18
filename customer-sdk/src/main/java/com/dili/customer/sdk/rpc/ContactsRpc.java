package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.Contacts;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:58
 */
@FeignClient(name = "customer-service", contextId = "contactsRpc", url = "${customerService.url:}")
public interface ContactsRpc {

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "/api/contacts/listAllContacts", method = RequestMethod.POST)
    BaseOutput<List<Contacts>> listAllContacts(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 保存客户联系人信息
     * @param customerContacts 客户联系人
     * @return
     */
    @RequestMapping(value = "/api/contacts/saveContacts", method = RequestMethod.POST)
    BaseOutput saveContacts(Contacts customerContacts);

}
