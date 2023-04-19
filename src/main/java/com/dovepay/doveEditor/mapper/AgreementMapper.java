package com.dovepay.doveEditor.mapper;

import com.dovepay.doveEditor.entity.TnetsignAgreement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AgreementMapper {
    public TnetsignAgreement getAgreementNameById(@Param("id") String id);
}
