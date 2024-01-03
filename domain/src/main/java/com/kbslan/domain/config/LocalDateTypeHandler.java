package com.kbslan.domain.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 保证高版本 Mybatis 可以正确处理 {@link LocalDate} 类型
 *
 * @author zhouchen
 * @since 2022-10-28
 */
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int columnIndex, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(columnIndex, parameter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object columnValue = rs.getObject(columnName);
        return toLocalDate(columnValue);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object columnValue = rs.getObject(columnIndex);
        return toLocalDate(columnValue);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object columnValue = cs.getObject(columnIndex);
        return toLocalDate(columnValue);
    }

    private LocalDate toLocalDate(Object columnValue) {
        if (columnValue instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) columnValue;
            return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

}
