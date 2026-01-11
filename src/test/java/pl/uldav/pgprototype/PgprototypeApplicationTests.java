package pl.uldav.pgprototype;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.uldav.pgprototype.service.job.SearchJob;
import pl.uldav.pgprototype.service.messaging.ProgressPublisher;
import pl.uldav.pgprototype.service.tracker.impl.JobProgressTracker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class PgprototypeApplicationTests {

    @Test
    void shouldThrottleProgressPublishing() throws InterruptedException {
        SearchJob job = new SearchJob(1_000_000);
        ProgressPublisher publisher = mock(ProgressPublisher.class);

        JobProgressTracker tracker =
                new JobProgressTracker(job, publisher);

        for (int i = 0; i < 100; i++) {
            tracker.onChunkProcessed(10_000);
            Thread.sleep(50);
        }

        verify(publisher, atMost(11)).publish(any());
    }
}
