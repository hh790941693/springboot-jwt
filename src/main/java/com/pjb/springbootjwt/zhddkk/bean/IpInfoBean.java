package com.pjb.springbootjwt.zhddkk.bean;

public class IpInfoBean {
    private String name;
    private String displayName;
    private String ip;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    @Override
    public String toString() {
        return "IpInfoBean [name=" + name + ", displayName=" + displayName + ", ip=" + ip + "]";
    }
}
