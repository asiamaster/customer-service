package com.dili.customer.rpc;

import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 16:50
 */
@FeignClient(name = "dili-uid",contextId = "uidRpc")
public interface UidRpc {

    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    @RequestMapping(value = "/api/bizNumber", method = RequestMethod.POST)
    BaseOutput<String> bizNumber(@RequestParam(value = "type") String type);
}
