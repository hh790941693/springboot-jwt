package com.pjb.springbootjwt.zhddkk.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;
import java.util.Map;

/**
 * sevice层.
 * @param <T> 实体
 */
public interface CoreService<T> extends IService<T> {

    List<T> findByKv(Object... param);

    T findOneByKv(Object... param);

    Map<String, Object> convertToMap(Object... param);

    EntityWrapper<T> convertToEntityWrapper(Object... params);
}
