package pl.uldav.pgprototype.service.query.pipeline;

import pl.uldav.pgprototype.service.query.sql.SqlCondition;

import java.util.List;

public interface PatientSearchPipeline {
    List<Long> execute(SqlCondition sqlCondition);
}
