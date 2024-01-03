package com.kbslan.domain.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2023/3/3 17:52
 */
@Component
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListIntegerTypeHandler extends BaseTypeHandler<List<Integer>> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> integerList, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, JSON.toJSONString(integerList));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        return getIntegerList(value);
    }


    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);

        return getIntegerList(value);
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);

        return getIntegerList(value);
    }

    private List<Integer> getIntegerList(String value) {
        if (StringUtils.isNotBlank(value)) {
            try {
                CollectionType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Integer.class);
                return objectMapper.readValue(value , type);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
