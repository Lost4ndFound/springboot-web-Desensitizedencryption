package com.lee.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lee.anno.DesensitizedEncryption;
import com.lee.config.DataDesensitizationDefine;
import com.lee.config.DataDesensitizationHandler;
import com.lee.model.ApiResult;
import com.lee.util.RSAUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lsw
 * @date: 2023/10/17 10:14
 */
@Component
@Aspect
@ConditionalOnBean(DataDesensitizationHandler.class)
public class DesensitizedEncryptionAspect {

    @Autowired
    private DataDesensitizationHandler handler;

    /**
     * 脱敏加密返回
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(desensitizedEncryption) || @annotation(desensitizedEncryption)")
    public Object encryptAdvice(ProceedingJoinPoint joinPoint, DesensitizedEncryption desensitizedEncryption) throws Throwable {
        Object proceed = joinPoint.proceed();
        if(proceed instanceof ApiResult){
            ApiResult r = (ApiResult) proceed;
            Object data = r.getData();
            List<DataDesensitizationDefine> defines = handler.getDefines();
            if(CollectionUtil.isNotEmpty(defines)){
                handler.handleDataDesensitization(data);
            }
            String jsonStr = JSONUtil.toJsonStr(data);
            if(StrUtil.isNotBlank(jsonStr)){
                String encryptData = RSAUtil.encryptByPublicKey(jsonStr, RSAUtil.WEB_PUBLIC_KEY);
                r.setData(encryptData);
            }
            return r;
        }
        return proceed;
    }

}
