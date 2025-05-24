package org.example.musk.auth.service.core.log.login;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.loginlog.MemberLoginLogMapper;
import org.example.musk.auth.entity.loginlog.MemberLoginLogDO;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * 会员登录记录 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
@PluginDynamicSource(group = "auth", ds = "log")
public class MemberLoginLogServiceImpl extends ServiceImpl<MemberLoginLogMapper, MemberLoginLogDO> implements MemberLoginLogService {

    @Override
    public int saveMember(MemberLoginLogDO info) {
        return this.baseMapper.insert(info);
    }
}
