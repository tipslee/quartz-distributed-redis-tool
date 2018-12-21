package com.tipslee.distributed.intercept;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public abstract class AbstractLockAspect implements ILockAspect {

    public void doAfterThrowing(JoinPoint jp, Throwable e) {
        unlock(jp, e);
    }

    public String getJoinPointName(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        String clazzName = jp.getTarget().getClass().getName();
        String clazzAndMethod = StringUtils.join(new String[]{clazzName, methodName}, "#");
        return clazzAndMethod;
    }
}
