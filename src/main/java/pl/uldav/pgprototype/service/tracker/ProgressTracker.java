package pl.uldav.pgprototype.service.tracker;

public interface ProgressTracker {
    void onChunkProcessed(long processedPatients);
}
