package com.pjb.springbootjwt.zhddkk.bean;

import com.pjb.springbootjwt.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tree<T> {
    /**
     *ID.
     */
    private String id;
    /**
     * text.
     */
    private String text;
    /**
     *open closed.
     */
    private Map<String, Object> state;
    /**
     * 是否选择 true/false.
     */
    private boolean checked = false;
    /**
     * 属性.
     */
    private Map<String, Object> attributes;

    /**
     * 子节点.
     */
    private List<Tree<T>> children = new ArrayList<Tree<T>>();

    /**
     * 父ID.
     */
    private String parentId;
    /**
     * 是否含有父节点.
     */
    private boolean hasParent = false;
    /**
     * 是否含有子节点.
     */
    private boolean hasChildren = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean isParent) {
        this.hasParent = isParent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setChildren(boolean isChildren) {
        this.hasChildren = isChildren;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Tree(String id, String text, Map<String, Object> state, boolean checked, Map<String, Object> attributes,
                List<Tree<T>> children, boolean isParent, boolean isChildren, String parentID) {
        super();
        this.id = id;
        this.text = text;
        this.state = state;
        this.checked = checked;
        this.attributes = attributes;
        this.children = children;
        this.hasParent = isParent;
        this.hasChildren = isChildren;
        this.parentId = parentID;
    }

    public Tree() {
        super();
    }

    @Override
    public String toString() {
        return JSONUtils.beanToJson(this);
    }

}