package com.pjb.springbootjwt.zhddkk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * build tree for layui.
 */
public class BuildLayuiTree {

    public static <T> LayuiTree<T> buildTree(List<LayuiTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }

        List<LayuiTree<T>> topNodes = new ArrayList<>();
        for (LayuiTree<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                continue;
            }
            children(nodes, children, pid);
        }

        LayuiTree<T> root = new LayuiTree<>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId("-1");
            root.setParentId("");
            root.setHasParent(false);
            root.setHasChildren(true);
            root.setChecked(true);
            root.setChildren(topNodes);
            root.setTitle("顶级节点");
            root.setSpread(true);
        }
        return root;
    }

    public static <T> List<LayuiTree<T>> buildList(List<LayuiTree<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        List<LayuiTree<T>> topNodes = new ArrayList<>();
        for (LayuiTree<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                continue;
            }
            children(nodes, children, pid);
        }
        return topNodes;
    }

    private static <T> void children(List<LayuiTree<T>> nodes, final LayuiTree<T> children, final String pId) {
        nodes.stream().filter(node -> node.getId() != null && node.getId().equals(pId)).forEach(node -> {
            node.getChildren().add(children);
            node.setHasParent(true);
            node.setHasChildren(true);
        });
    }
}