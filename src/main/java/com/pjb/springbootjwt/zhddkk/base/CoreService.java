package com.pjb.springbootjwt.zhddkk.base;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;

public interface CoreService<T> extends IService<T> {

    List<T> findByKv(Object... param);

    T findOneByKv(Object... param);

    Map<String, Object> convertToMap(Object... param);

    EntityWrapper<T> convertToEntityWrapper(Object... params);
}
