package pl.uldav.pgprototype.service.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.uldav.pgprototype.service.job.JobStatus;
import pl.uldav.pgprototype.service.job.SearchJob;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProgressDTO {
    private UUID jobId;
    private JobStatus status;
    private double progressPercent;
}
