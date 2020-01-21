package com.dili.customer.controller;

import com.dili.customer.service.FirmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@RestController
@RequestMapping("/firmInfo")
public class FirmInfoController {

    @Autowired
    private FirmInfoService firmInfoService;

}