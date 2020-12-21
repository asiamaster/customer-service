package com.dili.customer.api;

import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户服务公共数据接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/21 16:18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/commonData")
@Slf4j
public class CommonDataController {

    private final CommonDataService commonDataService;

    /**
     * 查询经营性质(用户类型)
     * @return
     */
    @PostMapping(value = "/listBusinessNature")
    public BaseOutput<List<DataDictionaryValue>> listBusinessNature() {
        return BaseOutput.successData(commonDataService.queryBusinessNature(null));
    }

    /**
     * 获取市场角色分类信息
     * @return
     */
    @PostMapping(value = "/listCharacterTypeGroup")
    public BaseOutput<List<CharacterTypeGroupDto>> listCharacterTypeGroup(@RequestParam("marketId") Long marketId) {
        return BaseOutput.successData(commonDataService.produceCharacterTypeGroup(null, marketId));
    }
}
