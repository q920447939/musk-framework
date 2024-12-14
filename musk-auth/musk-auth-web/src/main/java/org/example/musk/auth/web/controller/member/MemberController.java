package org.example.musk.auth.web.controller.member;

import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.web.controller.member.vo.MemberAddReqVO;
import org.example.musk.auth.web.controller.member.vo.MemberPageReqVO;
import org.example.musk.auth.web.controller.member.vo.MemberRespVO;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 会员 Controller
 *
 * @author 代码生成器
 */
@RestController
@RequestMapping("/api/member")
@Validated
@Slf4j
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/get")
    public CommonResult<MemberRespVO> get() {
        MemberDO memberDO = memberService.getMemberInfoByMemberId(ThreadLocalTenantContext.getMemberId());
        return CommonResultUtils.wrapEmptyObjResult(memberDO, () ->{
            return BeanUtils.toBean(memberDO, MemberRespVO.class);
        });
    }
}
