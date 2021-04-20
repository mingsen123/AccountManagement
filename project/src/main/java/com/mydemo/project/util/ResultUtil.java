package com.mydemo.project.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author allen
 */

public class ResultUtil {


    //分页查询的封装方法
    /**
     * 构建分页查询的返回结果
     * @param page Page对象
     * @return
     */
    public static R<Map<String,Object>> buildPageR(IPage<?> page){
        //因为泛型为Map,需要得到一个Map对象,通过HashMap得到
        Map<String, Object> data = new HashMap<>();
        //添加元素
        //添加总记录数
        data.put("count",page.getTotal());
        //添加
        data.put("records",page.getRecords());
        //返回数据,通过R.ok方法
        return R.ok(data);
    }

    /**
     * 成功失败响应信息工具方法
     * @param success
     * @return
     */
    public static R<Object> buildR(boolean success){
        //判断是否成功
        if(success){
            //保存成功不用返回数据
            return R.ok(null);
        }
        //保存失败
        return R.failed("操作失败");
    }
}
