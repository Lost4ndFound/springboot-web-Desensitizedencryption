package com.lee.model;

import java.io.Serializable;

/**
 * @author: lsw
 * @date: 2023/10/18 13:15
 */
public class Children extends Husband implements Serializable {

    private String childrenName;

    private String childrenPhone;

    public String getChildrenName() {
        return childrenName;
    }

    public void setChildrenName(String childrenName) {
        this.childrenName = childrenName;
    }

    public String getChildrenPhone() {
        return childrenPhone;
    }

    public void setChildrenPhone(String childrenPhone) {
        this.childrenPhone = childrenPhone;
    }
}
