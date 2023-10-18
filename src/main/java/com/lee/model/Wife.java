package com.lee.model;

import java.io.Serializable;

/**
 * @author: lsw
 * @date: 2023/10/18 14:17
 */
public class Wife implements Serializable {

    private String wifeName;

    private String wifePhone;

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public String getWifePhone() {
        return wifePhone;
    }

    public void setWifePhone(String wifePhone) {
        this.wifePhone = wifePhone;
    }
}
