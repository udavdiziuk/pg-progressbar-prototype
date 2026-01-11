package pl.uldav.pgprototype.controller.dto;

import lombok.Builder;
import lombok.Data;
import pl.uldav.pgprototype.service.job.JobStatus;

import java.util.UUID;

@Builder
@Data
public class SearchResponseDTO {
    private UUID jobId;
    private JobStatus jobStatus;
}
