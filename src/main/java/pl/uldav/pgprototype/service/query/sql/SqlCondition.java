package pl.uldav.pgprototype.service.query.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@AllArgsConstructor
public class SqlCondition {
    private String sql;
    private List<Object> params;
}
