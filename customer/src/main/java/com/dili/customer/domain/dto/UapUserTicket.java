package com.dili.customer.domain.dto;

import com.dili.uap.sdk.domain.UserTicket;
import lombok.Data;

/**
 * UAP用户凭证
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/20 16:30
 */
@Data
public class UapUserTicket {

    private UserTicket userTicket;
}
