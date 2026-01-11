package pl.uldav.pgprototype.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PatientRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Loads batch of patients
     * @param lastId - start point to load
     * @param chunkSize - size of the batch
     * @return list with loaded patientIds
     */
    public List<Long> loadChunk(long lastId, int chunkSize) {
        return jdbcTemplate.queryForList(
                """
                    SELECT id
                    FROM patients
                    WHERE id > ?
                    ORDER BY id
                    LIMIT ?
                    """,
                Long.class,
                lastId,
                chunkSize
        );
    }

    public Long countAll() {
        return jdbcTemplate.queryForObject(
            """
            SELECT count(*)
            FROM patients
            """, Long.class);
    }
}
