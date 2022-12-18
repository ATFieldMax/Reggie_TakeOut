package com.example.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//自定義元數據對象處理器
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入操作自動填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自動填充[insert]...");
        log.info(metaObject.toString());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        Long currentId = BaseContext.getCurrentId();

        metaObject.setValue("createUser",currentId);
        metaObject.setValue("updateUser",currentId);
    }

    //更新操作自動填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自動填充[update]...");
        log.info(metaObject.toString());

        Long currentId = BaseContext.getCurrentId();

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",currentId);
    }
}
