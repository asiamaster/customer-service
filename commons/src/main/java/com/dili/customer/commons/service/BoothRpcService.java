package com.dili.customer.commons.service;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.dto.AssetsQuery;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/22 9:46
 */
@RequiredArgsConstructor
@Service
public class BoothRpcService {

    private final AssetsRpc assetsRpc;

    /**
     * 根据条件查询摊位信息
     * @param assetsQuery 查询条件
     * @return 摊位数据结果
     */
    public List<AssetsDTO> listByExample(AssetsQuery assetsQuery) {
        if (Objects.isNull(assetsQuery)) {
            assetsQuery = new AssetsQuery();
        }
        assetsQuery.setBusinessType(1);
        BaseOutput<List<AssetsDTO>> output = assetsRpc.searchAssets(assetsQuery);
        return output.isSuccess() ? output.getData() : null;
    }
}
