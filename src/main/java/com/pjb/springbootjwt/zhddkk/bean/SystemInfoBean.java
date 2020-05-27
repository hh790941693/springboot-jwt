package com.pjb.springbootjwt.zhddkk.bean;

public class SystemInfoBean {
    private String osName;
    private String javaHome;
    private String javaVersion;
    private String dbVersion;
    private boolean nginxFlag;
    private String shareDir;

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public boolean isNginxFlag() {
        return nginxFlag;
    }

    public void setNginxFlag(boolean nginxFlag) {
        this.nginxFlag = nginxFlag;
    }

    public String getShareDir() {
        return shareDir;
    }

    public void setShareDir(String shareDir) {
        this.shareDir = shareDir;
    }

    @Override
    public String toString() {
        return "SystemInfoBean{" +
                "osName='" + osName + '\'' +
                ", javaHome='" + javaHome + '\'' +
                ", javaVersion='" + javaVersion + '\'' +
                ", dbVersion='" + dbVersion + '\'' +
                ", nginxFlag=" + nginxFlag +
                ", shareDir='" + shareDir + '\'' +
                '}';
    }
}
