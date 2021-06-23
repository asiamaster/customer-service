package com.dili.customer.domain.dto;

import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Firm;
import lombok.Data;

import java.util.List;

@Data
public class CustomerMarketCharacterTypeDto {

    private Firm firm;

    private List<DataDictionaryValue> characterTypes;
}
