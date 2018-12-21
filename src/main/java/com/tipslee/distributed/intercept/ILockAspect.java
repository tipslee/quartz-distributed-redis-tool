package com.tipslee.distributed.intercept;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author liqiang
 * @description Quartz切面接口
 * @time 2018年12月21日
 * @modifytime
 */
public interface ILockAspect {

    void lock(ProceedingJoinPoint jp) throws Throwable;

    void unlock(JoinPoint jp, Throwable e);

    void doAfterThrowing(JoinPoint jp, Throwable e);
}
