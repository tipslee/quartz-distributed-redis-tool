package com.tipslee.distributed.quartz;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class SyncInfoQuartz2 extends AbstractQuartz {

    public void doWork() {
        //模拟work过程
        System.out.println("quartz start");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
