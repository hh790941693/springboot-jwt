package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;

@Data
public class SystemInfoBean {
    private String osName;
    private String javaHome;
    private String javaVersion;
    private String dbVersion;
    private boolean nginxFlag;
    private String shareDir;
}
