package org.example.musk.auth.service.core.log.login;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.loginlog.MemberLoginLogMapper;
import org.example.musk.auth.entity.loginlog.MemberLoginLogDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * 会员登录记录 Service 实现类
 *
 * @author 马斯克源码
 */
@Service
@Validated
@Slf4j
public class MemberLoginLogServiceImpl extends ServiceImpl<MemberLoginLogMapper, MemberLoginLogDO> implements MemberLoginLogService {

    @Resource
    private MemberLoginLogMapper loginLogMapper;


}
