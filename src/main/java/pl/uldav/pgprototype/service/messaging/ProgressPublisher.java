package pl.uldav.pgprototype.service.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.uldav.pgprototype.service.job.SearchJob;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProgressPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publish(SearchJob job) {
        //For prototype. Combine with jobId in project
        String destination = "/topic/progress";
        log.info("Publishing progress to {}", destination);
        messagingTemplate.convertAndSend(destination,
                new ProgressDTO(job.getJobId(), job.getStatus(), job.getProgressPercent()));
    }
}
