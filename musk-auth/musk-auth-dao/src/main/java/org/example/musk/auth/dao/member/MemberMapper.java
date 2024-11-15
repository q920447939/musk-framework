package org.example.musk.auth.dao.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.auth.entity.member.MemberDO;

/**
 * 会员 Mapper
 *
 * @author musk
 */
@Mapper
public interface MemberMapper extends BaseMapper<MemberDO> {

}
