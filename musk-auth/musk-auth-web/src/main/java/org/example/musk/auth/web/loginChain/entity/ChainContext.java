package org.example.musk.auth.web.loginChain.entity;

import lombok.Data;
import org.example.musk.auth.entity.member.MemberDO;

/**
 * 链的上下文
 * ClassName: ChainContext
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
@Data
public class ChainContext {
    /**
     * ip
     */
    private String ip;
    /**
     * 用户信息实体
     */
    private MemberDO memberDO;
}
