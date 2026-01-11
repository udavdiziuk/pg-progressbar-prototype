package pl.uldav.pgprototype.service.tracker.impl;

import lombok.RequiredArgsConstructor;
import pl.uldav.pgprototype.service.job.SearchJob;
import pl.uldav.pgprototype.service.messaging.ProgressPublisher;
import pl.uldav.pgprototype.service.tracker.ProgressTracker;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class JobProgressTracker implements ProgressTracker {
    //for prototype only. Potentially move to config
    private static final long PUBLISH_INTERVAL_MS = 500;
    private final AtomicLong lastPublishTs = new AtomicLong(0);

    private final SearchJob job;
    private final ProgressPublisher publisher;

    @Override
    public void onChunkProcessed(long processedPatients) {
        job.getProcessedPatients().addAndGet(processedPatients);
        maybePublish();
    }

    private void maybePublish() {
        long now = System.currentTimeMillis();
        long last = lastPublishTs.get();
        if (now - last >= PUBLISH_INTERVAL_MS) {
            //
            if (lastPublishTs.compareAndSet(last, now)) {
                publisher.publish(job);
            }
        }
    }

    public void publishFinal() {
        publisher.publish(job);
    }
}
