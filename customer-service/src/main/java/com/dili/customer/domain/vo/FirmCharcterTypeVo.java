package com.dili.customer.domain.vo;

import com.dili.customer.domain.CharacterType;
import com.dili.uap.sdk.domain.Firm;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FirmCharcterTypeVo {

    private Firm firm;

    private Map<String, List<CharacterType>> characterTypeSubTypes;
}
