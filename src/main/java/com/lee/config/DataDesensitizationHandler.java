package com.lee.config;


import cn.hutool.core.util.StrUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author: lsw
 * @date: 2023/10/16 11:40
 */
@Component
@ConfigurationProperties(prefix = "desensitize")
public class DataDesensitizationHandler {

    /**
     * 脱敏规则定义列表
     */
    private List<DataDesensitizationDefine> defines;

    @PostConstruct
    public void init(){
        for (DataDesensitizationDefine define : defines) {
            define.init();
        }
    }

    /**
     * 数据脱敏
     * @param data
     * @throws Exception
     */
    public void handleDataDesensitization(Object data) throws Exception {
        // 获取子类及父类所有字段
        List<Field> declaredFields = getAllFields(data);
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String name = declaredField.getName();
            // 获取字段类型名称
            String typeName = declaredField.getGenericType().getTypeName();
            // 如果字段类型是自己创建的，那么进行递归修改改字段下的所有要脱敏的字段
            if (typeName.contains("com.lee") && !typeName.contains("List")) {
                handleDataDesensitization(declaredField.get(data));
            }
            // 如果字段类型是List，循环改列表的所有元素
            if (typeName.contains("List")) {
                String head = name.substring(0, 1);
                name = name.replaceFirst(head, head.toUpperCase());
                Method m = data.getClass().getMethod("get" + name);
                List list = (List) m.invoke(data);
                if (null == list) {
                    m = data.getClass().getMethod("set" + name, List.class);
                    m.invoke(data, new ArrayList<>());
                } else {
                    //如果是集合中只有一部分有值，其他属于都要显示
                    for (int i = 0; i < list.size(); i++) {
                        handleDataDesensitization(list.get(i));
                    }
                }
            }

            // 进行数据脱敏
            for (DataDesensitizationDefine define : defines) {
                String fieldName = define.getFieldName();
                if (StrUtil.isBlank(fieldName)) {
                    return;
                }
                String[] fieldNames = define.getFieldNames();
                for (String defineField : fieldNames) {
                    if (defineField.equalsIgnoreCase(name)) {
                        Object o = declaredField.get(data);
                        if (o instanceof String) {
                            String value = (String) o;
                            String handledValue;
                            if (StrUtil.isNotBlank(value)) {
                                if (value.length() >= (define.getIndex() + define.getCount())) {
                                    handledValue = value.replaceAll(define.getRegex(), define.getReplacement());
                                } else {
                                    int count = value.length() / 2;
                                    int index = count / 2;
                                    handledValue = value.replaceAll(define.resetRegex(index, count), define.resetReplacement(count));
                                }
                                declaredField.set(data, handledValue);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取子类以及父类
     * @param obj
     */
    private List<Field> getAllFields(Object obj){
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        Class<?> tempClass = obj.getClass().getSuperclass();
        while (tempClass != null && !tempClass.getName().equals("java.lang.Object")){
            Field[] declaredFields = tempClass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            tempClass = tempClass.getSuperclass();
        }
        return fields;
    }


    public List<DataDesensitizationDefine> getDefines() {
        return defines;
    }

    public void setDefines(List<DataDesensitizationDefine> defines) {
        this.defines = defines;
    }
}
