package com.pjb.springbootjwt.zhddkk.bean;

import java.util.ArrayList;
import java.util.List;

public class JsonResult {
    private int total;
    private List<?> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        if (null == rows) {
            rows = new ArrayList<>();
        }
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
