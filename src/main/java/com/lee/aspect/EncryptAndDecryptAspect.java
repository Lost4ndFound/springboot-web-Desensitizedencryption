package com.lee.aspect;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lee.anno.Decrypt;
import com.lee.anno.Encrypt;
import com.lee.model.ApiResult;
import com.lee.util.RSAUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author: lsw
 * @date: 2023/9/18 11:32
 */
@Component
@Aspect
public class EncryptAndDecryptAspect{


    /**
     * 加密返回
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(encrypt) || @annotation(encrypt)")
    public Object encryptAdvice(ProceedingJoinPoint joinPoint, Encrypt encrypt) throws Throwable {
        Object proceed = joinPoint.proceed();
        if(proceed instanceof ApiResult){
            ApiResult r = (ApiResult) proceed;
            Object data = r.getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            if(StrUtil.isNotBlank(jsonStr)){
                String encryptData = RSAUtil.encryptByPublicKey(jsonStr, RSAUtil.WEB_PUBLIC_KEY);
                r.setData(encryptData);
            }
            return r;
        }
        return proceed;
    }

    /**
     * 解密参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(decrypt) || @annotation(decrypt)")
    public Object decryptAdvice(ProceedingJoinPoint joinPoint, Decrypt decrypt) throws Throwable {
        // 收到请求,记录请求内容
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取输入流转换成body字符串
        ServletInputStream inputStream = requestAttributes.getRequest().getInputStream();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        if(StrUtil.isNotBlank(body)){
            // 解密body
            String json = RSAUtil.decryptByPrivateKey(body, RSAUtil.SERVICE_PRIVATE_KEY);
            if(json.contains("\\")){
                json = json.replace("\\", "");
            }
            if(json.startsWith("\"")){
                json = json.substring(json.indexOf("\"") + 1, json.lastIndexOf("\""));
            }
            // 获取注解中填写的参数类型
            Class<?>[] paramTypes = decrypt.paramType();
            Object[] args = joinPoint.getArgs();
            // 把解密后的参数重新赋值
            for (Class<?> paramType : paramTypes) {
                Object param = JSONUtil.toBean(json, paramType);
                for (Object arg : args) {
                    if(paramType.isInstance(arg)){
                        BeanUtil.copyProperties(param, arg);
                    }
                }
            }
        } else {
            return ApiResult.fail("未获取到请求内容");
        }
        return joinPoint.proceed();
    }




}
