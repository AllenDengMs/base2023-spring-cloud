package org.backend.cloud.database.mybatis.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.backend.cloud.common.utils.JSON;
import org.springframework.util.StringUtils;

public abstract class AbstractObjectTypeHandler<T> extends BaseTypeHandler<T> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setString(i, JSON.stringify(parameter));
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    String data = rs.getString(columnName);
    return StringUtils.isEmpty(data) ? null : JSON.parse(data, (Class<T>) getRawType());
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String data = rs.getString(columnIndex);
    return StringUtils.isEmpty(data) ? null : JSON.parse(data, (Class<T>) getRawType());
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String data = cs.getString(columnIndex);
    return StringUtils.isEmpty(data) ? null : JSON.parse(data, (Class<T>) getRawType());
  }

}
