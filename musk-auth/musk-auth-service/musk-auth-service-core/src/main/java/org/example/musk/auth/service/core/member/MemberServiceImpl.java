package org.example.musk.auth.service.core.member;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.constants.system.system.SystemConstant;
import org.example.musk.auth.dao.member.MemberMapper;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.entity.member.vo.MemberSaveReqVO;
import org.example.musk.auth.enums.MemberStatusEnums;
import org.example.musk.auth.enums.member.RegisterChannelEnums;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.example.musk.common.exception.BusinessPageExceptionEnum;

import java.util.Collections;
import java.util.List;



/**
 * 会员 Service 实现类
 *
 * @author musk
 */
@Service
@Validated
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, MemberDO> implements MemberService {

    @Resource
    private MemberMapper memberMapper;


    @Override
    public void delete(Integer id) {
        // 校验存在
        validateExists(id);
        // 删除
        memberMapper.deleteById(id);
    }

    private void validateExists(Integer id) {
        /**
         if (Mapper.selectById(id) == null) {
         throw exception(_NOT_EXISTS);
         }
         */
    }

    @Override
    public MemberDO get(Integer id) {
        return memberMapper.selectById(id);
    }


    @Override
    public MemberDO login(String userName, String password) {
        MemberDO memberDO = null;
        try {
            memberDO = getMemberInfoByMemberCode(userName);
        } catch (Exception e) {
            log.warn("[查询会员信息失败]",e);
            return null;
        }
        if (null == memberDO) {
            return null;
        }
        if (!StrUtil.equals(AESUtils.encryptHex(password, AESKeyEnum.PASSWORD_KEY), memberDO.getPassword())) {
            return null;
        }
        return memberDO;
    }

    @Override
    public MemberDO getMemberInfoByMemberId(Integer memberId) {
        MemberDO memberDO = memberMapper.selectById(memberId);
        if (null == memberDO) {
            throw  new BusinessException(BusinessPageExceptionEnum.USER_IS_NOT_EXISTS);
        }
        if (MemberStatusEnums.DISABLE.getStatus().equals(memberDO.getStatus())) {
            throw  new BusinessException(BusinessPageExceptionEnum.USER_IS_FORBIDDEN_ERROR);
        }
        return memberDO;
    }


    @Override
    public List<MemberDO> getMemberInfoByMemberId(List<Integer> memberId) {
        return memberMapper.selectBatchIds(memberId);
    }

    @Override
    public MemberDO queryMemberByMemberId(Integer memberId) {
        return  memberMapper.selectById(memberId);
    }

    @Override
    public MemberDO getMemberInfoByMemberCode(String memberCode) {
        MemberDO memberDO = memberMapper.selectOne(new LambdaQueryWrapper<MemberDO>()
                .eq(MemberDO::getMemberCode,memberCode));
        if (null == memberDO) {
            throw  new BusinessException(BusinessPageExceptionEnum.USER_IS_NOT_EXISTS);
        }
        if (MemberStatusEnums.DISABLE.getStatus().equals(memberDO.getStatus())) {
            throw  new BusinessException(BusinessPageExceptionEnum.USER_IS_FORBIDDEN_ERROR);
        }
        return memberDO;
    }

    @Override
    public MemberDO getMemberInfoByInvestSimpleIdExcludeSystemId(Integer inviteMemberSimpleId) {
        if (inviteMemberSimpleId <= SystemConstant.SYSTEM_MEMBER_ID) {
            return null;
        }
        return memberMapper.selectOne(new LambdaQueryWrapper<MemberDO>()
                .eq(MemberDO::getMemberSimpleId, inviteMemberSimpleId));
    }

    @Override
    public List<MemberDO> getMemberInfoByMemberIds(List<Integer> memberIds) {
        List<MemberDO> memberDOList = memberMapper.selectList(new LambdaQueryWrapper<MemberDO>()
                .in(MemberDO::getId,memberIds));
        if (CollUtil.isEmpty(memberDOList)) {
            return Collections.emptyList();
        }
        return memberDOList.stream().filter(k->!MemberStatusEnums.DISABLE.getStatus().equals(k.getStatus())).toList();
    }

    @Override
    public MemberDO register(MemberSaveReqVO createReqVO) {
        //检查传过来的注册渠道，是否在枚举类中
        if (null == RegisterChannelEnums.fromValue(createReqVO.getRegisterChannel())) {
            throw new BusinessException(BusinessPageExceptionEnum.REGISTER_ChANNEL_NOT_EXISTS);
        }
        if (memberMapper.exists(new LambdaQueryWrapper<MemberDO>()
                .eq(MemberDO::getMemberCode, createReqVO.getMemberCode())
        )) {
            throw  new BusinessException(BusinessPageExceptionEnum.REGISTER_USERNAME_EXISTS);
        }
        MemberDO memberInfoByInvestSimpleIdInfo = this.getMemberInfoByInvestSimpleIdExcludeSystemId(createReqVO.getInviteMemberSimpleId());
        if (createReqVO.getInviteMemberSimpleId() != SystemConstant.SYSTEM_MEMBER_ID && null == memberInfoByInvestSimpleIdInfo) {
            throw  new BusinessException(BusinessPageExceptionEnum.REGISTER_INVITE_MEMBER_SIMPLE_ID_NOT_EXISTS);
        }
        createReqVO.setPassword(AESUtils.encryptHex(createReqVO.getPassword(), AESKeyEnum.PASSWORD_KEY));
        MemberDO entity = BeanUtil.toBean(createReqVO, MemberDO.class);
        int result = memberMapper.insert(entity);
        if (result <= 0) {
            return null;
        }
        return entity;
    }


}
