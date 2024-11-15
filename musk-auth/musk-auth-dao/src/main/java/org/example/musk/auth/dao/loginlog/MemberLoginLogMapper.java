package org.example.musk.auth.dao.loginlog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.auth.entity.loginlog.MemberLoginLogDO;

/**
 * 会员登录记录 Mapper
 *
 * @author 马斯克源码
 */
@Mapper
public interface MemberLoginLogMapper extends BaseMapper<MemberLoginLogDO> {


}
