package com.tipslee.distributed.intercept.uid;

import com.tipslee.distributed.intercept.IdGenerator;

import java.util.UUID;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class UUIDGenerator implements IdGenerator {
    @Override
    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
