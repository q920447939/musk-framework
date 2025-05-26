package org.example.musk.auth.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.auth.entity.channel.MemberAuthChannelDO;

/**
 * 会员认证渠道 Mapper
 *
 * 使用 MyBatis-Plus ORM 风格，通过 LambdaQueryWrapper/LambdaUpdateWrapper
 * 在 Service 层动态生成 SQL，无需自定义方法
 *
 * @author musk
 */
@Mapper
public interface MemberAuthChannelMapper extends BaseMapper<MemberAuthChannelDO> {


}
