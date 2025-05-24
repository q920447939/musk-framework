package org.example.musk.auth.service.core.log.login;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.auth.entity.loginlog.MemberLoginLogDO;


/**
 * 会员登录记录 Service 接口
 *
 * @author
 */
public interface MemberLoginLogService extends IService<MemberLoginLogDO>{

    int saveMember(MemberLoginLogDO info);
}
