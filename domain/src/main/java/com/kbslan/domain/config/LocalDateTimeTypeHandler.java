package com.kbslan.domain.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 保证高版本 Mybatis 可以正确处理 {@link LocalDateTime} 类型
 *
 * @author zhouchen
 * @since 2022-10-28
 */
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int columnIndex, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(columnIndex, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object columnValue = rs.getObject(columnName);
        return toLocalDateTime(columnValue);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object columnValue = rs.getObject(columnIndex);
        return toLocalDateTime(columnValue);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object columnValue = cs.getObject(columnIndex);
        return toLocalDateTime(columnValue);
    }

    private LocalDateTime toLocalDateTime(Object columnValue) {
        if (columnValue instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) columnValue;
            return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

}
