package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class LayuiTree<T> {

    // 节点唯一索引值，用于对指定节点进行各类操作
    private String id;

    // 节点标题
    private String title;

    // 节点字段名
    private String field;

    // 节点是否初始为选中状态（如果开启复选框的话），默认 false
    private boolean checked = false;

    // 节点是否初始展开，默认 false
    private boolean spread = false;

    // 节点是否为禁用状态。默认 false
    private boolean disabled = false;

    // 子节点
    private List<LayuiTree<T>> children = new ArrayList<LayuiTree<T>>();

    //*********************************其他属性************************************

    // 父节点id
    private String parentId;

    // 是否有父节点
    private boolean hasParent = false;

    // 是否有子节点
    private boolean hasChildren = false;
}