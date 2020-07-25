package com.dili.customer.service.impl;

import com.dili.customer.domain.Attachment;
import com.dili.customer.mapper.AttachmentMapper;
import com.dili.customer.service.AttachmentService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

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
}