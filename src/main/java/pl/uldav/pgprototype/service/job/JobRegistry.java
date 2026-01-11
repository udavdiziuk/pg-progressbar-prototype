package pl.uldav.pgprototype.service.job;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for jobs of SQL pipelines
 */
@Component
public class JobRegistry {

    //For prototype in-memory, for project in DB
    private final Map<UUID, SearchJob> jobs = new ConcurrentHashMap<>();

    public SearchJob create(long totalPatients) {
        SearchJob job = new SearchJob(totalPatients);
        jobs.put(job.getJobId(), job);
        return job;
    }

    public SearchJob get(UUID jobId) {
        return jobs.get(jobId);
    }
}
