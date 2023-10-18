package com.lee.config;



import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * @author: lsw
 * @date: 2023/10/16 13:22
 */
public class DataDesensitizationDefine implements Serializable {

    public void init(){
        if(StrUtil.isNotBlank(fieldName)){
            fieldNames = fieldName.split(",");
        }
        if(StrUtil.isNotBlank(section)){
            String[] split = section.split(",");
            index = Integer.valueOf(split[0]);
            count = Integer.valueOf(split[1]);
            StringBuilder replacementSb = new StringBuilder();
            for (Integer i = 0; i < count; i++) {
                replacementSb.append("*");
            }
            replacement = replacementSb.toString();
            regex = "(?<=^.{" + index + "}).{" + count + "}";
        }
    }



    /**
     * 脱敏字段，多个字段逗号分隔
     */
    private String fieldName;

    /**
     * 要脱敏第n位开始，脱敏m位，传值为n,m
     */
    private String section;

    private String[] fieldNames;

    private Integer index;

    private Integer count;

    private String replacement;

    private String regex;

    public String resetRegex(int index, int count){
        return "(?<=^.{" + index + "}).{" + count + "}";
    }

    public String resetReplacement(int count){
        StringBuilder replacementSb = new StringBuilder();
        for (Integer i = 0; i < count; i++) {
            replacementSb.append("*");
        }
        return replacementSb.toString();
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
