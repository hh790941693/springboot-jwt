package com.pjb.springbootjwt.common.base;

import com.pjb.springbootjwt.common.utils.HttpContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.plugins.Page;

/**
 *
 * <pre>
 * </pre>
 *
 * <small> 2018年2月25日 | Aron</small>
 */
public abstract class AdminBaseController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <pre>
     * 自动获取分页参数，返回分页对象page
     * </pre>
     *
     * <small> 2018年4月15日 | Aron</small>
     *
     * @param e
     * @return
     */
    public <E> Page<E> getPage(Class<E> e) {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        return new Page<>(pageNumber, pageSize);
    }

    private int getParaToInt(String key, int defalut) {
        String pageNumber = HttpContextUtils.getHttpServletRequest().getParameter(key);
        if (StringUtils.isBlank(pageNumber)) {
            return defalut;
        }
        return Integer.parseInt(pageNumber);
    }
}
