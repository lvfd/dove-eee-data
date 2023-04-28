package com.dovepay.doveEditor.mapper;

import com.dovepay.doveEditor.entity.DeeeAtcl;
import com.dovepay.doveEditor.entity.DeeeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeeeMapper {
    public DeeeUser getDeeeUserByName(@Param("name") String name);
    public DeeeAtcl getDeeeAtclById(@Param("id") String id);
    public List<DeeeAtcl> getDeeeAtclList();
    public void deleteDeeeAtclById(@Param("id") String id);
    public void updateDeeeAtcl(DeeeAtcl deeeAtcl);
    public void insertDeeeAtcl(DeeeAtcl deeeAtcl);
    public int getDeeeAtclRowCount();
}
