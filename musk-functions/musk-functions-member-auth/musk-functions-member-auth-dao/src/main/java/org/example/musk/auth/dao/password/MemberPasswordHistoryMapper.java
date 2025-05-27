package org.example.musk.auth.dao.password;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.auth.entity.password.MemberPasswordHistoryDO;

/**
 * 会员密码历史记录 Mapper
 *
 * @author musk
 */
@Mapper
public interface MemberPasswordHistoryMapper extends BaseMapper<MemberPasswordHistoryDO> {

}
