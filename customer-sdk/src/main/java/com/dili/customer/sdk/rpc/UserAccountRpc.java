package com.dili.customer.sdk.rpc;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 17:44
 */
@FeignClient(name = "customer-service", contextId = "userAccountRpc", url = "${customerManageService.url:}")
public interface UserAccountRpc {
}
