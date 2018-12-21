package com.tipslee.distributed;

import com.tipslee.distributed.quartz.ISingleRunnableWorker;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class QuartzTest {

    @Test
    public void quartzRun() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");//初始化spring 容器});
        ISingleRunnableWorker worker = (ISingleRunnableWorker) appContext.getBean("syncInfoQuartz");
        worker.doWork();
    }


}
