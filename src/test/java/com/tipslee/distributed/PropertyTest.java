package com.tipslee.distributed;

import com.tipslee.distributed.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PropertyTest {


    @Before
    public void setBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1() {

    }


    @Test
    public void test2() {
        Properties props = PropertyUtils.load("/redis.properties");
        System.out.println(props.getProperty("redis.pool.maxIdle", "4"));
    }

}