package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.Attachment;
import com.dili.customer.domain.dto.AttachmentDto;
import com.dili.customer.mapper.AttachmentMapper;
import com.dili.customer.service.AttachmentService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 客户附件信息
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-25 17:14:18.
 */
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment, Long> implements AttachmentService {

    public AttachmentMapper getActualMapper() {
        return (AttachmentMapper)getDao();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchSave(List<Attachment> attachmentList) {
        if (CollectionUtil.isEmpty(attachmentList)) {
            return 0;
        }
        Set<Long> idSet = attachmentList.stream().map(t -> t.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
        AttachmentDto dto = new AttachmentDto();
        dto.setIdNotSet(idSet);
        dto.setCustomerId(attachmentList.get(0).getCustomerId());
        dto.setMarketId(attachmentList.get(0).getMarketId());
        //先删除数据库中已存在，但是不在本次传入的数据中的图片信息
        this.deleteByExample(dto);
        attachmentList.forEach(t -> {
            if (Objects.isNull(t.getId())) {
                this.insert(t);
            } else {
                this.update(t);
            }
        });
        return attachmentList.size();
    }
}