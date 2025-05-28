package org.example.musk.functions.member.level.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.member.level.dao.MemberPointsRuleMapper;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.enums.PointsRuleTypeEnum;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.model.entity.MemberPointsRuleDO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleUpdateReqVO;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.example.musk.functions.member.level.service.MemberPointsRuleService;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分规则服务实现类
 *
 * @author musk-functions-member-level
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class MemberPointsRuleServiceImpl implements MemberPointsRuleService {

    @Resource
    private MemberPointsRuleMapper memberPointsRuleMapper;

    @Resource
    private MemberPointsService memberPointsService;

    @Resource
    private MemberGrowthValueService memberGrowthValueService;


    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer createPointsRule(MemberPointsRuleCreateReqVO createReqVO) {
        // 检查规则编码是否已存在
        MemberPointsRuleDO existRule = memberPointsRuleMapper.selectByRuleCode( createReqVO.getRuleCode());
        if (existRule != null) {
            throw new BusinessException("规则编码已存在");
        }

        // 创建规则
        MemberPointsRuleDO rule = BeanUtil.copyProperties(createReqVO, MemberPointsRuleDO.class);
        memberPointsRuleMapper.insert(rule);

        return rule.getId();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void updatePointsRule(MemberPointsRuleUpdateReqVO updateReqVO) {
        // 检查规则是否存在
        MemberPointsRuleDO rule = memberPointsRuleMapper.selectById(updateReqVO.getId());
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }

        // 检查规则编码是否已存在（排除自身）
        MemberPointsRuleDO existRule = memberPointsRuleMapper.selectByRuleCode( updateReqVO.getRuleCode());
        if (existRule != null && !existRule.getId().equals(updateReqVO.getId())) {
            throw new BusinessException("规则编码已存在");
        }

        // 更新规则
        MemberPointsRuleDO updateRule = BeanUtil.copyProperties(updateReqVO, MemberPointsRuleDO.class);
        memberPointsRuleMapper.updateById(updateRule);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void deletePointsRule(Integer id) {
        // 检查规则是否存在
        MemberPointsRuleDO rule = memberPointsRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }


        // 删除规则
        memberPointsRuleMapper.deleteById(id);
    }

    @Override
    public MemberPointsRuleDO getPointsRule(Integer id) {
        return memberPointsRuleMapper.selectById(id);
    }

    @Override
    public List<MemberPointsRuleDO> getPointsRuleList() {
        return memberPointsRuleMapper.selectListByTenantIdAndDomainId();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void calculateConsumptionPointsAndGrowth(Integer memberId, Integer amount, String sourceId, String operator) {
        try {
            // 获取消费积分规则
            List<MemberPointsRuleDO> rules = memberPointsRuleMapper.selectListByRuleType(
                     PointsRuleTypeEnum.CONSUMPTION.getValue());

            if (rules.isEmpty()) {
                log.warn("未找到有效的消费积分规则，memberId={}, amount={}", memberId, amount);
                return;
            }

            // 找到当前有效的规则
            MemberPointsRuleDO rule = null;
            LocalDateTime now = LocalDateTimeUtil.now();
            for (MemberPointsRuleDO r : rules) {
                if (r.getEffectiveStartTime() != null && r.getEffectiveEndTime() != null) {
                    if (now.isAfter(r.getEffectiveStartTime()) && now.isBefore(r.getEffectiveEndTime())) {
                        rule = r;
                        break;
                    }
                } else if (r.getEffectiveStartTime() != null) {
                    if (now.isAfter(r.getEffectiveStartTime())) {
                        rule = r;
                        break;
                    }
                } else if (r.getEffectiveEndTime() != null) {
                    if (now.isBefore(r.getEffectiveEndTime())) {
                        rule = r;
                        break;
                    }
                } else {
                    rule = r;
                    break;
                }
            }

            if (rule == null) {
                log.warn("未找到当前有效的消费积分规则，memberId={}, amount={}", memberId, amount);
                return;
            }

            // 计算积分
            Integer points = 0;
            if (StrUtil.isNotBlank(rule.getRuleFormula())) {
                // 使用公式计算积分
                String formula = rule.getRuleFormula().replace("${amount}", String.valueOf(amount));
                points = evaluateFormula(formula);
            } else {
                // 使用固定值
                points = rule.getPointsValue();
            }

            // 计算成长值
            Integer growthValue = rule.getGrowthValue() != null ? rule.getGrowthValue() : 0;

            // 增加积分
            if (points > 0) {
                memberPointsService.addPoints(
                        memberId,
                        points,
                        PointsSourceTypeEnum.CONSUMPTION,
                        sourceId,
                        "消费奖励积分，订单：" + sourceId,
                        operator
                );
            }

            // 增加成长值
            if (growthValue > 0) {
                memberGrowthValueService.addGrowthValue(
                        memberId,
                        growthValue,
                        GrowthValueSourceTypeEnum.CONSUMPTION,
                        sourceId,
                        "消费奖励成长值，订单：" + sourceId,
                        operator
                );
            }

            log.info("计算消费积分和成长值成功，memberId={}, amount={}, points={}, growthValue={}",
                    memberId, amount, points, growthValue);
        } catch (Exception e) {
            log.error("计算消费积分和成长值异常", e);
            throw new BusinessException("计算积分异常：" + e.getMessage());
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void calculateSignInPointsAndGrowth(Integer memberId, Integer continuousDays, String operator) {
        try {

            // 获取签到积分规则
            List<MemberPointsRuleDO> rules = memberPointsRuleMapper.selectListByRuleType(
                     PointsRuleTypeEnum.SIGN_IN.getValue());

            if (rules.isEmpty()) {
                log.warn("未找到有效的签到积分规则，memberId={}, continuousDays={}", memberId, continuousDays);
                return;
            }

            // 找到当前有效的规则
            MemberPointsRuleDO rule = null;
            LocalDateTime now = LocalDateTimeUtil.now();
            for (MemberPointsRuleDO r : rules) {
                if (r.getEffectiveStartTime() != null && r.getEffectiveEndTime() != null) {
                    if (now.isAfter(r.getEffectiveStartTime()) && now.isBefore(r.getEffectiveEndTime())) {
                        rule = r;
                        break;
                    }
                } else if (r.getEffectiveStartTime() != null) {
                    if (now.isAfter(r.getEffectiveStartTime())) {
                        rule = r;
                        break;
                    }
                } else if (r.getEffectiveEndTime() != null) {
                    if (now.isBefore(r.getEffectiveEndTime())) {
                        rule = r;
                        break;
                    }
                } else {
                    rule = r;
                    break;
                }
            }

            if (rule == null) {
                log.warn("未找到当前有效的签到积分规则，memberId={}, continuousDays={}", memberId, continuousDays);
                return;
            }

            // 计算积分（连续签到可能有额外奖励）
            Integer points = rule.getPointsValue();
            if (continuousDays >= 7) {
                points = points * 2; // 连续签到7天以上，积分翻倍
            } else if (continuousDays >= 3) {
                points = points + 5; // 连续签到3天以上，额外奖励5积分
            }

            // 计算成长值
            Integer growthValue = rule.getGrowthValue() != null ? rule.getGrowthValue() : 0;

            // 生成签到记录ID
            String signInId = "sign_in_" + memberId + "_" + System.currentTimeMillis();

            // 增加积分
            if (points > 0) {
                memberPointsService.addPoints(
                        memberId,
                        points,
                        PointsSourceTypeEnum.SIGN_IN,
                        signInId,
                        "签到奖励积分，连续签到" + continuousDays + "天",
                        operator
                );
            }

            // 增加成长值
            if (growthValue > 0) {
                memberGrowthValueService.addGrowthValue(
                        memberId,
                        growthValue,
                        GrowthValueSourceTypeEnum.SIGN_IN,
                        signInId,
                        "签到奖励成长值，连续签到" + continuousDays + "天",
                        operator
                );
            }

            log.info("计算签到积分和成长值成功，memberId={}, continuousDays={}, points={}, growthValue={}",
                    memberId, continuousDays, points, growthValue);
        } catch (Exception e) {
            log.error("计算签到积分和成长值异常", e);
            throw new BusinessException("计算积分异常：" + e.getMessage());
        }
    }

    /**
     * 评估积分计算公式
     *
     * @param formula 公式
     * @return 计算结果
     */
    private Integer evaluateFormula(String formula) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object result = engine.eval(formula);
            if (result instanceof Number) {
                return ((Number) result).intValue();
            }
            return 0;
        } catch (ScriptException e) {
            log.error("评估积分计算公式异常，formula={}", formula, e);
            return 0;
        }
    }
}
