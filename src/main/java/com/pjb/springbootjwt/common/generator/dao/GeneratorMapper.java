package com.pjb.springbootjwt.common.generator.dao;

import com.pjb.springbootjwt.common.generator.domain.ColumnDO;
import com.pjb.springbootjwt.common.generator.domain.TableDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GeneratorMapper {

    @Select("select table_name tableName, engine, table_comment comments, create_time createTime from information_schema.tables where table_schema = (select database()) order by createTime desc")
    List<TableDO> list();

    @Select("select count(*) from information_schema.tables where table_schema = (select database())")
    int count(Map<String, Object> map);

    @Select("select table_name tableName, engine, table_comment comments, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = #{tableName}")
    TableDO get(String tableName);

    @Select("select column_name columnName, data_type dataType, column_comment comments, column_key columnKey, extra from information_schema.columns where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<ColumnDO> listColumns(String tableName);
}
