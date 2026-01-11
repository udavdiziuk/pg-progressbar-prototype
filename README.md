# PG progressbar prototype

This project is created just for prototype purpose.

## Purpose

One of my project contains a requirement: "I want to see a progress bar in percents and time when I execute complex analytic search query."
That's why I created this project as PoC.

## Research

I have not found any existing tools to be able to see progress of (basically) SELECT query.

## Architecture Overview

```
+============================================================================+
|                              CLIENT LAYER                                  |
|                                                                            |
|   +-------------+              +------------------------------------+      |
|   |  REST API   |              |        WebSocket Client            |      |
|   |  (request)  |              |   (subscribes to /topic/progress)  |      |
|   +------+------+              +-----------------^------------------+      |
+----------|-------------------------------------|---------------------------|
           |                                     |
           | POST /v1/search                     | ProgressDTO (jobId, status, %)
           v                                     |
+----------|-------------------------------------|---------------------------+
|          |      CONTROLLER LAYER               |                           |
|   +------v-----------------+                   |                           |
|   | SearchQueryController  |                   |                           |
|   +------+-----------------+                   |                           |
+----------|-------------------------------------|---------------------------+
           |                                     |
           v                                     |
+----------|-------------------------------------|---------------------------+
|          |       SERVICE LAYER                 |                           |
|          |                                     |                           |
|   +------v-------------------+                 |                           |
|   |  AnalyticQueryService    |                 |                           |
|   |  ------------------------|                 |                           |
|   |  1. Validate AST         |                 |                           |
|   |  2. Map to internal AST  |                 |                           |
|   |  3. Build SQL            |                 |                           |
|   |  4. Create SearchJob     |                 |                           |
|   |  5. Trigger async run    |                 |                           |
|   +------+-------------------+                 |                           |
|          |                                     |                           |
|          v                                     |                           |
|   +----------------------+      +--------------+-------------+             |
|   |  SearchJobRunner     |----->|    ProgressPublisher       |             |
|   |  (@Async)            |      |   (SimpMessagingTemplate)  |             |
|   +------+---------------+      +----------------------------+             |
|          |                                     ^                           |
|          | creates                             | publish(job)              |
|          v                                     |                           |
|   +----------------------+      +--------------+-----------+               |
|   | JobProgressTracker   |----->|   Throttling (500ms)     |               |
|   | ---------------------|      |  AtomicLong lastPublishTs|               |
|   | onChunkProcessed()   |      +--------------------------+               |
|   +------+---------------+                                                 |
|          |                                                                 |
|          v                                                                 |
|   +--------------------------------------------------------------------+   |
|   |              PatientSearchPipelineImpl                             |   |
|   |--------------------------------------------------------------------|   |
|   |  CHUNK_SIZE = 10,000 patients                                      |   |
|   |                                                                    |   |
|   |  while (hasMoreData):                                              |   |
|   |      1. loadChunk(lastId, CHUNK_SIZE) --> PatientRepository        |   |
|   |      2. filterChunk(chunk, sqlCondition)                           |   |
|   |      3. progressTracker.onChunkProcessed(chunk.size())             |   |
|   +--------------------------------------------------------------------+   |
|                                                                            |
|   +-----------------+       +-----------------+                            |
|   |   JobRegistry   |<----->|    SearchJob    |                            |
|   |   (in-memory)   |       |-----------------|                            |
|   |  ConcurrentMap  |       |  jobId: UUID    |                            |
|   +-----------------+       |  status: enum   |                            |
|                             |  totalPatients  |                            |
|                             |  processedPat.  |                            |
|                             |  progressPercent|                            |
|                             +-----------------+                            |
+============================================================================+
           |
           v
+============================================================================+
|                           DATABASE LAYER                                   |
|   +--------------------------------------------------------------------+   |
|   |                         PostgreSQL                                 |   |
|   |   patients table --> keyset pagination (WHERE id > ? LIMIT ?)      |   |
|   +--------------------------------------------------------------------+   |
+============================================================================+
```

---

## Data Flow Sequence

```
  Client                Controller           Service                  WebSocket
    |                       |                   |                         |
    |  POST /v1/search      |                   |                         |
    |---------------------->|                   |                         |
    |                       |  search(dto)      |                         |
    |                       |------------------>|                         |
    |                       |                   | validate & build SQL    |
    |                       |                   |--------+                |
    |                       |                   |<-------+                |
    |                       |                   |                         |
    |                       |                   | create SearchJob        |
    |                       |                   | jobRunner.run() @Async  |
    |                       |                   |--------+                |
    |  SearchResponseDTO    |                   |        |                |
    |<----------------------|<------------------|        |                |
    |  (jobId, PENDING)     |                   |        |                |
    |                       |                   |        v                |
    |                       |                   |   +-----------+         |
    |                       |                   |   | Pipeline  |         |
    |                       |                   |   | Loop:     |         |
    |                       |                   |   | chunk 1   |-------->| ProgressDTO
    |<------------------------------------------------------------------- | (10%)
    |                       |                   |   | chunk 2   |-------->| ProgressDTO
    |<------------------------------------------------------------------- | (20%)
    |                       |                   |   | ...       |         |
    |                       |                   |   | chunk N   |-------->| ProgressDTO
    |<------------------------------------------------------------------- | (100%)
    |                       |                   |   +-----------+         |
```