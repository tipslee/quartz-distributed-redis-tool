package com.tipslee.distributed.quartz;

/**
 * @author liqiang
 * @description
 * @time 2018年12月22日
 * @modifytime
 */
public abstract class AbstractQuartz {

    public abstract void doWork();

    public String getClazzAndMethodName(AbstractQuartz obj) {
        String result = obj.getClass().getName() + "#" + "doWork";
        return result;
    }
}
