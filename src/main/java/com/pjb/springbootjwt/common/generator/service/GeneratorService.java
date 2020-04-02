package com.pjb.springbootjwt.common.generator.service;

import com.pjb.springbootjwt.common.generator.domain.TableDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 1992lcg@163.com
 * @Time 2017年9月6日
 * @description
 * 
 */
@Service
public interface GeneratorService {

	List<TableDO> list();

	byte[] generatorCode(String[] tableNameArr);
}
