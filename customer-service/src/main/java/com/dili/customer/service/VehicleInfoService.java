package com.dili.customer.service;

import com.dili.customer.domain.VehicleInfo;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/29 11:05
 */
public interface VehicleInfoService extends BaseService<VehicleInfo, Long> {

    /**
     * 删除客户在某市场的对应的车辆信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    Integer deleteByCustomerAndMarket(Long customerId,Long marketId);

    /**
     * 批量更新或者删除车辆
     * @param vehicleInfoList 车辆信息
     * @return 记录数
     */
    Integer batchSaveOrUpdate(List<VehicleInfo> vehicleInfoList);

    /**
     * 根据客户及市场获取车辆信息
     * @param customerIdSet 客户ID集合
     * @param marketId 市场ID
     * @return
     */
    List<VehicleInfo> listByCustomerAndMarket(Set<Long> customerIdSet, Long marketId);

    /**
     * 客户车辆绑定
     * @param vehicleInfo 绑定信息
     * @return
     */
    Optional<String> bindingVehicle(VehicleInfo vehicleInfo);

    /**
     * 根据客户及市场获取车辆信息
     * 此方法会冗余返回车型文本信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    List<VehicleInfo> listByCustomerAndMarket(Long customerId, Long marketId);

    /**
     * 根据ID删除车辆信息并记录删除日志
     * @param id 数据ID
     * @return
     */
    Optional<String> deleteWithLogger(Long id);
}
