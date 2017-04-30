package me.liuwj.site.util.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.Instant;

/**
 * Created by vince on 2016-12-19.
 */
public class InstantTypeHandler extends BaseTypeHandler<Instant> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Instant parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter));
    }

    @Override
    public Instant getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return timestampToDateTime(rs.getTimestamp(columnName));
    }

    @Override
    public Instant getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return timestampToDateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public Instant getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return timestampToDateTime(cs.getTimestamp(columnIndex));
    }

    private Instant timestampToDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }
}
