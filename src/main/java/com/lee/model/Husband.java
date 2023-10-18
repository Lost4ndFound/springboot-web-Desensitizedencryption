package com.lee.model;

import java.io.Serializable;

/**
 * @author: lsw
 * @date: 2023/10/18 13:15
 */
public class Husband implements Serializable {

    private String parentName;

    private String parentPhone;

    private Wife parentWife;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public Wife getParentWife() {
        return parentWife;
    }

    public void setParentWife(Wife parentWife) {
        this.parentWife = parentWife;
    }
}
