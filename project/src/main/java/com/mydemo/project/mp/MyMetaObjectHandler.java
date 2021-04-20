package com.mydemo.project.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mydemo.project.entity.Account;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

/**
 * @author allen
 * 自动填充类
 * 对创建时间、修改时间、创建人、修改人的自动填充
 * 来源：mp官方文档自动填充
 */
//这个注解的作用作为spring容器，管理Bean
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时填充
     * @param metaObject 填充的对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //判断是否填充
        if(metaObject.hasSetter("createTime")){//判断这个字段是否存在或有值
            //填充，第一个参数为要填充的对象，第二个参数为要填充的字段，第三个参数为要填充的类型，第四个参数为要填充的值
            this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        }
        //判断是否有新增人字段
        if(metaObject.hasSetter("createAccountId")){
            //填充创建人
            //通过方法取得object类型的account对象
            Object account = RequestContextHolder.getRequestAttributes()
                    .getAttribute("account", RequestAttributes.SCOPE_SESSION);
            //判断创建人是否为空
            if(null != account){
                //对account进行强转得到accountId
                Long accountId = ((Account) account).getAccountId();
                this.strictInsertFill(metaObject,"createAccountId", Long.class,accountId);
            }
        }
    }

    /**
     * 修改时填充
     * @param metaObject 填充对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //判断是否填充
        if(metaObject.hasSetter("modifiedTime")){//判断这个字段是否存在或有值
            //填充，第一个参数为要填充的对象，第二个参数为要填充的字段，第三个参数为要填充的类型，第四个参数为要填充的值
            this.strictUpdateFill(metaObject,"modifiedTime", LocalDateTime.class,LocalDateTime.now());
        }
        //判断是否存在修改人字段
        if(metaObject.hasSetter("modifiedAccountId")){
            //填充修改人
            //通过方法取得object类型的account对象
            Object account = RequestContextHolder.getRequestAttributes()
                    .getAttribute("account", RequestAttributes.SCOPE_SESSION);
            //判断创建人是否为空
            if(null != account){
                //对account进行强转得到accountId
                Long accountId = ((Account) account).getAccountId();
                this.strictUpdateFill(metaObject,"modifiedAccountId", Long.class,accountId);
            }
        }
    }
}
