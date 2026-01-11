package pl.uldav.pgprototype.service.job;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class SearchJob {
    private final UUID jobId = UUID.randomUUID();
    private volatile JobStatus status = JobStatus.PENDING;
    private final Instant startedAt = Instant.now();
    private volatile Instant finishedAt;
    private final long totalPatients;
    private final AtomicLong processedPatients = new AtomicLong(0);

    public double getProgressPercent() {
        return totalPatients == 0
                ? 0
                : (processedPatients.get() * 100.0) / totalPatients;
    }
}
