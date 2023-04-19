package com.dovepay.doveEditor.mapper;

import com.dovepay.doveEditor.entity.DeeeAtcl;
import com.dovepay.doveEditor.entity.DeeeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeeeMapper {
    public DeeeUser getDeeeUserByName(@Param("name") String name);
    public DeeeAtcl getDeeeAtclById(@Param("id") String id);
    public void deleteDeeeAtclById(@Param("id") String id);
    public void updateDeeeAtcl(DeeeAtcl deeeAtcl);
    public void insertDeeeAtcl(DeeeAtcl deeeAtcl);
    public int getDeeeAtclRowCount();
}
