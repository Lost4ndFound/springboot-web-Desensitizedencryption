# 基于Springboot-Web的数据脱敏加密功能

## 功能介绍

在接口或Controller类上添加注解通过切面对接口的返回结果进行加密，脱敏，加密脱敏操作，还有前端传递参数的解密。

**加解密**

需要利用工具类`RSAUtil`生成两个公私钥对，分别对应前端公私钥和后端公私钥；前端加密用后端公钥，加密参数后传给后端，后端用后端私钥解密参数；后端加密用前端公钥，加密数据后返给前端，前端用前端私钥解密数据。

**脱敏**

使用脱敏功能需要在配置文件中配置脱敏的字段和脱敏字段哪些部位需要脱敏，下面注解模块会详细介绍

## 注解

### @Decrypt

> 解密：对前端加密的参数进行解密

```java
@Decrypt(paramType = Children.class)
```

注解需要传入参数类型，可以填写多个；注意接口要用Post请求且参数不要添加@RequestBody注解

### @Desensitize

> 脱敏：对返回前端的敏感数据字段进行脱敏

在配置文件中配置要加密的字段和要加密的位置

```yaml
desensitize:
  defines:
    - {fieldName: "childrenPhone,parentPhone,wifePhone", section: "3,4"}
    - {fieldName: "childrenName,parentName,wifeName", section: "1,1"}
```

* fieldName:脱敏的字段名称，多个字段名称用逗号分隔
* section:脱敏的位置，例`3,4` `3`代表从字符串的第3位开始脱敏，不包含第3位；`4` 代表要脱敏的位数，等量替换成 `*`

### @DesensitizedEncryption

> 脱敏加密：对返回前端的敏感数据字段进行脱敏，然后整体返回数据加密返回

### @Encrypt

> 加密：对返回前端的数据进行加密

## 测试结果

### 解密

![image-20231018152756864](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018152756864.png)

### 脱敏

![image-20231018152857041](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018152857041.png)

### 脱敏加密

![image-20231018153000014](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018153000014.png)

![image-20231018153059389](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018153059389.png)

### 加密

![image-20231018154412919](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018154412919.png)

![image-20231018154428237](https://github.com/Lost4ndFound/springboot-web-Desensitizedencryption/blob/main/img/image-20231018154428237.png)

