package com.lee.controller;

import cn.hutool.json.JSONUtil;
import com.lee.anno.Decrypt;
import com.lee.anno.Desensitize;
import com.lee.anno.DesensitizedEncryption;
import com.lee.anno.Encrypt;
import com.lee.model.ApiResult;
import com.lee.model.Children;
import com.lee.model.Wife;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lsw
 * @date: 2023/10/18 13:48
 */
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * 加密
     * @return
     */
    @GetMapping("encrypt")
    @Encrypt
    public ApiResult encrypt(){
        Children children = new Children();
        children.setParentName("李逍遥");
        children.setParentPhone("18888888888");
        children.setChildrenName("李忆如");
        children.setChildrenPhone("16666666666");
        Wife wife = new Wife();
        wife.setWifeName("赵灵儿");
        wife.setWifePhone("19999999999");
        children.setParentWife(wife);
        return ApiResult.data(children);
    }

    /**
     * 脱敏
     * @return
     */
    @GetMapping("desensitize")
    @Desensitize
    public ApiResult desensitize(){
        Children children = new Children();
        children.setParentName("李逍遥");
        children.setParentPhone("18888888888");
        children.setChildrenName("李忆如");
        children.setChildrenPhone("16666666666");
        Wife wife = new Wife();
        wife.setWifeName("赵灵儿");
        wife.setWifePhone("19999999999");
        children.setParentWife(wife);
        return ApiResult.data(children);
    }

    /**
     * 脱敏加密
     * @return
     */
    @GetMapping("desensitizedEncryption")
    @DesensitizedEncryption
    public ApiResult desensitizedEncryption(){
        Children children = new Children();
        children.setParentName("李逍遥");
        children.setParentPhone("18888888888");
        children.setChildrenName("李忆如");
        children.setChildrenPhone("16666666666");
        Wife wife = new Wife();
        wife.setWifeName("赵灵儿");
        wife.setWifePhone("19999999999");
        children.setParentWife(wife);
        return ApiResult.data(children);
    }


    /**
     * 解密
     * @return
     */
    @PostMapping("decrypt")
    @Decrypt(paramType = Children.class)
    public ApiResult decrypt(Children children){
        System.out.println(JSONUtil.toJsonStr(children));
        return ApiResult.data(children);
    }

}
