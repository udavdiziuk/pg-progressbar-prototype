package pl.uldav.pgprototype.service.query.pipeline;

import pl.uldav.pgprototype.service.query.sql.SqlCondition;
import pl.uldav.pgprototype.service.tracker.impl.JobProgressTracker;

public interface PatientSearchPipeline {
    void execute(SqlCondition sqlCondition, JobProgressTracker jobProgressTracker);
}
