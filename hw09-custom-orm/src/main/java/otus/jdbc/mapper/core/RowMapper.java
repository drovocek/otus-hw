package otus.jdbc.mapper.core;

import java.sql.ResultSet;
import java.util.List;

public interface RowMapper<T>{

     T mapRow(ResultSet rs);

    List<T> mapRows(ResultSet rs);
}
