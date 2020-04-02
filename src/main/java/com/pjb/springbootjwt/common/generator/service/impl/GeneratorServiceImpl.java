package com.pjb.springbootjwt.common.generator.service.impl;

import com.pjb.springbootjwt.common.generator.domain.ColumnDO;
import com.pjb.springbootjwt.common.generator.domain.TableDO;
import com.pjb.springbootjwt.common.generator.dao.GeneratorMapper;
import com.pjb.springbootjwt.common.generator.service.GeneratorService;
import com.pjb.springbootjwt.common.generator.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public List<TableDO> list() {
        List<TableDO> list = generatorMapper.list();
        return list;
    }

    @Override
    public byte[] generatorCode(String[] tableNameArr) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (String tableName : tableNameArr) {
            // 查询表信息
            TableDO tableDO = generatorMapper.get(tableName);
            // 查询列信息
            List<ColumnDO> columnList = generatorMapper.listColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(tableDO, columnList, zipOutputStream);
        }
        IOUtils.closeQuietly(zipOutputStream);
        return outputStream.toByteArray();
    }
}
