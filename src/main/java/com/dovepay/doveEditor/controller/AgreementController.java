package com.dovepay.doveEditor.controller;

import com.dovepay.doveEditor.mapper.AgreementMapper;
import com.dovepay.doveEditor.entity.TnetsignAgreement;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/doveFetch")
public class AgreementController {

    @Resource
    private AgreementMapper agreementMapper;

    @GetMapping("/{id}")
    public TnetsignAgreement getAgreement(@PathVariable("id") String id) {
        return agreementMapper.getAgreementNameById(id);
    }

    @PostMapping("/getData")
    public TnetsignAgreement getAgreement(@RequestBody Map<String, Object> map) {
        return agreementMapper.getAgreementNameById((String) map.get("id"));
    }

    @RequestMapping("/index")
    public String index() {
        return "dove-fetch";
    }

    @PostMapping("/getPO")
    public Map save(@RequestBody Map<String, Object> map) {
        return map;
    }
}
