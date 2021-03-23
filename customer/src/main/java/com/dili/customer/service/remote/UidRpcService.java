package com.dili.customer.service.remote;

import cn.hutool.json.JSONUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.uid.sdk.rpc.feign.UidFeignRpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 16:52
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UidRpcService {

    private final UidFeignRpc uidFeignRpc;

    /**
     * 获取业务编号
     * @param type
     * @return
     */
    public String getBizNumber(String type) {
        try {
            BaseOutput<String> stringBaseOutput = uidFeignRpc.getBizNumber(type);
            if (stringBaseOutput.isSuccess()) {
                return stringBaseOutput.getData();
            } else {
                log.warn(String.format("根据类型【%s】获取编号失败:%s", type, JSONUtil.toJsonStr(stringBaseOutput)));
            }
        } catch (Exception e) {
            log.error(String.format("根据类型【%s】获取编号异常:%s", type, e.getMessage()), e);
        }
        return null;
    }
}
