package org.example.musk.plugin.lock.core;


import java.util.function.Supplier;

public interface Lock {

    default  boolean tryLock(String key){
        return this.tryLock(key,60);
    }

    /**
     *
     * @param key key
     * @param ttl 锁的时间,单位:秒
     * @return
     */
    boolean tryLock(String key,long ttl);

    boolean unLock(String key);


    default  <T> T safeExec(String lockKey , Supplier<T> supplier){
        return safeExec(lockKey,60,supplier);
    }
    default   <T> T safeExec(String lockKey,long ttl , Supplier<T> supplier){
        boolean b = false;
        try {
            if ( !(b = tryLock(lockKey,ttl)) ) {
                throw new RuntimeException("获取锁失败,key=" + lockKey);
            }
            return  supplier.get();
        } finally {
            if (b) {
                unLock(lockKey);
            }
        }
    }
}
