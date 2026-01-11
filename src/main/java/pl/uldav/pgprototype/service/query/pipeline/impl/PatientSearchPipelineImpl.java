package pl.uldav.pgprototype.service.query.pipeline.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.uldav.pgprototype.repository.PatientRepository;
import pl.uldav.pgprototype.service.query.pipeline.PatientSearchPipeline;
import pl.uldav.pgprototype.service.query.sql.SqlCondition;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PatientSearchPipelineImpl implements PatientSearchPipeline {

    private static final int CHUNK_SIZE = 10_000;
    private final JdbcTemplate jdbcTemplate;
    private final PatientRepository patientRepository;

    /**
     * Executes SQL created from AST query
     * @param sqlCondition - sql to execute
     * @return - List of patientIds found by sqlCondition
     */
    @Override
    public List<Long> execute(SqlCondition sqlCondition) {
        List<Long> result = new ArrayList<>();
        long lastId = 0;
        long processed = 0;

        while(true) {
            //load batch of patient ids according to CHUNK_SIZE
            List<Long> chunk = patientRepository.loadChunk(lastId, CHUNK_SIZE);
            if (chunk.isEmpty()) {
                //we finished processing
                break;
            }

            List<Long> matched = filterChunk(chunk, sqlCondition);
            result.addAll(matched);
            lastId = chunk.getLast();
            processed += chunk.size();

            log.info("Processed {} patients, matched {}", processed, result.size());
        }

        return result;
    }

    /**
     * Filter batch of patientIds with provided condition
     * @param patientIds - IDs of patients to search in
     * @param condition - condition to filter
     * @return - List of IDs that are matched according to provided condition
     */
    private List<Long> filterChunk(List<Long> patientIds, SqlCondition condition) {
        String sql = """
        SELECT p.id
        FROM (SELECT unnest(?) AS id) p
        WHERE %s
        """.formatted(condition.getSql());

        List<Object> params = new ArrayList<>();
        params.add(patientIds.toArray(new Long[0]));
        params.addAll(condition.getParams());

        return jdbcTemplate.queryForList(
                sql,
                Long.class,
                params.toArray()
        );
    }

}
