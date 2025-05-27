package org.example.musk.auth.dao.security;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.auth.entity.security.MemberSecurityLogDO;

/**
 * 会员安全操作日志 Mapper
 *
 * @author musk
 */
@Mapper
public interface MemberSecurityLogMapper extends BaseMapper<MemberSecurityLogDO> {

}
