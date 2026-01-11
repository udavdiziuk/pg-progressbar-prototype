package pl.uldav.pgprototype.service.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.uldav.pgprototype.service.messaging.ProgressPublisher;
import pl.uldav.pgprototype.service.query.pipeline.impl.PatientSearchPipelineImpl;
import pl.uldav.pgprototype.service.query.sql.SqlCondition;
import pl.uldav.pgprototype.service.tracker.impl.JobProgressTracker;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SearchJobRunner {
    private final PatientSearchPipelineImpl pipeline;
    private final ProgressPublisher progressPublisher;

    @Async
    public void run(SearchJob job, SqlCondition query) {
        JobProgressTracker tracker = new JobProgressTracker(job, progressPublisher);
        try {
            job.setStatus(JobStatus.RUNNING);

            pipeline.execute(query, tracker);
            job.setStatus(JobStatus.COMPLETED);
            job.setFinishedAt(Instant.now());
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            throw e;
        } finally {
            //always publish last job status
            tracker.publishFinal();
        }
    }
}
