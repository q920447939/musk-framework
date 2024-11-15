package org.example.musk.auth.service.core.member;

import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;

import java.util.List;


/**
 * 会员 Service 接口
 *
 * @author musk
 * @date 2024/08/13
 */
public interface MemberService{

    /**
     * 删除会员
     *
     * @param id 编号
     */
    void delete(Integer id);

    /**
     * 获得会员
     *
     * @param id 编号
     * @return 会员
     */
    MemberDO get(Integer id);


    /**
     * 登录
     *
     * @param userName        用户名
     * @param password        密码
     * @return {@link MemberDO }
     */
    MemberDO login( String userName, String password);


    /**
     * 获得会员信息通过会员ID
     *
     * @param memberId 会员ID
     * @return {@link MemberDO }
     */
    MemberDO getMemberInfoByMemberId(Integer memberId);

    List<MemberDO> getMemberInfoByMemberId(List<Integer> memberId);

    /**
     * 查询会员通过会员ID
     *
     * @param memberId 会员ID
     * @return {@link MemberDO }
     */
    MemberDO queryMemberByMemberId(Integer memberId);


    /**
     * 获得会员信息通过会员代码
     *
     * @param memberCode      会员代码
     * @return {@link MemberDO }
     */
    MemberDO getMemberInfoByMemberCode(String memberCode);

    /**
     * 获得会员信息通过投资简单ID排除系统ID
     *
     * @param inviteMemberSimpleId 邀请会员简单ID
     * @return {@link MemberDO }
     */
    MemberDO getMemberInfoByInvestSimpleIdExcludeSystemId( Integer inviteMemberSimpleId);


    /**
     * 获得会员信息通过会员ID集合
     *
     * @param memberIds       会员ID集合
     * @return {@link List }<{@link MemberDO }>
     */
    List<MemberDO> getMemberInfoByMemberIds(List<Integer> memberIds);


    /**
     * 寄存器
     *
     * @param createReqVO 创建请求VO
     * @return {@link MemberDO }
     */
    MemberDO register(MemberSaveReqVO createReqVO);

}
