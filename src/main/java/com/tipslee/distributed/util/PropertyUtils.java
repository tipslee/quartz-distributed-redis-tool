package com.tipslee.distributed.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class PropertyUtils {


    public static Properties load(String s) {
        InputStream in = null;
        Properties pro = null;
        try {
            pro = new Properties();
            in = PropertyUtils.class.getResourceAsStream(s);
            pro.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pro;
    }
}
