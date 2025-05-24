package org.example.musk.auth.service.core.wrapper.lock;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.extra.spring.SpringUtil;
import org.example.musk.auth.service.core.lambdas.functions.VoidFunction;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.middleware.redis.RedisUtil;

import java.util.function.Supplier;

import org.example.musk.common.exception.BusinessPageExceptionEnum;


/**
 * @Description:
 * @date 2024年08月06日
 */
@Slf4j
public class LockWrapper {

    public static <T> T lock(String key, Supplier<T> supplier) {
        return lock(key,supplier,()-> new BusinessException(BusinessPageExceptionEnum.BACK_LOCK_KEY_FAIL));
    }

    public static <T> T lock(String key, Supplier<T> supplier,Supplier<BusinessException> sThrows) {
        boolean lockSucc = false;
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        try {
            lockSucc = redisUtil.tryLock(key);
            if (!lockSucc) {
                throw sThrows.get();
            }
            return supplier.get();
        } catch (Exception e) {
            log.error(" 处理异常", e);
            throw e;
        } finally {
            if (lockSucc) {
                redisUtil.unLock(key);
            }
        }
    }


    public static  void lockVoid(String key, VoidFunction voidFunction) {
        lockVoid(key,voidFunction,()-> new BusinessException(BusinessPageExceptionEnum.BACK_LOCK_KEY_FAIL));
    }

    public static void lockVoid(String key, VoidFunction voidFunction, Supplier<BusinessException> sThrows) {
        boolean lockSucc = false;
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        try {
            lockSucc = redisUtil.tryLock(key);
            if (!lockSucc) {
                throw sThrows.get();
            }
            voidFunction.execute();
        } catch (Exception e) {
            log.error(" 处理异常", e);
            throw e;
        } finally {
            if (lockSucc) {
                redisUtil.unLock(key);
            }
        }
    }

    /**
     * 可返回前端的锁异常， 优雅提示
     * @param key
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T> T facadeLock(String key, Supplier<T> supplier) {
        return lock(key,supplier,()-> new BusinessException(BusinessPageExceptionEnum.FACADE_GET_LOCK_FAIL));
    }


}
