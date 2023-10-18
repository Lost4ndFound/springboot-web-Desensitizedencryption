package com.lee.aspect;


import cn.hutool.core.collection.CollectionUtil;
import com.lee.anno.Desensitize;
import com.lee.config.DataDesensitizationDefine;
import com.lee.config.DataDesensitizationHandler;
import com.lee.model.ApiResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lsw
 * @date: 2023/10/16 13:48
 */
@Component
@Aspect
public class DesensitizeAspect {

    @Autowired
    private DataDesensitizationHandler handler;

    /**
     * 脱敏返回
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(desensitize) || @annotation(desensitize)")
    public Object dataDesensitizationAdvice(ProceedingJoinPoint joinPoint, Desensitize desensitize) throws Throwable {
        Object proceed = joinPoint.proceed();
        if(proceed instanceof ApiResult){
            ApiResult r = (ApiResult) proceed;
            Object data = r.getData();
            List<DataDesensitizationDefine> defines = handler.getDefines();
            if(CollectionUtil.isNotEmpty(defines)){
                handler.handleDataDesensitization(data);
            }
            return r;
        }
        return proceed;
    }

}
